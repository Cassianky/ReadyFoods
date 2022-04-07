/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.CustomerSessionBeanLocal;
import ejb.session.stateless.RecipeSessionBeanLocal;
import ejb.session.stateless.SubscriptionSessionBeanLocal;
import entity.Customer;
import entity.OrderLineItem;
import entity.Recipe;
import entity.Subscription;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import util.exception.CustomerNotFoundException;
import util.exception.NoOngoingSubscriptionException;

/**
 *
 * @author angler
 */
@Named(value = "subscriptionSelectRecipesManagedBean")
@SessionScoped
public class SubscriptionSelectRecipesManagedBean implements Serializable {

    @EJB(name = "SubscriptionSessionBeanLocal")
    private SubscriptionSessionBeanLocal subscriptionSessionBeanLocal;

    @EJB(name = "CustomerSessionBeanLocal")
    private CustomerSessionBeanLocal customerSessionBeanLocal;

    @EJB(name = "RecipeSessionBeanLocal")
    private RecipeSessionBeanLocal recipeSessionBeanLocal;

    private List<Recipe> allRecipes;

    private List<OrderLineItem> orderLineItems;
    private Recipe recipeToView;

    private Customer currentCustomerEntity;
    private Subscription ongoingSubscription;
    private Integer remaining;
    
    // Need to find a way to 1. Save the customer's current recipe selection 2. Session restarts, need to retrieve the current recipe selection and show to customer

    /**
     * Creates a new instance of SubscriptionSelectRecipesManagedBean
     */
    public SubscriptionSelectRecipesManagedBean() {

    }

    @PostConstruct
    public void postConstruct() {
        setCurrentCustomerEntity((Customer) FacesContext.getCurrentInstance().
                getExternalContext().getSessionMap().get("currentCustomer"));
        Customer customer;
        try {
            setAllRecipes(recipeSessionBeanLocal.retrieveAllRecipes());
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("recipeToView", getRecipeToView());
            customer = customerSessionBeanLocal.retrieveCustomerByCustomerId(getCurrentCustomerEntity().getCustomerId());

            // This line throws NoONgoingSubscriptionException
            this.setOngoingSubscription(subscriptionSessionBeanLocal.retrieveOngoingSubscriptionForCustomer(getCurrentCustomerEntity().getCustomerId()));

            // Generate list of order line items
            this.setOrderLineItems(new ArrayList<>());

            System.out.println("Generating order line items for use in selectRecipes.xhtml");

            for (Recipe recipe : allRecipes) {
                OrderLineItem oli = new OrderLineItem();
                oli.setRecipeSubTotal(BigDecimal.ZERO); // no need to update as amount user is charged is predetermined?
                oli.setRecipe(recipe);
                oli.setQuantity(0);
                this.getOrderLineItems().add(oli);
            }

            System.out.println(orderLineItems);
            this.setRemaining(ongoingSubscription.getNumOfRecipes());

        } catch (CustomerNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Customer not found" + ex.getMessage(), null));
        } catch (NoOngoingSubscriptionException ex) {
            this.setOngoingSubscription(null);

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Create new" + ex.getMessage(), null));
        }

    }

    public void updateRemaningRecipes(ValueChangeEvent event) {
        System.out.println("Update");
        Integer selected = 0;
        for (OrderLineItem oli : orderLineItems) {
            selected += oli.getQuantity();
            
        }
        
        setRemaining((Integer) ongoingSubscription.getNumOfRecipes() - selected);
    }

    /**
     * @return the allRecipes
     */
    public List<Recipe> getAllRecipes() {
        return allRecipes;
    }

    /**
     * @param allRecipes the allRecipes to set
     */
    public void setAllRecipes(List<Recipe> allRecipes) {
        this.allRecipes = allRecipes;
    }

    /**
     * @return the recipeToView
     */
    public Recipe getRecipeToView() {
        return recipeToView;
    }

    /**
     * @param recipeToView the recipeToView to set
     */
    public void setRecipeToView(Recipe recipeToView) {
        this.recipeToView = recipeToView;
    }

    /**
     * @return the currentCustomerEntity
     */
    public Customer getCurrentCustomerEntity() {
        return currentCustomerEntity;
    }

    /**
     * @param currentCustomerEntity the currentCustomerEntity to set
     */
    public void setCurrentCustomerEntity(Customer currentCustomerEntity) {
        this.currentCustomerEntity = currentCustomerEntity;
    }

    /**
     * @return the ongoingSubscription
     */
    public Subscription getOngoingSubscription() {
        return ongoingSubscription;
    }

    /**
     * @param ongoingSubscription the ongoingSubscription to set
     */
    public void setOngoingSubscription(Subscription ongoingSubscription) {
        this.ongoingSubscription = ongoingSubscription;
    }

    /**
     * @return the orderLineItems
     */
    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    /**
     * @param orderLineItems the orderLineItems to set
     */
    public void setOrderLineItems(List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    /**
     * @return the remaining
     */
    public Integer getRemaining() {
        return remaining;
    }

    /**
     * @param remaining the remaining to set
     */
    public void setRemaining(Integer remaining) {
        this.remaining = remaining;
    }

}
