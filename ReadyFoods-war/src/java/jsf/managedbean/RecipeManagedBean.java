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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import util.exception.RecipeNotFoundException;

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
    private String keyword;
    private Recipe recipeToView;

    private Recipe recipe;

    public RecipeManagedBean() {
        recipeToView = new Recipe();
        allRecipesToView = new ArrayList<>();
    }

    @PostConstruct
    public void postConstruct() {
        allRecipesToView = recipeSessionBeanLocal.retrieveAllRecipes();

        setKeyword((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("keywordString"));

        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("recipeToView", recipeToView);
    }

    public void viewRecipeDetails(ActionEvent event) throws IOException {
        Long recipeId = (Long) event.getComponent().getAttributes().get("recipeToView");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().replace("recipeToView", recipeId);
        FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getApplicationContextPath() + "/recipeManagement/viewSingleRecipe.xhtml");
    }

    public void searchRecipesByString() {
        System.out.println("Entered search method");

        if (keyword == null || keyword.trim().length() == 0) {
            this.allRecipesToView = recipeSessionBeanLocal.retrieveAllRecipes();
        }
        {
            this.allRecipesToView = recipeSessionBeanLocal.searchRecipesByName(this.keyword);
        }
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

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("keywordString", keyword);
    }

}
