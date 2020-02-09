package cn.mon.entity;

import java.util.Date;

public class Comment {
	private String author;
	
	private String content;
	
	private Date commentTime;
	
	
	public Comment() {
		super();
	}

	public Comment(String author, String content, Date commentTime) {
		super();
		this.author = author;
		this.content = content;
		this.commentTime = commentTime;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Date getCommentTime() {
		return commentTime;
	}

	public void setCommentTime(Date commentTime) {
		this.commentTime = commentTime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "Comment [author=" + author + ", commentTime=" + commentTime
				+ ", content=" + content + "]";
	}

}