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
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import util.exception.CustomerNotFoundException;
import util.exception.NoOngoingSubscriptionException;

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
        this.pastSubscriptions = new ArrayList<>();
    }

    @PostConstruct
    public void postConstruct() {
        currentCustomerEntity = (Customer) FacesContext.getCurrentInstance().
                getExternalContext().getSessionMap().get("currentCustomer");
        Customer customer;
        try {
            customer = customerSessionBeanLocal.retrieveCustomerByCustomerId(currentCustomerEntity.getCustomerId());
            this.ongoingSubscription = subscriptionSessionBeanLocal.
                    retrieveOngoingSubscriptionForCustomer(currentCustomerEntity.getCustomerId());
            System.out.println("Ongoing subscription" + ongoingSubscription.getNumOfPeople());
            

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
