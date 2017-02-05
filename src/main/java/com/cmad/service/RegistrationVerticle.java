package com.cmad.service;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;

import com.cmad.infra.MongoService;
import com.cmad.model.UserDetail;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.Json;

public class RegistrationVerticle extends AbstractVerticle {
	public void start() throws Exception {
		handleRegistraion();
		handleProfileUpdate();
	}

	private void handleRegistraion() {
		vertx.eventBus().consumer(Topics.REGISTRATION, message -> {
			System.out.println("RegistrationVerticle.handleRegistraion() inside ");
			UserDetail userDetail = Json.decodeValue(message.body().toString(), UserDetail.class);
			System.out.println("RegistrationVerticle.handleRegistraion() userDetail = "+userDetail);
			if(userDetail!=null){
				System.out.println("RegistrationVerticle.handleRegistraion() Fields "+userDetail.toString());
			}

			Datastore dataStore = MongoService.getDataStore();
			
			//Performing user name validations
			if(!performUserNameValidation(message, userDetail, dataStore))
				return;
			
			//Performing other validations
			if(!performCommonValidations(message, userDetail))
				return;
			
			BasicDAO<UserDetail, String> dao = new BasicDAO<>(UserDetail.class, dataStore);
			dao.save(userDetail);
			Object user = dao.save(userDetail);

			MongoService.close();
			System.out.println("RegistrationVerticle.handleRegistraion() user = "+user);
			System.out.println("RegistrationVerticle.handleRegistraion() user class = "+user.getClass());
			if(user==null){
				message.fail(404, "X. No User created");
			}else{
				message.reply(Json.encodePrettily(user));
			}
		});
	}

	private void handleProfileUpdate() {

		vertx.eventBus().consumer(Topics.PROFILE_UPDATE, message -> {
			System.out.println("RegistrationVerticle.handleProfileUpdate() inside ");
			UserDetail userDetail = Json.decodeValue(message.body().toString(), UserDetail.class);
			if(userDetail!=null){
				System.out.println("RegistrationVerticle.handleProfileUpdate() UserDetail= "+userDetail.toString());
			}

			if(userDetail.getUsername() == null || userDetail.getUsername().trim().isEmpty())	{
				message.fail(404, "Pr1. User name is not valid");
				return;
			}
			
			//Performing other validations
			if(!performCommonValidations(message, userDetail))
				return;
			
			System.out.println("RegistrationVerticle.handleProfileUpdate() common validations passed");
			
///////
			Datastore dataStore = MongoService.getDataStore();
			Query query = dataStore.createQuery(UserDetail.class).field("username").equal(userDetail.getUsername().trim());
			UpdateOperations ops = dataStore.createUpdateOperations(UserDetail.class)
					.set("pwd", userDetail.getPwd())
					.set("fullName", userDetail.getFullName())
					.set("phno", userDetail.getPhno())
					.set("areaofinterest", userDetail.getAreaofinterest());

			UpdateResults results = dataStore.update(query, ops, false);
///////			
			
			MongoService.close();
			System.out.println("RegistrationVerticle.handleProfileUpdate() results = "+results);
			System.out.println("RegistrationVerticle.handleProfileUpdate() results.getUpdatedCount() = "+results.getUpdatedCount());
			if(results == null || results.getUpdatedCount() <= 0){
				message.fail(404, "Pr2. No Record updated as there is no user by name "+userDetail.getUsername().trim());
			}else{
				message.reply(Json.encodePrettily(results.getUpdatedCount()));
			}
		});
	}

	private boolean performUserNameValidation(Message<Object> message, UserDetail userDetail, Datastore dataStore) {
		String userName = userDetail.getUsername();
		
		if(userName == null || userName.trim().isEmpty())	{
			System.out.println("RegistrationVerticle.performUserNameValidation() user name validation failed");
			message.fail(404, "U1. User name can't be empty");
			return false;
		}
		//Validating if similar user already exists
		long count = dataStore.createQuery(UserDetail.class).field("username").equal(userName.trim()).count();
		System.out.println("RegistrationVerticle.performUserNameValidation() count = "+count);
		if(count > 0)	{
			System.out.println("RegistrationVerticle.performUserNameValidation() same user name already exists");
			message.fail(404, "U2. User already exists");
			return false;
		}
		
		int indexOfAt = userName.trim().indexOf('@');
		System.out.println("RegistrationVerticle.performUserNameValidation() index of @ is "+indexOfAt);
		if(indexOfAt == -1)	{
			System.out.println("RegistrationVerticle.performUserNameValidation() Username/Email validation failed");
			message.fail(404, "U3. Username/Email is not a valid Email id");
			return false;
		}
		return true;
	}
	
	private boolean performCommonValidations(Message message, UserDetail userDetail)	{
		boolean validationPassed = true;
		 if(userDetail.getPwd() == null || userDetail.getPwd() == "")	{
				System.out.println("RegistrationVerticle.performCommonValidations() pwd validation failed");
				message.fail(404, "C1. Password can't be empty");
				validationPassed = false;
			} else if(userDetail.getFullName() == null || userDetail.getFullName() == "")	{
				System.out.println("RegistrationVerticle.performCommonValidations() full name validation failed");
				message.fail(404, "C2. FullName can't be empty");
				validationPassed = false;
			} else if(userDetail.getPhno() == null || userDetail.getPhno() == "")	{
				System.out.println("RegistrationVerticle.performCommonValidations() ph. no. validation failed");
				message.fail(404, "C3. Phone no. can't be empty");
				validationPassed = false;
			} else if(userDetail.getAreaofinterest() == null || userDetail.getAreaofinterest() == "")	{
				System.out.println("RegistrationVerticle.performCommonValidations() area of interest validation failed");
				message.fail(404, "C4. Areaofinterest can't be empty");
				validationPassed = false;
			}
		 return validationPassed;
	}
}
