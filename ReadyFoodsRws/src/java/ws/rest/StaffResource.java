/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.session.stateless.StaffSessionBeanLocal;
import entity.Enquiry;
import entity.Ingredient;
import entity.Staff;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import util.exception.IngredientExistsException;
import util.exception.IngredientNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.StaffNotFoundException;
import util.exception.StaffUsernameExistException;
import util.exception.UnknownPersistenceException;
import ws.datamodel.CreateStaffReq;

/**
 * REST Web Service
 *
 * @author Eugene Chua
 */
@Path("Staff")
public class StaffResource {

    @Context
    private UriInfo context;

    private final SessionBeanLookup sessionBeanLookup;
    private final StaffSessionBeanLocal staffSessionBeanLocal;

    public StaffResource() {
        sessionBeanLookup = new SessionBeanLookup();
        staffSessionBeanLocal = sessionBeanLookup.lookupStaffSessionBeanLocal();
    }

    @Path("staffLogin")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response staffLogin(@QueryParam("username") String username,
            @QueryParam("password") String password) {
        try {

            Staff staff = staffSessionBeanLocal.staffLogin(username, password);
            System.out.println("********** StaffResource.staffLogin(): Staff  login remotely via web service");
            staff.setPassword(null);
            staff.setSalt(null);

            return Response.status(Response.Status.OK).entity(staff).build();
        } catch (InvalidLoginCredentialException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @Path("createStaff")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createStaff(CreateStaffReq createStaffReq) {
        if (createStaffReq != null) {
            try {
                Staff currentStaff = staffSessionBeanLocal.staffLogin(createStaffReq.getUsername(), createStaffReq.getPassword());
                System.out.println("********** StaffResource.createStaff(): Staff " + currentStaff.getUsername() + " login remotely via web service");
                Staff newStaff = createStaffReq.getNewStaff();
                System.out.println("********** StaffResource.createStaff(): Staff " + newStaff.getFirstName());
                Long newStaffId = staffSessionBeanLocal.createNewStaff(createStaffReq.getNewStaff());
                Staff newStafff = staffSessionBeanLocal.retrieveStaffByStaffId(newStaffId);

                return Response.status(Response.Status.OK).entity(newStafff).build();
            } catch (InvalidLoginCredentialException ex) {
                return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
            } catch (UnknownPersistenceException | InputDataValidationException | StaffNotFoundException ex) {
                return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
            } catch (StaffUsernameExistException ex) {
                return Response.status(Response.Status.CONFLICT).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid create new staff request").build();
        }
    }
    
    @Path("retrieveStaff/{staffId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveStaff(
            @QueryParam("username") String username,
            @QueryParam("password") String password,
            @PathParam("staffId") Long staffId) {
        try {

            Staff currentStaff = staffSessionBeanLocal.staffLogin(username, password);

            System.out.println("********** EnquiryResource.retrieveEnquiry(): Staff "
                    + currentStaff.getUsername() + " login remotely via web service");

            Staff staffToView = staffSessionBeanLocal.retrieveStaffByStaffId(staffId);

            return Response.status(Status.OK).entity(staffToView).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    @Path("retrieveAllStaff")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllEnquires(@QueryParam("username") String username,
            @QueryParam("password") String password) {
        try {

            Staff currentStaff = staffSessionBeanLocal.staffLogin(username, password);

            System.out.println("********** EnquiryResource.retrieveAllStaff(): Staff "
                    + currentStaff.getUsername() + " login remotely via web service");

            List<Staff> staffs = staffSessionBeanLocal.retrieveAllStaffs();


            GenericEntity<List<Staff>> genericEntity = new GenericEntity<List<Staff>>(staffs) {
            };

            return Response.status(Status.OK).entity(genericEntity).build();
        } catch (InvalidLoginCredentialException ex) {
            return Response.status(Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
}
