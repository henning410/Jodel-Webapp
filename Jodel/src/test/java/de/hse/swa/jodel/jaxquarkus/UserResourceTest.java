package de.hse.swa.jodel.jaxquarkus;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;

import org.junit.jupiter.api.Test;

import de.hse.swa.jodel.orm.model.User;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class UserResourceTest {

	@Test
	public void testUserEndpoint() {
		given()
			.when().get("users")
			.then()
			.statusCode(200);
	}
	
    @Test
    public void testGetByID() {
    	User user = new User();
    	user.setUser_id(1);
    	user.setUsername("heweit00");
    	user.setPassword("1234");

    	given()
    		.queryParam("id", 1)
    		.when().get("/users/id")
    		.then()
    		.statusCode(200)
    		.body("user_id", is(1),
    				"username", is("heweit00"),
    				"password", is("1234")
    		);
    }
    
    @Test
    public void testCreateUser() {
    	RestAssured.registerParser("text/plain", Parser.JSON);
    	User user = new User();
    	user.setUsername("Maximilian");
    	user.setGoogleId("132135");

    	given()
    		.queryParam("googleId", "132135")
    		.queryParam("username", "Maximilian")
    		.when().post("/users/createUser")
        	.then()
        	.statusCode(200)
        	.body("user_id", is(3),
        			"username", is("Maximilian"),
        			"googleId", is("132135")
        	);
    }
}