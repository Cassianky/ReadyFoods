/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Subscription;
import java.util.List;
import javax.ejb.Local;
import util.exception.CreateNewSubscriptionException;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.NoOngoingSubscriptionException;
import util.exception.SubscriptionNotFoundException;

/**
 *
 * @author angler
 */
@Local
public interface SubscriptionSessionBeanLocal {

    public Subscription createNewSubscription(Long customerId, Subscription newSubscription) throws CustomerNotFoundException, CreateNewSubscriptionException, InputDataValidationException;

    public List<Subscription> retrieveAllSubscriptions();

    public Subscription retrieveSubscriptionBySubscriptionId(Long subId) throws SubscriptionNotFoundException;

    public Subscription retrieveOngoingSubscriptionForCustomer(Long customerId) throws CustomerNotFoundException, NoOngoingSubscriptionException;

    public void cancelSubscription(Long subscriptionId) throws SubscriptionNotFoundException;
    
}
