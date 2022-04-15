/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.session.stateless.OrderEntitySessionBeanLocal;
import ejb.session.stateless.StaffSessionBeanLocal;
import entity.Customer;
import entity.OrderEntity;
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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import util.exception.InvalidLoginCredentialException;

/**
 * REST Web Service
 *
 * @author PYT
 */
@Path("OrderEntity")
public class OrderEntityResource {

    StaffSessionBeanLocal staffSessionBean = lookupStaffSessionBeanLocal();

    OrderEntitySessionBeanLocal orderEntitySessionBean = lookupOrderEntitySessionBeanLocal();   
    
    
   
    @Context
    private UriInfo context;

   
    public OrderEntityResource() {
    }
    
    @Path("retrieveAllOrders")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllEnquires(@QueryParam("username") String username,
            @QueryParam("password") String password) {
        try {

            Staff staff = staffSessionBean.staffLogin(username, password);

            System.out.println("********** CustomerResource.retrieveAllOrders(): Staff "
                    + staff.getUsername() + " login remotely via web service");

            List<OrderEntity> orders = orderEntitySessionBean.retrieveAllOrders();

            for (OrderEntity order : orders) {
                System.out.println("****************************");
                System.out.println(order.getOrderEntityId());
                order.setDateForDelivery(null);
                
                
                order.getCustomer().getEnquiries().clear();
                order.getCustomer().getFoodDiaryRecords().clear();
                order.getCustomer().setCreditCard(null);
                order.getCustomer().getBookedmarkedRecipes().clear();
                order.getCustomer().getFoods().clear();
                order.getCustomer().getOrders().clear();
                order.getCustomer().getSubscriptions().clear();
                order.getCustomer().setSalt(null);
                order.getCustomer().setPassword(null);

            }

            GenericEntity<List<OrderEntity>> genericEntity = new GenericEntity<List<OrderEntity>>(orders) {
            };

            return Response.status(Status.OK).entity(genericEntity).build();
        } catch (InvalidLoginCredentialException ex) {
            return Response.status(Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    

    private OrderEntitySessionBeanLocal lookupOrderEntitySessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (OrderEntitySessionBeanLocal) c.lookup("java:global/ReadyFoods/ReadyFoods-ejb/OrderEntitySessionBean!ejb.session.stateless.OrderEntitySessionBeanLocal");
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
