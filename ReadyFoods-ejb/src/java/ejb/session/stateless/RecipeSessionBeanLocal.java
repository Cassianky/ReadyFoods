/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Recipe;
import java.util.List;
import javax.ejb.Local;
import util.exception.CategoryNotFoundException;
import util.exception.CreateRecipeException;
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

    public Recipe createNewRecipe(Recipe newRecipe, List<Long> categoriesId, List<Long>recipeSpecificationId)throws CategoryNotFoundException, CreateRecipeException,
            RecipeTitleExistException, UnknownPersistenceException,
            InputDataValidationException;

    public Recipe retrieveRecipeByRecipeId(Long recipeId) throws RecipeNotFoundException;

    public List<Recipe> searchRecipesByName(String searchString);

    public List<Recipe> filterRecipesByCategory(List<Long> categoryIds, String condition);

    public List<Recipe> retrieveAllRecipes();

}
