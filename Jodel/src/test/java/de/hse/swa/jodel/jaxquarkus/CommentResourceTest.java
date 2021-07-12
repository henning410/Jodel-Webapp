package de.hse.swa.jodel.jaxquarkus;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;

import org.junit.jupiter.api.Test;

import de.hse.swa.jodel.orm.model.Comment;
import de.hse.swa.jodel.orm.model.Post;
import de.hse.swa.jodel.orm.model.User;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.MediaType;

@QuarkusTest
public class CommentResourceTest {

	@Test
	public void testCommentEndpoint() {
		given()
			.when().get("/comments/getComments")
			.then()
			.statusCode(200);
	}
	
    @Test
    public void testGetCommentsForPost() {
    	Response response = 
    			given()
    			.contentType(MediaType.APPLICATION_JSON)
    			.queryParam("postid", 2)
    			.when()
    			.get("/comments/getComments");
    	response.then()
    	.statusCode(200);
    	
    	List<Comment> comments = Arrays.asList(response.getBody().as(Comment[].class));
    	Integer size = comments.size();
    	assert(size.equals(2));
    }
    
  
    @Test
    public void testCreateComment() {
    	//First create comment and then check, if number of comments increased for that post
    	Response response = given()
    		.contentType(MediaType.APPLICATION_JSON)
    		.queryParam("lat", 1100.5641)
    		.queryParam("lon", 100.561)
    		.queryParam("date", "2021-07-06")
    		.queryParam("text", "This is some test comment")
    		.queryParam("postid", 2)
    		.queryParam("userid", 2)
    		.when().post("/comments/createComment");
        response.then()
        .statusCode(200);
        Comment comment= response.getBody().as(Comment.class);
        assert(comment.getPost().getPost_id().equals(2));
        assert(comment.getLatitude().equals(1100.5641));
        assert(comment.getLongitude().equals(100.561));
        assert(comment.getText().equals("This is some test comment"));
        assert(comment.getPostdate().equals("2021-07-06"));
        assert(comment.getUser().getUser_id().equals(2));
        
        Response response2 = 
    			given()
    			.contentType(MediaType.APPLICATION_JSON)
    			.queryParam("postid", 2)
    			.when()
    			.get("/comments/getComments");
    	response2.then()
    	.statusCode(200);
    	
    	List<Comment> comments = Arrays.asList(response2.getBody().as(Comment[].class));
    	Integer size = comments.size();
    	assert(size.equals(3));
    }
}