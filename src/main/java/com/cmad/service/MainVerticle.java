package com.cmad.service;

import java.util.Iterator;

import com.cmad.auth.TokenValidator;
import com.cmad.infra.MongoService;
import com.cmad.rabbitmq.mesaging.RPCRabbitMqServerVerticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;


public class MainVerticle extends AbstractVerticle {
	
	private static Router router;
	public static Vertx vertx;

	public void start(Future<Void> future) throws Exception {
		startServer("start()", vertx);
	}

	private static void startServer(String str, Vertx vertxArg)	{
		System.out.println("starting...MainVerticle from "+str);
//		vertx = vertxArg;
//		if(vertx == null)	{
//			vertx = Vertx.vertx();
//		}
		VertxOptions vertxOps = new VertxOptions();
		vertxOps.setMaxWorkerExecuteTime(VertxOptions.DEFAULT_MAX_WORKER_EXECUTE_TIME * 60);
		vertx = Vertx.vertx(vertxOps);
		
		router = Router.router(vertx);
		vertx.deployVerticle(RegistrationVerticle.class.getName(), new DeploymentOptions().setWorker(true));
		vertx.deployVerticle(LoginVerticle.class.getName(), new DeploymentOptions().setWorker(true));
		vertx.deployVerticle(BlogVerticle.class.getName(), new DeploymentOptions().setWorker(true));
		vertx.deployVerticle(RPCRabbitMqServerVerticle.class.getName(), new DeploymentOptions().setWorker(true));
		// ------------------------------------------//
		router.route("/about").handler(rctx -> {
			HttpServerResponse response = rctx.response();
			response.putHeader("content-type", "text/html")
					.end("<h1>Hello from my first Vert.x 3 application via routers</h1>");
		});
		// ------------------------------------------//
		router.route("/*").handler(StaticHandler.create("spiderman"));
		// router.route("/static/*").handler(StaticHandler.create("web"));
		// ------------------------------------------//
		
		setRegistrationHandler(vertx);
		
		setProfileUpdateHandler(vertx);
		
		setLoginHandler(vertx);
		
		setLogoutHandler(vertx);
		
		setBlogCreateHandler(vertx);
		
		setRecentBlogFetchHandler(vertx);
		
		setFavoriteBlogsFetchHandler(vertx);
		
		setBlogFetchHandler(vertx);
		
		setUpdateCommentsHandler(vertx);
		// ------------------------------------------//
System.out.println("MainVerticle.startServer() Blog verticle listening @ 8080");
		vertx.createHttpServer().requestHandler(router::accept).listen(8080);
//		vertx.createHttpServer().requestHandler(router::accept).websocketHandler(new CustomWebSocketHandler()).listen(8080);
		
		// ,
		// result -> {
		// if (result.succeeded()) {
		//// future.complete();
		// } else {
		//// future.fail(result.cause());
		// }
		// });
		//		
	}
	
	public static void main(String[] args) {
//		String token = JAuth.getJoken("58981c348364f232c07a311e", "e1@", "login", 10000000);
//		System.out.println("MainVerticle.main() token = "+token);
//		JAuth.validateJoken(token);
		
		
		startServer("main()", null);
	}
	
	private static void setLoginHandler(Vertx vertx) {
System.out.println("MainVerticle.setLoginHandler() entered");		
		router.route(Paths.P_LOGIN).handler(BodyHandler.create());
		router.post(Paths.P_LOGIN).handler(rctx -> {
System.out.println("MainVerticle.setLoginHandler() got request");			
//			printHTTPServerRequest(rctx);
System.out.println("MainVerticle.setLoginHandler() rctx.getBody() = "+rctx.getBody());
			DeliveryOptions deliveryOps = getCustomDeliveryOptions();
			vertx.eventBus().send(Topics.LOGIN, rctx.getBodyAsJson(), deliveryOps, r -> {
				System.out.println("MainVerticle.setLoginHandler() r = "+r);
				System.out.println("MainVerticle.setLoginHandler() r.result() " + r.result());

				if (r.result() != null) {
					System.out.println("MainVerticle.setLoginHandler() r.result().body() " + r.result().body());
					System.out.println("MainVerticle.setLoginHandler() r.result().body().toString() " + r.result().body().toString());

					rctx.response().setStatusCode(200).end(r.result().body().toString());
				} else {
					rctx.response().setStatusCode(404).end(r.cause().getMessage());
				}				
			});
		});
	}

