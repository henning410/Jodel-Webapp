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
@Path("/comments")
public class CommentResource {

    @Inject 
    CommentDao commentDao;

    @Context
    HttpServerRequest request;

    @GET
    @Path("getComments")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Comment> getComments(@QueryParam("postid") Integer postid){
        System.out.println("Versuche comments zu laden: " + postid);
        return commentDao.getComments(postid);
    }

    @POST
    @Path("createComment")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Comment createComment(@QueryParam("lat") double lat, @QueryParam("lon") double lon, @QueryParam("date") String date, @QueryParam("text") String text, @QueryParam("postid") Integer postid, @QueryParam("userid") Integer userid) {

        System.out.println(lat);
        System.out.println(lon);
        System.out.println(date);
        System.out.println(text);
        System.out.println(postid);
        System.out.println(userid);

        User tempu = new User();
        tempu.setUser_id(userid);

        Post tempp = new Post();
        tempp.setPost_id(postid);

        Comment comment = new Comment(text, lon, lat, tempu, date, tempp);
        // commentDao.save(comment);
        return commentDao.addComment(comment);
    }

}