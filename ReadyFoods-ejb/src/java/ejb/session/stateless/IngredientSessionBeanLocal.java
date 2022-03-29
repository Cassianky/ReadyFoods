/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Ingredient;
import javax.ejb.Local;
import util.exception.IngredientExistsException;
import util.exception.IngredientInsufficientStockQuantityException;
import util.exception.IngredientNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author Eugene Chua
 */
@Local
public interface IngredientSessionBeanLocal {

    public Ingredient retrieveIngredientByIngredientId(Long ingredientId) throws IngredientNotFoundException;

    public void debitQuantityAtHand(Long ingredientId, Integer quantityToDebit) throws IngredientNotFoundException, IngredientInsufficientStockQuantityException;

    public void creditQuantityOnHand(Long ingredientId, Integer quantityToCredit) throws IngredientNotFoundException;

    public Ingredient createNewIngredient(Ingredient newIngredient) throws IngredientExistsException, UnknownPersistenceException, InputDataValidationException;
    
}