	private static void setLogoutHandler(Vertx vertx) {
System.out.println("MainVerticle.setLogoutHandler() entered");		
		router.route(Paths.P_LOGOUT).handler(BodyHandler.create());
		router.post(Paths.P_LOGOUT).handler(rctx -> {
			DeliveryOptions deliveryOps = getCustomDeliveryOptions();
			vertx.eventBus().send(Topics.LOGOUT, rctx.getBodyAsJson(), deliveryOps, r -> {
				System.out.println("MainVerticle.setLogoutHandler() r = "+r);
				System.out.println("MainVerticle.setLogoutHandler() r.result() " + r.result());

				if (r.result() != null) {
					System.out.println("MainVerticle.setLogoutHandler() r.result().body() " + r.result().body());
					System.out.println("MainVerticle.setLogoutHandler() r.result().body().toString() " + r.result().body().toString());

					rctx.response().setStatusCode(200).end(r.result().body().toString());
				} else {
					rctx.response().setStatusCode(404).end(r.cause().getMessage());
				}				
			});
		});
	}
	
	private static void setRegistrationHandler(Vertx vertx) {
System.out.println("MainVerticle.setRegistrationHandler() entered");
		router.route(Paths.P_REGISTRATION).handler(BodyHandler.create());
		router.post(Paths.P_REGISTRATION).handler(rctx -> {
			DeliveryOptions deliveryOps = getCustomDeliveryOptions();
			vertx.eventBus().send(Topics.REGISTRATION, rctx.getBodyAsJson(), deliveryOps, r -> {
				if (r.result() != null) {
					rctx.response().setStatusCode(200).end(r.result().body().toString());
				} else {
					rctx.response().setStatusCode(404).end(r.cause().getMessage());
				}
			});
		});
	}

	private static void setProfileUpdateHandler(Vertx vertx) {
System.out.println("MainVerticle.setProfileUpdateHandler() entered");		
		router.route(Paths.P_PROFILE_UPDATE).handler(BodyHandler.create());
		router.post(Paths.P_PROFILE_UPDATE).handler(rctx -> {
			
			System.out.println("MainVerticle.setProfileUpdateHandler() Got request");			
//			printHTTPServerRequest(rctx);

			System.out.println("MainVerticle.setProfileUpdateHandler() Body is "+rctx.getBodyAsJson());
			
			if(!validateToken(rctx))	{
				rctx.response().setStatusCode(404).end("Token authentication failed for Profile update please Re-login");
				return;
			}
			DeliveryOptions deliveryOps = getCustomDeliveryOptions();
			vertx.eventBus().send(Topics.PROFILE_UPDATE, rctx.getBodyAsJson(), deliveryOps, r -> {
				if (r.result() != null) {
					rctx.response().setStatusCode(200).end(r.result().body().toString());
				} else {
					rctx.response().setStatusCode(404).end(r.cause().getMessage());
				}
			});
		});
	}
	
	private static void setBlogCreateHandler(Vertx vertx) {
System.out.println("MainVerticle.setBlogCreateHandler() entered");		
		router.route(Paths.P_CREATE_NEW_BLOG).handler(BodyHandler.create());
		router.post(Paths.P_CREATE_NEW_BLOG).handler(rctx -> {

			System.out.println("MainVerticle.setBlogCreateHandler() Got request");			
//			printHTTPServerRequest(rctx);

			if(!validateToken(rctx))	{
				rctx.response().setStatusCode(404).end("Token authentication failed for Blog creation please Re-login");
				return;
			}
			DeliveryOptions deliveryOps = getCustomDeliveryOptions();
			vertx.eventBus().send(Topics.CREATE_NEW_BLOG, rctx.getBodyAsJson(), deliveryOps, r -> {
//System.out.println("MainVerticle.setBlogCreateHandler() r = "+r);
//if(r != null)	{
//	System.out.println("MainVerticle.setBlogCreateHandler() r.result() = "+r.result());
//	if(r.result() != null)		{
//		System.out.println("MainVerticle.setBlogCreateHandler() r.result().body() = "+r.result().body());
//	}
//}
				if (r.result() != null) {
					rctx.response().setStatusCode(200).end(r.result().body().toString());
				} else {
					rctx.response().setStatusCode(404).end(r.cause().getMessage());
				}
			});
		});
	}

