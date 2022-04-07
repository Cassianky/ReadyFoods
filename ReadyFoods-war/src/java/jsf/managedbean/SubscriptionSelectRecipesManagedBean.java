/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.CustomerSessionBeanLocal;
import ejb.session.stateless.OrderEntitySessionBeanLocal;
import ejb.session.stateless.RecipeSessionBeanLocal;
import ejb.session.stateless.SubscriptionSessionBeanLocal;
import entity.Customer;
import entity.OrderEntity;
import entity.OrderLineItem;
import entity.Recipe;
import entity.Subscription;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.faces.view.ViewScoped;
import util.enumeration.Status;
import util.exception.CreateNewOrderException;
import util.exception.CustomerNotFoundException;
import util.exception.NoOngoingSubscriptionException;

/**
 *
 * @author angler
 */
@Named(value = "subscriptionSelectRecipesManagedBean")
@ViewScoped
public class SubscriptionSelectRecipesManagedBean implements Serializable {

    @EJB(name = "OrderEntitySessionBeanLocal")
    private OrderEntitySessionBeanLocal orderEntitySessionBeanLocal;

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
    private OrderEntity currentOrder;

    private Integer remaining;

    // Need to find a way to 1. Save the customer's current recipe selection 2. Session restarts, need to retrieve the current recipe selection and show to customer
    /**
     * Creates a new instance of SubscriptionSelectRecipesManagedBean
     */
    public SubscriptionSelectRecipesManagedBean() {
        this.setOrderLineItems(new ArrayList<>());

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
            System.out.println("Generating order line items for use in selectRecipes.xhtml");
            this.setRemaining(ongoingSubscription.getNumOfRecipes());

            currentOrder = ongoingSubscription.getCurrentOrder();
            

            List<Long> usedRecipeIds = new ArrayList<Long>();

            if (currentOrder != null) {
                System.out.println("Current order not null");
                for (OrderLineItem orderLineItem : currentOrder.getOrderLineItems()) {
                    orderLineItems.add(orderLineItem);
                    orderLineItem.getRecipe().getCategories().size();
                    usedRecipeIds.add(orderLineItem.getRecipe().getRecipeId());
                    this.remaining -= orderLineItem.getQuantity();
                }
            }

            for (Recipe recipe : allRecipes) {
                if (!usedRecipeIds.contains(recipe.getRecipeId())) {
                    OrderLineItem oli = new OrderLineItem();
                    oli.setRecipeSubTotal(BigDecimal.ZERO); // no need to update as amount user is charged is predetermined?
                    oli.setRecipe(recipe);
                    oli.setQuantity(0);
                    this.getOrderLineItems().add(oli);
                }
            }

            System.out.println(orderLineItems);
            

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

    public void updateSelection(ActionEvent event) {
        try {
            if (currentOrder == null) {

                List<OrderLineItem> lineItemsToBuy = new ArrayList<>();

                for (OrderLineItem oli : orderLineItems) {
                    if (oli.getQuantity() != 0) {
                        lineItemsToBuy.add(oli);
                    }
                }
                
                OrderEntity newOrder = new OrderEntity(ongoingSubscription.getNumOfPeople(), ongoingSubscription.getWeeklyPrice(), false,
                        new Date(), Status.PENDING, lineItemsToBuy);
                
                orderEntitySessionBeanLocal.createNewSubscriptionOrder(currentCustomerEntity.getCustomerId(), newOrder); 
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Recipe selection has been saved!  ID: " + newOrder.getOrderEntityId(), null));
                
              

            } else {
                 FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Updating of recipe selection not implemented yet :(" , null));
                
            }
        } catch (CustomerNotFoundException ex) {
            Logger.getLogger(SubscriptionSelectRecipesManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CreateNewOrderException ex) {
            Logger.getLogger(SubscriptionSelectRecipesManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoOngoingSubscriptionException ex) {
            Logger.getLogger(SubscriptionSelectRecipesManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }

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
