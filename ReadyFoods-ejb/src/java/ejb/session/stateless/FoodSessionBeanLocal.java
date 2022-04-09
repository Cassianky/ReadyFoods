/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Food;
import java.util.List;
import javax.ejb.Local;
import util.exception.CustomerNotFoundException;
import util.exception.DeleteFoodException;
import util.exception.FoodNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author ngcas
 */
@Local
public interface FoodSessionBeanLocal {

    public Long createNewFood(Food newFood, Long customerId) throws CustomerNotFoundException, UnknownPersistenceException, InputDataValidationException;
    
    public Food retrieveFoodByFoodId(Long foodId) throws FoodNotFoundException;

    public void deleteFoodByFoodId(Long foodId, Long customerId) throws FoodNotFoundException, DeleteFoodException, CustomerNotFoundException;

    public List<Food> retrieveAllFoodsByCustomerId(Long customerId) throws CustomerNotFoundException;
}
