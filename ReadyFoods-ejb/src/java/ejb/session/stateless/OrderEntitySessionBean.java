/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import entity.CustomisedIngredient;
import entity.OrderEntity;
import entity.OrderLineItem;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.Status;
import util.exception.CreateNewOrderException;
import util.exception.CustomerNotFoundException;
import util.exception.IngredientInsufficientStockQuantityException;
import util.exception.IngredientNotFoundException;
import util.exception.OrderNotFoundException;

/**
 *
 * @author ngcas
 */
@Stateless
public class OrderEntitySessionBean implements OrderEntitySessionBeanLocal {

    @EJB(name = "IngredientSessionBeanLocal")
    private IngredientSessionBeanLocal ingredientSessionBeanLocal;

    @EJB
    private CustomerSessionBeanLocal customerSessionBeanLocal;

    @PersistenceContext(unitName = "ReadyFoods-ejbPU")
    private EntityManager entityManager;

    @Resource
    private EJBContext eJBContext;

    public OrderEntitySessionBean() {
    }

    @Override
    public OrderEntity createNewOrder(Long customerId, OrderEntity newOrderEntity) throws CustomerNotFoundException, CreateNewOrderException {
        if (newOrderEntity != null) {
            try {
                Customer customerEntity = customerSessionBeanLocal.retrieveCustomerByCustomerId(customerId);

                newOrderEntity.setCustomer(customerEntity);
                customerEntity.getOrders().add(newOrderEntity);

                if (customerEntity.getCreditCard() == null) {
                    eJBContext.setRollbackOnly();
                }

                entityManager.persist(newOrderEntity);

                for (OrderLineItem orderLineItemEntity : newOrderEntity.getOrderLineItems()) {
                    for (CustomisedIngredient ci : orderLineItemEntity.getCustomisedIngredients()) {
                        ingredientSessionBeanLocal.debitQuantityAtHand(ci.getIngredientId(), ci.getQuantityOfIngredient());
                    }
                    entityManager.persist(orderLineItemEntity);
                }
                entityManager.flush();
                return newOrderEntity;
            } catch (IngredientNotFoundException | IngredientInsufficientStockQuantityException ex) {
                // The line below rolls back all changes made to the database.
                eJBContext.setRollbackOnly();

                throw new CreateNewOrderException(ex.getMessage());
            }
        } else {
            throw new CreateNewOrderException("Order information not provided");
        }

    }

    @Override
    public List<OrderEntity> retrieveAllOrders() {
        Query query = entityManager.createQuery("SELECT o FROM OrderEntity o");

        return query.getResultList();
    }

    @Override
    public List<OrderEntity> retrieveAllOrdersForACustomer(Long customerId
    ) {
        Query query = entityManager.createQuery("SELECT o FROM OrderEntity o WHERE o.customer.customerId=:inCustomerId");
        query.setParameter("inCustomerId", customerId);
        return query.getResultList();
    }

    @Override
    public OrderEntity retrieveOrderByOrderId(Long orderId) throws OrderNotFoundException {
        OrderEntity orderEntity = entityManager.find(OrderEntity.class, orderId);
        if (orderEntity != null) {
            orderEntity.getOrderLineItems().size();
            return orderEntity;
        } else {
            throw new OrderNotFoundException("Order " + orderId + " does not exist!");
        }

    }

    @Override
    public void updateOrderStatusReceieved(Long orderId) throws OrderNotFoundException {
        OrderEntity orderEntity = entityManager.find(OrderEntity.class, orderId);
        if (orderEntity != null) {
            orderEntity.getOrderLineItems().size();
            orderEntity.setStatus(Status.RECEIVED);
        } else {
            throw new OrderNotFoundException("Order " + orderId + " does not exist!");
        }
    }
}
