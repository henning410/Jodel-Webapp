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

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.jboss.logging.Logger;

import de.hse.swa.jodel.orm.model.User;

@ApplicationScoped
public class UserDao {

    @Inject
    EntityManager em;

	private static final Logger LOGGER = Logger.getLogger(UserDao.class);
	
	public User getUser(Integer id) {
		return em.find(User.class, id);
	}
	
	public User login(String username, String password) {
		try {
			LOGGER.debug("Checking for user name and password");
			return (User) em.createQuery("SELECT u FROM User u WHERE u.username=:username AND "
					+ "u.password=:password")
					.setParameter("username", username)
					.setParameter("password", password).getSingleResult();

		} catch(NoResultException e) {
			// User u = new User();
			// u.setUser_id(0L);
			// return u;
			return null;
		}
	}

	public List<User> getUsers() {
		Query q = em.createQuery("select c from User c");
		List<User> users = q.getResultList();
		return users;
	}

	@Transactional
	public User addUser(User user) {
		em.persist(user);
		return user;
	}

	public User getUserByGoogleId(String googleId){
		try {
			return (User) em.createQuery("SELECT u FROM User u WHERE u.googleId=:googleId").setParameter("googleId", googleId).getSingleResult();
		} catch(NoResultException e){
			return null;
		}
	}

    @Transactional
    public User save(User user) {
    	if (user.getUser_id() != null) {
    		user = em.merge(user);
    	} else {
        	em.persist(user);
    	}
    	return user;
    }
	

	@Transactional
	public void deleteUser(Long id) {

		User cm = em.find(User.class, id);
		if (cm != null) {
			em.remove(cm);
		}
	}
	
    @Transactional
    public void deleteAllUsers() {
    	try {

    	    Query del = em.createQuery("DELETE FROM User WHERE id >= 0");
    	    del.executeUpdate();

    	} catch (SecurityException | IllegalStateException  e) {
    	    e.printStackTrace();
    	}

    	return;
    }
    
    @Transactional
    public void deleteAllOrmUsers() {
    	try {

    	    Query del = em.createQuery("DELETE FROM User WHERE id >= 10");
    	    del.executeUpdate();

    	} catch (SecurityException | IllegalStateException  e) {
    	    e.printStackTrace();
    	}

    	return;
    }
}
