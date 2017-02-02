package com.cmad.service;

import org.mongodb.morphia.dao.BasicDAO;

import com.cmad.infra.MongoService;
import com.cmad.model.UserDetail;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.Json;

public class RegistrationVerticle extends AbstractVerticle {
	public void start() throws Exception {
		vertx.eventBus().consumer(Topics.REGISTRATION, message -> {
			System.out.println("UserVerticle.start() register ");
			UserDetail regData = Json.decodeValue(message.body().toString(), UserDetail.class);
			if(regData!=null){
				System.out.println("RegistrationVerticle.start()getFullName "+regData.getFullName());
				System.out.println("RegistrationVerticle.start()pwd "+regData.getPwd());
				System.out.println("RegistrationVerticle.start() usrName"+regData.getUsername());
				System.out.println("RegistrationVerticle.start() phone number"+regData.getPhno());
			}
			
			if(regData.getUsername().indexOf('@')==-1)	{
				message.fail(404, "No User created");
			}
			
			BasicDAO<UserDetail, String> dao = new BasicDAO<>(UserDetail.class, MongoService.getDataStore());
			dao.save(regData);
			/*Query<UserDetail> query=dao.createQuery();
			query.
			query.and(
					query.criteria("username").equal(loginData.getUsername()),
					query.criteria("pwd").equal(loginData.getPwd()));*/
//			Object user =dao.save(regData).getId();/*query.get()*/;
//			System.out.println("RegistrationVerticle.start() Before adding "+MongoService.getDataStore().getCount(UserDetail.class));
			Object user = dao.save(regData);
//			System.out.println("RegistrationVerticle.start() After adding "+MongoService.getDataStore().getCount(UserDetail.class));
//			System.out.println("RegistrationVerticle.start() After adding "+MongoService.getDataStore().getByKeys(keys));
			
				MongoService.close();
//				System.out.println("RegistrationVerticle.start() After close "+MongoService.getDataStore().getCount(UserDetail.class));
				System.out.println("RegistrationVerticle.start() user-id = "+user);
				System.out.println("RegistrationVerticle.start() user class = "+user.getClass());
			if(user==null){
				message.fail(404, "No User created");
//				message.reply("No User created");
			}else{
				message.reply(Json.encodePrettily(user));
			}
			
			/*dao.createQuery().*/
		});
	}
}
