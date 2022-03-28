/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.CustomerSessionBeanLocal;
import ejb.session.stateless.EnquirySessionBeanLocal;
import entity.Customer;
import entity.Enquiry;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import util.exception.CreateNewEnquiryException;
import util.exception.CustomerNotFoundException;

/**
 *
 * @author angler
 */
@Named(value = "enquiryManagedBean")
@ViewScoped
public class EnquiryManagedBean implements Serializable {

    @EJB(name = "EnquirySessionBeanLocal")
    private EnquirySessionBeanLocal enquirySessionBeanLocal;

    @EJB(name = "CustomerSessionBeanLocal")
    private CustomerSessionBeanLocal customerSessionBeanLocal;

    private List<Enquiry> pastEnquiries;
    private Enquiry newEnquiry;
    private Customer currentCustomerEntity;

    /**
     * Creates a new instance of EnquiryManagedBean
     */
    public EnquiryManagedBean() {
        this.pastEnquiries = new ArrayList<>();
        this.newEnquiry = new Enquiry();
        
    }

    @PostConstruct
    public void postConstruct() {
        currentCustomerEntity = (Customer) FacesContext.getCurrentInstance().
                getExternalContext().getSessionMap().get("currentCustomerEntity");
        Customer customer;
        try {
            customer = customerSessionBeanLocal.retrieveCustomerByCustomerId(currentCustomerEntity.getCustomerId());

            this.pastEnquiries = customer.getEnquiries();
        } catch (CustomerNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Customer not found" + ex.getMessage(), null));
        }
    }

    public void doCreateNewEnquiry(ActionEvent event) {

        try {
            Enquiry enquiry = enquirySessionBeanLocal.createNewEnquiry(currentCustomerEntity.getCustomerId(), newEnquiry);
            pastEnquiries.add(newEnquiry);

            this.newEnquiry = new Enquiry();

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Enquiry has been submitted. (Enquiry ID: " + enquiry.getEnquiryId() + ")", null));
        } catch (CreateNewEnquiryException | CustomerNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, ""
                            + "An error has occurred while creating the new enquiry: " + ex.getMessage(), null));
        }
    }

    /**
     * @return the pastEnquiries
     */
    public List<Enquiry> getPastEnquiries() {
        return pastEnquiries;
    }

    /**
     * @param pastEnquiries the pastEnquiries to set
     */
    public void setPastEnquiries(List<Enquiry> pastEnquiries) {
        this.pastEnquiries = pastEnquiries;
    }

    /**
     * @return the newEnquiry
     */
    public Enquiry getNewEnquiry() {
        return newEnquiry;
    }

    /**
     * @param newEnquiry the newEnquiry to set
     */
    public void setNewEnquiry(Enquiry newEnquiry) {
        this.newEnquiry = newEnquiry;
    }

}
