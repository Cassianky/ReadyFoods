package ws.rest;

import ejb.session.stateless.IngredientSessionBeanLocal;
import ejb.session.stateless.StaffSessionBeanLocal;
import entity.Ingredient;
import entity.Staff;
import java.util.List;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import util.exception.IngredientExistsException;
import util.exception.IngredientNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateCategoryException;
import util.exception.UpdateIngredientException;
import ws.datamodel.CreateIngredientReq;
import ws.datamodel.UpdateIngredientReq;

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

    @Path("retrieveAllIngredients")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllIngredients(@QueryParam("username") String username,
            @QueryParam("password") String password) {

        try {
            Staff staff = staffSessionBeanLocal.staffLogin(username, password);
            System.out.println("********** IngredientResource.retrieveAllIngredients(): Staff " + staff.getUsername() + " login remotely via web service");

            List<Ingredient> ingredients = ingredientSessionBeanLocal.retrieveAllIngredients();

            GenericEntity<List<Ingredient>> genericIngredients = new GenericEntity<List<Ingredient>>(ingredients) {
            };

            return Response.status(Status.ACCEPTED).entity(genericIngredients).build();
        } catch (InvalidLoginCredentialException ex) {
            return Response.status(Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @Path("retrieveIngredient/{ingredientId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveIngredient(@QueryParam("username") String username,
            @QueryParam("password") String password,
            @QueryParam("ingredientId") Long ingredientId) {

        try {
            Staff staff = staffSessionBeanLocal.staffLogin(username, password);
            System.out.println("********** IngredientResource.retrieveIngredient(): Staff " + staff.getUsername() + " login remotely via web service");

            Ingredient ingredient = ingredientSessionBeanLocal.retrieveIngredientByIngredientId(ingredientId);

            GenericEntity<Ingredient> genericIngredient = new GenericEntity<Ingredient>(ingredient) {
            };

            return Response.status(Status.ACCEPTED).entity(ingredient).build();
        } catch (InvalidLoginCredentialException ex) {
            return Response.status(Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createIngredient(CreateIngredientReq createIngredientReq) {
        if (createIngredientReq != null) {
            try {
                Staff staff = staffSessionBeanLocal.staffLogin(createIngredientReq.getUsername(), createIngredientReq.getPassword());
                System.out.println("********** IngredientResource.createIngredient(): Staff " + staff.getUsername() + " login remotely via web service");

                Long newIngredientId = ingredientSessionBeanLocal.createNewIngredient(createIngredientReq.getIngredient());

                Ingredient newIngredient = ingredientSessionBeanLocal.retrieveIngredientByIngredientId(newIngredientId);

                return Response.status(Response.Status.OK).entity(newIngredient).build();
            } catch (InvalidLoginCredentialException ex) {
                return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
            } catch (IngredientExistsException ex) {
                return Response.status(Response.Status.CONFLICT).entity(ex.getMessage()).build();
            } catch (IngredientNotFoundException | UnknownPersistenceException | InputDataValidationException ex) {
                return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid create new product request").build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateIngredient(UpdateIngredientReq updateIngredientReq) {
        if (updateIngredientReq != null) {
            try {
                Staff staff = staffSessionBeanLocal.staffLogin(updateIngredientReq.getUsername(),
                        updateIngredientReq.getPassword());
                ingredientSessionBeanLocal.updateIngredient(updateIngredientReq.getIngredient());
                return Response.status(Response.Status.OK).build();
            } catch (InvalidLoginCredentialException ex) {
                return Response.status(Status.UNAUTHORIZED).entity(ex.getMessage()).build();
            } catch (UpdateIngredientException ex) {
                return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
            } catch (Exception ex) {
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid update category request").build();
        }
    }

}