	private static void setRecentBlogFetchHandler(Vertx vertx) {
System.out.println("MainVerticle.setRecentBlogFetchHandler() entered");
		router.post(Paths.P_GET_RECENT_BLOG_WITH_COMMENTS).handler(rctx -> {
			System.out.println("MainVerticle.setRecentBlogFetchHandler() got request");
//			printHTTPServerRequest(rctx);
			DeliveryOptions deliveryOps = getCustomDeliveryOptions();
			vertx.eventBus().send(Topics.GET_RECENT_BLOG_WITH_COMMENTS, rctx.getBodyAsJson(), deliveryOps, r -> {
				if (r.result() != null) {
					rctx.response().setStatusCode(200).end(r.result().body().toString());
				} else {
					rctx.response().setStatusCode(404).end(r.cause().getMessage());
				}
			});
		});
	}

	private static void setFavoriteBlogsFetchHandler(Vertx vertx) {
System.out.println("MainVerticle.setFavoriteBlogsFetchHandler() entered");
//		router.route(Paths.P_GET_BLOG_WITH_COMMENTS).handler(BodyHandler.create());
		router.post(Paths.P_GET_FAV_BLOGS_LIST).handler(rctx -> {
			System.out.println("MainVerticle.setFavoriteBlogsFetchHandler() got request");
//			printHTTPServerRequest(rctx);
			String uId = rctx.pathParams().get("userId");
			System.out.println("MainVerticle.setFavoriteBlogsFetchHandler() uId = "+uId);
			DeliveryOptions deliveryOps = getCustomDeliveryOptions();
			vertx.eventBus().send(Topics.GET_FAV_BLOGS_LIST, uId, deliveryOps, r -> {
				if (r.result() != null) {
					rctx.response().setStatusCode(200).end(r.result().body().toString());
				} else {
					rctx.response().setStatusCode(404).end(r.cause().getMessage());
				}
			});
		});
	}
	
	private static void setBlogFetchHandler(Vertx vertx) {
System.out.println("MainVerticle.setBlogFetchHandler() entered");
//		router.route(Paths.P_GET_BLOG_WITH_COMMENTS).handler(BodyHandler.create());
		router.post(Paths.P_GET_BLOG_WITH_COMMENTS).handler(rctx -> {
			System.out.println("MainVerticle.setBlogFetchHandler() got request");
//			printHTTPServerRequest(rctx);
			String blogId = rctx.pathParams().get("blogId");
			System.out.println("MainVerticle.setBlogFetchHandler() blogId = "+blogId);
			DeliveryOptions deliveryOps = getCustomDeliveryOptions();
			vertx.eventBus().send(Topics.GET_BLOG_WITH_COMMENTS, blogId, deliveryOps, r -> {
				if (r.result() != null) {
					rctx.response().setStatusCode(200).end(r.result().body().toString());
				} else {
					rctx.response().setStatusCode(404).end(r.cause().getMessage());
				}
			});
		});
	}
	
