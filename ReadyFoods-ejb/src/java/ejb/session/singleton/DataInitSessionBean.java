package ejb.session.singleton;

import ejb.session.stateless.CustomerSessionBeanLocal;
import entity.Customer;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.ActivityLevel;
import util.enumeration.DietType;
import util.enumeration.Gender;
import util.exception.CustomerEmailExistsException;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

@Singleton
@LocalBean
@Startup

public class DataInitSessionBean {

    @EJB
    private CustomerSessionBeanLocal customerSessionBeanLocal;

    @PersistenceContext(unitName = "ReadyFoods-ejbPU")
    private EntityManager em;

    @PostConstruct
    public void postConstruct()
    {
        try
        {
            customerSessionBeanLocal.retrieveCustomerByEmail("customer1@gmail.com");
        }
        catch(CustomerNotFoundException ex)
        {
            initializeData();
        }
    }
    
    private void initializeData()
    {
        try
        {
            Customer customer1 = new Customer("customer1", "customer1", "customer1", "99999999", "password", "customer1@gmail.com", "123 Street", 20, DietType.VEGAN, Gender.FEMALE, ActivityLevel.HIGH);
            customerSessionBeanLocal.createNewCustomer(customer1);
            
        }
        catch(InputDataValidationException| UnknownPersistenceException | CustomerEmailExistsException ex)
        {
            ex.printStackTrace();
        }
    }
}
