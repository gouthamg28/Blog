package com.cmad.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.mapping.Mapper;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.Sort;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;

import com.cmad.auth.TokenValidator;
import com.cmad.infra.MongoService;
import com.cmad.model.Blog;
import com.cmad.model.UserDetail;
import com.cmad.model.dto.CommentDTO;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;

/*
 * Takes care of following use cases
 * 1. Creation of new blog
 * 2. Fetching a blog along with comments when client sends blog-id
 * 3. Fetching recent blog along with comments when user logs in to show in Home/Landing page
 * 4. Fetching favorite blogs of an user based on his area of interest
 * 5. Adding comments to an existing blog 
 */
public class BlogVerticle  extends AbstractVerticle {

	public void start() throws Exception {
		//To create a new blog
		handleNewBlogCreation();
		
		//To fetch recent blog available in DB
		handleFetchRecentBlog();
		
		//To fetch favorite blogs 
		handleFetchFavoriteBlogsList();
		
		//To fetch a existing blog when client passes blog-id
		handleFetchBlog();
		
		//To update comments on existing blogs
		handleUpdateComments();
	}

	/*
	 * To create a new blog
	 */
	private void handleNewBlogCreation() {
		vertx.eventBus().consumer(Topics.CREATE_NEW_BLOG, message -> {
			System.out.println("BlogVerticle.handleNewBlogCreation() inside --> "+message.body().toString());
			Blog newBlog = Json.decodeValue(message.body().toString(), Blog.class);
			if(newBlog != null){
				System.out.println("BlogVerticle.handleNewBlogCreation() newBlog= "+newBlog.toString());
				System.out.println("BlogVerticle.handleNewBlogCreation() newBlog.getBlogContent() "+newBlog.getBlogContent());
			}

			Datastore dataStore = MongoService.getDataStore();
			
			//Performing validations
			if(!isValidBlog(message, newBlog))
				return;

			BasicDAO<Blog, String> dao = new BasicDAO<>(Blog.class, dataStore);
			Object blog = dao.save(newBlog);

			MongoService.close();
			System.out.println("BlogVerticle.handleNewBlogCreation() blog = "+blog);
			System.out.println("BlogVerticle.handleNewBlogCreation() blog class = "+blog.getClass());
			if(blog == null){
				message.fail(404, "X. No Blog created");
			}else{
				message.reply(Json.encodePrettily(blog));
			}
		});
	}

	/*
	 * To fetch recent blog available in DB
	 */
	private void handleFetchRecentBlog() {
		vertx.eventBus().consumer(Topics.GET_RECENT_BLOG_WITH_COMMENTS, message -> {

			Datastore datastore = MongoService.getDataStore();
			
			final Query<Blog> blogFetchQuery = datastore.createQuery(Blog.class).order(Sort.descending("blogCreatedTimeStamp"));
					
			final List<Blog> blogs = blogFetchQuery.asList();
//			System.out.println("BlogVerticle.handleFetchRecentBlog() blogs = "+blogs);

			Blog recentBlog = null;
			if(blogs != null && blogs.size() > 0)	{
				recentBlog = blogs.get(0);
			}
			
			System.out.println("BlogVerticle.handleFetchBlog() recentBlog = "+recentBlog);
			if(recentBlog == null)	{
				message.fail(404, "X. No Blog available");
			}	else	{
				message.reply(Json.encodePrettily(recentBlog));
			}

			MongoService.close();
		});
	}

	/*
	 * To fetch favorite blogs 
	 */
	private void handleFetchFavoriteBlogsList() {
		vertx.eventBus().consumer(Topics.GET_FAV_BLOGS_LIST, message -> {
			System.out.println("BlogVerticle.handleFetchFavoriteBlogsList() message="+message);
			System.out.println("BlogVerticle.handleFetchFavoriteBlogsList() message.body()="+message.body());
			String userId = message.body().toString();
			System.out.println("BlogVerticle.handleFetchBlog() inside --> userId = "+userId);

			Datastore datastore = MongoService.getDataStore();
			
			final Query<UserDetail> userDetailQuery = datastore.createQuery(UserDetail.class).field(Mapper.ID_KEY).equal(userId);
			UserDetail user  = userDetailQuery.get();
			
			System.out.println("BlogVerticle.handleFetchFavoriteBlogsList() --> user= "+user);
			
			if(user != null)	{
				final Query<Blog> favBlogsFetchQuery = datastore.createQuery(Blog.class)
						.field("blogAreaOfInterest").equal(user.getAreaofinterest());
				final List<Blog> favBlogs = favBlogsFetchQuery.asList();
				
				System.out.println("BlogVerticle.handleFetchBlog() --> favBlogs = "+favBlogs);
				if(favBlogs == null || favBlogs.size() <= 0)	{
					message.fail(404, "No Favorite blogs available");
				}	else	{
					System.out.println("BlogVerticle.handleFetchFavoriteBlogsList() favBlogs.size()= "+favBlogs.size());
//					String results = "";
					Hashtable results = new Hashtable();
					Blog tempBlog;
					for(int i=0; i<favBlogs.size(); i++)	{
						tempBlog = favBlogs.get(i);
//						results = results + tempBlog.getBlog_id() + ":" + tempBlog.getBlogTitle() + ", ";
						results.put(tempBlog.getBlog_id(), tempBlog.getBlogTitle());
					}
//					message.reply(Json.encodePrettily(favBlogs));
					message.reply(Json.encodePrettily(results));
				}				
			}

			MongoService.close();
		});
	}

