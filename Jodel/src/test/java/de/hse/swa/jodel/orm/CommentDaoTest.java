package de.hse.swa.jodel.orm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import javax.inject.Inject;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.hse.swa.jodel.orm.dao.CommentDao;
import de.hse.swa.jodel.orm.dao.PostDao;
import de.hse.swa.jodel.orm.dao.UserDao;
import de.hse.swa.jodel.orm.dao.VotingDao;
import de.hse.swa.jodel.orm.model.Comment;
import de.hse.swa.jodel.orm.model.Post;
import de.hse.swa.jodel.orm.model.User;
import de.hse.swa.jodel.orm.model.Voting;
import io.quarkus.test.junit.QuarkusTest;


@QuarkusTest
class CommentDaoTest {
	
	@Inject
	CommentDao commentdao;
	
	@Inject
	VotingDao votingdao;
	
	private Comment createComment(String content) {
		Comment post = new Comment();
		post.setText(content);
		return post;
	}
	
	public void addTwoComments() {
		User user = new User();
		user.setUser_id(1);
		Comment first = createComment("Das ist ein Kommentar");
		first.setUser(user);
		commentdao.save(first);
		Comment second= createComment("Das ist noch ein Kommentar");
		second.setUser(user);
		commentdao.save(second);
	}
	
	private void printComments(List<Comment> comments) {
		for(Comment comment : comments) {
            System.out.println(comment.getComment_id() + "|" + comment.getText() + "|" + comment.getUser().getUsername());
        }
	}
	
	@BeforeEach
	public void clearAllFromDatabase() {
		votingdao.removeAllVotings();
		commentdao.removeAllComments();
	}
	
	
	@Test
	void addComments_2() {
		addTwoComments();
		List<Comment> comments = commentdao.getComments();
		assertEquals(comments.size(),2);
		printComments(comments);
	}
	
}
