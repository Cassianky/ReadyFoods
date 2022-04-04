/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.CustomerSessionBeanLocal;
import ejb.session.stateless.OrderEntitySessionBeanLocal;
import entity.Customer;
import entity.CustomisedIngredient;
import entity.OrderEntity;
import entity.OrderLineItem;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import util.enumeration.Status;
import util.exception.CheckOutShoppingCartException;
import util.exception.CreateNewOrderException;
import util.exception.CustomerNotFoundException;
import util.exception.ShoppingCartIsEmptyException;

/**
 *
 * @author PYT
 */
@Named(value = "shoppingCartManagedBean")
@SessionScoped
public class ShoppingCartManagedBean implements Serializable {

    @EJB(name = "OrderEntitySessionBeanLocal")
    private OrderEntitySessionBeanLocal orderEntitySessionBeanLocal;

    @EJB(name = "CustomerSessionBeanLocal")
    private CustomerSessionBeanLocal customerSessionBeanLocal;
    
    private ArrayList<OrderLineItem> orderLineItems;
    
    private BigDecimal totalPrice;
    
    private OrderLineItem orderLineItemToUpdate;
    
    public ShoppingCartManagedBean() {
        this.orderLineItems = new ArrayList<>();
    }
    
    public void removeFromShoppingCart(ActionEvent event) throws IOException {
        OrderLineItem oli = (OrderLineItem) event.getComponent().getAttributes().get("orderLineItemToRemove");
        orderLineItems.remove(oli);
        System.err.println("******* Recipe: " + oli.getRecipe().getRecipeId() + " removed from cart!");
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Product: " + oli.getRecipe().getRecipeTitle() + " removed from cart!", null));
    }
    
    public void checkoutShoppingCart(ActionEvent event)throws IOException, CheckOutShoppingCartException, ShoppingCartIsEmptyException {
        Customer customer = (Customer) event.getComponent().getAttributes().get("customerToCheckOut");
        Integer serialNo = 1;
        Integer totalQuantity = 0;
        BigDecimal totalAmt = BigDecimal.valueOf(0);
        
        if (!orderLineItems.isEmpty()) {
            try {
                
                OrderEntity newOrderEntity = new OrderEntity(1,totalPrice,false,new Date(),Status.PENDING,orderLineItems,customer);
                
                orderEntitySessionBeanLocal.createNewOrder(customer.getCustomerId(), newOrderEntity);
                
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Shopping cart checked out successfully!", null));
            
            } catch (CustomerNotFoundException  ex) {
                throw new CheckOutShoppingCartException(ex.getMessage());
            } catch (CreateNewOrderException ex) {
                Logger.getLogger(ShoppingCartManagedBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            throw new ShoppingCartIsEmptyException("Shopping cart is empty, cannot checkout.");
        }
    }
    
    public void doUpdateOrderLineItem(ActionEvent event)throws IOException{
        OrderLineItem oli = (OrderLineItem)event.getComponent().getAttributes().get("orderLineItemToUpdate");
        setOrderLineItemToUpdate(oli);
    }
    
    public void removeCustomisedIngredientFromOrderLineItem(ActionEvent event)throws IOException{
        CustomisedIngredient ci = (CustomisedIngredient)event.getComponent().getAttributes().get("customisedIngredientToRemove");
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
        for(OrderLineItem orderLineItem:orderLineItems){
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
   
    
}
