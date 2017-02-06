package com.cmad.model;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity
public class Blog {

		@Id
		private String blog_id = new ObjectId().toString();
		
		private String blogTitle;
		private String content;
		private String author_username;
		private String timestamp;
		private String areaofinterest;

		public String getBlog_id() {
			return blog_id;
		}
		public void setBlog_id(String blog_id) {
			this.blog_id = blog_id;
		}
		public String getBlogTitle() {
			return blogTitle;
		}
		public void setBlogTitle(String blogTitle) {
			this.blogTitle = blogTitle;
		}
		public String getContent() {
			return content;
		}
		public void setContent(String content) {
			this.content = content;
		}
		public String getAuthor_username() {
			return author_username;
		}
		public void setAuthor_username(String author_username) {
			this.author_username = author_username;
		}
		public String getTimestamp() {
			return timestamp;
		}
		public void setTimestamp(String timestamp) {
			this.timestamp = timestamp;
		}
		public String getAreaofinterest() {
			return areaofinterest;
		}
		public void setAreaofinterest(String areaofinterest) {
			this.areaofinterest = areaofinterest;
		}
		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return "BlogDetail  blogTitle: "+blogTitle+", author_username: "+author_username+", timestamp: "+timestamp+", areaofinterest: "+areaofinterest;
		}

}
