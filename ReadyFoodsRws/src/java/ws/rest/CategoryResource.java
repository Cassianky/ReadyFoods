/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.session.stateless.CategorySessionBeanLocal;
import ejb.session.stateless.StaffSessionBeanLocal;
import entity.Category;
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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import util.exception.InvalidLoginCredentialException;

/**
 * REST Web Service
 *
 * @author ngcas
 */
@Path("Category")
public class CategoryResource {

    StaffSessionBeanLocal staffSessionBeanLocal = lookupStaffSessionBeanLocal();

    CategorySessionBeanLocal categorySessionBeanLocal = lookupCategorySessionBeanLocal();

    @Context
    private UriInfo context;

    
    
    public CategoryResource() {
    }

    @Path("retrieveAllParentCategories")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllParentCategories(@QueryParam("username") String username, 
                                        @QueryParam("password") String password)
    {
        try {
        Staff staff = staffSessionBeanLocal.staffLogin(username, password);
        System.out.println("********** CategoryResource.retrieveAllParentCategories(): Staff " 
                + staff.getUsername() + " login remotely via web service");
        
        List<Category> categories = categorySessionBeanLocal.retrieveAllParentCategories();
        
        for(Category category: categories)
        {
            category.getSubCategories().clear();
            category.getRecipes().clear();
        }
        GenericEntity<List<Category>> genericEntity = new GenericEntity<List<Category>>(categories) {};
        return Response.status(Status.OK).entity(genericEntity).build();
        } catch (InvalidLoginCredentialException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    @Path("retrieveAllLeafCategories")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllLeafCategories(@QueryParam("username") String username, 
                                        @QueryParam("password") String password)
    {
        try {
        Staff staff = staffSessionBeanLocal.staffLogin(username, password);
        System.out.println("********** CategoryResource.retrieveAllLeafCategories(): Staff " 
                + staff.getUsername() + " login remotely via web service");
        
        List<Category> categories = categorySessionBeanLocal.retrieveAllSubCategories();
        
        for(Category category: categories)
        {
            category.getSubCategories().clear();
            category.getRecipes().clear();
        }
        GenericEntity<List<Category>> genericEntity = new GenericEntity<List<Category>>(categories) {};
        return Response.status(Status.OK).entity(genericEntity).build();
        } catch (InvalidLoginCredentialException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    /**
     * PUT method for updating or creating an instance of CategoryResource
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    public void putXml(String content) {
    }

    private CategorySessionBeanLocal lookupCategorySessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (CategorySessionBeanLocal) c.lookup("java:global/ReadyFoods/ReadyFoods-ejb/CategorySessionBean!ejb.session.stateless.CategorySessionBeanLocal");
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