	/*
	 * To fetch a existing blog when client passes blog-id
	 */
	private void handleFetchBlog() {
		vertx.eventBus().consumer(Topics.GET_BLOG_WITH_COMMENTS, message -> {
			String blodId = message.body().toString();
			System.out.println("BlogVerticle.handleFetchBlog() inside --> blodId = "+blodId);

			Datastore datastore = MongoService.getDataStore();
			
			final Query<Blog> blogFetchQuery = datastore.createQuery(Blog.class)
					.field(Mapper.ID_KEY).equal(blodId);
			Blog actualBlog  = blogFetchQuery.get();
			
			System.out.println("BlogVerticle.handleFetchBlog() actualBlog = "+actualBlog);
			System.out.println("BlogVerticle.handleFetchBlog() its comments = "+actualBlog.getComments());
			if(actualBlog == null)	{
				message.fail(404, "X. Blog with id "+blodId+" is not available");
			}	else	{
				message.reply(Json.encodePrettily(actualBlog));
			}

			MongoService.close();
		});
	}

	/*
	 * To update comments on existing blogs
	 */
	private void handleUpdateComments() {
		vertx.eventBus().consumer(Topics.UPDATE_COMMENTS, message -> {
			String blogId = (String) ((JsonObject)message.body()).getValue("blogId");
			JsonObject comData = (JsonObject) ((JsonObject)message.body()).getValue("commentData");
			CommentDTO commentDTO = Json.decodeValue(comData.toString(), CommentDTO.class);

			Datastore dataStore = MongoService.getDataStore();
			
			//Performing validations
			if(!isValidComment(message, commentDTO))
				return;
			
///////
			Query query = dataStore.createQuery(Blog.class).field("blog_id").equal(blogId);

			UpdateOperations ops = dataStore.createUpdateOperations(Blog.class)
					.push("comments", commentDTO);

			UpdateResults results = dataStore.update(query, ops, false);
///////			
			
			MongoService.close();

			if(results == null || results.getUpdatedCount() <= 0){
				message.fail(404, "Pr2. No Record updated as there is no blog with id "+blogId);
			}else{
				message.reply(Json.encodePrettily(commentDTO));
			}
		});
	}
	
	private boolean isValidBlog(Message<Object> message, Blog newBlog) {
		boolean isValidBlog = true;
		 if(newBlog.getBlogTitle() == null || newBlog.getBlogTitle() == "")	{
				System.out.println("BlogVerticle.isValidBlog() Blog Title can't be empty");
				message.fail(404, "B1. Blog Title can't be empty");
				isValidBlog = false;
			} else if(newBlog.getBlogContent() == null || newBlog.getBlogContent() == "")	{
				System.out.println("BlogVerticle.isValidBlog() Blog content can't be empty");
				message.fail(404, "B2. Blog content can't be empty");
				isValidBlog = false;
			} else if(newBlog.getBlogAuthorUsername() == null || newBlog.getBlogAuthorUsername() == "")	{
				System.out.println("BlogVerticle.isValidBlog() Blog Author can't be empty");
				message.fail(404, "B3. Blog Author can't be empty");
				isValidBlog = false;
			} else if(newBlog.getBlogAreaOfInterest() == null || newBlog.getBlogAreaOfInterest() == "")	{
				System.out.println("BlogVerticle.isValidBlog() Blog AreaOfInterest can't be empty");
				message.fail(404, "B4. Blog AreaOfInterest can't be empty");
				isValidBlog = false;
			}
		 return isValidBlog;
	}
	
	private boolean isValidComment(Message<Object> message, CommentDTO commentDTO) {
		boolean isValidBlog = true;
		 /*if(commentDTO.getBlog_id() == null || commentDTO.getBlog_id() == "")	{
				System.out.println("BlogVerticle.isValidComment() Blog Id can't be empty");
				message.fail(404, "C1. Blog Id can't be empty");
				isValidBlog = false;
			} else */if(commentDTO.getCommentAuthorUsername() == null || commentDTO.getCommentAuthorUsername() == "")	{
				System.out.println("BlogVerticle.isValidComment() Comment author can't be empty");
				message.fail(404, "C2. Comment author can't be empty");
				isValidBlog = false;
			} else if(commentDTO.getCommentText() == null || commentDTO.getCommentText() == "")	{
				System.out.println("BlogVerticle.isValidComment() Comment Text can't be empty");
				message.fail(404, "C3. Comment Text can't be empty");
				isValidBlog = false;
			}
		 return isValidBlog;
	}

}
