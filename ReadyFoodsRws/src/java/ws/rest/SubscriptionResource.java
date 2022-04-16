/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.session.stateless.StaffSessionBeanLocal;
import ejb.session.stateless.SubscriptionSessionBeanLocal;
import entity.Staff;
import entity.Subscription;
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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import util.exception.InvalidLoginCredentialException;

/**
 * REST Web Service
 *
 * @author angler
 */
@Path("Subscription")
public class SubscriptionResource {

    StaffSessionBeanLocal staffSessionBean = lookupStaffSessionBeanLocal();

    SubscriptionSessionBeanLocal subscriptionSessionBean = lookupSubscriptionSessionBeanLocal();
    
    
    

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of SubscriptionResource
     */
    public SubscriptionResource() {
    }
    
    @Path("retrieveAllSubscriptions")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllSubscriptions(@QueryParam("username") String username,
            @QueryParam("password") String password) {
        try {

            Staff staff = staffSessionBean.staffLogin(username, password);

            System.out.println("********** SubscriptionResource.retrieveAllSubscriptions(): Staff "
                    + staff.getUsername() + " login remotely via web service");

            List<Subscription> subscriptions = subscriptionSessionBean.retrieveAllSubscriptions();

            for (Subscription sub : subscriptions) {
                sub.setCurrentOrder(null);
                sub.getSubscriptionOrders().clear();
            }

            GenericEntity<List<Subscription>> genericEntity = new GenericEntity<List<Subscription>>(subscriptions) {
            };

            return Response.status(Status.OK).entity(genericEntity).build();
        } catch (InvalidLoginCredentialException ex) {
            return Response.status(Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    private SubscriptionSessionBeanLocal lookupSubscriptionSessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (SubscriptionSessionBeanLocal) c.lookup("java:global/ReadyFoods/ReadyFoods-ejb/SubscriptionSessionBean!ejb.session.stateless.SubscriptionSessionBeanLocal");
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
