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
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import util.exception.InvalidLoginCredentialException;
import util.exception.UpdateCategoryException;
import ws.datamodel.CreateCategoryReq;
import ws.datamodel.UpdateCategoryReq;

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
            @QueryParam("password") String password) {
        try {
            Staff staff = staffSessionBeanLocal.staffLogin(username, password);
            System.out.println("********** CategoryResource.retrieveAllParentCategories(): Staff "
                    + staff.getUsername() + " login remotely via web service");

            List<Category> categories = categorySessionBeanLocal.retrieveAllParentCategories();

            for (Category category : categories) {
                category.getSubCategories().clear();
                //parent category of parent category is null
                category.setParentCategory(null);
                category.getRecipes().clear();
            }
            GenericEntity<List<Category>> genericEntity = new GenericEntity<List<Category>>(categories) {
            };
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
            @QueryParam("password") String password) {
        try {
            Staff staff = staffSessionBeanLocal.staffLogin(username, password);
            System.out.println("********** CategoryResource.retrieveAllLeafCategories(): Staff "
                    + staff.getUsername() + " login remotely via web service");

            List<Category> categories = categorySessionBeanLocal.retrieveAllSubCategories();

            for (Category category : categories) {
                category.getParentCategory().getSubCategories().clear();
                category.getSubCategories().clear();
                category.getRecipes().clear();
            }
            GenericEntity<List<Category>> genericEntity = new GenericEntity<List<Category>>(categories) {
            };
            return Response.status(Status.OK).entity(genericEntity).build();
        } catch (InvalidLoginCredentialException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    @Path("retrieveNonDietCategories")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveNonDietCategories(@QueryParam("username") String username,
            @QueryParam("password") String password) {
        try {
            Staff staff = staffSessionBeanLocal.staffLogin(username, password);
            System.out.println("********** CategoryResource.retrieveNonDietCategories(): Staff "
                    + staff.getUsername() + " login remotely via web service");

            List<Category> categories = categorySessionBeanLocal.retrieveNonDietTypeSubCategories();

            for (Category category : categories) {
                category.getParentCategory().getSubCategories().clear();
                category.getSubCategories().clear();
                category.getRecipes().clear();
            }
            GenericEntity<List<Category>> genericEntity = new GenericEntity<List<Category>>(categories) {
            };
            return Response.status(Status.OK).entity(genericEntity).build();
        } catch (InvalidLoginCredentialException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    @Path("retrieveDietCategories")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveDietCategories(@QueryParam("username") String username,
            @QueryParam("password") String password) {
        try {
            Staff staff = staffSessionBeanLocal.staffLogin(username, password);
            System.out.println("********** CategoryResource.retrieveDietCategories(): Staff "
                    + staff.getUsername() + " login remotely via web service");

            List<Category> categories = categorySessionBeanLocal.retrieveDietTypeSubCategories();

            for (Category category : categories) {
                category.getParentCategory().getSubCategories().clear();
                category.getSubCategories().clear();
                category.getRecipes().clear();
            }
            GenericEntity<List<Category>> genericEntity = new GenericEntity<List<Category>>(categories) {
            };
            return Response.status(Status.OK).entity(genericEntity).build();
        } catch (InvalidLoginCredentialException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCategory(CreateCategoryReq createCategoryReq) {
        if (createCategoryReq != null) {
            try {
                Staff staff = staffSessionBeanLocal.staffLogin(createCategoryReq.getUsername(), createCategoryReq.getPassword());
                
                Category categoryCreated = categorySessionBeanLocal.createNewCategory(createCategoryReq.getCategory(), createCategoryReq.getParentId());
                
                return Response.status(Response.Status.OK).entity(categoryCreated.getCategoryId()).build();
            } catch (InvalidLoginCredentialException ex) {
                return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
            } catch (Exception ex) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }

        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid create new category request").build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCategory(UpdateCategoryReq updateCategoryReq)
    {
        //Reusing CreateCategoryReq for update Category
        if(updateCategoryReq != null)
        {
            System.out.print("CHECK LOGINNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN");
            try {
                 System.out.print("$$$$$$$$$$$$$$$$$" + updateCategoryReq.getUsername());
                 System.out.print("$$$$$$$$$$$$$$$$$" + updateCategoryReq.getPassword());
                 System.out.print("$$$$$$$$$$$$$$$$$" + updateCategoryReq.getCategory());
                Staff staff = staffSessionBeanLocal.staffLogin(updateCategoryReq.getUsername(), 
                        updateCategoryReq.getPassword());
                System.out.println("********** CategoryResource.updateCategory(): Staff " + staff.getUsername() + " login remotely via web service");
                System.out.print("CHECK LOGINNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN 22222222222222222222");
                categorySessionBeanLocal.updateCategory(updateCategoryReq.getCategory());
                System.out.print("CHECK LOGINNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN 33333333333333333333");
                
                return Response.status(Response.Status.OK).build();
                
            } catch(InvalidLoginCredentialException ex)
            {
                return Response.status(Status.UNAUTHORIZED).entity(ex.getMessage()).build();
            } catch(UpdateCategoryException ex)
            {
                return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
            }
            catch(Exception ex)
            {
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        } else
        {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid update category request").build();
        }
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
