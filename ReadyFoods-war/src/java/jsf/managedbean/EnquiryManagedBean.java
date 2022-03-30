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
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import util.exception.CreateNewEnquiryException;
import util.exception.CustomerNotFoundException;
import util.exception.EnquiryNotFoundException;
import util.exception.EnquiryResolveException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

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

    private List<Enquiry> filteredEnquiries;

    /**
     * Creates a new instance of EnquiryManagedBean
     */
    public EnquiryManagedBean() {
        this.newEnquiry = new Enquiry();

    }

    @PostConstruct
    public void postConstruct() {
        currentCustomerEntity = (Customer) FacesContext.getCurrentInstance().
                getExternalContext().getSessionMap().get("currentCustomer");
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

            if (filteredEnquiries != null) {
                filteredEnquiries.add(enquiry);
            }

            this.newEnquiry = new Enquiry();

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Enquiry has been submitted. (Enquiry ID: " + enquiry.getEnquiryId() + ")", null));
        } catch (CreateNewEnquiryException | CustomerNotFoundException | InputDataValidationException | UnknownPersistenceException ex) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, ""
                            + "An error has occurred while creating the new enquiry: " + ex.getMessage(), null));
        }
    }

    public void deleteEnquiry(ActionEvent event) {
        try {
            Enquiry enquiryToDel = (Enquiry) event.getComponent().getAttributes().get("enquiryToDelete");

            enquirySessionBeanLocal.deleteEnquiry(enquiryToDel.getEnquiryId());

            this.pastEnquiries.remove(enquiryToDel);

            if (filteredEnquiries != null) {
                filteredEnquiries.remove(enquiryToDel);
            }

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Enquiry removed successfully", null));
        } catch (EnquiryNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting enquiry: " + ex.getMessage(), null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }

    public void resolveEnquiry(ActionEvent event) {
        try {
            Enquiry enquiryToResolve = (Enquiry) event.getComponent().getAttributes().get("enquiryToResolve");

            enquirySessionBeanLocal.resolveEnquiry(enquiryToResolve.getEnquiryId());
            this.pastEnquiries = enquirySessionBeanLocal.retrieveAllEnquires();

            if (filteredEnquiries.contains(enquiryToResolve)) {
                for (int i = 0; i < filteredEnquiries.size(); i++) {
                    filteredEnquiries.set(i, enquirySessionBeanLocal.retrieveEnquiryByEnquiryId(filteredEnquiries.get(i).getEnquiryId()));

                }

            }

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Enquiry resolved successfully", null));
        } catch (EnquiryNotFoundException | EnquiryResolveException ex) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while resolving enquiry: " + ex.getMessage(), null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
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

    /**
     * @return the filteredEnquiries
     */
    public List<Enquiry> getFilteredEnquiries() {
        return filteredEnquiries;
    }

    /**
     * @param filteredEnquiries the filteredEnquiries to set
     */
    public void setFilteredEnquiries(List<Enquiry> filteredEnquiries) {
        this.filteredEnquiries = filteredEnquiries;
    }

}