	private static void setUpdateCommentsHandler(Vertx vertx) {
		System.out.println("MainVerticle.setUpdateCommentsHandler() entered");		
		router.route(Paths.P_UPDATE_COMMENTS).handler(BodyHandler.create());
		router.post(Paths.P_UPDATE_COMMENTS).handler(rctx -> {
			
			System.out.println("MainVerticle.setUpdateCommentsHandler() Got request");			
//			printHTTPServerRequest(rctx);

//			if(!validateToken(rctx))	{
//				rctx.response().setStatusCode(404).end("Token authentication failed for updating comments please Re-login");
//				return;
//			}
			
			JsonObject tempJsonObj = new JsonObject();
			String blog_Id = rctx.pathParams().get("blogId");
			System.out.println("MainVerticle.setUpdateCommentsHandler() blog_Id:-->"+blog_Id);
			tempJsonObj.put("blogId", blog_Id);
			tempJsonObj.put("commentData", rctx.getBodyAsJson());
			DeliveryOptions deliveryOps = getCustomDeliveryOptions();
			vertx.eventBus().send(Topics.UPDATE_COMMENTS, tempJsonObj, deliveryOps, r -> {
				if (r.result() != null) {
					rctx.response().setStatusCode(200).end(r.result().body().toString());
				} else {
					rctx.response().setStatusCode(404).end(r.cause().getMessage());
				}
			});
		});
	}

	
//	Performing token validation based on RoutingContext data
	private static boolean validateToken(RoutingContext rctx)	{
		boolean isValid = false;

		HttpServerRequest httpServerRequest = rctx.request();
		MultiMap headers = httpServerRequest.headers();
		if(headers != null)	{
			String id = headers.get("id");
			String token = headers.get("token");
			System.out.println("MainVerticle.validateToken() id = "+id);
			System.out.println("MainVerticle.validateToken() token = "+token);
			if(TokenValidator.isValidToken(id, token, MongoService.getDataStore()))
				isValid = true;
		}
		System.out.println("MainVerticle.validateToken() isValid = "+isValid);
		return isValid;
	}
	
//	Performing token validation based on id token passed
	public static boolean validateToken(String id, String token)	{
		boolean isValid = false;

//		System.out.println("MainVerticle.validateToken() id = "+id);
//		System.out.println("MainVerticle.validateToken() token = "+token);
		if(TokenValidator.isValidToken(id, token, MongoService.getDataStore()))
			isValid = true;
		System.out.println("MainVerticle.validateToken() isValid = "+isValid);
		return isValid;
	}
	
	private static DeliveryOptions getCustomDeliveryOptions()	{
		DeliveryOptions deliveryOps = new DeliveryOptions();
		deliveryOps.setSendTimeout(600*1000*6);//setting timeout of 600sec*6(60mins)
		return deliveryOps;
	}
	
	private static void printHTTPServerRequest(RoutingContext rctx)	{
		HttpServerRequest httpServerRequest = rctx.request();
		System.out.println("MainVerticle.printHTTPServerRequest() httpServerRequest.query() = "+httpServerRequest.query());
		System.out.println("MainVerticle.printHTTPServerRequest() httpServerRequest.absoluteURI() = "+httpServerRequest.absoluteURI());
		System.out.println("MainVerticle.printHTTPServerRequest() httpServerRequest.path() = "+httpServerRequest.path());
		System.out.println("MainVerticle.printHTTPServerRequest() httpServerRequest.uri() = "+httpServerRequest.uri());
		
		MultiMap headers = httpServerRequest.headers();
		if(headers != null)	{
			Iterator headerIterator = headers.iterator();
			Object nextHeader;
			System.out.println("MainVerticle.printHTTPServerRequest() $$$$$$ HEADERS STARTS");
			while(headerIterator.hasNext())	{
				nextHeader = headerIterator.next();
				System.out.println("MainVerticle.printHTTPServerRequest() nextHeader = "+nextHeader);
			}
		}
		
		MultiMap params = httpServerRequest.params();
		System.out.println("MainVerticle.printHTTPServerRequest() params = "+params);
		if(params != null)	{
			Iterator paramIterator = params.iterator();
			System.out.println("MainVerticle.printHTTPServerRequest() paramIterator = "+paramIterator);
			if(paramIterator != null){
				System.out.println("MainVerticle.printHTTPServerRequest() paramIterator.hasNext() = "+paramIterator.hasNext());
			}
			Object nextParam;
			System.out.println("MainVerticle.printHTTPServerRequest() ######## PARAM KEY VALUE PAIRS STARTS");
			while(paramIterator.hasNext())	{
				nextParam = paramIterator.next();
				System.out.println("MainVerticle.printHTTPServerRequest() nextParam = "+nextParam);
			}
		}
		
		System.out.println("MainVerticle.printHTTPServerRequest() httpServerRequest.uri() = "+httpServerRequest.params());
	}

}
