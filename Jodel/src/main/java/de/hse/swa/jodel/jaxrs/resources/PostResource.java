package de.hse.swa.jodel.jaxrs.resources;

//import java.lang.invoke.InjectedProfile;
import java.util.List;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

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
@Path("/posts")
public class PostResource {

    @Inject 
    PostDao postDao;

    @Context
    HttpServerRequest request;

    // @GET
    // @Path("getPost")
    // @Produces(MediaType.APPLICATION_JSON)
    // public Post getPost(@QueryParam("id") Integer id) {
    //     System.out.println("Im Backend Post Get angekommen");
    //     System.out.println(postDao.getPost(Long.valueOf(id)).getText());
    //     System.out.println(postDao.getPost(Long.valueOf(id)).getUser());
    //     System.out.println(postDao.getPost(Long.valueOf(id)).getlatitude());
    //     System.out.println(postDao.getPost(Long.valueOf(id)).getlongitude());
    //     return postDao.getPost(Long.valueOf(id));
    // }

    @GET
    @Path("getAllPosts")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Post> getPosts() {
        return postDao.getPosts();
    }

    @POST
    @Path("createPost")
    //@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Post createPost(@QueryParam("lat") double lat, @QueryParam("lon") double lon, @QueryParam("text") String text, @QueryParam("date") String date, @QueryParam("userid") Integer userid) {

        User tempu = new User();
        tempu.setUser_id(userid);

        Post post = new Post(text, lon, lat, tempu, date);
        // postDao.save(post);
        return postDao.addPost(post);
    }
}