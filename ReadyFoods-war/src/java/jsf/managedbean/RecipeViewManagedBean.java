/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.Recipe;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import javax.annotation.PostConstruct;

/**
 *
 * @author Eugene Chua
 */
@Named(value = "recipeViewManagedBean")
@ViewScoped
public class RecipeViewManagedBean implements Serializable {

    private Recipe recipe;

    public RecipeViewManagedBean() {
        recipe = new Recipe();
    }

    @PostConstruct
    public void postConstruct() {
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }
    

}
