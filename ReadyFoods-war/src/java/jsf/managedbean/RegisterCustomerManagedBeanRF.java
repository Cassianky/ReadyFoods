package jsf.managedbean;

import ejb.session.stateless.CustomerSessionBeanLocal;
import entity.Customer;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import util.enumeration.ActivityLevel;
import util.enumeration.DietType;
import util.enumeration.Gender;
import util.exception.CustomerEmailExistsException;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

@Named(value = "registerCustomerManagedBeanRF")
@ViewScoped
public class RegisterCustomerManagedBeanRF implements Serializable {

    @EJB
    private CustomerSessionBeanLocal customerSessionBeanLocal;

    private Customer newCustomer;
    private Gender[] genders;
    private ActivityLevel[] activityLevels;
    private DietType[] dietTypes;
    
    public RegisterCustomerManagedBeanRF() 
    {
        newCustomer = new Customer();
    }
    
    @PostConstruct 
    public void postConstruct() {

    }
    public void registerNewCustomer(ActionEvent event)
    {        
                  
        try
        {
            Long newCustomerId = customerSessionBeanLocal.createNewCustomer(newCustomer);
            Customer ce = customerSessionBeanLocal.retrieveCustomerByCustomerId(newCustomerId);
 
            newCustomer = new Customer();

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Customer Registered Successfully! (Email: " + ce.getEmail() + ")", null));
        }
        catch(CustomerNotFoundException | InputDataValidationException | UnknownPersistenceException | CustomerEmailExistsException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while registering: " + ex.getMessage(), null));
        }
    }
    /**
     * @return the newCustomer
     */
    public Customer getNewCustomer() {
        return newCustomer;
    }

    /**
     * @param newCustomer the newCustomer to set
     */
    public void setNewCustomer(Customer newCustomer) {
        this.newCustomer = newCustomer;
    }

    /**
     * @return the genders
     */
    public Gender[] getGenders() {
        return Gender.values();
    }

    /**
     * @param genders the genders to set
     */
    public void setGenders(Gender[] genders) {
        this.genders = genders;
    }

    /**
     * @return the activityLevels
     */
    public ActivityLevel[] getActivityLevels() {
        return ActivityLevel.values();
    }

    /**
     * @param activityLevels the activityLevels to set
     */
    public void setActivityLevels(ActivityLevel[] activityLevels) {
        this.activityLevels = activityLevels;
    }

    /**
     * @return the dietTypes
     */
    public DietType[] getDietTypes() {
        return DietType.values();
    }

    /**
     * @param dietTypes the dietTypes to set
     */
    public void setDietTypes(DietType[] dietTypes) {
        this.dietTypes = dietTypes;
    }
    
}
