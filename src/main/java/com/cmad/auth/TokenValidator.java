package com.cmad.auth;

import java.util.List;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;

import com.cmad.model.UserDetail;

import io.vertx.core.eventbus.Message;

public class TokenValidator {
	/*
	 * Checks whether Token was generated for the same user & check if token has expired.
	 */
	public static boolean isValidToken(Message<Object> message, String id, String token, Datastore dataStore) {
		boolean isValid = false;

		final Query<UserDetail> query = dataStore.createQuery(UserDetail.class)
				.field("id").equal(id)
				.field("token").equal(token);
		final List<UserDetail> users = query.asList();

		System.out.println("TokenValidator.isValidToken() users = "+users);

		if((users != null) && users.size() == 1)		{
			boolean tokenAlive =  JAuth.validateJoken(token);
			if(tokenAlive)	{
				isValid = true;
			}	else	{
				message.fail(401, "Token has expired");
			}
		}else	{
			message.fail(401, "Invalid Token");
		}
		return isValid;
	}
}
