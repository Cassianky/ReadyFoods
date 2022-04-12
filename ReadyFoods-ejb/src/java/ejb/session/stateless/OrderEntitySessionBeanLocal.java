/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.OrderEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.CreateNewOrderException;
import util.exception.CustomerNotFoundException;
import util.exception.NoOngoingSubscriptionException;
import util.exception.OrderNotFoundException;

/**
 *
 * @author ngcas
 */
@Local
public interface OrderEntitySessionBeanLocal {

    public OrderEntity retrieveOrderByOrderId(Long orderId) throws OrderNotFoundException;

    public OrderEntity createNewOrder(Long customerId, OrderEntity newOrderEntity) throws CustomerNotFoundException, CreateNewOrderException;

    public List<OrderEntity> retrieveAllOrdersForACustomer(Long customerId);

    public List<OrderEntity> retrieveAllOrders();

    public void updateOrderStatusReceieved(Long orderId) throws OrderNotFoundException;

    public OrderEntity createNewSubscriptionOrder(Long customerId, OrderEntity newOrderEntity) throws CustomerNotFoundException, CreateNewOrderException, NoOngoingSubscriptionException;

    public OrderEntity deleteSubscriptionOrder(Long customerId, Long oldOrderEntityId) throws CustomerNotFoundException, NoOngoingSubscriptionException, OrderNotFoundException;

    public OrderEntity retrieveOrderByCustomerId(Long recipeId, Long customerId) throws OrderNotFoundException;

}
