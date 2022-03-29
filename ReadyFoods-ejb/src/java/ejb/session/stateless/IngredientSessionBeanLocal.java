/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Ingredient;
import javax.ejb.Local;

/**
 *
 * @author Eugene Chua
 */
@Local
public interface IngredientSessionBeanLocal {

    public Ingredient createNewIngredient(Ingredient newIngredient);
    
}
