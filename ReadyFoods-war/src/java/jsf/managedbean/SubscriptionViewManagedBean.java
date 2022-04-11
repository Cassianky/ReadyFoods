/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.SubscriptionSessionBeanLocal;
import entity.OrderEntity;
import entity.Subscription;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import util.exception.SubscriptionNotFoundException;

/**
 *
 * @author angler
 */
@Named(value = "subscriptionViewManagedBean")
@ViewScoped
public class SubscriptionViewManagedBean implements Serializable {

    @EJB(name = "SubscriptionSessionBeanLocal")
    private SubscriptionSessionBeanLocal subscriptionSessionBeanLocal;

    /**
     * Creates a new instance of SubscriptionViewManagedBean
     */
    private Long subId;
    private Subscription subscription;
    
    private List<OrderEntity> orders;

    public SubscriptionViewManagedBean() {
    }

    @PostConstruct
    public void postConstruct() {
        try {
            subId = Long.valueOf(FacesContext.getCurrentInstance()
                    .getExternalContext().getRequestParameterMap().get("subIdToView"));
            subscription = subscriptionSessionBeanLocal.retrieveSubscriptionBySubscriptionId(subId);
            
            setOrders(subscription.getSubscriptionOrders());

        } catch (NumberFormatException ex) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "ID not provided", null));
        } catch (SubscriptionNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "An error has occurred while retrieving the subscription record: " + ex.getMessage(), null));
        }
    }

    /**
     * @return the subscription
     */
    public Subscription getSubscription() {
        return subscription;
    }

    /**
     * @param subscription the subscription to set
     */
    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    /**
     * @return the orders
     */
    public List<OrderEntity> getOrders() {
        return orders;
    }

    /**
     * @param orders the orders to set
     */
    public void setOrders(List<OrderEntity> orders) {
        this.orders = orders;
    }

}
