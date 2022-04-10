/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.CreditCardSessionBeanLocal;
import entity.CreditCard;
import entity.Customer;
import java.io.IOException;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.Date;
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
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author PYT
 */
@Named(value = "paymentManagedBean")
@ViewScoped
public class PaymentManagedBean implements Serializable {

    @EJB(name = "CreditCardSessionBeanLocal")
    private CreditCardSessionBeanLocal creditCardSessionBeanLocal;

    @NotNull(message = "Credit Card Number is required")
    @Size(min=19, max = 19)
    private String ccNumber;

    @NotNull(message = "CVV is required")
    @Min(100)
    @Max(999)
    private Integer CVV;

    @NotNull(message = "Name on card is required")
    @Size(min=5, max = 64)
    private String nameOnCard;

    @NotNull(message = "Expiry date is required")
    private Date expiryDate;

    @Inject
    private ShoppingCartManagedBean shoppingCartManagedBean;
    
    public PaymentManagedBean() {
    }

    @PostConstruct
    public void postConstruct() {
        System.out.println("jsf.managedbean.PaymentManagedBean.postConstruct()");
    }

    public void foo() {

    }

    public void saveCC(ActionEvent event) throws IOException {
        System.out.println("jsf.managedbean.PaymentManagedBean.saveCC()");
        Customer currentCustomer = (Customer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentCustomer");
        CreditCard newCreditCard = new CreditCard(ccNumber, CVV, nameOnCard, expiryDate);
        try {
            Long ccId = creditCardSessionBeanLocal.createNewCreditCard(newCreditCard, currentCustomer.getCustomerId());
            CreditCard createdCard = creditCardSessionBeanLocal.retrieveCreditCardByCreditCardId(ccId);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Credit Card successfully created!", "Credit Card Number: " + createdCard.getCcNumber()));
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

}
