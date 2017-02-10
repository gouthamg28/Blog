package com.cmad.model.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.mongodb.morphia.annotations.PrePersist;

//import java.util.Date;

public class CommentDTO {

//	private String blog_id;
	private String commentAuthorUsername;
	private String commentCreatedTimeStamp;
	private String commentText;

//	public String getBlog_id() {
//		return blog_id;
//	}
//	public void setBlog_id(String blog_id) {
//		this.blog_id = blog_id;
//	}
	public String getCommentAuthorUsername() {
		return commentAuthorUsername;
	}
	public void setCommentAuthorUsername(String commentAuthorUsername) {
		this.commentAuthorUsername = commentAuthorUsername;
	}
	public String getCommentCreatedTimeStamp() {
		return commentCreatedTimeStamp;
	}
	public void setCommentCreatedTimeStamp(String commentCreatedTimeStamp) {
		this.commentCreatedTimeStamp = commentCreatedTimeStamp;
	}
	public String getCommentText() {
		return commentText;
	}
	public void setCommentText(String commentText) {
		this.commentText = commentText;
	}

	@PrePersist
	public void prePersist(){
		String timeStamp = new SimpleDateFormat("dd/MMM/yyyy HH:mm:ss").format(new Date());
		System.out.println("CommentDTO.prePersist() commented time = "+timeStamp);
		commentCreatedTimeStamp = (commentCreatedTimeStamp==null)?timeStamp:commentCreatedTimeStamp;
	}

	@Override
	public String toString() {
//		return " blog_id:"+blog_id+", commentAuthorUsername:"+commentAuthorUsername+", commentText:"+commentText;
		return " commentAuthorUsername:"+commentAuthorUsername+", commentText:"+commentText;
	}
}
