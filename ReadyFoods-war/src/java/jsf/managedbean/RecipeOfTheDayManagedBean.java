/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.EjbTimerSessionBeanLocal;
import ejb.session.stateless.RecipeSessionBeanLocal;
import entity.Recipe;
import entity.RecipeOfTheDay;
import java.io.IOException;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import util.exception.CustomerNotFoundException;
import util.exception.RecipeNotFoundException;

/**
 *
 * @author ngcas
 */
@Named(value = "recipeOfTheDayManagedBean")
@ViewScoped
public class RecipeOfTheDayManagedBean implements Serializable {

    @EJB(name = "RecipeSessionBeanLocal")
    private RecipeSessionBeanLocal recipeSessionBeanLocal;

    @EJB(name = "EjbTimerSessionBeanLocal")
    private EjbTimerSessionBeanLocal ejbTimerSessionBeanLocal;

    private Recipe recipe;

    public RecipeOfTheDayManagedBean() {
    }

    @PostConstruct
    public void postConstruct() {
        try {
            List<RecipeOfTheDay> recipes = ejbTimerSessionBeanLocal.retrieveRecipeOftheDay();
            RecipeOfTheDay recipeOfTheDay = recipes.get(0);
            if (recipeOfTheDay != null) {
                recipe = recipeSessionBeanLocal.retrieveRecipeByRecipeId(recipeOfTheDay.getRecipeId());
            }
        } catch (RecipeNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Recipe Of The Day Not Found!: " + ex.getMessage(), null));
        }
    }
    
    public void viewRecipeDetails(ActionEvent event) throws IOException {
         FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("recipeToView", recipe.getRecipeId());
        FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getApplicationContextPath() + "/recipeManagement/viewSingleRecipe.xhtml");
    }

    /**
     * @return the recipe
     */
    public Recipe getRecipe() {
        return recipe;
    }

    /**
     * @param recipe the recipe to set
     */
    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

}
