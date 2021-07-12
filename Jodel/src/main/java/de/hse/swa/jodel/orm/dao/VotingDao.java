package de.hse.swa.jodel.orm.dao;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import de.hse.swa.jodel.orm.model.User;
import de.hse.swa.jodel.orm.model.Voting;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class VotingDao {

    @Inject
    EntityManager em; 

    public Voting getVotings(Integer commentid) {
		String sql = 
		"SELECT * " +
		"FROM voting " +
		"WHERE comment_id = " + commentid;
		Query query = em.createNativeQuery(sql, Voting.class); 
    	 //TypedQuery<Voting> query = em.createQuery("SELECT d FROM Voting d ORDER BY value DESC", Voting.class);
    	//  Voting results = query.getSingleResult();
    	 return (Voting)  query.getSingleResult();
    }
    
    public Voting getVoting(Integer id) {
   	 	return em.find(Voting.class, id);
    }

	public List<Voting> getVotings(){
		String sql = "SELECT * FROM voting";
		Query query = em.createNativeQuery(sql, Voting.class); 
		List<Voting> results = query.getResultList();
    	 return results;
	}

	public Boolean checkForVoted(Integer voting_id, Integer user_id){
		try{
			Query query = em.createNativeQuery("SELECT * FROM voting_user WHERE voting_id=:voting_id AND user_id=:user_id")
				.setParameter("voting_id", voting_id)
				.setParameter("user_id", user_id);
			Object result = query.getSingleResult();
			System.out.println("User hat schon gevoted");
			return true;
		} catch(Exception e) {
				System.out.println("User hat noch nicht gevoted");
				return false;
		}
	}
    
    @Transactional
	public Voting save(Voting voting) {
			em.merge(voting);
		return voting;
	}

    @Transactional
    public void removeVoting(Voting voting) {
    	em.remove(voting);
    	return;
    }

	@Transactional
	public Voting updateVoting(Voting voting){
		em.createNativeQuery("UPDATE voting SET value=:value WHERE voting_id=:voting_id")
			.setParameter("value", voting.getValue())
			.setParameter("voting_id", voting.getVoting_id())
			.executeUpdate();
		return voting;
	}

	@Transactional
	public Boolean addUserToVoting(Integer voting_id, Integer user_id){
		try{
			Query query = em.createNativeQuery("SELECT * FROM voting_user WHERE voting_id=:voting_id AND user_id=:user_id")
				.setParameter("voting_id", voting_id)
				.setParameter("user_id", user_id);
			Object result = query.getSingleResult();
			System.out.println("Eintrag gibt es schon");
			return false;
		} catch(Exception e) {
			System.out.println("Eintrag gibt es noch nicht");
			em.createNativeQuery("INSERT INTO voting_user VALUES(?, ?)")
				.setParameter(1, voting_id)
				.setParameter(2, user_id)
				.executeUpdate();
				return true;
		}
	}
    
    @Transactional
	public Voting addVoting(Voting voting) {
		em.persist(voting);
		return voting;
	}
    
    @Transactional
    public void removeAllVotings() {
    	try {
    	    Query del = em.createQuery("DELETE FROM Voting WHERE id >= 0");
    	    del.executeUpdate();

    	} catch (SecurityException | IllegalStateException  e) {
    	    e.printStackTrace();
    	}

    	return;
    }


}
