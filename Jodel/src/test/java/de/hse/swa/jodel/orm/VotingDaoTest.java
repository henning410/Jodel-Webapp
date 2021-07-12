package de.hse.swa.jodel.orm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import javax.inject.Inject;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.hse.swa.jodel.orm.dao.UserDao;
import de.hse.swa.jodel.orm.dao.VotingDao;
import de.hse.swa.jodel.orm.model.Comment;
import de.hse.swa.jodel.orm.model.Post;
import de.hse.swa.jodel.orm.model.User;
import de.hse.swa.jodel.orm.model.Voting;
import io.quarkus.test.junit.QuarkusTest;


@QuarkusTest
class VotingDaoTest {
	
	@Inject
	VotingDao votingdao;
	
	private Voting createVoting(Integer value) {
		Voting voting = new Voting();
		voting.setValue(value);
		return voting;
	}
	
	public void addTwoVotings() {
		Comment comment = new Comment();
		comment.setComment_id(1);
		Voting first = createVoting(10);
		first.setComment(comment);
		votingdao.save(first);
		Voting second= createVoting(20);
		second.setComment(comment);
		votingdao.save(second);
	}
	
	private void printVotings(List<Voting> votings) {
		for(Voting voting : votings) {
            System.out.println(voting.getVoting_id()+ "|" + voting.getValue() + "|" + voting.getComment().getComment_id());
        }
	}
	
	@BeforeEach
	public void clearAllFromDatabase() {
		votingdao.removeAllVotings();
	}
	
	
	@Test
	void addVoting_2() {
		addTwoVotings();
		List<Voting> votings = votingdao.getVotings();
		assertEquals(votings.size(),2);
		printVotings(votings);
	}
	
	
}
