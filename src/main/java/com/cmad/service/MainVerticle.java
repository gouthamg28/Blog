package com.cmad.service;

import com.cmad.auth.JAuth;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

public class MainVerticle extends AbstractVerticle {
	
	private static Router router;

	public void start(Future<Void> future) throws Exception {
		startServer("start()");
		/*


		System.out.println("starting...");
//		Vertx vertx = Vertx.vertx();
		Router router = Router.router(vertx);
		vertx.deployVerticle(LoginVerticle.class.getName(), new DeploymentOptions().setWorker(true));
		vertx.deployVerticle(RegistrationVerticle.class.getName(), new DeploymentOptions().setWorker(true));
	//------------------------------------------//	
		router.route("/about").handler(rctx -> {
			HttpServerResponse response = rctx.response();
			response.putHeader("content-type", "text/html")
					.end("<h1>Hello from my first Vert.x 3 application via routers</h1>");
		});
	//------------------------------------------//	
		router.route("/*").handler(StaticHandler.create("spiderman"));
		//router.route("/static/*").handler(StaticHandler.create("web"));
	//------------------------------------------//	
		router.route(Paths.P_REGISTRATION).handler(BodyHandler.create());
		router.post(Paths.P_REGISTRATION).handler(rctx -> {
			System.out.println("MainVerticle.start() inside register ");
			String name = rctx.request().getParam("fullName");
			String pwd= rctx.request().getParam("pwd");
			String areaofinterest= rctx.request().getParam("areaofinterest");
			System.out.println("MainVerticle.start() name "+name);
			System.out.println("MainVerticle.start() pwd "+pwd);
			System.out.println("MainVerticle.start() areaofinterest "+areaofinterest);
			vertx.eventBus().send(Topics.REGISTRATION, rctx.getBodyAsJson(), r -> {
				System.out.println("MainVerticle.start() register r "+r);
				System.out.println("MainVerticle.start() register r.result() "+r.result());
				if(r.result() != null)	{
					System.out.println("MainVerticle.start() register r.result().body() "+r.result().body());
					System.out.println("MainVerticle.start() register r.result().body().toString() "+r.result().body().toString());
					rctx.response().setStatusCode(200).end(r.result().body().toString());
				}	else	{
					rctx.response().setStatusCode(404).end("No user created");
				}
			});
			rctx.response().setStatusCode(200).putHeader("content-type", "application/json; charset=utf-8")
					.end();
		});
	//------------------------------------------//	
		router.route("/api/login").handler(BodyHandler.create());
		router.post("/api/login").handler(rctx -> {
			vertx.eventBus().send("com.cisco.cmad.projects.login", rctx.getBodyAsJson(), r -> {
				System.out.println("MainVerticle.start() message "+r.result().body().toString());
				rctx.response().setStatusCode(200).end(r.result().body().toString());
//				rctx.response().setStatusCode(200).end(Json.encodePrettily(obj));
			});
		});
		vertx.createHttpServer().requestHandler(router::accept).listen(8080);
//		,
//				result -> {
//					if (result.succeeded()) {
////						future.complete();
//					} else {
////						future.fail(result.cause());
//					}
//				});
	//

	*/}

	private static void startServer(String str)	{
		System.out.println("starting...from "+str);
		Vertx vertx = Vertx.vertx();
		router = Router.router(vertx);
		vertx.deployVerticle(RegistrationVerticle.class.getName(), new DeploymentOptions().setWorker(true));
		vertx.deployVerticle(LoginVerticle.class.getName(), new DeploymentOptions().setWorker(true));

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
		// ------------------------------------------//

		vertx.createHttpServer().requestHandler(router::accept).listen(8080);
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
		startServer("main()");
	}

	private static void setLoginHandler(Vertx vertx) {
		router.route(Paths.P_LOGIN).handler(BodyHandler.create());
		router.post(Paths.P_LOGIN).handler(rctx -> {

			vertx.eventBus().send(Topics.LOGIN, rctx.getBodyAsJson(), r -> {
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
		router.route(Paths.P_LOGOUT).handler(BodyHandler.create());
		router.post(Paths.P_LOGOUT).handler(rctx -> {

			vertx.eventBus().send(Topics.LOGOUT, rctx.getBodyAsJson(), r -> {
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

		router.route(Paths.P_REGISTRATION).handler(BodyHandler.create());
		router.post(Paths.P_REGISTRATION).handler(rctx -> {

			vertx.eventBus().send(Topics.REGISTRATION, rctx.getBodyAsJson(), r -> {
				if (r.result() != null) {
					rctx.response().setStatusCode(200).end(r.result().body().toString());
				} else {
					rctx.response().setStatusCode(404).end(r.cause().getMessage());
				}
			});
		});
	}

	private static void setProfileUpdateHandler(Vertx vertx) {
		router.route(Paths.P_PROFILE_UPDATE).handler(BodyHandler.create());
		router.post(Paths.P_PROFILE_UPDATE).handler(rctx -> {
			vertx.eventBus().send(Topics.PROFILE_UPDATE, rctx.getBodyAsJson(), r -> {
				if (r.result() != null) {
					rctx.response().setStatusCode(200).end(r.result().body().toString());
				} else {
					rctx.response().setStatusCode(404).end(r.cause().getMessage());
				}
			});
		});
	}
}
