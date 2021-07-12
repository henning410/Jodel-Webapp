package de.hse.swa.jodel.jaxquarkus;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;

import org.junit.jupiter.api.Test;

import de.hse.swa.jodel.orm.model.Comment;
import de.hse.swa.jodel.orm.model.Post;
import de.hse.swa.jodel.orm.model.User;
import de.hse.swa.jodel.orm.model.Voting;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.MediaType;

@QuarkusTest
public class VotingResourceTest {

	@Test
	public void testVotingEndpoint() {
		given()
			.queryParam("id", 1)
			.when().get("/votings/getVoting")
			.then()
			.statusCode(200);
	}
	
    @Test
    public void testGetVotingForComment() {
    	Response response = 
    			given()
    			.contentType(MediaType.APPLICATION_JSON)
    			.queryParam("id", 2)
    			.when()
    			.get("/votings/getVoting");
    	response.then()
    	.statusCode(200);
    	
    	Voting voting= response.getBody().as(Voting.class);
    	assert(voting.getValue().equals(3));
    }
    
    @Test
    public void testCheckIfUserHasVoted() {
    	given()
		.queryParam("votingid", 1)
		.queryParam("userid", 1)
		.when().get("/votings/getVoted")
		.then()
		.statusCode(200)
		.body(is("false"));
    }
    
    @Test 
    public void testCreateVoting() {
    	//first we need to create some comment to create voting for that comment
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
    	//create voting for that comment
    	Response response2 = given()
    			.contentType(MediaType.APPLICATION_JSON)
    			.queryParam("commentid", 4)
    			.when().post("/votings/createVoting");
    	response2.then()
    	.statusCode(200);
    	
    	Voting voting = response2.getBody().as(Voting.class);
    	assert(voting.getComment().getComment_id().equals(4));
    	assert(voting.getValue().equals(0));
    }
    
    @Test
    public void testUpdateVoting() {
    	Response response = given()
    			.contentType(MediaType.APPLICATION_JSON)
    			.queryParam("votingid", 1)
    			.queryParam("value", 15)
    			.queryParam("commentid", 1)
    			.queryParam("userid", 2)
    			.when().post("/votings/updateVoting");
    	
    	response.then()
    		.statusCode(200);
    	
    	Voting voting = response.getBody().as(Voting.class);
    	assert(voting.getValue().equals(15));
    	assert(voting.getVoting_id().equals(1));
    	assert(voting.getUsers().iterator().next().getUser_id().equals(2));
    }
}