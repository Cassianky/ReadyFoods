/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Order;
import java.util.List;
import javax.ejb.Local;
import util.exception.CreateNewOrderException;
import util.exception.CustomerNotFoundException;
import util.exception.OrderNotFoundException;

/**
 *
 * @author PYT
 */
@Local
public interface OrderSessionBeanLocal {

    public Order retrieveOrderByOrderId(Long orderId) throws OrderNotFoundException;

    public List<Order> retrieveAllOrdersForACustomer(Long customerId);

    public List<Order> retrieveAllOrders();

    public Order createNewOrder(Long customerId, Order newOrderEntity) throws CustomerNotFoundException, CreateNewOrderException;
    
}
