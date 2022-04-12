/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Ingredient;
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
import util.exception.IngredientExistsException;
import util.exception.IngredientInsufficientStockQuantityException;
import util.exception.IngredientNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**x
 *
 * @author Eugene Chua
 */
@Stateless
public class IngredientSessionBean implements IngredientSessionBeanLocal {

    @PersistenceContext(unitName = "ReadyFoods-ejbPU")
    private EntityManager em;

    @EJB
    private RecipeSessionBeanLocal recipeSessionBeanLocal;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public IngredientSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Long createNewIngredient(Ingredient newIngredient) throws IngredientExistsException, UnknownPersistenceException, InputDataValidationException {

        Set<ConstraintViolation<Ingredient>> constraintViolations = validator.validate(newIngredient);
        if (constraintViolations.isEmpty()) {
            try {
                em.persist(newIngredient);
                em.flush();
                System.out.println("********ejb.session.stateless.IngredientSessionBean.createNewIngredient()");

                return newIngredient.getIngredientId();
            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new IngredientExistsException();
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

    }

    @Override 
    public List<Ingredient> retrieveAllIngredients() {
        Query query = em.createQuery("SELECT i FROM Ingredient i ORDER BY i.name ASC");
        
        List<Ingredient> ingredients = query.getResultList();
        
        return ingredients;
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Ingredient>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

    @Override
    public void debitQuantityAtHand(Long ingredientId, Integer quantityToDebit) throws IngredientNotFoundException, IngredientInsufficientStockQuantityException {
        Ingredient ingredient = retrieveIngredientByIngredientId(ingredientId);

        if (ingredient.getStockQuantity() >= quantityToDebit) {
            ingredient.setStockQuantity(ingredient.getStockQuantity() - quantityToDebit);
        } else {
            throw new IngredientInsufficientStockQuantityException("Ingredient " + ingredient.getIngredientId() + " stock quantity is " + ingredient.getStockQuantity() + " versus quantity to debit of " + quantityToDebit);
        }
    }

    @Override
    public void creditQuantityOnHand(Long ingredientId, Integer quantityToCredit) throws IngredientNotFoundException {
        Ingredient ingredient = retrieveIngredientByIngredientId(ingredientId);
        ingredient.setStockQuantity(ingredient.getStockQuantity() + quantityToCredit);
    }

    @Override
    public Ingredient retrieveIngredientByIngredientId(Long ingredientId) throws IngredientNotFoundException {
        Ingredient ingredient = em.find(Ingredient.class, ingredientId);

        if (ingredient != null) {
            return ingredient;
        } else {
            throw new IngredientNotFoundException("Ingredient ID " + ingredientId + " does not exist!");
        }
    }
    
//    @Override
//    public void deleteIngredient(Long ingredientId) throws IngredientNotFoundException {
//        
//        Ingredient ingredientToDelete = retrieveIngredientByIngredientId(ingredientId);
//        
//        //List<Recipe> recipes = recipeSessionBeanLocal.retrieveAllRecipesByIngredientId();
//        
//    }

}
