/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.session.stateless.ReviewSessionBeanLocal;
import ejb.session.stateless.StaffSessionBeanLocal;
import entity.Customer;
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
import util.exception.StaffNotFoundException;

/**
 * REST Web Service
 *
 * @author PYT
 */
@Path("Review")
public class ReviewResource {

    StaffSessionBeanLocal staffSessionBean = lookupStaffSessionBeanLocal();

    ReviewSessionBeanLocal reviewSessionBean = lookupReviewSessionBeanLocal();
    
    
    
    

    @Context
    private UriInfo context;

    
    public ReviewResource() {
    }
    
    @Path("retrieveAllReviews")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllReviews(@QueryParam("username") String username,
            @QueryParam("password") String password) {
        try {

            Staff staff = staffSessionBean.staffLogin(username, password);

            System.out.println("********** CustomerResource.retrieveAllCustomers(): Staff "
                    + staff.getUsername() + " login remotely via web service");

            List<Review> reviews = reviewSessionBean.retrieveAllReviews();
            
            for(Review review: reviews){
                review.getRecipe().setCaloriesPerServing(null);
                review.getRecipe().setCarbsPerServing(null);
                review.getRecipe().setCategories(null);
                review.getRecipe().setComments(null);
                review.getRecipe().setCookingTime(null);
                review.getRecipe().setFatsPerServing(null);
                review.getRecipe().setIngredientSpecificationList(null);
                review.getRecipe().setPicUrl(null);
                review.getRecipe().setProteinsPerServing(null);
                review.getRecipe().setRecipeChef(null);
                review.getRecipe().setRecipeSteps(null);
                review.getRecipe().setReviews(null);
                review.getRecipe().setVideoURL(null);
                review.getRecipe().setSugarPerServing(null);
                review.getCustomer().getEnquiries().clear();
                review.getCustomer().getFoodDiaryRecords().clear();
                review.getCustomer().setCreditCard(null);
                review.getCustomer().getBookedmarkedRecipes().clear();
                review.getCustomer().getFoods().clear();
                review.getCustomer().getOrders().clear();
                review.getCustomer().getSubscriptions().clear();
                review.getCustomer().setSalt(null);
                review.getCustomer().setPassword(null);
            }


            GenericEntity<List<Review>> genericEntity = new GenericEntity<List<Review>>(reviews) {
            };

            return Response.status(Response.Status.OK).entity(genericEntity).build();
        } catch (InvalidLoginCredentialException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    @Path("deleteReview/{reviewId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteReview(
            @QueryParam("username") String username,
            @QueryParam("password") String password,
            @PathParam("reviewId") Long reviewId) {
        try {

            Staff currentStaff = staffSessionBean.staffLogin(username, password);

            System.out.println("********** ReviewResource.deleteReview(): Staff "
                    + currentStaff.getUsername() + " login remotely via web service");

             
            reviewSessionBean.deleteReview(reviewId);

            return Response.status(Response.Status.OK).build();
        } catch (InvalidLoginCredentialException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        }
    }

    

    private ReviewSessionBeanLocal lookupReviewSessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (ReviewSessionBeanLocal) c.lookup("java:global/ReadyFoods/ReadyFoods-ejb/ReviewSessionBean!ejb.session.stateless.ReviewSessionBeanLocal");
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
}
