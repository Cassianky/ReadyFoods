/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import entity.Subscription;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CreateNewSubscriptionException;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.NoOngoingSubscriptionException;
import util.exception.SubscriptionNotFoundException;

/**
 *
 * @author angler
 */
@Stateless
public class SubscriptionSessionBean implements SubscriptionSessionBeanLocal {

    @EJB(name = "CustomerSessionBeanLocal")
    private CustomerSessionBeanLocal customerSessionBeanLocal;

    @PersistenceContext(unitName = "ReadyFoods-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public SubscriptionSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Subscription createNewSubscription(Long customerId, Subscription newSubscription)
            throws CustomerNotFoundException, CreateNewSubscriptionException, InputDataValidationException {
        Set<ConstraintViolation<Subscription>> constraintViolations = validator.validate(newSubscription);

        if (constraintViolations.isEmpty()) {

            if (newSubscription != null) {

                Customer customer = customerSessionBeanLocal.retrieveCustomerByCustomerId(customerId);
                //newSaleTransactionEntity.setSubscriptionEntity(staffEntity);
                customer.getSubscriptions().add(newSubscription);

                em.persist(newSubscription);

                em.flush();

                return newSubscription;

            } else {
                throw new CreateNewSubscriptionException("Subscription information not provided");
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    // basic cancellation w no edge case check
    public void cancelSubscription(Long subscriptionId) throws SubscriptionNotFoundException {
        Subscription subToCancel = retrieveSubscriptionBySubscriptionId(subscriptionId);
        subToCancel.setOngoing(false);
        
    
    }

    @Override
    public List<Subscription> retrieveAllSubscriptions() {
        Query query = em.createQuery("SELECT s FROM Subscription s");
        List<Subscription> subscriptions = query.getResultList();

        for (Subscription s : subscriptions) {
            s.getSubscriptionOrders().size();

        }

        return subscriptions;
    }

    @Override
    public Subscription retrieveSubscriptionBySubscriptionId(Long subId) throws SubscriptionNotFoundException {
        Subscription subscription = em.find(Subscription.class, subId);

        if (subscription != null) {
            subscription.getSubscriptionOrders().size();
            return subscription;
        } else {
            throw new SubscriptionNotFoundException("Subscription ID " + subId + " does not exist!");
        }
    }

    @Override
    public Subscription retrieveOngoingSubscriptionForCustomer(Long customerId)
            throws CustomerNotFoundException, NoOngoingSubscriptionException {
        Customer customer = customerSessionBeanLocal.retrieveCustomerByCustomerId(customerId);

        List<Subscription> subscriptions = customer.getSubscriptions();
        for (Subscription s : subscriptions) {
            if (s.getOngoing()) {
                s.getSubscriptionOrders().size();
                return s;
            }
        }
        throw new NoOngoingSubscriptionException("Customer does not have an ongoing subscription.");

    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Subscription>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - "
                    + constraintViolation.getInvalidValue() + "; "
                    + constraintViolation.getMessage();
        }

        return msg;
    }

}
