package ejb.session.stateless;

import entity.Category;
import entity.Recipe;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CategoryNotFoundException;
import util.exception.CreateRecipeException;
import util.exception.InputDataValidationException;
import util.exception.RecipeNotFoundException;
import util.exception.RecipeTitleExistException;
import util.exception.UnknownPersistenceException;

@Stateless
public class RecipeSessionBean implements RecipeSessionBeanLocal {

    @PersistenceContext(unitName = "ReadyFoods-ejbPU")
    private EntityManager em;

    @EJB
    private CategorySessionBeanLocal categorySessionBeanLocal;
    @EJB
    private OrderSessionBeanLocal orderSessionBeanLocal;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public RecipeSessionBean() {
        this.validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }

    @Override
    public Recipe createNewRecipe(Recipe newRecipe, List<Long> categoriesId)
            throws CategoryNotFoundException, CreateRecipeException,
            RecipeTitleExistException, UnknownPersistenceException,
            InputDataValidationException {

//front end to take in a list of categories id, refer to product management xhtml
        Set<ConstraintViolation<Recipe>> constraintViolations = validator.validate(newRecipe);

        if (constraintViolations.isEmpty()) {

            try {
                if (categoriesId.isEmpty()) {
                    throw new CreateRecipeException("A category must be selected for the recipe");
                }

                List<Category> categoriesList = new ArrayList<>();

                for (Long categoryId : categoriesId) {
                    categoriesList.add(categorySessionBeanLocal.
                            retrieveCategoryByCategoryId(categoryId));
                }

                newRecipe.setCategories(categoriesList);
                em.persist(newRecipe);
                em.flush();

            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new RecipeTitleExistException();
                    } else {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
        return newRecipe;
    }

    @Override
    public Recipe retrieveRecipeByRecipeId(Long recipeId) throws RecipeNotFoundException {

        Recipe retrievedRecipe = em.find(Recipe.class, recipeId);

        if (retrievedRecipe != null) {
            retrievedRecipe.getCategories().size();
            retrievedRecipe.getRecipeSteps().size();
            retrievedRecipe.getIngredientSpecificationList().size();
            
            return retrievedRecipe;
        } else {
            throw new RecipeNotFoundException("Recipe ID " + recipeId + " does not exist!");
        }

    }

    //kiv retrieving recipe by chef
    @Override
    public List<Recipe> searchRecipesByName(String searchString) {

        Query query = em.createQuery("SELECT r FROM Recipe r WHERE r.recipeTitle LIKE :inSearchString ORDER BY r.recipeTitle ASC");
        List<Recipe> recipes = query.getResultList();

        recipes.size();
        for(Recipe r : recipes) {
            r.getCategories().size();
            r.getRecipeSteps().size();
            r.getIngredientSpecificationList().size();
        }

        return recipes;
    }

    //filter recipe by date?
    
    @Override
    public List<Recipe> filterRecipesByCategory(List<Long> categoryIds, String condition) { //condition be displayed as a button for user to select

        List<Recipe> recipes = new ArrayList<>();

        if (categoryIds.isEmpty() || categoryIds == null
                || !condition.equals("AND") || !condition.equals("OR")) {
            return recipes;
        } else {
            if (condition.equals("OR")) {
                Query query = em.createQuery("SELECT r FROM Recipe r, IN (r.categories) rc WHERE rc.categoryId IN :inCategoryIds ORDER BY r.recipeTitle ASC");
                query.setParameter("inCategoryIds", categoryIds);
                query.getResultList();
            } else {
                String selectClause = "Select r FROM Recipe r";
                String whereClause = "";
                Boolean firstCat = true;
                Integer catCount = 1;

                for (Long catId : categoryIds) {
                    selectClause += ", IN (r.categories) rc" + catCount;

                    if (firstCat) {
                        whereClause = "WHERE rc.categoryID = " + catId;
                        firstCat = false;
                    } else {
                        whereClause += " AND rc" + catCount + ".tagId = " + catId;
                    }

                    catCount++;
                }

                String jpql = selectClause + " " + whereClause + "ORDER BY r.recipeTitle ASC";
                Query query = em.createQuery(jpql);
                recipes = query.getResultList();

                recipes.size();
                for (Recipe r : recipes) {
                    r.getCategories().size();
                    r.getRecipeSteps().size();
                }

                Collections.sort(recipes, (Recipe r1, Recipe r2) 
                        -> r1.getRecipeTitle().compareTo(r2.getRecipeTitle()));
            }
        }
        
        return recipes;
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Recipe>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

}
