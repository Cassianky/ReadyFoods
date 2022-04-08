package jsf.managedbean;

import ejb.session.stateless.CustomerSessionBeanLocal;
import entity.Customer;
import java.io.Serializable;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import javax.annotation.PostConstruct;
import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import util.email.EmailManager;
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
    private String path;

    public RegisterCustomerManagedBeanRF() {
        newCustomer = new Customer();
    }

    @PostConstruct
    public void postConstruct() {
        path = FacesContext.getCurrentInstance().getExternalContext().getInitParameter("WELCOME_IMAGE_PATH");
    }

    public void registerNewCustomer(ActionEvent event) {

        try {
            Long newCustomerId = customerSessionBeanLocal.createNewCustomer(newCustomer);
            Customer ce = customerSessionBeanLocal.retrieveCustomerByCustomerId(newCustomerId);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Customer Registered Successfully! (Email: " + ce.getEmail() + ")", null));
            
            Future<Boolean> asyncResult = sendWelcomeEmail(ce.getFirstName(), ce.getEmail());

            //Email sending is slow, please be patient!
            Thread thread = new Thread() {
                public void run() {
                    try {
                        if (asyncResult.get()) {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Email sent successfully", null));
                        } else {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while sending email", null));
                        }
                    } catch (ExecutionException | InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            };

            thread.start();

            newCustomer = new Customer();

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Customer Registered Successfully! (Email: " + ce.getEmail() + ")", null));
        } catch (CustomerEmailExistsException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Customer Email already has Account! ", null));
        } catch (InterruptedException | CustomerNotFoundException | InputDataValidationException | UnknownPersistenceException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while registering: " + ex.getMessage(), null));
        }
    }

    @Asynchronous
    public Future<Boolean> sendWelcomeEmail(String name, String email) throws InterruptedException {
        EmailManager emailManager = new EmailManager("readyfoodscorporation@gmail.com", "fgdlhkfsl4648795");
        Boolean result = emailManager.email(name, "readyfoodscorporation@gmail.com", email, path);
        return new AsyncResult<>(result);
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
