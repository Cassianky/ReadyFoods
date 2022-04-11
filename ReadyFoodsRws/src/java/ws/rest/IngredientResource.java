package ws.rest;

import ejb.session.stateless.IngredientSessionBeanLocal;
import ejb.session.stateless.StaffSessionBeanLocal;
import entity.Ingredient;
import entity.Staff;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import util.exception.IngredientExistsException;
import util.exception.IngredientNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;
import ws.datamodel.CreateIngredientReq;

/**
 * REST Web Service
 *
 * @author Eugene Chua
 */
@Path("Ingredient")
public class IngredientResource {

    

    @Context
    private UriInfo context;

    private final SessionBeanLookup sessionBeanLookup;
    
    private final IngredientSessionBeanLocal ingredientSessionBeanLocal;
    private final StaffSessionBeanLocal staffSessionBeanLocal;
    

    public IngredientResource() {
        
        sessionBeanLookup = new SessionBeanLookup();
        
        this.ingredientSessionBeanLocal = sessionBeanLookup.lookupIngredientSessionBeanLocal();
        this.staffSessionBeanLocal = sessionBeanLookup.lookupStaffSessionBeanLocal();
        
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createProduct(CreateIngredientReq createIngredientReq)
    {
        if(createIngredientReq != null)
        {
            try
            {
                Staff staff = staffSessionBeanLocal.staffLogin(createIngredientReq.getUsername(), createIngredientReq.getPassword());
                System.out.println("********** IngredientResource.createIngredient(): Staff " + staff.getUsername() + " login remotely via web service");
                
                Long newIngredientId  = ingredientSessionBeanLocal.createNewIngredient(createIngredientReq.getIngredient());
                
                Ingredient newIngredient = ingredientSessionBeanLocal.retrieveIngredientByIngredientId(newIngredientId);
                
                return Response.status(Response.Status.OK).entity(newIngredient).build();
            }
            catch(InvalidLoginCredentialException ex)
            {
                return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
            }
            catch(IngredientExistsException ex) {
                return Response.status(Response.Status.CONFLICT).entity(ex.getMessage()).build();
            }
            catch(IngredientNotFoundException | UnknownPersistenceException | InputDataValidationException ex) {
                return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
            }
        }
        else
        {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid create new product request").build();
        }
    }

}
