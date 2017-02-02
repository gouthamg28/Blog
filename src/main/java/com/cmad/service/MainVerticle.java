package com.cmad.service;

import com.mongodb.util.JSON;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

public class MainVerticle extends AbstractVerticle {
	
	public void start(Future<Void> future) throws Exception {


		System.out.println("starting...");
//		Vertx vertx = Vertx.vertx();
		Router router = Router.router(vertx);
		vertx.deployVerticle(UserVerticle.class.getName(), new DeploymentOptions().setWorker(true));
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
			/*rctx.response().setStatusCode(200).putHeader("content-type", "application/json; charset=utf-8")
					.end();*/
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

	}
	
public static void main(String[] args) {

	System.out.println("starting...");
	Vertx vertx = Vertx.vertx();
	Router router = Router.router(vertx);
	vertx.deployVerticle(UserVerticle.class.getName(), new DeploymentOptions().setWorker(true));
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
		/*rctx.response().setStatusCode(200).putHeader("content-type", "application/json; charset=utf-8")
				.end();*/
	});
//------------------------------------------//	
	router.route("/api/login").handler(BodyHandler.create());
	router.post("/api/login").handler(rctx -> {
		vertx.eventBus().send("com.cisco.cmad.projects.login", rctx.getBodyAsJson(), r -> {
			System.out.println("MainVerticle.start() message "+r.result().body().toString());
			rctx.response().setStatusCode(200).end(r.result().body().toString());
//			rctx.response().setStatusCode(200).end(Json.encodePrettily(obj));
		});
	});
	vertx.createHttpServer().requestHandler(router::accept).listen(8080);
//	,
//			result -> {
//				if (result.succeeded()) {
////					future.complete();
//				} else {
////					future.fail(result.cause());
//				}
//			});
//
}

}
