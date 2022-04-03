/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.CategorySessionBeanLocal;
import ejb.session.stateless.RecipeSessionBeanLocal;
import entity.Recipe;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

@Named(value = "recipeManagedBean")
@ViewScoped
public class RecipeManagedBean implements Serializable {

    @EJB
    RecipeSessionBeanLocal recipeSessionBeanLocal;
    @EJB
    CategorySessionBeanLocal categorySessionBeanLocal;

    private List<Recipe> allRecipesToView;
    private Recipe recipeToView;

    public RecipeManagedBean() {
        recipeToView = new Recipe();
    }

    @PostConstruct
    public void postConstruct() {
        setAllRecipesToView(recipeSessionBeanLocal.retrieveAllRecipes());
    }

    public void viewRecipeDetails(ActionEvent event) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect("/recipeManagement/viewSingleRecipe.xhtml");
    }

    public List<Recipe> getAllRecipesToView() {
        return allRecipesToView;
    }

    public void setAllRecipesToView(List<Recipe> allRecipesToView) {
        this.allRecipesToView = allRecipesToView;
    }

    public Recipe getRecipeToView() {
        return recipeToView;
    }

    public void setRecipeToView(Recipe recipeToView) {
        this.recipeToView = recipeToView;
    }

}
