/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.session.stateless.IngredientSpecificaitonSessionBeanLocal;
import ejb.session.stateless.StaffSessionBeanLocal;
import entity.IngredientSpecification;
import entity.Staff;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import util.exception.IngredientExistsException;
import util.exception.IngredientNotFoundException;
import util.exception.IngredientSpecificationNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;
import ws.datamodel.CreateIngredientSpecificationReq;


@Path("IngredientSpecification")
public class IngredientSpecificationResource {

    private final SessionBeanLookup sessionBeanLookup;
    
    private final StaffSessionBeanLocal staffSessionBeanLocal;
    private final IngredientSpecificaitonSessionBeanLocal ingredientSpecificaitonSessionBeanLocal;
    
    @Context
    private UriInfo context;

    public IngredientSpecificationResource() {

        sessionBeanLookup = new SessionBeanLookup();
        
        this.staffSessionBeanLocal = sessionBeanLookup.lookupStaffSessionBeanLocal();
        this.ingredientSpecificaitonSessionBeanLocal = sessionBeanLookup.lookupIngredientSpecificationSessionBeanLocal();   
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createIngredientSpecification(CreateIngredientSpecificationReq createIngredientSpecificationReq) {
        if (createIngredientSpecificationReq != null) {
            try {
                Staff staff = staffSessionBeanLocal.staffLogin(createIngredientSpecificationReq.getUsername(), createIngredientSpecificationReq.getPassword());
                System.out.println("********** IngredientResource.createIngredient(): Staff " + staff.getUsername() + " login remotely via web service");

                Long newIngredientSpecificationId = ingredientSpecificaitonSessionBeanLocal.createNewIngredientSpecification(createIngredientSpecificationReq.getIngredientSpecification(), createIngredientSpecificationReq.getIngredientId());

                IngredientSpecification newIngredientSpecification = ingredientSpecificaitonSessionBeanLocal.retrieveIngredientSpecificationById(newIngredientSpecificationId);

                return Response.status(Response.Status.OK).entity(newIngredientSpecification).build();
            } catch (InvalidLoginCredentialException ex) {
                return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
            } catch (IngredientExistsException ex) {
                return Response.status(Response.Status.CONFLICT).entity(ex.getMessage()).build();
            } catch (IngredientNotFoundException | IngredientSpecificationNotFoundException | UnknownPersistenceException | InputDataValidationException ex) {
                return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid create new product request").build();
        }
    }

}
