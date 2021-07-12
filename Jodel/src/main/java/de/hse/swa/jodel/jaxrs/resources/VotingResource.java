package de.hse.swa.jodel.jaxrs.resources;

//import java.lang.invoke.InjectedProfile;
import java.util.List;
import java.util.Iterator;
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
import de.hse.swa.jodel.orm.dao.VotingDao;
import de.hse.swa.jodel.orm.model.User;
import de.hse.swa.jodel.orm.model.Voting;
import io.vertx.core.http.HttpServerRequest;

@RequestScoped
@Path("/votings")
public class VotingResource {

    @Inject
    VotingDao votingDao;

    @Inject
    CommentDao commentDao;

    @Inject
    UserDao userDao;
    
    @Context
    HttpServerRequest request;
    
    @GET
    @Path("getVoting")
    @Produces(MediaType.APPLICATION_JSON)
    public Voting getVotings(@QueryParam("id") Integer commentid) {
        System.out.println("Try to get voting with id: " + commentid);
        return votingDao.getVotings(commentid);
    }

    @GET
    @Path("getVoted")
    @Produces(MediaType.APPLICATION_JSON)
    public Boolean getVoted(@QueryParam("votingid") Integer votingid, @QueryParam("userid") Integer userid) {
        System.out.println("Check for voting: " + votingid + ", " + userid);
        return votingDao.checkForVoted(votingid, userid);
    }

    @POST
    @Path("createVoting")
    //@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Voting createVoting(@QueryParam("commentid") Integer commentid) {
        System.out.println("KommentID: " + commentid);
        Comment tempc = new Comment();
        tempc.setComment_id(commentid);

        Voting voting = new Voting(tempc);
        return votingDao.addVoting(voting);
    }

    @POST
    @Path("updateVoting")
    //@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Voting updateVoting(@QueryParam("votingid") Integer votingid, @QueryParam("value") Integer value, @QueryParam("commentid") Integer commentid, @QueryParam("userid") Integer userid) {

        if(votingDao.addUserToVoting(votingid, userid)){
            System.out.println("Value muss erhöht werden");

            Comment c = new Comment();
            c.setComment_id(commentid);

            Voting v = new Voting();
            v.setVoting_id(votingid);
            v.setValue(value);
            v.setComment(c);
            votingDao.updateVoting(v);
        }
        return votingDao.getVoting(votingid);
    }
    
}