/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import entity.Order;
import entity.OrderLineItem;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.CreateNewOrderException;
import util.exception.CustomerNotFoundException;
import util.exception.OrderNotFoundException;

/**
 *
 * @author PYT
 */
@Stateless
public class OrderSessionBean implements OrderSessionBeanLocal {

    @EJB
    private CustomerSessionBeanLocal customerSessionBeanLocal;

    @PersistenceContext(unitName = "ReadyFoods-ejbPU")
    private EntityManager entityManager;
    
    //inject customerSessionBean here

    public OrderSessionBean() {
    }

    @Override
    public Order createNewOrder(Long customerId, Order newOrderEntity) throws CustomerNotFoundException, CreateNewOrderException {
        if (newOrderEntity != null) {

            Customer customerEntity = customerSessionBeanLocal.retrieveCustomerByCustomerId(customerId);
            newOrderEntity.setCustomer(customerEntity);
            customerEntity.getOrders().add(newOrderEntity);

            entityManager.persist(newOrderEntity);

            for (OrderLineItem orderLineItemEntity : newOrderEntity.getOrderLineItems()) {
//                    productEntitySessionBeanLocal.debitQuantityOnHand(saleTransactionLineItemEntity.getProductEntity().getProductId(), saleTransactionLineItemEntity.getQuantity());
                entityManager.persist(orderLineItemEntity);
            }

            entityManager.flush();

            return newOrderEntity;
        } //            catch(ProductNotFoundException | ProductInsufficientQuantityOnHandException ex)
        //            {
        //                // The line below rolls back all changes made to the database.
        //                eJBContext.setRollbackOnly();
        //                
        //                throw new CreateNewSaleTransactionException(ex.getMessage());
        //            }
        else {
            throw new CreateNewOrderException("Order information not provided");
        }
    }

    @Override
    public List<Order> retrieveAllOrders() {
        Query query = entityManager.createQuery("SELECT o FROM Order o");

        return query.getResultList();
    }
    
    @Override
    public List<Order> retrieveAllOrdersForACustomer(Long customerId) {
        Query query = entityManager.createQuery("SELECT o FROM Order o WHERE o.customer.customerId=:inCustomerId");
        query.setParameter("inCsutomerId",customerId);
        return query.getResultList();
    }
    
    @Override
    public Order retrieveOrderByOrderId(Long orderId) throws OrderNotFoundException {
        Order orderEntity = entityManager.find(Order.class,orderId);
        if (orderEntity != null){
            orderEntity.getOrderLineItems().size();
            return orderEntity;
        } else {
            throw new OrderNotFoundException("Order " + orderId + " does not exist!");
        }
        
    }

}
