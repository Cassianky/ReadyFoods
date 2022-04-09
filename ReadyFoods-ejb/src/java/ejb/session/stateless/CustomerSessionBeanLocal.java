package ejb.session.stateless;

import entity.Customer;
import java.util.List;
import javax.ejb.Local;
import util.exception.CustomerEmailExistsException;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateCustomerException;

@Local
public interface CustomerSessionBeanLocal {

    public Customer retrieveCustomerByCustomerId(Long customerId) throws CustomerNotFoundException;

    public Long createNewCustomer(Customer newCustomer) throws CustomerEmailExistsException, UnknownPersistenceException, InputDataValidationException;

    public Customer retrieveCustomerByEmail(String email) throws CustomerNotFoundException;

    public Customer customerLogin(String email, String password) throws InvalidLoginCredentialException;

    public void updateCustomer(Customer customer) throws CustomerNotFoundException, UpdateCustomerException, InputDataValidationException;

    public List<Customer> retrieveAllCustomers();

    public void updatePassword(Customer customer, String oldPassword, String newPassword) throws InputDataValidationException, CustomerNotFoundException, InvalidLoginCredentialException;
}
