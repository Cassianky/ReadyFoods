/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.CreditCardSessionBeanLocal;
import ejb.session.stateless.CustomerSessionBeanLocal;
import ejb.session.stateless.IngredientSpecificaitonSessionBeanLocal;
import ejb.session.stateless.OrderEntitySessionBeanLocal;
import ejb.session.stateless.RecipeSessionBeanLocal;
import entity.CreditCard;
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
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
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

    @EJB(name = "CreditCardSessionBeanLocal")
    private CreditCardSessionBeanLocal creditCardSessionBeanLocal;

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
    
    private OrderEntity newOrderEntity;
   
    private Integer numPax;
    
    private Boolean hasNoCC;

    public ShoppingCartManagedBean() {
        this.orderLineItems = new ArrayList<>();
        this.currentOrderLineItem = new OrderLineItem();
        this.currentRecipe = new Recipe();
        numPax = 1;
        hasNoCC = true;
    }
    
    @PostConstruct
    public void postConstruct() {
        
        
    }

    public void removeFromShoppingCart(ActionEvent event) throws IOException {
        OrderLineItem oli = (OrderLineItem) event.getComponent().getAttributes().get("orderLineItemToRemove");
         System.out.println("******* Recipe: " + oli.getRecipe().getRecipeId() + " removed from cart!");
        orderLineItems.remove(oli);
//       
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Product: " + oli.getRecipe().getRecipeTitle() + " removed from cart!", null));
    }

    public void checkoutShoppingCart(ActionEvent event) throws IOException, CheckOutShoppingCartException, ShoppingCartIsEmptyException {
        
        Customer customer = (Customer)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentCustomer");
        
        if (!orderLineItems.isEmpty()) {
            newOrderEntity = new OrderEntity(numPax, totalPrice, false, new Date(), Status.PENDING, orderLineItems, customer);
            if(customer.getCreditCard() != null){
                System.out.println("Customer has credit card!");
                //
                FacesContext.getCurrentInstance().getExternalContext().redirect("orderPayment.xhtml");
                hasNoCC = false;
            } else {
                System.out.println("Customer has no credit card!");
            }
            
            
//                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Shopping cart checked out successfully!", null));
        } else {
            throw new ShoppingCartIsEmptyException("Shopping cart is empty, cannot checkout.");
        }

    }
    
    public void confirmOrder(ActionEvent event) throws IOException{
        Customer customer = (Customer)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentCustomer");
        try {
            orderEntitySessionBeanLocal.createNewOrder(customer.getCustomerId(), newOrderEntity);
        } catch (CustomerNotFoundException ex) {
            ex.printStackTrace();
        } catch (CreateNewOrderException ex) {
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error occured while creating new order: " + ex.getMessage(), null));
        }
    }
    
    public String getCustomerAddress() {
        Customer customer = (Customer)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentCustomer");
        return customer.getAddress();
    }
    
    public String getCustomerCC() {
        Customer customer = (Customer)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentCustomer");
        try {
            Customer retrievedCustomer = customerSessionBeanLocal.retrieveCustomerByCustomerId(customer.getCustomerId());
            CreditCard retrievedCard = retrievedCustomer.getCreditCard();
            return retrievedCard.getCcNumber();
        } catch (CustomerNotFoundException ex) {
            ex.printStackTrace();
        }
        return null;
        
    }
    
    public void redirectToAddCC() throws IOException{
         FacesContext.getCurrentInstance().getExternalContext().redirect("addCC.xhtml");
    }
    
    public void updateNumPax(ValueChangeEvent event){
        numPax = (Integer) event.getNewValue();
    }
    
    public void addRecipeFromRecipeView(Recipe recipeFromView){
        
        System.out.println("**********Cart managed bean called");
        List<CustomisedIngredient>cis = new ArrayList<>();
        for(IngredientSpecification is: recipeFromView.getIngredientSpecificationList()){
            BigDecimal subT = is.getIngredient().getUnitPrice().multiply(BigDecimal.valueOf(is.getQuantityPerServing()));
            CustomisedIngredient newCi = new CustomisedIngredient(is.getQuantityPerServing(),is.getPreparationMethod(),is.getIngredient().getIngredientId(),is.getIngredient().getName(),is.getIngredient().getUnitPrice(),subT);
            cis.add(newCi);
        }
        
        OrderLineItem newOli = new OrderLineItem(cis, recipeFromView);
        newOli.getRecipeSubTotal();
        
        orderLineItems.add(newOli);
       
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
            CustomisedIngredient cIToReset = (CustomisedIngredient) event.getComponent().getAttributes().get("cIToReset");
            Long recipeId = orderLineItemToUpdate.getRecipe().getRecipeId();
            Recipe retrievedRecipe = recipeSessionBeanLocal.retrieveRecipeByRecipeId(recipeId);
            IngredientSpecification is = ingredientSpecificationSessionBeanLocal.retrieveIngredientSpecificationByIngredientId(cIToReset.getIngredientId());
            Integer realQuantity = is.getQuantityPerServing();
            System.out.println("resetCustomisedIngredFromCart*******" + cIToReset.getCustomisedIngredientId());
            cIToReset.setQuantityOfIngredient(realQuantity);
            orderLineItemToUpdate.getCustomisedIngredients().remove(cIToReset);
            orderLineItemToUpdate.getCustomisedIngredients().add(cIToReset);
        
        } catch (RecipeNotFoundException ex) {
            Logger.getLogger(ShoppingCartManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public void doUpdateOrderLineItem(ActionEvent event) throws IOException {
        OrderLineItem oli = (OrderLineItem) event.getComponent().getAttributes().get("orderLineItemToUpdate");
        setOrderLineItemToUpdate(oli);
    }

    public void removeCustomisedIngredientFromOrderLineItem(ActionEvent event) throws IOException {
        CustomisedIngredient ci = (CustomisedIngredient) event.getComponent().getAttributes().get("customisedIngredientToRemove");
        orderLineItemToUpdate.getCustomisedIngredients().remove(ci);
        
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
        
        totalP = totalP.multiply(BigDecimal.valueOf(numPax));

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
     * @return the orderEntitySessionBeanLocal
     */
    public OrderEntitySessionBeanLocal getOrderEntitySessionBeanLocal() {
        return orderEntitySessionBeanLocal;
    }

    /**
     * @param orderEntitySessionBeanLocal the orderEntitySessionBeanLocal to set
     */
    public void setOrderEntitySessionBeanLocal(OrderEntitySessionBeanLocal orderEntitySessionBeanLocal) {
        this.orderEntitySessionBeanLocal = orderEntitySessionBeanLocal;
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

    /**
     * @return the newOrderEntity
     */
    public OrderEntity getNewOrderEntity() {
        return newOrderEntity;
    }

    /**
     * @param newOrderEntity the newOrderEntity to set
     */
    public void setNewOrderEntity(OrderEntity newOrderEntity) {
        this.newOrderEntity = newOrderEntity;
    }

    /**
     * @return the numPax
     */
    public Integer getNumPax() {
        return numPax;
    }

    /**
     * @param numPax the numPax to set
     */
    public void setNumPax(Integer numPax) {
        this.numPax = numPax;
    }

    /**
     * @return the hasCC
     */
    public Boolean getHasNoCC() {
        return hasNoCC;
    }

    /**
     * @param hasCC the hasCC to set
     */
    public void setHasNoCC(Boolean hasCC) {
        this.hasNoCC = hasCC;
    }

}
