/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.IngredientSpecification;
import javax.ejb.Local;
import util.exception.IngredientExistsException;
import util.exception.IngredientNotFoundException;
import util.exception.IngredientSpecificationNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author PYT
 */
@Local
public interface IngredientSpecificaitonSessionBeanLocal {

    public Long createNewIngredientSpecification(IngredientSpecification newIngredientSpecifcation, Long ingredientId) throws IngredientExistsException, UnknownPersistenceException, InputDataValidationException, IngredientNotFoundException;

    public IngredientSpecification retrieveIngredientSpecificationById(Long id) throws IngredientSpecificationNotFoundException;
    
}
