/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CommentEntity;
import entity.Recipe;
import entity.Review;
import java.util.List;
import javax.ejb.Local;
import util.exception.CategoryNotFoundException;
import util.exception.CreateRecipeException;
import util.exception.IngredientSpecificationNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.RecipeNotFoundException;
import util.exception.RecipeTitleExistException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author Eugene Chua
 */
@Local
public interface RecipeSessionBeanLocal {

    public Recipe createNewRecipe(Recipe newRecipe, List<Long> categoriesId, List<Long> recipeSpecificationId) throws CategoryNotFoundException, CreateRecipeException,
            RecipeTitleExistException, UnknownPersistenceException,
            InputDataValidationException;

    public Recipe retrieveRecipeByRecipeId(Long recipeId) throws RecipeNotFoundException;

    public List<Recipe> searchRecipesByName(String searchString);
    
    public List<Recipe> searchRecipesByIngredients(List<Long> ingredientIds, String condition);

    public List<Recipe> retrieveAllRecipes();

    public List<CommentEntity> getAllComments(Recipe recipe);

    public void updateRecipeContent(Recipe recipeToUpdate) throws RecipeNotFoundException, 
            IngredientSpecificationNotFoundException, CategoryNotFoundException;

    public List<Review> getAllReviews(Recipe recipe);


}
