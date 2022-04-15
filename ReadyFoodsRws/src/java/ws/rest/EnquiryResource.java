/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.session.stateless.EnquirySessionBeanLocal;
import ejb.session.stateless.StaffSessionBeanLocal;
import entity.Enquiry;
import entity.Staff;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import util.exception.EnquiryNotFoundException;
import util.exception.InvalidLoginCredentialException;
import ws.datamodel.UpdateEnquiryReq;

/**
 * REST Web Service
 *
 * @author angler
 */
@Path("Enquiry")
public class EnquiryResource {

    StaffSessionBeanLocal staffSessionBeanLocal = lookupStaffSessionBeanLocal();

    EnquirySessionBeanLocal enquirySessionBeanLocal = lookupEnquirySessionBeanLocal();

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of EnquiryResource
     */
    public EnquiryResource() {
    }

    @Path("retrieveAllEnquiries")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllEnquires(@QueryParam("username") String username,
            @QueryParam("password") String password) {
        try {

            Staff staff = staffSessionBeanLocal.staffLogin(username, password);

            System.out.println("********** EnquiryResource.retrieveAllEnquires(): Staff "
                    + staff.getUsername() + " login remotely via web service");

            List<Enquiry> enquires = enquirySessionBeanLocal.retrieveAllEnquires();

            for (Enquiry enquiry : enquires) {
                enquiry.getCustomer().getEnquiries().clear();
                enquiry.getCustomer().getFoodDiaryRecords().clear();
                enquiry.getCustomer().setCreditCard(null);
                enquiry.getCustomer().getBookedmarkedRecipes().clear();
                enquiry.getCustomer().getFoods().clear();
                enquiry.getCustomer().getOrders().clear();
                enquiry.getCustomer().getSubscriptions().clear();
                enquiry.getCustomer().setSalt(null);
                enquiry.getCustomer().setPassword(null);

            }

            GenericEntity<List<Enquiry>> genericEntity = new GenericEntity<List<Enquiry>>(enquires) {
            };

            return Response.status(Status.OK).entity(genericEntity).build();
        } catch (InvalidLoginCredentialException ex) {
            return Response.status(Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @Path("retrieveEnquiry/{enquiryId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveEnquiry(
            @QueryParam("username") String username,
            @QueryParam("password") String password,
            @PathParam("enquiryId") Long enquiryId) {
        try {

            Staff staff = staffSessionBeanLocal.staffLogin(username, password);

            System.out.println("********** EnquiryResource.retrieveEnquiry(): Staff "
                    + staff.getUsername() + " login remotely via web service");

            Enquiry enquiry = enquirySessionBeanLocal.retrieveEnquiryByEnquiryId(enquiryId);

            enquiry.setCustomer(null);

            return Response.status(Status.OK).entity(enquiry).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateEnquiryResponse(UpdateEnquiryReq updateEnquiryReq) {
        if (updateEnquiryReq != null) {
            try {
                 Staff staff = staffSessionBeanLocal.staffLogin(updateEnquiryReq.getUsername(),
                         updateEnquiryReq.getPassword());
                 System.out.println("********** Logged in to update enquiry" + (updateEnquiryReq.getEnquiry()));
                 
                 enquirySessionBeanLocal.updateEnquiry(updateEnquiryReq.getEnquiry().getEnquiryId(), 
                         updateEnquiryReq.getResponse(), updateEnquiryReq.getResolved());

                
                return Response.status(Response.Status.OK).build();

            } catch (EnquiryNotFoundException | InvalidLoginCredentialException ex) {
                Logger.getLogger(EnquiryResource.class.getName()).log(Level.SEVERE, null, ex);
                return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid update product request").build();
        }
    }

    private EnquirySessionBeanLocal lookupEnquirySessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (EnquirySessionBeanLocal) c.lookup("java:global/ReadyFoods/ReadyFoods-ejb/EnquirySessionBean!ejb.session.stateless.EnquirySessionBeanLocal");
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
