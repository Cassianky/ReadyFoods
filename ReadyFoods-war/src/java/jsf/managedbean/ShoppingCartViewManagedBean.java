/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.IngredientSessionBeanLocal;
import ejb.session.stateless.IngredientSpecificaitonSessionBeanLocal;
import ejb.session.stateless.RecipeSessionBeanLocal;
import entity.IngredientSpecification;
import entity.Recipe;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import util.enumeration.PreparationMethod;
import util.exception.IngredientSpecificationNotFoundException;

/**
 *
 * @author PYT
 */
@Named(value = "shoppingCartViewManagedBean")
@ViewScoped
public class ShoppingCartViewManagedBean implements Serializable {

    @EJB(name = "IngredientSpecificationSessionBeanLocal")
    private IngredientSpecificaitonSessionBeanLocal ingredientSpecificationSessionBeanLocal;

    @EJB(name = "IngredientSessionBeanLocal")
    private IngredientSessionBeanLocal ingredientSessionBeanLocal;

    @EJB(name = "RecipeSessionBeanLocal")
    private RecipeSessionBeanLocal recipeSessionBeanLocal;
    
    
    
    private Recipe currentRecipe;
    
    private PreparationMethod[] prepEnums = PreparationMethod.values();
    
    public ShoppingCartViewManagedBean() {
        this.currentRecipe = new Recipe();
    }
    @Inject
    private ShoppingCartManagedBean shoppingCartManagedBean;
    
    @PostConstruct
    public void postConstruct() {
        
    }
    
    public void addRecipeToCart(ActionEvent event) {

        Recipe recipe = (Recipe) event.getComponent().getAttributes().get("recipeToAdd");
        if (currentRecipe != null){
            if(recipe.getRecipeId() == currentRecipe.getRecipeId()){
                System.out.println("addRecipeToCart()********:" + " you have already added this recipe to cart");
            }
        }
        
        
        setCurrentRecipe(recipe);
        System.out.println("addRecipeToCart()********:" + getCurrentRecipe().getRecipeTitle());
        for(IngredientSpecification is:getCurrentRecipe().getIngredientSpecificationList()){
            System.out.println("Ingredient Spec********:" + is.getIngredient().getName()+", " + is.getQuantityPerServing());
        }


    }

    public void removeIngredSpecFromRecipe(ActionEvent event) {
        IngredientSpecification ingredSpecToRemove = (IngredientSpecification) event.getComponent().getAttributes().get("ingredSpecToRemove");
        System.out.println("removeIngredSpecFromRecipe*******" + ingredSpecToRemove.getIngredientSpecificationId());
        for (IngredientSpecification is : getCurrentRecipe().getIngredientSpecificationList()) {
            if (is.getIngredientSpecificationId() == ingredSpecToRemove.getIngredientSpecificationId()) {
                getCurrentRecipe().getIngredientSpecificationList().remove(ingredSpecToRemove);
                break;
            }
        }
    }
    
    public void reset(ActionEvent event){
        try {
            IngredientSpecification ingredSpecToReset = (IngredientSpecification) event.getComponent().getAttributes().get("ingredSpecToReset");
            Long ingredSpecId = ingredSpecToReset.getIngredientSpecificationId();
            System.out.println("resetIngredSpecFromRecipe*******" + ingredSpecId);
            IngredientSpecification retrievedIs = ingredientSpecificationSessionBeanLocal.retrieveIngredientSpecificationById(ingredSpecId);
            currentRecipe.getIngredientSpecificationList().remove(ingredSpecToReset);
            currentRecipe.getIngredientSpecificationList().add(retrievedIs);
        } catch (IngredientSpecificationNotFoundException ex) {
            ex.printStackTrace();
        }
    }
       
    
    
    public void confirmAddToCart() {
        
    }
   

    /**
     * @return the currentRecipe
     */
    public Recipe getCurrentRecipe() {
        return currentRecipe;
    }

    /**
     * @param currentRecipe the currentRecipe to set
     */
    public void setCurrentRecipe(Recipe currentRecipe) {
        this.currentRecipe = currentRecipe;
    }

    /**
     * @return the prepEnums
     */
    public PreparationMethod[] getPrepEnums() {
        return prepEnums;
    }

    /**
     * @param prepEnums the prepEnums to set
     */
    public void setPrepEnums(PreparationMethod[] prepEnums) {
        this.prepEnums = prepEnums;
    }
    
}
