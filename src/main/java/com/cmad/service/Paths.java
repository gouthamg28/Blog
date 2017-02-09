package com.cmad.service;

public interface Paths {

	/////////Verticle1 - RegistrationVerticle
	public static String P_REGISTRATION = "/api/user/registeration";

	public static String P_PROFILE_UPDATE = "/api/user/profileupdate";

	/////////Verticle2 - LoginVerticle 
	public static String P_LOGIN = "/api/user/login";
	
	public static String P_LOGOUT = "/api/user/logout";
	
	/////////Verticle3 - BlogVerticle
	public static String P_CREATE_NEW_BLOG = "/api/blog/";
	
	public static String P_GET_FAV_BLOGS = "/api/blog/favorites";

	public static String P_GET_BLOG_WITH_COMMENTS = "/api/blog/:blogId";
	
	public static String P_SEARCH_BLOGS = "/api/blog/searchblogs/:searchText";
	
	public static String P_ADD_COMMENTS = "/api/blog/comment/:blogId";
}
