/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import entity.Subscription;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.CreateNewSubscriptionException;
import util.exception.CustomerNotFoundException;
import util.exception.SubscriptionNotFoundException;

/**
 *
 * @author angler
 */
@Stateless
public class SubscriptionSessionBean implements SubscriptionSessionBeanLocal {

    @PersistenceContext(unitName = "ReadyFoods-ejbPU")
    private EntityManager em;

    public Subscription createNewSubscription(Long customerId, Subscription newSubscription)
            throws CustomerNotFoundException, CreateNewSubscriptionException {

        if (newSubscription != null) {

            //Customer customer = customerSessionBeanLocal.retrieveCustomerByCustomerId(customerId);
            Customer customer = null; // placeholder
            //newSaleTransactionEntity.setStaffEntity(staffEntity);
            customer.getSubscriptions().add(newSubscription);

            em.persist(newSubscription);

            em.flush();

            return newSubscription;

        } else {
            throw new CreateNewSubscriptionException("Subscription information not provided");
        }
    }

    public List<Subscription> retrieveAllSubscriptions() {
        Query query = em.createQuery("SELECT s FROM Subscription s");

        return query.getResultList();
    }

    public Subscription retrieveSubscriptionBySubscriptionId(Long subId) throws SubscriptionNotFoundException {
        Subscription subscription = em.find(Subscription.class, subId);

        if (subscription != null) {
            return subscription;
        } else {
            throw new SubscriptionNotFoundException("Subscription ID " + subId + " does not exist!");
        }
    }

}
