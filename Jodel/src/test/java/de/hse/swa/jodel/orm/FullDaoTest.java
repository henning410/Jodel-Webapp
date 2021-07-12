package de.hse.swa.jodel.orm;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
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
class FullDaoTest {
	
    @Inject
    PostDao postdao;
    
    @Inject
    UserDao userdao;
    
    @Inject
    CommentDao commentdao;
    
    @Inject
    VotingDao votingdao;
    
    private User createUser(String prefix) {
		User user = new User();
		user.setUsername(prefix);
		user.setPassword(prefix + "Password");
		return user;
	}
	
    public void addTwoUsers() {
		User first = createUser("First");
		userdao.save(first);
		User second = createUser("Second");
		userdao.save(second);
	}
	
    private Post createPost(String content) {
		Post post = new Post();
		post.setText(content);
		return post;
	}
	
    public void addTwoPosts() {
		Post first = createPost("Das ist Post 1");
		postdao.save(first);
		Post second= createPost("Das ist Post 2");
		postdao.save(second);
	}
    
    public Comment createComment(String content) {
    	Comment comment = new Comment();
    	comment.setText(content);
    	return comment;
    }
    
    public void addTwoComments() {
    	Comment first = createComment("Das ist ein Kommentar");
    	commentdao.save(first);
    	Comment second = createComment("Das ist noch ein Kommentar");
    	commentdao.save(second);
    }
    
    public Voting createVoting(Integer value) {
    	Voting voting= new Voting();
    	voting.setValue(value);
    	return voting;
    }
    
    public void addTwoVotings() {
    	Voting first = createVoting(5);
    	votingdao.save(first);
    	Voting second = createVoting(80);
    	votingdao.save(second);
    }
    
    
    private void printPosts(List<Post> posts) {
		for(Post post : posts) {
            //printing all comments a post has
    		List<Comment> comments = commentdao.getComments(post.getPost_id());
    		
    		for (Comment comment: comments) {
    			Voting voting = votingdao.getVoting(comment.getComment_id());
    			System.out.println(post.getPost_id() + "|" + post.getText() + "|" + comment.getText() + "|" + comment.getUser().getUsername() + "|" + voting.getValue());
    		}
        }
	}
	
	@BeforeEach
	public void clearAllFromDatabase() {
		votingdao.removeAllVotings();
		commentdao.removeAllComments();
		postdao.removeAllPosts();
		userdao.deleteAllUsers();
	}
	
	@Test
	void addFullStack() {
		
		//create users
		User user1 = createUser("Max");
		userdao.addUser(user1);
		userdao.save(user1);
		User user2 = createUser("Marie");
		userdao.addUser(user2);
		userdao.save(user2);
		
		//create post with 1 comment
		Post firstPost = createPost("This is some Post");
		firstPost.setUser(user2);
		postdao.save(firstPost);
		Comment firstComment = createComment("Testkommentar");
		firstComment.setPost(firstPost);
		firstComment.setUser(user2);
		commentdao.addComment(firstComment);
		commentdao.save(firstComment);
		Voting firstVoting = createVoting(14);
		firstVoting.setComment(firstComment);
		votingdao.addVoting(firstVoting);
		
		//create post with 1 comment
		Post secondPost = createPost("Also some post");
		secondPost.setUser(user1);
		postdao.save(secondPost);
		Comment secondComment = createComment("Test Komment");
		secondComment.setPost(secondPost);
		secondComment.setUser(user1);
		commentdao.addComment(secondComment);
		commentdao.save(secondComment);
		Voting secondVoting = createVoting(1);
		secondVoting.setComment(secondComment);
		votingdao.addVoting(secondVoting);
		
		
		//create post with 2 comments
		Post thirdPost = createPost("random post meme");
		thirdPost.setUser(user1);
		postdao.save(thirdPost);
		
		Comment thirdComment = createComment("Das ist ein Kommentar");
		thirdComment.setPost(thirdPost);
		thirdComment.setUser(user1);
		commentdao.addComment(thirdComment);
		commentdao.save(thirdComment);
		Voting thirdVoting = createVoting(3);
		secondVoting.setComment(thirdComment);
		votingdao.addVoting(thirdVoting);
		
		Comment fourthComment = createComment("noch ein Kommentar");
		fourthComment.setPost(thirdPost);
		fourthComment.setUser(user2);
		commentdao.addComment(fourthComment);
		commentdao.save(fourthComment);
		Voting fourthVoting = createVoting(0);
		secondVoting.setComment(fourthComment);
		votingdao.addVoting(fourthVoting);
		
		
		List<Post> posts = postdao.getPosts();
		assertEquals(posts.size(),3);
		printPosts(posts);
		
	}
}
