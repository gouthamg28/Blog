package com.cmad.service;

import java.util.List;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;

import com.cmad.auth.JAuth;
import com.cmad.infra.MongoService;
import com.cmad.model.UserDetail;
import com.cmad.model.dto.LoginDTO;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.Json;

/*
 * Takes care of login & logout usecases
 */
public class LoginVerticle extends AbstractVerticle {

	
	@Override
	public void start() throws Exception {
		handleLogin();
		handleLogout();
	}

	private void handleLogin() {
		vertx.eventBus().consumer(Topics.LOGIN, message -> {
			LoginDTO loginData = Json.decodeValue(message.body().toString(), LoginDTO.class);
			
			System.out.println("LoginVerticle.handleLogin() "+loginData);
			System.out.println("LoginVerticle.handleLogin() username:"+loginData.getUsername());
			System.out.println("LoginVerticle.handleLogin() pwd:"+loginData.getPwd());
			
			
			Datastore datastore = MongoService.getDataStore();
			
			final Query<UserDetail> loginQuery = datastore.createQuery(UserDetail.class)
				.field("username").equal(loginData.getUsername())
				.field("pwd").equal(loginData.getPwd());
			final List<UserDetail> users = loginQuery.asList();
			
			System.out.println("LoginVerticle.handleLogin() users = "+users);
			
				
			if(users == null || users.isEmpty()){
				message.fail(401, "No User Found with User name, Password entered");
			}else{
				System.out.println("LoginVerticle.handleLogin() users.get(0) = "+users.get(0));
				UserDetail user = users.get(0);
				user.setPwd("");
				String token = generateToken(user);
				user.setToken(token);
				
				updateTokenToDB(datastore, user);
				
				message.reply(Json.encodePrettily(user));
				MongoService.close();
			}
			
			/*dao.createQuery().*/
		});
	}

	private void handleLogout() {
		vertx.eventBus().consumer(Topics.LOGOUT, message -> {
			LoginDTO loginData = Json.decodeValue(message.body().toString(), LoginDTO.class);
			
			System.out.println("LoginVerticle.handleLogout() "+loginData.toString());
			System.out.println("LoginVerticle.handleLogout() token: "+loginData.getToken());
			System.out.println("LoginVerticle.handleLogout() id: "+loginData.getId());
			
			
			Datastore datastore = MongoService.getDataStore();
			
			final Query<UserDetail> logoutQuery = datastore.createQuery(UserDetail.class)
				.field("id").equal(loginData.getId())
				.field("token").equal(loginData.getToken());
			final List<UserDetail> users = logoutQuery.asList();
			
			System.out.println("LoginVerticle.handleLogout() users = "+users);
			
				
			if(users == null || users.isEmpty()){
				message.fail(401, "No User Found with User name to logout");
			}else{
				System.out.println("LoginVerticle.handleLogout() users.get(0) = "+users.get(0));
				UserDetail user = users.get(0);
				user.setPwd("");
				
				resetTokenInDB(datastore, user);
				
				message.reply(Json.encodePrettily(user));
				MongoService.close();
			}
			
			/*dao.createQuery().*/
		});
	}
	
	private void updateTokenToDB(Datastore datastore, UserDetail user) {
		Query tokenQuery = datastore.createQuery(UserDetail.class).field("username").equal(user.getUsername());
		UpdateOperations ops = datastore.createUpdateOperations(UserDetail.class)
				.set("token", user.getToken());
		UpdateResults results = datastore.update(tokenQuery, ops, false);
	}
	
	private void resetTokenInDB(Datastore datastore, UserDetail user) {
		Query tokenQuery = datastore.createQuery(UserDetail.class).field("id").equal(user.getId());
		UpdateOperations ops = datastore.createUpdateOperations(UserDetail.class)
				.set("token", "");
		UpdateResults results = datastore.update(tokenQuery, ops, false);
	}

	
	/*
	 * Generating token which is valid for specified time
	 */
	private String generateToken(UserDetail user)	{
		String token = JAuth.getJoken(user.getId(), user.getUsername(), "login", 300000);
		System.out.println("LoginVerticle.getToken() token = "+token);
		return token;
	}
}
