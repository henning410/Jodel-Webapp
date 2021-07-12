/*========================================================================*
 *                                                                        *
 * This software is governed by the GPL version 2.                        *
 *                                                                        *
 * Copyright: Joerg Friedrich, University of Applied Sciences Esslingen   *
 *                                                                        *
 * $Id:$
 *                                                                        *
 *========================================================================*/
package de.hse.swa.jodel.orm.dao;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import de.hse.swa.jodel.orm.model.Comment;
import de.hse.swa.jodel.orm.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

@ApplicationScoped
public class CommentDao {

    @Inject
	EntityManager em;

	public List<Comment> getComments(Integer postid) {
		String sql = 
		"SELECT comment.* " + 
		"FROM comment LEFT JOIN(" +
			"SELECT comment_id, " + 
			"value " +
			"FROM voting" +
		") voting ON (voting.comment_id = comment.comment_id) " +
		"WHERE comment.post_id=" + postid + 
		" ORDER BY voting.value DESC";
		Query query = em.createNativeQuery(sql, Comment.class);
		List<Comment> results = query.getResultList();
		return results;
	}

	public Comment getComment(Long id) {
		return em.find(Comment.class, id);
	}

	public List<Comment> getComments(){
		String sql = "SELECT * FROM comment";
		Query query = em.createNativeQuery(sql, Comment.class);
		List<Comment> results = query.getResultList();
		return results;
	}

	@Transactional
	public Comment save(Comment comment) {
		if (comment.getComment_id() != null) {
			comment = em.merge(comment);
		} else {
			em.persist(comment);
		}
		return comment;
	}

	// @Transactional
	// public List<Comment> getCommentsByPostID(Integer post_id){
	// 	Query query = em.createQuery("SELECT u FROM Comment u WHERE u.post IN (SELECT p FROM Post p WHERE p.post_id=:id)", Comment.class);
	// 	query.setParameter("id", post_id);
	// 	List<Comment> results = query.getResultList();
	// 	return results;

	// 	// TypedQuery<Comment> query = em.createQuery("Select u FROM Comment u WHERE post_id = " + post_id, Comment.class);
	// 	// List<Comment> results = query.getResultList();
	// 	// return results;
	// }
	
	
	@Transactional
	public Comment addComment(Comment comment) {
		em.persist(comment);
		return comment;
	}

	@Transactional
	public void removeComment(Comment comment) {
		em.remove(comment);
	}

	@Transactional
	public void removeAllComments() {
		try {
			Query del = em.createQuery("DELETE FROM Comment WHERE comment_id >= 0");
			del.executeUpdate();
		} catch (SecurityException | IllegalStateException e) {
			e.printStackTrace();
		}
		return;
	}
    

}
