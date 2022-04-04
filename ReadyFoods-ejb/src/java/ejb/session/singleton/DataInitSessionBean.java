package ejb.session.singleton;

import ejb.session.stateless.CustomerSessionBeanLocal;
import ejb.session.stateless.FoodSessionBeanLocal;
import entity.Customer;
import entity.Food;
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
    private FoodSessionBeanLocal foodSessionBeanLocal;

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
            Food food = new Food("fries", 1000, 100, 100, 100, 100);
            foodSessionBeanLocal.createNewFood(food, customer1.getCustomerId());
            Food food2 = new Food("apple", 1000, 100, 100, 100, 100);
            foodSessionBeanLocal.createNewFood(food2, customer1.getCustomerId());
        }
        catch(CustomerNotFoundException | InputDataValidationException| UnknownPersistenceException | CustomerEmailExistsException ex)
        {
            ex.printStackTrace();
        }
    }
}
