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
	
	public static String P_GET_RECENT_BLOG_WITH_COMMENTS = "/api/blog/recent";
	
	public static String P_GET_FAV_BLOGS_LIST = "/api/blog/favorites/:userId";

	public static String P_GET_BLOG_WITH_COMMENTS = "/api/blog/:blogId";
	
	public static String P_SEARCH_BLOGS = "/api/blog/searchblogs/:searchText";
	
	public static String P_UPDATE_COMMENTS = "/api/blog/comment/:blogId";
}
