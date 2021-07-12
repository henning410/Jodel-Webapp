package de.hse.swa.jodel.orm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import javax.inject.Inject;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.hse.swa.jodel.orm.dao.PostDao;
import de.hse.swa.jodel.orm.dao.UserDao;
import de.hse.swa.jodel.orm.dao.VotingDao;
import de.hse.swa.jodel.orm.model.Comment;
import de.hse.swa.jodel.orm.model.Post;
import de.hse.swa.jodel.orm.model.User;
import de.hse.swa.jodel.orm.model.Voting;
import io.quarkus.test.junit.QuarkusTest;


@QuarkusTest
class PostDaoTest {
	
	@Inject
	PostDao postdao;
	
	private Post createPost(String content) {
		User user = new User();
		user.setUser_id(1);
		Post post = new Post();
		post.setText(content);
		post.setPostdate("2021-06-07");	
		post.setUser(user);
		return post;
	}
	
	public void addTwoPosts() {
		Post first = createPost("Das ist Post 1");
		first.setPost_id(4);
		postdao.save(first);
		Post second= createPost("Das ist Post 2");
		second.setPost_id(5);
		postdao.save(second);
	}
	
	private void printPosts(List<Post> posts) {
		for(Post post : posts) {
            System.out.println(post.getPost_id() + "|" + post.getText() + "|" + post.getUser().getUsername());
        }
	}
	
//	@BeforeEach
//	public void clearAllFromDatabase() {
//		postdao.removeAllPosts();
//	}
	
	@Test
	void addPost_2() {
		addTwoPosts();
		List<Post> posts = postdao.getPosts();
		assertEquals(posts.size(),5);
		printPosts(posts);
	}
	
}
