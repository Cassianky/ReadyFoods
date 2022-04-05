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
import entity.OrderLineItem;
import entity.Recipe;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
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

    @Inject
    private ShoppingCartManagedBean shoppingCartManagedBean;

    private Recipe currentRecipe;

    private PreparationMethod[] prepEnums = PreparationMethod.values();

    public ShoppingCartViewManagedBean() {

    }

    @PostConstruct
    public void postConstruct() {
        this.currentRecipe = new Recipe();
    }

    public void addRecipeToCart(ActionEvent event) {

        Recipe recipe = (Recipe) event.getComponent().getAttributes().get("recipeToAdd");
        Boolean done = false;

        for (OrderLineItem oli : shoppingCartManagedBean.getOrderLineItems()) {
            System.out.println(oli.getRecipe().getRecipeId());
            if (oli.getRecipe().getRecipeId() == recipe.getRecipeId()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Recipe has already been added to cart! Go to 'my shopping cart' to edit it!", null));
                done = true;
                break;
            }
        }
        if (done == false) {
            if (currentRecipe != null && currentRecipe.getRecipeId() == null) {
                setCurrentRecipe(recipe);
                System.out.println("addRecipeToCart()********:" + getCurrentRecipe().getRecipeTitle());
            } else {
                System.out.println("addRecipeToCart()********:" + " you have already added this recipe to cart");
            }

            for (IngredientSpecification is : getCurrentRecipe().getIngredientSpecificationList()) {
                System.out.println("Ingredient Spec********:" + is.getIngredient().getName() + ", " + is.getQuantityPerServing());
            }
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

    public void reset(ActionEvent event) {
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
        shoppingCartManagedBean.addRecipeFromRecipeView(currentRecipe);
        System.out.println("**********Add To Cart");
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

    /**
     * @return the shoppingCartManagedBean
     */
    public ShoppingCartManagedBean getShoppingCartManagedBean() {
        return shoppingCartManagedBean;
    }

    /**
     * @param shoppingCartManagedBean the shoppingCartManagedBean to set
     */
    public void setShoppingCartManagedBean(ShoppingCartManagedBean shoppingCartManagedBean) {
        this.shoppingCartManagedBean = shoppingCartManagedBean;
    }

}
