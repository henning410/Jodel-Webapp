package de.hse.swa.jodel.jaxquarkus;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;

import org.junit.jupiter.api.Test;

import de.hse.swa.jodel.orm.model.Post;
import de.hse.swa.jodel.orm.model.User;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.MediaType;

@QuarkusTest
public class PostResourceTest {

	@Test
	public void testPostEndpoint() {
		given()
			.when().get("/posts/getAllPosts")
			.then()
			.statusCode(200);
	}
	
    @Test
    public void testGetAllPosts() {
    	Response response = 
    			given()
    			.contentType(MediaType.APPLICATION_JSON)
    			.when()
    			.get("/posts/getAllPosts");
    	response.then()
    	.statusCode(200);
    	
    	List<Post> posts = Arrays.asList(response.getBody().as(Post[].class));
    	Integer size = posts.size();
    	assert(size.equals(3));
    }
    
    @Test
    public void testCreatePost() {
    	Response response = given()
    		.contentType(MediaType.APPLICATION_JSON)
    		.queryParam("lat", 1100.5641)
    		.queryParam("lon", 100.561)
    		.queryParam("text", "This is some test post")
    		.queryParam("date", "2021-07-06")
    		.queryParam("userid", 1)
    		.when().post("/posts/createPost");
        response.then()
        .statusCode(200);
        Post post = response.getBody().as(Post.class);
        assert(post.getPost_id().equals(4));
        assert(post.getlatitude().equals(1100.5641));
        assert(post.getlongitude().equals(100.561));
        assert(post.getText().equals("This is some test post"));
        assert(post.getPostdate().equals("2021-07-06"));
        assert(post.getUser().getUser_id().equals(1));
        
        Response response2 = 
    			given()
    			.contentType(MediaType.APPLICATION_JSON)
    			.when()
    			.get("/posts/getAllPosts");
    	response2.then()
    	.statusCode(200);
    	
    	List<Post> posts = Arrays.asList(response2.getBody().as(Post[].class));
    	Integer size = posts.size();
    	assert(size.equals(4));
        
        
    }
}