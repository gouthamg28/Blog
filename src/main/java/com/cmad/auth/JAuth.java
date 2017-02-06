package com.cmad.auth;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.mapping.Mapper;

import com.cmad.infra.MongoService;
import com.cmad.model.UserDetail;

import java.security.Key;
import java.security.NoSuchAlgorithmException;

import io.jsonwebtoken.*;

import java.util.Base64;
import java.util.Date;

public class JAuth {
	
	static String secret; 
	
	// create new key
	private static String getSecret(){
		SecretKey secretKey = null;
		try {
			secretKey = KeyGenerator.getInstance("AES").generateKey();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		// get base64 encoded version of the key
		if(secret == null)	{
			secret = Base64.getEncoder().encodeToString(secretKey.getEncoded());
		}
		
		System.out.println("JAuth.getSecret() secret = "+secret);
		return secret;
	}
	
	//Sample method to construct a JWT
	private static String createJWT(String id, String issuer, String subject, long ttlMillis) {
	 
	    //The JWT signature algorithm we will be using to sign the token
	    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
	 
	    long nowMillis = System.currentTimeMillis();
	    Date now = new Date(nowMillis);
	 
	    //We will sign our JWT with our ApiKey secret
	    byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(getSecret());
	    Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
	 
	    //Let's set the JWT Claims
	    JwtBuilder builder = Jwts.builder().setId(id)
                                .setIssuedAt(now)
                                .setSubject(subject)
                                .setIssuer(issuer)
                                .signWith(signatureAlgorithm, signingKey);
	 
	    //if it has been specified, let's add the expiration
	    if (ttlMillis >= 0) {
	    	long expMillis = nowMillis + ttlMillis;
	    	Date exp = new Date(expMillis);
	    	builder.setExpiration(exp);
	    }
	 
	    //Builds the JWT and serializes it to a compact, URL-safe string
	    return builder.compact();
	}
	
	//Sample method to validate and read the JWT
	private static Date parseJWT(String jwt) {
		//This line will throw an exception if it is not a signed JWS (as expected)
		Date expiryDate = null;
		try {
			Claims claims = Jwts.parser()         
					.setSigningKey(DatatypeConverter.parseBase64Binary(getSecret()))
					.parseClaimsJws(jwt).getBody();

			System.out.println("ID: " + claims.getId());
			System.out.println("Issuer/userName: " + claims.getIssuer());
			System.out.println("Subject: " + claims.getSubject());
			System.out.println("Expiration: " + claims.getExpiration());
			expiryDate = claims.getExpiration();
		} catch (ExpiredJwtException e) {
			e.printStackTrace();
		} catch (UnsupportedJwtException e) {
			e.printStackTrace();
		} catch (MalformedJwtException e) {
			e.printStackTrace();
		} catch (SignatureException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}

		return expiryDate;
	}
	
	public static String getJoken(String id, String issuer, String subject, long ttlMillis) {
//		return createJWT( id,  issuer,  subject, 115200);
		return createJWT( id,  issuer,  subject, ttlMillis);
		
	}
	
	public static boolean validateJoken(String jToken) {
		boolean isValid = false;
		Date expirationDate = parseJWT(jToken);
	    long nowMillis = System.currentTimeMillis();
	    Date currentDate = new Date(nowMillis);
	    System.out.println("JAuth.validateJoken() expirationDate: "+expirationDate);
	    System.out.println("JAuth.validateJoken() currentDate: "+currentDate);
	    if(currentDate != null && expirationDate != null)	{
	    	isValid = currentDate.before(expirationDate);
	    }
		return isValid;
	}
}
