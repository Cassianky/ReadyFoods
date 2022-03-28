/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.OrderSessionBeanLocal;
import entity.Customer;
import entity.Order;
import java.io.IOException;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;

/**
 *
 * @author PYT
 */
@Named(value = "orderManagedBean")
@ViewScoped
public class OrderManagedBean implements Serializable {

    @EJB(name = "OrderSessionBeanLocal")
    private OrderSessionBeanLocal orderSessionBeanLocal;
    
    ArrayList<Order> listOfOrders;
    public OrderManagedBean() {
        this.listOfOrders = new ArrayList<>();
    }
    
    public List<Order> retrieveAllOrdersForACustomer() throws IOException {
        Customer customerEntity = (Customer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentCustomerEntity");
        Long customerId = customerEntity.getCustomerId();
        return orderSessionBeanLocal.retrieveAllOrdersForACustomer(customerId);

    }
    
}
