package com.cmad.service;

import java.util.List;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.mapping.Mapper;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.Sort;

import com.cmad.infra.MongoService;
import com.cmad.model.Blog;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.Json;

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
		
		//To fetch a existing blog when client passes blog-id
		handleFetchBlog();
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
			
			//Performing token validation
//			if(!TokenValidator.isValidToken(message, userDetail.getId(), userDetail.getToken(), dataStore))
//				return;
			
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
			
//			BasicDAO<Blog, String> dao = new BasicDAO<>(Blog.class, datastore);

			final Query<Blog> blogFetchQuery = datastore.createQuery(Blog.class).order(Sort.descending("blogCreatedTimeStamp"));
					
			final List<Blog> blogs = blogFetchQuery.asList();
			System.out.println("BlogVerticle.handleFetchRecentBlog() blogs = "+blogs);

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
	 * To fetch a existing blog when client passes blog-id
	 */
	private void handleFetchBlog() {
		vertx.eventBus().consumer(Topics.GET_BLOG_WITH_COMMENTS, message -> {
			String blodId = message.body().toString();
			System.out.println("BlogVerticle.handleFetchBlog() inside --> blodId = "+blodId);
//			if(blodId != null){
//				System.out.println("BlogVerticle.handleFetchBlog() newBlog= "+newBlog.toString());
//				System.out.println("BlogVerticle.handleFetchBlog() newBlog.getBlogContent() "+newBlog.getBlogContent());
//			}

			Datastore datastore = MongoService.getDataStore();
			
			//Performing token validation
//			if(!performCommonValidations(message, userDetail))
//				return;
			
//			BasicDAO<Blog, String> dao = new BasicDAO<>(Blog.class, datastore);

			final Query<Blog> blogFetchQuery = datastore.createQuery(Blog.class)
					.field(Mapper.ID_KEY).equal(blodId);
//			final List<Blog> blogs = blogFetchQuery.asList();
			Blog actualBlog  = blogFetchQuery.get();
			
			System.out.println("BlogVerticle.handleFetchBlog() actualBlog = "+actualBlog);
			if(actualBlog == null)	{
				message.fail(404, "X. Blog with id "+blodId+" is not available");
			}	else	{
				message.reply(Json.encodePrettily(actualBlog));
			}

			MongoService.close();
		});
	}

}
