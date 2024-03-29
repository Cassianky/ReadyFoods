/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.CreditCardSessionBeanLocal;
import ejb.session.stateless.CustomerSessionBeanLocal;
import ejb.session.stateless.SubscriptionSessionBeanLocal;
import entity.CreditCard;
import entity.Customer;
import entity.Subscription;
import java.io.IOException;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import util.exception.CreditCardNotFoundException;
import util.exception.CustomerNotFoundException;
import util.exception.DeleteCreditCardException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author PYT
 */
@Named(value = "paymentManagedBean")
@ViewScoped
public class PaymentManagedBean implements Serializable {

    @EJB(name = "CustomerSessionBeanLocal")
    private CustomerSessionBeanLocal customerSessionBeanLocal;

    @EJB(name = "CreditCardSessionBeanLocal")
    private CreditCardSessionBeanLocal creditCardSessionBeanLocal;

    @NotNull(message = "Credit Card Number is required")
    @Size(min = 19, max = 19)
    private String ccNumber;

    @NotNull(message = "CVV is required")
    @Min(100)
    @Max(999)
    private Integer CVV;

    @NotNull(message = "Name on card is required")
    @Size(min = 5, max = 64)
    private String nameOnCard;

    @NotNull(message = "Expiry date is required")
    private Date expiryDate;

    private Boolean isSubscribed;

    @Inject
    private ShoppingCartManagedBean shoppingCartManagedBean;

    @Inject
    private ProfileManagedBean profileManagedBean;

    public PaymentManagedBean() {
    }

    @PostConstruct
    public void postConstruct() {
        try {
            Customer customer = (Customer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentCustomer");
            Customer currentCustomer = customerSessionBeanLocal.retrieveCustomerByCustomerId(customer.getCustomerId());
            List<Subscription> subscriptions = currentCustomer.getSubscriptions();
            isSubscribed = false;
            //If subscribed, user cannot delete credit card
            for (Subscription sub : subscriptions) {
                if (sub.getOngoing() == true) {
                    isSubscribed = true;
                }
            }
            System.out.println("jsf.managedbean.PaymentManagedBean.postConstruct()");
            if (profileManagedBean.getCreditCard() != null) {
                setCcNumber(profileManagedBean.getCreditCard().getCcNumber());
                setCVV(profileManagedBean.getCreditCard().getCVV());
                setExpiryDate(profileManagedBean.getCreditCard().getExpiryDate());
                setNameOnCard(profileManagedBean.getCreditCard().getNameOnCard());
            } else {
                System.out.println("jsf.managedbean.PaymentManagedBean.postConstruct()");

            }
        } catch (CustomerNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Customer ID is not found!" + ex.getMessage(), ""));
        }
    }

    public void foo() {

    }

    public void deleteCard(ActionEvent event) {
        try {
            creditCardSessionBeanLocal.deleteCreditCardByCustomerId(profileManagedBean.getCurrentCustomer().getCustomerId());
            setCVV(null);
            setCcNumber(null);
            setExpiryDate(null);
            setNameOnCard(null);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Credit Card deleted successfully!: ", null));
        } catch (DeleteCreditCardException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error occured while adding deleting CC: " + ex.getMessage(), null));
        } catch (CreditCardNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "CC Not Found!: " + ex.getMessage(), null));
        } catch (CustomerNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Customer Not Found!: " + ex.getMessage(), null));
        }
    }

    public void saveCC(ActionEvent event) throws IOException {
        System.out.println("jsf.managedbean.PaymentManagedBean.saveCC()");
        Customer currentCustomer = (Customer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentCustomer");
        CreditCard newCreditCard = new CreditCard(ccNumber, CVV, nameOnCard, expiryDate);
        try {
            Long ccId = creditCardSessionBeanLocal.createNewCreditCard(newCreditCard, currentCustomer.getCustomerId());
            CreditCard createdCard = creditCardSessionBeanLocal.retrieveCreditCardByCreditCardId(ccId);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Credit Card successfully updated!", "Credit Card Number: " + createdCard.getCcNumber()));
            shoppingCartManagedBean.setCreditCard(createdCard);

        } catch (UnknownPersistenceException ex) {
            ex.printStackTrace();
        } catch (CustomerNotFoundException ex) {
            ex.printStackTrace();
        } catch (InputDataValidationException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error occured while adding new CC: " + ex.getMessage(), null));
        } catch (CreditCardNotFoundException ex) {
            Logger.getLogger(PaymentManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void redirectBackToCheckOut() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewShoppingCart.xhtml");
    }

    public void redirectToOrderPayment() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect("orderPayment.xhtml");
    }

    /**
     * @return the CVV
     */
    public Integer getCVV() {
        return CVV;
    }

    /**
     * @param CVV the CVV to set
     */
    public void setCVV(Integer CVV) {
        System.out.println("jsf.managedbean.PaymentManagedBean.setCVV(): " + CVV);
        this.CVV = CVV;
    }

    /**
     * @return the nameOnCard
     */
    public String getNameOnCard() {
        return nameOnCard;
    }

    /**
     * @param nameOnCard the nameOnCard to set
     */
    public void setNameOnCard(String nameOnCard) {
        System.out.println("jsf.managedbean.PaymentManagedBean.setNameOnCard(): " + nameOnCard);
        this.nameOnCard = nameOnCard;
    }

    /**
     * @return the expiryDate
     */
    public Date getExpiryDate() {
        return expiryDate;
    }

    /**
     * @param expiryDate the expiryDate to set
     */
    public void setExpiryDate(Date expiryDate) {
        System.out.println("jsf.managedbean.PaymentManagedBean.setExpiryDate()");
        this.expiryDate = expiryDate;
    }

    /**
     * @return the ccNumber
     */
    public String getCcNumber() {
        return ccNumber;
    }

    /**
     * @param ccNumber the ccNumber to set
     */
    public void setCcNumber(String ccNumber) {
        System.out.println("jsf.managedbean.PaymentManagedBean.setCcNumber(): " + ccNumber);
        this.ccNumber = ccNumber;
    }

    /**
     * @return the isSubscribed
     */
    public Boolean getIsSubscribed() {
        return isSubscribed;
    }

    /**
     * @param isSubscribed the isSubscribed to set
     */
    public void setIsSubscribed(Boolean isSubscribed) {
        this.isSubscribed = isSubscribed;
    }

}
