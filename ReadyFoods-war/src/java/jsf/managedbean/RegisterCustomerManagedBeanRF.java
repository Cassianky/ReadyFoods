package jsf.managedbean;

import ejb.session.stateless.CustomerSessionBeanLocal;
import entity.Customer;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
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
    private LocalDate maxDate;

    public RegisterCustomerManagedBeanRF() {
        newCustomer = new Customer();
        LocalDate today = LocalDate.now();
        int yearToday = today.getYear();
        int monthToday = today.getMonthValue();
        int dateToday = today.getDayOfMonth();
        maxDate = LocalDate.of(yearToday-18, monthToday, dateToday);
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
 
            Future<Boolean> asyncResult = customerSessionBeanLocal.sendWelcomeEmail(ce.getFirstName(), ce.getEmail(), path);

             FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "A welcome email is also sent! Please check your email!", null));

            newCustomer = new Customer();

        } catch (CustomerEmailExistsException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Customer Email already has Account! ", null));
        } catch (InterruptedException | CustomerNotFoundException | InputDataValidationException | UnknownPersistenceException ex) {
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

    /**
     * @return the maxDate
     */
    public LocalDate getMaxDate() {
        return maxDate;
    }

    /**
     * @param maxDate the maxDate to set
     */
    public void setMaxDate(LocalDate maxDate) {
        this.maxDate = maxDate;
    }

    

}
