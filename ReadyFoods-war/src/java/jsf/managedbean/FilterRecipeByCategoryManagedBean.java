/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.CategorySessionBeanLocal;
import ejb.session.stateless.RecipeSessionBeanLocal;
import entity.Category;
import entity.Recipe;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
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

    private TreeNode treeNode;
    private TreeNode selectedTreeNode;
    private List<Category> parentCategories;
    private List<Category> subCategoriesToView;
    private List<Recipe> recipes;

    public FilterRecipeByCategoryManagedBean() {
    }

    @PostConstruct
    public void postConstruct() {
        try {
            List<Category> parentCategories = categorySessionBeanLocal.retrieveAllParentCategories();
            treeNode = new DefaultTreeNode("Root", null);

            for (Category c : parentCategories) {
                createTreeNode(c, getTreeNode());
            }

            Long selectedCategoryId = (Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("recipeFilterCategory");

            if (selectedCategoryId != null) {
                for (TreeNode tn : treeNode.getChildren()) {
                    Category c = (Category) tn.getData();

                    if (c.getCategoryId().equals(selectedCategoryId)) {
                        selectedTreeNode = tn;
                        break;
                    } else {
                        selectedTreeNode = searchTreeNode(selectedCategoryId, tn);
                    }

                }
            }
            
            filterProduct();
        } catch (CategoryNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error while retreiving category: " + ex.getMessage(), null));
        }
    }

    public void createTreeNode(Category parentCategory, TreeNode parentTreeNode) {

        TreeNode childNode = new DefaultTreeNode(parentCategory, parentTreeNode);

        for (Category c : parentCategory.getSubCategories()) {
            createTreeNode(c, childNode);
        }
    }

    public TreeNode searchTreeNode(Long selectedCategoryId, TreeNode treeNode) {
        for (TreeNode tn : treeNode.getChildren()) {
            Category c = (Category) tn.getData();

            if (c.getCategoryId().equals(selectedCategoryId)) {
                return tn;
            } else {
                return searchTreeNode(selectedCategoryId, tn);
            }
        }
        return null;
    }
    
    public void filterProduct() {
        if(selectedTreeNode != null) {
            try {
                Category c = (Category)selectedTreeNode.getData();
                recipes = recipeSessionBeanLocal.filterRecipesBySingleCategory(c.getCategoryId());
            } catch (Exception e) {
            }
        }
    }

    public TreeNode getTreeNode() {
        return treeNode;
    }

    public void setTreeNode(TreeNode treeNode) {
        this.treeNode = treeNode;
    }

    public TreeNode getSelectedTreeNode() {
        return selectedTreeNode;
    }

    public void setSelectedTreeNode(TreeNode selectedTreeNode) {
        this.selectedTreeNode = selectedTreeNode;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }

}
