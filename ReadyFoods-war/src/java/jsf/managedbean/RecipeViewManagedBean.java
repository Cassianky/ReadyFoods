/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.RecipeSessionBeanLocal;
import entity.Recipe;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import util.exception.RecipeNotFoundException;

/**
 *
 * @author Eugene Chua
 */
@Named(value = "recipeViewManagedBean")
@ViewScoped
public class RecipeViewManagedBean implements Serializable {

    @EJB(name = "RecipeSessionBeanLocal")
    private RecipeSessionBeanLocal recipeSessionBeanLocal;

    
    private Recipe recipe;
    private String formattedRecipeSteps;
    private Long recipeId;

    public RecipeViewManagedBean() {
        recipeId = (Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("recipeToView");
    }

    @PostConstruct
    public void postConstruct() {
        try {  
            recipe = recipeSessionBeanLocal.retrieveRecipeByRecipeId(getRecipeId());
        } catch (RecipeNotFoundException ex) {
            ex.printStackTrace();
        }
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

    /**
     * @return the recipeId
     */
    public Long getRecipeId() {
        return recipeId;
    }

    /**
     * @param recipeId the recipeId to set
     */
    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
    }

}
