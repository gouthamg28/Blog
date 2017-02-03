package com.cmad.service;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;

import com.cmad.infra.MongoService;
import com.cmad.model.UserDetail;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.Json;

public class RegistrationVerticle extends AbstractVerticle {
	public void start() throws Exception {
		handleRegistraion();
		handleProfileUpdate();
	}

	private void handleRegistraion() {
		vertx.eventBus().consumer(Topics.REGISTRATION, message -> {
			System.out.println("RegistrationVerticle.handleRegistraion() inside ");
			UserDetail regData = Json.decodeValue(message.body().toString(), UserDetail.class);
			if(regData!=null){
				System.out.println("RegistrationVerticle.handleRegistraion() usrName: "+regData.getUsername());
				System.out.println("RegistrationVerticle.handleRegistraion() pwd: "+regData.getPwd());
				System.out.println("RegistrationVerticle.handleRegistraion() FullName: "+regData.getFullName());
				System.out.println("RegistrationVerticle.handleRegistraion() phone number: "+regData.getPhno());
				System.out.println("RegistrationVerticle.handleRegistraion() area of interest: "+regData.getAreaofinterest());
			}

			Datastore dataStore = MongoService.getDataStore();
			
			//None of the fields should be empty
			if(regData.getFullName() == null || regData.getFullName() == "")	{
				System.out.println("RegistrationVerticle.handleRegistraion() full name validation failed");
				message.fail(404, "1. FullName can't be empty");
				return;
			} else if(regData.getUsername() == null || regData.getUsername() == "")	{
				System.out.println("RegistrationVerticle.handleRegistraion() user name validation failed");
				message.fail(404, "2. User name can't be empty");
				return;
			} else if(regData.getPwd() == null || regData.getPwd() == "")	{
				System.out.println("RegistrationVerticle.handleRegistraion() pwd validation failed");
				message.fail(404, "3. Password can't be empty");
				return;
			} else if(regData.getPhno() == null || regData.getPhno() == "")	{
				System.out.println("RegistrationVerticle.handleRegistraion() ph. no. validation failed");
				message.fail(404, "4. Phone no. can't be empty");
				return;
			} else if(regData.getAreaofinterest() == null || regData.getAreaofinterest() == "")	{
				System.out.println("RegistrationVerticle.handleRegistraion() area of interest validation failed");
				message.fail(404, "5. Areaofinterest can't be empty");
				return;
			}

			
			//Validating if similar user already exists
			long count = dataStore.createQuery(UserDetail.class).field("username").equal(regData.getUsername()).count();
			System.out.println("RegistrationVerticle.handleRegistraion() count = "+count);
			if(count > 0)	{
				System.out.println("RegistrationVerticle.handleRegistraion() user already exists");
				message.fail(404, "6. User already exists");
				return;
			}
			
			System.out.println("RegistrationVerticle.handleRegistraion() index of @ is "+regData.getUsername().indexOf('@'));
			if(regData.getUsername().indexOf('@')==-1)	{
				System.out.println("RegistrationVerticle.handleRegistraion() Email validation failed");
				message.fail(404, "7. No User created due to email vailidation failed");
				return;
			}

			BasicDAO<UserDetail, String> dao = new BasicDAO<>(UserDetail.class, dataStore);
			dao.save(regData);
			Object user = dao.save(regData);

			MongoService.close();
			System.out.println("RegistrationVerticle.handleRegistraion() user-id = "+user);
			System.out.println("RegistrationVerticle.handleRegistraion() user class = "+user.getClass());
			if(user==null){
				message.fail(404, "No User created");
				//				message.reply("No User created");
			}else{
				message.reply(Json.encodePrettily(user));
			}
		});
	}
	
	private void handleProfileUpdate() {

		vertx.eventBus().consumer(Topics.REGISTRATION, message -> {
			System.out.println("RegistrationVerticle.handleProfileUpdate() inside ");
			UserDetail regData = Json.decodeValue(message.body().toString(), UserDetail.class);
			if(regData!=null){
				System.out.println("RegistrationVerticle.handleProfileUpdate() usrName: "+regData.getUsername());
				System.out.println("RegistrationVerticle.handleProfileUpdate() pwd: "+regData.getPwd());
				System.out.println("RegistrationVerticle.handleProfileUpdate() FullName: "+regData.getFullName());
				System.out.println("RegistrationVerticle.handleProfileUpdate() phone number: "+regData.getPhno());
				System.out.println("RegistrationVerticle.handleProfileUpdate() area of interest: "+regData.getAreaofinterest());
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
			//			System.out.println("RegistrationVerticle.handleProfileUpdate() Before adding "+MongoService.getDataStore().getCount(UserDetail.class));
			Object user = dao.save(regData);
			//			System.out.println("RegistrationVerticle.handleProfileUpdate() After adding "+MongoService.getDataStore().getCount(UserDetail.class));
			//			System.out.println("RegistrationVerticle.handleProfileUpdate() After adding "+MongoService.getDataStore().getByKeys(keys));

			MongoService.close();
			System.out.println("RegistrationVerticle.handleProfileUpdate() user-id = "+user);
			System.out.println("RegistrationVerticle.handleProfileUpdate() user class = "+user.getClass());
			if(user==null){
				message.fail(404, "No User created");
				//				message.reply("No User created");
			}else{
				message.reply(Json.encodePrettily(user));
			}
		});
	}

}
