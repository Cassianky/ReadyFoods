package ejb.session.stateless;

import entity.Customer;
import entity.FoodDiaryRecord;
import entity.Recipe;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Future;
import javax.ejb.Local;
import util.exception.CustomerEmailExistsException;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateCustomerException;
import wrapper.FoodWrapper;

@Local
public interface CustomerSessionBeanLocal {

    public Customer retrieveCustomerByCustomerId(Long customerId) throws CustomerNotFoundException;

    public Long createNewCustomer(Customer newCustomer) throws CustomerEmailExistsException, UnknownPersistenceException, InputDataValidationException;

    public Customer retrieveCustomerByEmail(String email) throws CustomerNotFoundException;

    public Customer customerLogin(String email, String password) throws InvalidLoginCredentialException;

    public void updateCustomer(Customer customer) throws CustomerNotFoundException, UpdateCustomerException, InputDataValidationException;

    public List<Customer> retrieveAllCustomers();

    public void updatePassword(Customer customer, String oldPassword, String newPassword) throws InputDataValidationException, CustomerNotFoundException, InvalidLoginCredentialException;

    public void addBookmarkedRecipe(Recipe recipeToAdd, Long customerId);

    public void removeBookmarkedRecipe(Recipe recipeToRemove, Long customerId);

    public FoodWrapper customerConsumedMacroForTheWeek(Long customerId);

    public FoodWrapper customerConsumedMacro(Long customerId);

    public HashMap<String, Double> customerMacroGoals(Long customerId) throws CustomerNotFoundException;

    public List<FoodDiaryRecord> topFoodForEachMacro(Long customerId, String typeMacro);

    public Future<Boolean> sendWelcomeEmail(String name, String email, String path) throws InterruptedException;

    public Future<Boolean> sendOrderInvoiceEmail(String name, String email, String path) throws InterruptedException;
}
