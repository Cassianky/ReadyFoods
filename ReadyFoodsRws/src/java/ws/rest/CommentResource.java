/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.session.stateless.CommentSessionBeanLocal;
import ejb.session.stateless.RecipeSessionBeanLocal;
import ejb.session.stateless.StaffSessionBeanLocal;
import entity.CommentEntity;
import entity.Recipe;
import entity.Review;
import entity.Staff;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import util.exception.InvalidLoginCredentialException;

/**
 * REST Web Service
 *
 * @author PYT
 */
@Path("Comment")
public class CommentResource {

    CommentSessionBeanLocal commentSessionBean = lookupCommentSessionBeanLocal();

    StaffSessionBeanLocal staffSessionBean = lookupStaffSessionBeanLocal();

    RecipeSessionBeanLocal recipeSessionBean = lookupRecipeSessionBeanLocal();
    
    
    
    

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of CommentResource
     */
    public CommentResource() {
    }

    
    @Path("retrieveCommentsForRecipe/{recipeId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllComments(
            @QueryParam("username") String username,
            @QueryParam("password") String password,
            @PathParam("recipeId") Long recipeId) {
        try {

            Staff staff = staffSessionBean.staffLogin(username, password);

            System.out.println("********** CommentResource.retrieveAllComments(): Staff "
                    + staff.getUsername() + " login remotely via web service");
            Recipe r = recipeSessionBean.retrieveRecipeByRecipeId(recipeId);

            List<CommentEntity> comments = recipeSessionBean.getAllComments(r);
            
            
            GenericEntity<List<CommentEntity>> genericEntity = new GenericEntity<List<CommentEntity>>(comments) {
            };

            return Response.status(Response.Status.OK).entity(genericEntity).build();
        } catch (InvalidLoginCredentialException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    @Path("deleteComment/{commentEntityId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteComment(
            @QueryParam("username") String username,
            @QueryParam("password") String password,
            @QueryParam("recipeId") String recipeId,
            @PathParam("commentEntityId") Long commentEntityId) {
        try {
            System.out.println("********************************" + recipeId);

            Staff currentStaff = staffSessionBean.staffLogin(username, password);

            System.out.println("********** ReviewResource.deleteReview(): Staff "
                    + currentStaff.getUsername() + " login remotely via web service");
            
            Long thing = Long.valueOf(recipeId);

             commentSessionBean.deleteComment(commentEntityId, thing);
            

            return Response.status(Response.Status.OK).build();
        } catch (InvalidLoginCredentialException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        }
    }

    private RecipeSessionBeanLocal lookupRecipeSessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (RecipeSessionBeanLocal) c.lookup("java:global/ReadyFoods/ReadyFoods-ejb/RecipeSessionBean!ejb.session.stateless.RecipeSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    private StaffSessionBeanLocal lookupStaffSessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (StaffSessionBeanLocal) c.lookup("java:global/ReadyFoods/ReadyFoods-ejb/StaffSessionBean!ejb.session.stateless.StaffSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    private CommentSessionBeanLocal lookupCommentSessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (CommentSessionBeanLocal) c.lookup("java:global/ReadyFoods/ReadyFoods-ejb/CommentSessionBean!ejb.session.stateless.CommentSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
