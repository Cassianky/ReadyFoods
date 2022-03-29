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
import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import util.exception.CreateNewSubscriptionException;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.NoOngoingSubscriptionException;

/**
 *
 * @author angler
 */
@Named(value = "newSubscriptionManagedBean")
@ViewScoped
public class NewSubscriptionManagedBean implements Serializable {

    @EJB(name = "SubscriptionSessionBeanLocal")
    private SubscriptionSessionBeanLocal subscriptionSessionBeanLocal;

    @EJB(name = "CustomerSessionBeanLocal")
    private CustomerSessionBeanLocal customerSessionBeanLocal;

    private Subscription newSubscription;
    private Customer currentCustomerEntity;

    private BigDecimal shippingCost = BigDecimal.valueOf(5);

    private Boolean ongoing; // customer has an ongoing subscription

    /**
     * Creates a new instance of NewSubscriptionManagedBean
     */
    public NewSubscriptionManagedBean() {
        this.newSubscription = new Subscription();

    }

    @PostConstruct
    public void postConstruct() {
        setCurrentCustomerEntity((Customer) FacesContext.getCurrentInstance().
                getExternalContext().getSessionMap().get("currentCustomer"));
        Customer customer;
        try {
            customer = customerSessionBeanLocal.retrieveCustomerByCustomerId(getCurrentCustomerEntity().getCustomerId());
            Subscription ongoingSubscription = subscriptionSessionBeanLocal.
                    retrieveOngoingSubscriptionForCustomer(getCurrentCustomerEntity().getCustomerId());

            this.ongoing = !(ongoingSubscription == null);

        } catch (CustomerNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Customer not found" + ex.getMessage(), null));
        } catch (NoOngoingSubscriptionException ex) {

            this.ongoing = false;

            this.newSubscription = new Subscription(new Date(),
                    1, 2, 2, BigDecimal.ZERO, true);

            calculateWeeklyPrice();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Create new" + ex.getMessage(), null));
        }

    }



    public void updateWeeklyPrice(AjaxBehaviorEvent event) {
        calculateWeeklyPrice();
        //System.out.println(this.newSubscription.getWeeklyPrice());
    }

    private void calculateWeeklyPrice() {
        BigDecimal price = BigDecimal.valueOf(9.99 * (this.newSubscription.getNumOfPeople() * this.newSubscription.getNumOfRecipes()));
        this.newSubscription.setWeeklyPrice(price);

    }

    public void doCreatenewSubscription(ActionEvent event) throws CustomerNotFoundException, CreateNewSubscriptionException, InputDataValidationException {
        this.newSubscription.setRemainingDuration(this.newSubscription.getDuration() * 4);
        Subscription createdSubscription = subscriptionSessionBeanLocal.
                createNewSubscription(getCurrentCustomerEntity().getCustomerId(), newSubscription);

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Subscription successful: "
                        + createdSubscription.getSubscriptionId(), null));

    }

    /**
     * @return the newSubscription
     */
    public Subscription getNewSubscription() {
        return newSubscription;
    }

    /**
     * @param newSubscription the newSubscription to set
     */
    public void setNewSubscription(Subscription newSubscription) {
        this.newSubscription = newSubscription;
    }

    /**
     * @return the ongoing
     */
    public Boolean getOngoing() {
        return ongoing;
    }

    /**
     * @param ongoing the ongoing to set
     */
    public void setOngoing(Boolean ongoing) {
        this.ongoing = ongoing;
    }

    /**
     * @return the shippingCost
     */
    public BigDecimal getShippingCost() {
        return shippingCost;
    }

    /**
     * @param shippingCost the shippingCost to set
     */
    public void setShippingCost(BigDecimal shippingCost) {
        this.shippingCost = shippingCost;
    }

    /**
     * @return the currentCustomerEntity
     */
    public Customer getCurrentCustomerEntity() {
        return currentCustomerEntity;
    }

    /**
     * @param currentCustomerEntity the currentCustomerEntity to set
     */
    public void setCurrentCustomerEntity(Customer currentCustomerEntity) {
        this.currentCustomerEntity = currentCustomerEntity;
    }

}
