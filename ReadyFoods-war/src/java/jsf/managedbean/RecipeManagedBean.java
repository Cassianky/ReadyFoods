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
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;

@Named(value = "recipeManagedBean")
@ViewScoped
public class RecipeManagedBean implements Serializable {

    @EJB
    RecipeSessionBeanLocal recipeSessionBeanLocal;
    @EJB
    CategorySessionBeanLocal categorySessionBeanLocal;

    @Inject
    private RecipeViewManagedBean recipeViewManagedBean;
    
    private List<Recipe> allRecipesToView;
    private Recipe recipeToView;

    public RecipeManagedBean() {
        recipeToView = new Recipe();
        allRecipesToView = new ArrayList<>();
    }

    @PostConstruct
    public void postConstruct() {
        allRecipesToView = recipeSessionBeanLocal.retrieveAllRecipes();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("recipeToView", recipeToView);
    }

    public void viewRecipeDetails(ActionEvent event) throws IOException {
        recipeToView = (Recipe) event.getComponent().getAttributes().get("recipeToView");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().replace("recipeToView", recipeToView);
        FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getApplicationContextPath() + "/recipeManagement/viewSingleRecipe.xhtml");
    }

    public List<Recipe> getAllRecipesToView() {
        return allRecipesToView;
    }

    public Recipe getRecipeToView() {
        return recipeToView;
    }

    public RecipeViewManagedBean getRecipeViewManagedBean() {
        return recipeViewManagedBean;
    }

    public void setRecipeViewManagedBean(RecipeViewManagedBean recipeViewManagedBean) {
        this.recipeViewManagedBean = recipeViewManagedBean;
    }

}
