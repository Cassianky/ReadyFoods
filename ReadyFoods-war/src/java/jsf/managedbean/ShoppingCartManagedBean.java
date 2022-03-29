/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.CustomerSessionBeanLocal;
import entity.OrderLineItem;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

/**
 *
 * @author PYT
 */
@Named(value = "shoppingCartManagedBean")
@SessionScoped
public class ShoppingCartManagedBean implements Serializable {

    @EJB(name = "CustomerSessionBeanLocal")
    private CustomerSessionBeanLocal customerSessionBeanLocal;
    
    private ArrayList<OrderLineItem> orderLineItems;
    
    private BigDecimal totalPrice;

    
    public ShoppingCartManagedBean() {
        this.orderLineItems = new ArrayList<>();
    }
    
    public void removeFromShoppingCart(ActionEvent event) throws IOException {
        OrderLineItem recipe = (OrderLineItem) event.getComponent().getAttributes().get("orderLineItemToRemove");
        orderLineItems.remove(recipe);
        System.err.println("******* Recipe: " + recipe.getRecipe().getRecipeId() + " removed from cart!");
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Product: " + recipe.getRecipe().getRecipeTitle() + " removed from cart!", null));
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
        return this.totalPrice;
    }

 
    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
   
    
}
