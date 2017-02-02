package com.cmad.service;

public interface Paths {

	/////////Verticle1
	public static String P_REGISTRATION = "/api/user/registeration";

	public static String P_PROFILE_UPDATE = "/api/user/profileupdate";

	/////////Verticle2
	public static String P_LOGIN = "/api/user/login";
	
	public static String P_LOGOUT = "/api/user/logout";
	
	/////////Verticle3
	public static String P_GET_FAV_POSTS = "/api/user/blog/getfavposts";

	public static String P_GET_BLOG_WITH_COMMENTS = "/api/user/blog/getblogwithcomments";
	
	public static String P_CREATE_NEW_BLOG = "/api/user/blog/createnewblog";
	
	public static String P_SEARCH_BLOGS = "/api/user/blog/searchblogs";
	
	public static String P_ADD_COMMENTS = "/api/user/blog/addcomment";
}
