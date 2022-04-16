/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.CustomerSessionBeanLocal;
import ejb.session.stateless.SubscriptionSessionBeanLocal;
import entity.Customer;
import entity.Subscription;
import java.io.IOException;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import util.exception.CustomerNotFoundException;
import util.exception.NoOngoingSubscriptionException;
import util.exception.OrderNotFoundException;
import util.exception.SubscriptionNotFoundException;

/**
 *
 * @author angler
 */
@Named(value = "subscriptionManagedBean")
@ViewScoped
public class SubscriptionManagedBean implements Serializable {

    @EJB(name = "SubscriptionSessionBeanLocal")
    private SubscriptionSessionBeanLocal subscriptionSessionBeanLocal;

    @EJB(name = "CustomerSessionBeanLocal")
    private CustomerSessionBeanLocal customerSessionBeanLocal;
    private Customer currentCustomerEntity;

    private List<Subscription> pastSubscriptions;
    private Subscription ongoingSubscription;

    /**
     * Creates a new instance of SubscriptionManagementSessionBean
     */
    public SubscriptionManagedBean() {

    }

    @PostConstruct
    public void postConstruct() {
        currentCustomerEntity = (Customer) FacesContext.getCurrentInstance().
                getExternalContext().getSessionMap().get("currentCustomer");
        Customer customer;
        try {
            customer = customerSessionBeanLocal.retrieveCustomerByCustomerId(currentCustomerEntity.getCustomerId());
            this.pastSubscriptions = customer.getSubscriptions();

            // This line throws NoONgoingSubscriptionException
            this.ongoingSubscription = subscriptionSessionBeanLocal.
                    retrieveOngoingSubscriptionForCustomer(currentCustomerEntity.getCustomerId());

        } catch (CustomerNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Customer not found" + ex.getMessage(), null));
        } catch (NoOngoingSubscriptionException ex) {
            this.setOngoingSubscription(null);

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Create new" + ex.getMessage(), null));
        }

    }

//    public void cancelSubscription(ActionEvent event) {
//        try {
//            subscriptionSessionBeanLocal.cancelSubscription(ongoingSubscription.getSubscriptionId());
//
//            FacesContext.getCurrentInstance().addMessage(null,
//                    new FacesMessage(FacesMessage.SEVERITY_INFO,
//                            "Subscription successfully cancelled: "
//                            + ongoingSubscription.getSubscriptionId(), null));
//            this.ongoingSubscription = null;
//            Customer customer = customerSessionBeanLocal.retrieveCustomerByCustomerId(currentCustomerEntity.getCustomerId());
//            this.pastSubscriptions = customer.getSubscriptions();
//
//        } catch (SubscriptionNotFoundException ex) {
//            FacesContext.getCurrentInstance().addMessage(null,
//                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
//                            "Subscription not found" + ex.getMessage(), null));
//
//        } catch (CustomerNotFoundException ex) {
//            Logger.getLogger(SubscriptionManagedBean.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//    }
    public void cancelSubscription(ActionEvent event) {
        try {
            subscriptionSessionBeanLocal.cancelSubscription(currentCustomerEntity.getCustomerId(), ongoingSubscription.getSubscriptionId());

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Subscription successfully cancelled: "
                            + ongoingSubscription.getSubscriptionId(), null));
            this.ongoingSubscription = null;
            Customer customer = customerSessionBeanLocal.retrieveCustomerByCustomerId(currentCustomerEntity.getCustomerId());
            this.pastSubscriptions = customer.getSubscriptions();

        } catch (SubscriptionNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Subscription not found" + ex.getMessage(), null));

        } catch (CustomerNotFoundException | NoOngoingSubscriptionException | OrderNotFoundException ex) {
             FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Error when cancelling sub" + ex.getMessage(), null));
        }

    }

    public void redirectSubOrder(ActionEvent event) throws IOException {
        Long subIdToView = (Long) event.getComponent().getAttributes().get("subIdToView");

        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("subIdToView", subIdToView);

        FacesContext.getCurrentInstance().getExternalContext().redirect("viewSubscriptionOrder.xhtml");
    }

    /**
     * @return the pastSubscriptions
     */
    public List<Subscription> getPastSubscriptions() {
        return pastSubscriptions;
    }

    /**
     * @param pastSubscriptions the pastSubscriptions to set
     */
    public void setPastSubscriptions(List<Subscription> pastSubscriptions) {
        this.pastSubscriptions = pastSubscriptions;
    }

    /**
     * @return the ongoingSubscription
     */
    public Subscription getOngoingSubscription() {
        return ongoingSubscription;
    }

    /**
     * @param ongoingSubscription the ongoingSubscription to set
     */
    public void setOngoingSubscription(Subscription ongoingSubscription) {
        this.ongoingSubscription = ongoingSubscription;
    }

}
