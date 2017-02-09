package com.cmad.service;

public interface Topics {

	/////////Verticle1
	public static String REGISTRATION = "com.cisco.cmad.projects.registration";
	
	public static String PROFILE_UPDATE = "com.cisco.cmad.projects.profileupdate";

	/////////Verticle2
	public static String LOGIN = "com.cisco.cmad.projects.login";
	
	public static String LOGOUT = "com.cisco.cmad.projects.logout";
	
	/////////Verticle3
	public static String CREATE_NEW_BLOG = "com.cisco.cmad.projects.createnewblog";
	
	public static String GET_FAV_BLOGS = "com.cisco.cmad.projects.getfavblogs";

	public static String GET_BLOG_WITH_COMMENTS = "com.cisco.cmad.projects.getblogwithcomments";
	
	public static String SEARCH_BLOGS = "com.cisco.cmad.projects.searchblogs";
	
	public static String ADD_COMMENTS = "com.cisco.cmad.projects.addcomment";
}
