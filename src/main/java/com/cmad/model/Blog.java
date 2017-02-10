package com.cmad.model;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.PrePersist;

@Entity
public class Blog {

		@Id
		private String blog_id = new ObjectId().toString();
		
		private String blogTitle;
		private String blogContent;
		private String blogAuthorUsername;
		private Date blogCreatedTimeStamp;
		private String blogAreaOfInterest;
		private List comments;

	
		public String getBlog_id() {
			return blog_id;
		}
		public String getBlogTitle() {
			return blogTitle;
		}
		public void setBlogTitle(String blogTitle) {
			this.blogTitle = blogTitle;
		}
		public String getBlogContent() {
			return blogContent;
		}
		public void setBlogContent(String content) {
			this.blogContent = content;
		}
		public String getBlogAuthorUsername() {
			return blogAuthorUsername;
		}
		public void setAuthorUsername(String author_username) {
			this.blogAuthorUsername = author_username;
		}
		public Date getBlogTimestamp() {
			return blogCreatedTimeStamp;
		}
		public String getBlogAreaOfInterest() {
			return blogAreaOfInterest;
		}
		public void setBlogAreaOfInterest(String blogAreaOfInterest) {
			this.blogAreaOfInterest = blogAreaOfInterest;
		}
		public List getComments() {
			return comments;
		}
		public void setComments(List comments) {
			this.comments = comments;
		}		
		@PrePersist
		public void prePersist(){
			blogCreatedTimeStamp = (blogCreatedTimeStamp==null)?new Date():blogCreatedTimeStamp;
		}

		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return " blogTitle: "+blogTitle+", author_username: "+blogAuthorUsername+", timestamp: "+blogCreatedTimeStamp+", areaofinterest: "+blogAreaOfInterest+", blog_id: "+blog_id;
		}
}
