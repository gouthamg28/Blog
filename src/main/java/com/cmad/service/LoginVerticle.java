package com.cmad.service;

import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;

import com.cmad.infra.MongoService;
import com.cmad.model.UserDetail;
import com.cmad.model.dto.LoginDTO;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.Json;

public class LoginVerticle extends AbstractVerticle {

	
	@Override
	public void start() throws Exception {
		vertx.eventBus().consumer("com.cisco.cmad.projects.login", message -> {
			LoginDTO loginData = Json.decodeValue(message.body().toString(), LoginDTO.class);
			System.out.println("LoginVerticle.start()"+loginData);
			
			System.out.println("LoginVerticle.start() userbname"+loginData.getUsername());
			System.out.println("LoginVerticle.start() pwd "+loginData.getPwd());
			BasicDAO<UserDetail, String> dao = new BasicDAO<>(UserDetail.class, MongoService.getDataStore());
			Query<UserDetail> query=dao.createQuery();
			query.and(
					query.criteria("username").equal(loginData.getUsername()),
					query.criteria("pwd").equal(loginData.getPwd()));
			UserDetail user =query.get();
				MongoService.close();
			if(user==null){
				message.reply("No User Found");
			}else{
				message.reply(Json.encodePrettily(user));
			}
			
			/*dao.createQuery().*/
		});
	}
}
