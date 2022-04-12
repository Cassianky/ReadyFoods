package ejb.session.stateless;

import entity.Category;
import entity.CommentEntity;
import entity.Recipe;
import entity.IngredientSpecification;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import util.exception.IngredientSpecificationNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.RecipeNotFoundException;
import util.exception.RecipeTitleExistException;
import util.exception.UnknownPersistenceException;

@Stateless
public class RecipeSessionBean implements RecipeSessionBeanLocal {

    @EJB(name = "IngredientSpecificationSessionBeanLocal")
    private IngredientSpecificaitonSessionBeanLocal ingredientSpecificationSessionBeanLocal;

    @PersistenceContext(unitName = "ReadyFoods-ejbPU")
    private EntityManager em;

    @EJB
    private CategorySessionBeanLocal categorySessionBeanLocal;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public RecipeSessionBean() {
        this.validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }

    @Override
    public List<CommentEntity> getAllComments(Recipe recipe) {
        Recipe r = em.find(Recipe.class, recipe.getRecipeId());
        return r.getComments();
    }

    @Override
    public Recipe createNewRecipe(Recipe newRecipe, List<Long> categoriesId, List<Long> ingredientSpecificationId)
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

                for (Long isId : ingredientSpecificationId) {
                    IngredientSpecification is = ingredientSpecificationSessionBeanLocal.retrieveIngredientSpecificationById(isId);
                    newRecipe.getIngredientSpecificationList().add(is);
                    System.out.println("ejb.session.stateless.RecipeSessionBean.createNewRecipe()" + is.getIngredientSpecificationId());

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
            } catch (IngredientSpecificationNotFoundException ex) {
                Logger.getLogger(RecipeSessionBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
        return newRecipe;
    }

    @Override
    public List<Recipe> retrieveAllRecipes() {

        Query query = em.createQuery("SELECT r FROM Recipe r ORDER BY r.recipeTitle");

        List<Recipe> recipes = query.getResultList();

        for (Recipe r : recipes) {
            r.getIngredientSpecificationList().size();
            r.getCategories().size();
        }

        return recipes;
    }

    @Override
    public Recipe retrieveRecipeByRecipeId(Long recipeId) throws RecipeNotFoundException {

        Recipe retrievedRecipe = em.find(Recipe.class, recipeId);

        if (retrievedRecipe != null) {
            retrievedRecipe.getCategories().size();
            retrievedRecipe.getRecipeSteps();
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
        query.setParameter("inSearchString", "%" + searchString + "%");
        List<Recipe> recipes = query.getResultList();

        recipes.size();
        for (Recipe r : recipes) {
            r.getCategories().size();
            r.getRecipeSteps();
            r.getIngredientSpecificationList().size();
        }

        return recipes;
    }

    @Override
    public void updateRecipeContent(Recipe recipeToUpdate) throws RecipeNotFoundException, IngredientSpecificationNotFoundException, CategoryNotFoundException {
        if (recipeToUpdate != null && recipeToUpdate.getRecipeId() != null) {
            Set<ConstraintViolation<Recipe>> constraintViolations = validator.validate(recipeToUpdate);

            if (constraintViolations.isEmpty()) {
                if (!recipeToUpdate.getIngredientSpecificationList().isEmpty()) {
                    if (!recipeToUpdate.getCategories().isEmpty()) {
                        Recipe updatedRecipe = retrieveRecipeByRecipeId(recipeToUpdate.getRecipeId());
                        updatedRecipe.setCaloriesPerServing(recipeToUpdate.getCaloriesPerServing());
                        updatedRecipe.setCarbsPerServing(recipeToUpdate.getCarbsPerServing());
                        updatedRecipe.setCategories(recipeToUpdate.getCategories());
                        updatedRecipe.setCookingTime(recipeToUpdate.getCookingTime());
                        updatedRecipe.setFatsPerServing(recipeToUpdate.getFatsPerServing());
                        updatedRecipe.setIngredientSpecificationList(recipeToUpdate.getIngredientSpecificationList());
                        updatedRecipe.setPicUrl(recipeToUpdate.getPicUrl());
                        updatedRecipe.setProteinsPerServing(recipeToUpdate.getProteinsPerServing());
                        updatedRecipe.setRecipeChef(recipeToUpdate.getRecipeChef());
                        updatedRecipe.setRecipeSteps(recipeToUpdate.getRecipeSteps());
                        updatedRecipe.setSugarPerServing(recipeToUpdate.getSugarPerServing());
                        updatedRecipe.setVideoURL(recipeToUpdate.getVideoURL());
                    } else {
                        throw new IngredientSpecificationNotFoundException("Ingredient list cannot be empty!");
                    }
                } else {
                    throw new CategoryNotFoundException("Category list cannot be empty!");
                }
            } else {
                throw new RecipeNotFoundException("Recipe to be updated does not match existing records!");
            }
        }
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Recipe>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

}
