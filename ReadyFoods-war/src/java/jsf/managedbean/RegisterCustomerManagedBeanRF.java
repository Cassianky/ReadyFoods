package jsf.managedbean;

import ejb.session.stateless.CustomerSessionBeanLocal;
import entity.Customer;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import util.exception.CustomerEmailExistsException;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

@Named(value = "registerCustomerManagedBeanRF")
@RequestScoped
public class RegisterCustomerManagedBeanRF implements Serializable {

    @EJB
    private CustomerSessionBeanLocal customerSessionBeanLocal;

    private Customer newCustomer;
    
    public RegisterCustomerManagedBeanRF() 
    {
        newCustomer = new Customer();
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
    
}
