/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.CustomerSessionBeanLocal;
import ejb.session.stateless.IngredientSpecificaitonSessionBeanLocal;
import ejb.session.stateless.OrderEntitySessionBeanLocal;
import ejb.session.stateless.RecipeSessionBeanLocal;
import entity.Customer;
import entity.CustomisedIngredient;
import entity.IngredientSpecification;
import entity.OrderEntity;
import entity.OrderLineItem;
import entity.Recipe;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import util.enumeration.PreparationMethod;
import util.enumeration.Status;
import util.exception.CheckOutShoppingCartException;
import util.exception.CreateNewOrderException;
import util.exception.CustomerNotFoundException;
import util.exception.IngredientSpecificationNotFoundException;
import util.exception.RecipeNotFoundException;
import util.exception.ShoppingCartIsEmptyException;

/**
 *
 * @author PYT
 */
@Named(value = "shoppingCartManagedBean")
@SessionScoped
public class ShoppingCartManagedBean implements Serializable {

    @EJB(name = "IngredientSpecificationSessionBeanLocal")
    private IngredientSpecificaitonSessionBeanLocal ingredientSpecificationSessionBeanLocal;

    @EJB(name = "RecipeSessionBeanLocal")
    private RecipeSessionBeanLocal recipeSessionBeanLocal;

    @EJB(name = "OrderEntitySessionBeanLocal")
    private OrderEntitySessionBeanLocal orderEntitySessionBeanLocal;

    @EJB(name = "CustomerSessionBeanLocal")
    private CustomerSessionBeanLocal customerSessionBeanLocal;
    
    

    private ArrayList<OrderLineItem> orderLineItems;

    private BigDecimal totalPrice;

    private OrderLineItem orderLineItemToUpdate;

    private ArrayList<Recipe> recipes;

    private Recipe currentRecipe;

    private OrderLineItem currentOrderLineItem;

    private PreparationMethod[] prepEnums = PreparationMethod.values();

    public ShoppingCartManagedBean() {
        this.orderLineItems = new ArrayList<>();
        this.currentOrderLineItem = new OrderLineItem();
        this.currentRecipe = new Recipe();
    }
    
    @PostConstruct
    public void postConstruct() {
        
    }

    public void removeFromShoppingCart(ActionEvent event) throws IOException {
        OrderLineItem oli = (OrderLineItem) event.getComponent().getAttributes().get("orderLineItemToRemove");
        orderLineItems.remove(oli);
        System.err.println("******* Recipe: " + oli.getRecipe().getRecipeId() + " removed from cart!");
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Product: " + oli.getRecipe().getRecipeTitle() + " removed from cart!", null));
    }

    public void checkoutShoppingCart(ActionEvent event) throws IOException, CheckOutShoppingCartException, ShoppingCartIsEmptyException {
        Customer customer = (Customer) event.getComponent().getAttributes().get("customerToCheckOut");
        Integer serialNo = 1;
        Integer totalQuantity = 0;
        BigDecimal totalAmt = BigDecimal.valueOf(0);

        if (!orderLineItems.isEmpty()) {
            try {

                OrderEntity newOrderEntity = new OrderEntity(1, totalPrice, false, new Date(), Status.PENDING, orderLineItems, customer);

                orderEntitySessionBeanLocal.createNewOrder(customer.getCustomerId(), newOrderEntity);

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Shopping cart checked out successfully!", null));

            } catch (CustomerNotFoundException ex) {
                throw new CheckOutShoppingCartException(ex.getMessage());
            } catch (CreateNewOrderException ex) {
                Logger.getLogger(ShoppingCartManagedBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            throw new ShoppingCartIsEmptyException("Shopping cart is empty, cannot checkout.");
        }
    }

    public void addRecipeToCart(ActionEvent event) {

        Recipe recipe = (Recipe) event.getComponent().getAttributes().get("recipeToAdd");
        
        setCurrentRecipe(recipe);
        System.out.println("addRecipeToCart()********:" + currentRecipe.getRecipeTitle());
        for(IngredientSpecification is:currentRecipe.getIngredientSpecificationList()){
            System.out.println("Ingredient Spec********:" + is.getIngredient().getName());
        }
//        currentOrderLineItem.setRecipe(currentRecipe);
//        orderLineItems.add(currentOrderLineItem);

    }

    public void removeIngredSpecFromRecipe(ActionEvent event) {
        IngredientSpecification ingredSpecToRemove = (IngredientSpecification) event.getComponent().getAttributes().get("ingredSpecToRemove");
        System.out.println("removeIngredSpecFromRecipe*******" + ingredSpecToRemove.getIngredientSpecificationId());
        for (IngredientSpecification is : currentRecipe.getIngredientSpecificationList()) {
            if (is.getIngredientSpecificationId() == ingredSpecToRemove.getIngredientSpecificationId()) {
                currentRecipe.getIngredientSpecificationList().remove(ingredSpecToRemove);
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
    
    
    public void doUpdateOrderLineItem(ActionEvent event) throws IOException {
        OrderLineItem oli = (OrderLineItem) event.getComponent().getAttributes().get("orderLineItemToUpdate");
        setOrderLineItemToUpdate(oli);
    }

    public void removeCustomisedIngredientFromOrderLineItem(ActionEvent event) throws IOException {
        CustomisedIngredient ci = (CustomisedIngredient) event.getComponent().getAttributes().get("customisedIngredientToRemove");
    }

    public CustomerSessionBeanLocal getCustomerSessionBeanLocal() {
        return customerSessionBeanLocal;
    }

    public void setCustomerSessionBeanLocal(CustomerSessionBeanLocal customerSessionBeanLocal) {
        this.customerSessionBeanLocal = customerSessionBeanLocal;
    }

    public ArrayList<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public void setOrderLineItems(ArrayList<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    public BigDecimal getTotalPrice() {
        BigDecimal totalP = BigDecimal.valueOf(0);
        for (OrderLineItem orderLineItem : orderLineItems) {
            totalP = totalP.add(orderLineItem.getRecipeSubTotal());
        }

        setTotalPrice(totalP);
        return totalP;
    }

    public void setTotalPrice(BigDecimal totalPrice) {

        this.totalPrice = totalPrice;
    }

    /**
     * @return the orderLineItemToUpdate
     */
    public OrderLineItem getOrderLineItemToUpdate() {
        return orderLineItemToUpdate;
    }

    /**
     * @param orderLineItemToUpdate the orderLineItemToUpdate to set
     */
    public void setOrderLineItemToUpdate(OrderLineItem orderLineItemToUpdate) {
        this.orderLineItemToUpdate = orderLineItemToUpdate;
    }

    /**
     * @return the recipes
     */
    public ArrayList<Recipe> getRecipes() {
        return recipes;
    }

    /**
     * @param recipes the recipes to set
     */
    public void setRecipes(ArrayList<Recipe> recipes) {
        this.recipes = recipes;
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
     * @return the currentOrderLineItem
     */
    public OrderLineItem getCurrentOrderLineItem() {
        return currentOrderLineItem;
    }

    /**
     * @param currentOrderLineItem the currentOrderLineItem to set
     */
    public void setCurrentOrderLineItem(OrderLineItem currentOrderLineItem) {
        this.currentOrderLineItem = currentOrderLineItem;
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
