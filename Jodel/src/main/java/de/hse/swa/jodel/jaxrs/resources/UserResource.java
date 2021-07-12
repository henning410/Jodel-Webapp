package de.hse.swa.jodel.jaxrs.resources;

//import java.lang.invoke.InjectedProfile;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.*;

import de.hse.swa.jodel.orm.dao.CommentDao;
import de.hse.swa.jodel.orm.dao.PostDao;
import de.hse.swa.jodel.orm.model.Comment;
import de.hse.swa.jodel.orm.model.Post;
import de.hse.swa.jodel.orm.dao.UserDao;
import de.hse.swa.jodel.orm.model.User;
import io.vertx.core.http.HttpServerRequest;

@RequestScoped
@Path("/users")
public class UserResource {

    @Inject
    UserDao userDao;

    @Inject
    CommentDao commentDao;
    
    @Context
    HttpServerRequest request;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> getUsers() {
        return userDao.getUsers();
    }
    
    @GET
    @Path("id")
    @Produces(MediaType.APPLICATION_JSON)
    public User getUser(@QueryParam("id") Integer id) {
        return userDao.getUser(id);
    }

    @GET
    @Path("getByGoogleId")
    @Produces(MediaType.APPLICATION_JSON)
    public User getUserByGoogleId(@QueryParam("id") String id){
        System.out.println("Suche nach User mit id: " + id);
        return userDao.getUserByGoogleId(id);
    }

    @POST
    @Path("createUser")
    // @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public User createUser(@QueryParam("googleId") String googleId, @QueryParam("username") String username){
        System.out.print("Erstelle Google User mit id: " + googleId);
        User tempu = new User();
        tempu.setGoogleId(googleId);
        tempu.setUsername(username);
        return userDao.addUser(tempu);
    }

    /**
     * Update an existing user or create a new one
     * @param user
     * @return the updated user
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public User addUser(User user) {
        return userDao.save(user);
    } 
    
    /**
     * Create a new user
     * @param user
     * @return the new user
     */
    @POST
    @Path("signUp")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public User updateUser(@QueryParam("username") String username, @QueryParam("password") String password) {
        System.out.println("User erstellen");
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        return userDao.save(user);
    }
    
    /**
     * Create a new user
     * @param user
     * @return the new user
     */
    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public User login(User user) {
        System.out.println(user.getUsername());
        return userDao.login(user.getUsername(), user.getPassword());
    }
    
    
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public void removeAllUsers() {
    	userDao.deleteAllUsers();
    }
}