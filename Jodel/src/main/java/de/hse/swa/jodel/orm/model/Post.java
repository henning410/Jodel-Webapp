/*========================================================================*
 *                                                                        *
 * This software is governed by the GPL version 2.                        *
 *                                                                        *
 * Copyright: Joerg Friedrich, University of Applied Sciences Esslingen   *
 *                                                                        *
 * $Id:$
 *                                                                        *
 *========================================================================*/
package de.hse.swa.jodel.orm.model;

import java.io.Serializable;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

// import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * The persistent class for the POST database table.
 * 
 */
@Entity
@Table(name = "post")
public class Post implements Serializable {
	
	private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "postSeq", sequenceName = "ZSEQ_POST_ID", allocationSize = 1, initialValue = 4)
    @GeneratedValue(generator = "postSeq")
    
    @Column(name = "post_id", unique = true)
	private Integer post_id;

	@Column(name = "text", length = 450)
	private String text;

	@Column(name = "longitude")
	private Double longitude;

	@Column(name = "latitude")
	private Double latitude;

	@Column(name = "postdate")
	private String postdate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	// @OneToMany(mappedBy="post")
	// private List<Comment> comments;

	public Post() {
	}

	public Post(String text, Double longitude, Double latitude, User user, String date){
		this.text = text;
		this.longitude = longitude;
		this.latitude = latitude;
		this.user = user;
		this.postdate = date;
	}
	
	public Integer getPost_id() {
		return post_id;
	}

	public void setPost_id(Integer id) {
		this.post_id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Double getlongitude() {
		return longitude;
	}

	public void setlongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getlatitude() {
		return latitude;
	}

	public void setlatitude(Double latitude) {
		this.latitude = latitude;
	}

	public String getPostdate() {
		return postdate;
	}

	public void setPostdate(String postdate) {
		this.postdate = postdate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	// public List<Comment> getComments() {
	// 	return comments;
	// }

	// public void setComments(List<Comment> comments) {
	// 	this.comments = comments;
	// }

	// public Comment addComment(Comment comment) {
	// 	getComments().add(comment);
	// 	comment.setPost(this);
	// 	return comment;
	// }

	// public Comment removeComment(Comment comment) {
	// 	getComments().remove(comment);
	// 	comment.setPost(null);
	// 	return comment;
	// }


}