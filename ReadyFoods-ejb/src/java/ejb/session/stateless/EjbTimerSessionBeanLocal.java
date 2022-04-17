/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RecipeOfTheDay;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author ngcas
 */
@Local
public interface EjbTimerSessionBeanLocal {

    public void recipeOfTheDay();

    public List<RecipeOfTheDay> retrieveRecipeOftheDay();

    public void recipeOfTheDayInitialise();

    public void reorderIngredientQuantity();
    
}
