/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.CustomerSessionBeanLocal;
import ejb.session.stateless.OrderEntitySessionBeanLocal;
import entity.Customer;
import entity.OrderEntity;
import entity.OrderLineItem;
import java.io.IOException;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import util.enumeration.Status;
import util.exception.CustomerNotFoundException;
import util.exception.OrderNotFoundException;

/**
 *
 * @author PYT
 */
@Named(value = "orderManagedBean")
@ViewScoped
public class OrderManagedBean implements Serializable {

    @EJB(name = "CustomerSessionBeanLocal")
    private CustomerSessionBeanLocal customerSessionBeanLocal;

    @EJB(name = "OrderSessionBeanLocal")
    private OrderEntitySessionBeanLocal orderSessionBeanLocal;

    private Customer currentCustomerEntity;

    private List<OrderEntity> listOfOrders;
    
    private Status[] statusEnum = Status.values();
    
   

    public OrderManagedBean() {
        this.listOfOrders = new ArrayList<>();

    }

    @PostConstruct
    public void postConstruct() {
        setCurrentCustomerEntity((Customer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentCustomer"));
        Customer customer;
        try {
            customer = customerSessionBeanLocal.retrieveCustomerByCustomerId(getCurrentCustomerEntity().getCustomerId());
            this.setListOfOrders(customer.getOrders());
        } catch (CustomerNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Customer not found" + ex.getMessage(), null));
        }
    }

    public List<OrderEntity> retrieveAllOrdersForACustomer() throws IOException {
        return listOfOrders;

    }

    public void doViewOrderEntity(ActionEvent event) throws IOException {
        try {
            Long orderId = (Long) event.getComponent().getAttributes().get("orderToView");
            
            OrderEntity orderRetrieved = orderSessionBeanLocal.retrieveOrderByOrderId(orderId);
            Status statusRetrieved = orderRetrieved.getStatus();
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("orderToView", orderRetrieved);
            FacesContext.getCurrentInstance().getExternalContext().redirect("viewAnOrder.xhtml");

            
        } catch (OrderNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public List<OrderEntity> getListOfOrders() {
        setListOfOrders(orderSessionBeanLocal.retrieveAllOrdersForACustomer(currentCustomerEntity.getCustomerId()));
        return orderSessionBeanLocal.retrieveAllOrdersForACustomer(currentCustomerEntity.getCustomerId());
    }

    /**
     * @param listOfOrders the listOfOrders to set
     */
    public void setListOfOrders(ArrayList<OrderEntity> listOfOrders) {
        this.listOfOrders = listOfOrders;
    }

    public void updateStatus(ActionEvent event) {

        Long orderEntityId = (Long) event.getComponent().getAttributes().get("orderToupdate");
        try {
            OrderEntity orderEntity = orderSessionBeanLocal.updateOrderStatusReceieved(orderEntityId);
            getListOfOrders();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Status for Order( " + orderEntity.getOrderEntityId() +") has been successfully updated to RECEIVED", null));

        } catch (OrderNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error when updating status: " + ex.getMessage(), null));
        }

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
     * @param listOfOrders the listOfOrders to set
     */
    public void setListOfOrders(List<OrderEntity> listOfOrders) {
        this.listOfOrders = listOfOrders;
    }


    /**
     * @return the statusEnum
     */
    public Status[] getStatusEnum() {
        return statusEnum;
    }

    /**
     * @param statusEnum the statusEnum to set
     */
    public void setStatusEnum(Status[] statusEnum) {
        this.statusEnum = statusEnum;
    }

    

}
