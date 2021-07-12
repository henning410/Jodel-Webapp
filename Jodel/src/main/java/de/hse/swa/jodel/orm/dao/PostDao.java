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
import de.hse.swa.jodel.orm.model.Post;

import java.util.List;

@ApplicationScoped
public class PostDao {

    @Inject
	EntityManager em;
	
	//Make sure posts are sorted by newest date
	public List<Post> getPosts(){
		String sql = 
		"SELECT post.* " +
		"FROM post LEFT JOIN (" +
			"SELECT post_id, " + 
			"COUNT(*) AS SumOfComments " +
			"FROM comment " +
			"GROUP BY post_id" +
		") comment ON (comment.post_id = post.post_id) " +
		"ORDER BY post.postdate DESC, comment.SumOfComments DESC";
		Query query = em.createNativeQuery(sql, Post.class);
		List<Post> results = query.getResultList();
		// System.out.println(results.get(0).getPostdate());
		return results;
	}
	
	public Post getPost(Long id) {
		return em.find(Post.class, id);
	}
	
	@Transactional
	public Post save(Post post) {
		if(post.getPost_id() != null) {
			post = em.merge(post);
		}else {
			em.persist(post);
		}
		return post;
	}
	
	@Transactional 
	public void removePost(Post post) {
		em.remove(post);
	}
	
	@Transactional
	public Post addPost(Post post) {
		em.persist(post);
		return post;
	}
	
	@Transactional
	public void removeAllPosts() {
		try {
			Query del = em.createQuery("DELETE FROM Post WHERE post_id >= 0");
			del.executeUpdate();
		} catch(SecurityException | IllegalStateException e) {
			e.printStackTrace();
		}
		return;
	}
	
	public Boolean login(String name, String password) {
		String queryString = "SELECT u FROM Post AS u WHERE u.name = :uname";
		
		TypedQuery<Post> checkCredentials = em.createQuery(queryString, Post.class);
		checkCredentials.setParameter("uname", name);
		List<Post> results = checkCredentials.getResultList();
		return (results.size() > 0);
	}


}
