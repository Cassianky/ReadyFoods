package ws.rest;

import ejb.session.stateless.RecipeSessionBeanLocal;
import ejb.session.stateless.StaffSessionBeanLocal;
import entity.Category;
import entity.Recipe;
import entity.Staff;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import util.exception.CategoryNotFoundException;
import util.exception.CreateRecipeException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.RecipeNotFoundException;
import util.exception.RecipeTitleExistException;
import util.exception.UnknownPersistenceException;
import ws.datamodel.CreateRecipeReq;

@Path("Recipe")
public class RecipeResource {

    @Context
    private UriInfo context;

    private final SessionBeanLookup sessionBeanLookup;

    private final RecipeSessionBeanLocal recipeSessionBeanLocal;
    private final StaffSessionBeanLocal staffSessionBeanLocal;

    public RecipeResource() {

        sessionBeanLookup = new SessionBeanLookup();

        this.staffSessionBeanLocal = sessionBeanLookup.lookupStaffSessionBeanLocal();
        this.recipeSessionBeanLocal = sessionBeanLookup.lookupRecipeSessionBeanLocal();
    }

    @Path("retrieveAllRecipes")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllRecipes(@QueryParam("username") String username,
            @QueryParam("password") String password) {

        try {
            Staff staff = staffSessionBeanLocal.staffLogin(username, password);
            System.out.println("********** RecipeResource.retrieveAllRecipes(): Staff " + staff.getUsername() + " login remotely via web service");

            List<Recipe> recipes = recipeSessionBeanLocal.retrieveAllRecipes();

            for (Recipe r : recipes) {
                //r.getCategories().clear();

                for (Category c : r.getCategories()) {
                    c.setParentCategory(null);
                    c.getRecipes().clear();
                }
            }

            GenericEntity<List<Recipe>> genericRecipes = new GenericEntity<List<Recipe>>(recipes) {
            };

            return Response.status(Response.Status.ACCEPTED).entity(genericRecipes).build();
        } catch (InvalidLoginCredentialException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @Path("retrieveRecipe/{recipeId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveRecipe(@QueryParam("username") String username,
            @QueryParam("password") String password,
            @QueryParam("recipeId") Long recipeId) {

        try {
            Staff staff = staffSessionBeanLocal.staffLogin(username, password);
            System.out.println("********** RecipeResource.retrieveRecipe(): Staff " + staff.getUsername() + " login remotely via web service");

            Recipe recipe = recipeSessionBeanLocal.retrieveRecipeByRecipeId(recipeId);

            for (Category c : recipe.getCategories()) {
                c.getRecipes().clear();
            }

//            recipe.getCategories().clear();
            GenericEntity<Recipe> genericRecipe = new GenericEntity<Recipe>(recipe) {
            };

            return Response.status(Response.Status.ACCEPTED).entity(recipe).build();
        } catch (InvalidLoginCredentialException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createRecipe(CreateRecipeReq createRecipeReq) {
        if (createRecipeReq != null) {
            try {
                Staff staff = staffSessionBeanLocal.staffLogin(createRecipeReq.getUsername(), createRecipeReq.getPassword());
                System.out.println("********** RecipeResource.createRecipe(): Staff " + staff.getUsername() + " login remotely via web service");
                System.out.println("******cate size***: " + createRecipeReq.getCategoryIds().size());
                System.out.println("******ingre spec size***: " + createRecipeReq.getIngredientSpecificationIds().size());
                Recipe newRecipe = recipeSessionBeanLocal.createNewRecipe(createRecipeReq.getRecipe(), createRecipeReq.getCategoryIds(), createRecipeReq.getIngredientSpecificationIds());
                
                return Response.status(Response.Status.OK).entity(newRecipe.getRecipeId()).build();
            } catch (InvalidLoginCredentialException ex) {
                return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
            } catch (RecipeTitleExistException ex) {
                return Response.status(Response.Status.CONFLICT).entity(ex.getMessage()).build();
            } catch (CreateRecipeException | CategoryNotFoundException | UnknownPersistenceException | InputDataValidationException ex) {
                return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid create new product request").build();
        }
    }

}
