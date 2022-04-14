/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.session.stateless.StaffSessionBeanLocal;
import entity.Staff;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import util.exception.InvalidLoginCredentialException;

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
                                @QueryParam("password") String password)
    {
        try
        {

            Staff staff = staffSessionBeanLocal.staffLogin(username, password);
            System.out.println("********** StaffResource.staffLogin(): Staff  login remotely via web service");
            staff.setPassword(null);
            staff.setSalt(null);
            
            return Response.status(Response.Status.OK).entity(staff).build();
        }
        catch(InvalidLoginCredentialException ex)
        {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        }
        catch(Exception ex)
        {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
}