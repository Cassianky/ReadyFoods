/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.OrderEntity;
import entity.OrderLineItem;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author PYT
 */
@Named(value = "orderViewManagedBean")
@ViewScoped
public class OrderViewManagedBean implements Serializable {
    
    private OrderEntity orderToView;
    
    private List<OrderLineItem> orderLineItems;
    
    public OrderViewManagedBean() {
    }
    
    @PostConstruct
    public void postConstruct() {
        try{
        orderToView = (OrderEntity)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("orderToView");
        setOrderLineItems(orderToView.getOrderLineItems());
        } catch (NumberFormatException ex){
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Order not provided", null));
        }
    }

    /**
     * @return the orderToView
     */
    public OrderEntity getOrderToView() {
        return orderToView;
    }

    /**
     * @param orderToView the orderToView to set
     */
    public void setOrderToView(OrderEntity orderToView) {
        this.orderToView = orderToView;
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
    
}
