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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import javax.persistence.FetchType;
import javax.persistence.GenerationType;
import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;


/**
 * The persistent class for the VOTING database table.
 * 
 */
@Entity
@Table(name = "voting")
public class Voting implements Serializable {
	private static final long serialVersionUID = 1L;
	
    @Id
    @SequenceGenerator(name = "voteSeq", sequenceName = "ZSEQ_VOTE_ID", allocationSize = 1, initialValue = 4)
    @GeneratedValue(generator = "voteSeq")
	
	@Column(name = "voting_id")
	private Integer voting_id;

	@Column(name = "value")
	private Integer value;
	
	@ManyToOne
	@JoinColumn(name = "comment_id")
	private Comment comment;
	
	@ManyToMany(cascade = {
        CascadeType.MERGE
    })
	@JoinTable(name="voting_user", 
	joinColumns=@JoinColumn(name="voting_id"),
	inverseJoinColumns=@JoinColumn(name="user_id"))
	private Set<User> users;
	
	public Voting() {
		this.users = new HashSet();
	}

	public Voting(Comment comment){
		this.value = 0;
		this.comment = comment;
	}
	
	public Integer getVoting_id() {
		return voting_id;
	}
	
	public void setVoting_id(Integer id) {
		this.voting_id = id;
	}
	
	public Comment getComment() {
		return comment;
	}
	
	public void setComment(Comment comment) {
		this.comment= comment;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}
	
	public User addUser(User user) {
		users.add(user);
		return user;
	}

	// public User removeUser(User user) {
	// 	getUsers().remove(user);
	// 	return user;
	// }

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}
	

}