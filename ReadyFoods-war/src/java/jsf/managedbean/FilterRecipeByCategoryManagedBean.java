/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.CategorySessionBeanLocal;
import ejb.session.stateless.RecipeSessionBeanLocal;
import entity.Category;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import util.exception.CategoryNotFoundException;

/**
 *
 * @author Eugene Chua
 */
@Named(value = "filterRecipeByCategoryManagedBean")
@ViewScoped
public class FilterRecipeByCategoryManagedBean implements Serializable {

    @EJB
    CategorySessionBeanLocal categorySessionBeanLocal;
    @EJB
    RecipeSessionBeanLocal recipeSessionBeanLocal;

//    private TreeNode treeNode;
//    private TreeNode selectedTreeNode;
    private List<Category> parentCategories;
//    private List<Category> subCategoriesToView;
//    private List<Recipe> recipes;

    public FilterRecipeByCategoryManagedBean() {
    }

    @PostConstruct
    public void postConstruct() {
        try {
            System.out.println("Filter recipe by category bean post constructing started");
            this.parentCategories = categorySessionBeanLocal.retrieveAllParentCategories();

        } catch (CategoryNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error while retreiving category: " + ex.getMessage(), null));
        }
    }

    public List<Category> getParentCategories() {
        return parentCategories;
    }

    public void setParentCategories(List<Category> parentCategories) {
        this.parentCategories = parentCategories;
    }

}
