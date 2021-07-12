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

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


// import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * The persistent class for the TUSER database table.
 * 
 */
@Entity
@Table(name = "user")
public class User implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
    @Id
    @SequenceGenerator(name = "userSeq", sequenceName = "ZSEQ_TUSER_ID", allocationSize = 1, initialValue = 3)
    @GeneratedValue(generator = "userSeq")
    
    @Column(name = "user_id")
    private Integer user_id;

    @Column(name = "username", length=64, unique = true)
    private String username;
    
    @Column(name = "password", length=64)
    private String password;

	@Column(name="googleId", unique= true)
	private String googleId;

	// bi-directional one-to-many association to posts
	// @OneToMany(mappedBy="user")
	// private Set<Post> posts;

	// @OneToMany(mappedBy="user")
	// private List<Comment> comments  = new ArrayList<>();
	
	@ManyToMany(mappedBy="users")
	private Set<Voting> votings;

	public User() {
		this.votings = new HashSet();
	}

	public User(String name) {
		this.username = name;
	}

	public Integer getUser_id() {
		return user_id;
	}

	public void setUser_id(Integer id) {
		this.user_id = id;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String name) {
		this.username = name;
	}
	
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getGoogleId(){
		return this.googleId;
	}

	public void setGoogleId(String googleId){
		this.googleId = googleId;
	}

	// public List<Post> getPosts() {
	// 	return posts;
	// }

	// public void setPosts(List<Post> posts) {
	// 	this.posts = posts;
	// }

	// public Post addPost(Post post) {
	// 	getPosts().add(post);
	// 	return post;
	// }

	// public Post removePost(Post post) {
	// 	getPosts().remove(post);
	// 	return post;
	// }

	// public List<Comment> getComments() {
	// 	return comments;
	// }

	// public void setComments(List<Comment> comments) {
	// 	this.comments = comments;
	// }

	// public Comment addComment(Comment comment) {
	// 	getComments().add(comment);
	// 	return comment;
	// }

	// public Comment removeComment(Comment comment) {
	// 	getComments().remove(comment);
	// 	return comment;
	// }
	
	// public Set<Voting> getVotings() {
	// 	return votings;
	// }

	// public void setVotings(Set<Voting> votings) {
	// 	this.votings = votings;
	// }

	// public Voting addVoting(Voting voting) {
	// 	getVotings().add(voting);
	// 	return voting;
	// }

	// public Voting removeVoting(Voting voting) {
	// 	getVotings().remove(voting);
	// 	return voting;
	// }
	
}