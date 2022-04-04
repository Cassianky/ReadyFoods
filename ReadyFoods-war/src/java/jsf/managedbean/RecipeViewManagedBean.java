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
import javax.faces.context.FacesContext;

/**
 *
 * @author Eugene Chua
 */
@Named(value = "recipeViewManagedBean")
@ViewScoped
public class RecipeViewManagedBean implements Serializable {

    private Recipe recipe;
    private String formattedRecipeSteps;

    public RecipeViewManagedBean() {
        recipe = (Recipe) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("recipeToView");
    }

    @PostConstruct
    public void postConstruct() {
        System.out.println("Test recipe: " + recipe.getRecipeTitle());
        System.out.println(recipe.getRecipeSteps());
        System.out.println(formattedRecipeSteps);
    }

    public Recipe getRecipe() {
        System.out.println("Recipe: " + recipe.getRecipeTitle());
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public String getFormattedRecipeSteps() {
        return formattedRecipeSteps;
    }

    public void setFormattedRecipeSteps(String formattedRecipeSteps) {
        this.formattedRecipeSteps = formattedRecipeSteps;
    }

}
