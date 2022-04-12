/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.OrderEntity;
import entity.Subscription;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import util.enumeration.Status;

/**
 *
 * @author angler
 */
@Stateless
public class ProcessRecipeSelectionManagedBean implements ProcessRecipeSelectionManagedBeanLocal {

    @EJB(name = "SubscriptionSessionBeanLocal")
    private SubscriptionSessionBeanLocal subscriptionSessionBeanLocal;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Schedule(dayOfWeek = "7", hour = "23", minute = "59", second = "59")
    public void weeklyProcess() {
         System.out.println("**********Processing subscription orders manually***********");

        List<Subscription> ongoingSubscriptions = subscriptionSessionBeanLocal.retrieveAllOngoingSubscriptions();

        for (Subscription sub : ongoingSubscriptions) {
            if (sub.getCurrentOrder() == null) {
                System.out.println("TODO: Generate for customer if they do not select");
                
                
                
            } else {
                System.out.println("Current order found");
                sub.setRemainingDuration(sub.getRemainingDuration() - 1);
                
                sub.getCurrentOrder().setStatus(Status.PROCESSED);
                sub.getCurrentOrder().setPaid(Boolean.TRUE);
                sub.setCurrentOrder(null);
                
                if (sub.getRemainingDuration() == 0) {
                    sub.setOngoing(Boolean.FALSE);
                }
            }
        }

    }

    public void process() {
        System.out.println("**********Processing subscription orders manually***********");

        List<Subscription> ongoingSubscriptions = subscriptionSessionBeanLocal.retrieveAllOngoingSubscriptions();

        for (Subscription sub : ongoingSubscriptions) {
            if (sub.getCurrentOrder() == null) {
                System.out.println("TODO: Generate for customer if they do not select");
                
                
                
            } else {
                System.out.println("Current order found");
                sub.setRemainingDuration(sub.getRemainingDuration() - 1);
                
                sub.getCurrentOrder().setStatus(Status.PROCESSED);
                sub.getCurrentOrder().setPaid(Boolean.TRUE);
                sub.setCurrentOrder(null);
                
                if (sub.getRemainingDuration() == 0) {
                    sub.setOngoing(Boolean.FALSE);
                }
            }
        }

    }
}
