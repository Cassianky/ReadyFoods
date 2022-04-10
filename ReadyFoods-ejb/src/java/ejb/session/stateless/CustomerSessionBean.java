package ejb.session.stateless;

import entity.Customer;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CustomerEmailExistsException;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateCustomerException;
import util.security.CryptographicHelper;

@Stateless
public class CustomerSessionBean implements CustomerSessionBeanLocal {

    @PersistenceContext(unitName = "ReadyFoods-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public CustomerSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Customer retrieveCustomerByCustomerId(Long customerId) throws CustomerNotFoundException {
        Customer customer = em.find(Customer.class, customerId);

        if (customer != null) {
            //May not need to load the enquiries here
            customer.getEnquiries().size();

            customer.getSubscriptions().size();
            customer.getOrders().size();
            customer.getBookedmarkedRecipes().size();
            customer.getFoods().size();
            customer.getCreditCard();
            customer.getFoodDiaryRecords().size();

            return customer;
        } else {
            throw new CustomerNotFoundException("Customer ID " + customerId + " does not exist!");
        }
    }

    @Override
    public Long createNewCustomer(Customer newCustomer) throws CustomerEmailExistsException, UnknownPersistenceException, InputDataValidationException 
    {
        Set<ConstraintViolation<Customer>>constraintViolations = validator.validate(newCustomer);
        
        if(constraintViolations.isEmpty())
        {
            try
            {
                em.persist(newCustomer);
                em.flush();

                return newCustomer.getCustomerId();
            }
            catch(PersistenceException ex)
            {
                if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
                {
                    if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
                    {
                        throw new CustomerEmailExistsException();
                    }
                    else
                    {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                }
                else
                {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
        }
        else
        {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    @Override
    public void updatePassword(Customer customer, String oldPassword, String newPassword) throws InputDataValidationException, CustomerNotFoundException, InvalidLoginCredentialException
    {
        if(customer != null && customer.getCustomerId() != null)
        {
            Set<ConstraintViolation<Customer>>constraintViolations = validator.validate(customer);
        
            if(constraintViolations.isEmpty())
            {
                Customer customerToUpdate = retrieveCustomerByCustomerId(customer.getCustomerId());
                String oldPasswordHash = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(oldPassword + customer.getSalt()));
            
                if (customer.getPassword().equals(oldPasswordHash)) {
                customerToUpdate.setSalt(CryptographicHelper.getInstance().generateRandomString(32));
                customerToUpdate.setPassword(newPassword);
            } else 
            {
                throw new InvalidLoginCredentialException("Customer does not exist or invalid password!");
            }
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } else {
            throw new CustomerNotFoundException("Customer ID not provided for customer to be updated");
        }
    }
            
    @Override
    public List<Customer> retrieveAllCustomers()
    {
        Query query = em.createQuery("SELECT c FROM Customer c");
        
        return query.getResultList();
    }
    
    @Override
    public Customer retrieveCustomerByEmail(String email) throws CustomerNotFoundException
    {
        Query query = em.createQuery("SELECT c FROM Customer c WHERE c.email = :inEmail");
        query.setParameter("inEmail", email);

        try 
        {
            return (Customer) query.getSingleResult();
        } 
        catch (NoResultException | NonUniqueResultException ex) 
        {
            throw new CustomerNotFoundException("Customer Email " + email + " does not exist!");
        }
    }

    @Override
    public Customer customerLogin(String email, String password) throws InvalidLoginCredentialException 
    {
        try {
            Customer customer = retrieveCustomerByEmail(email);
            String passwordHash = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(password + customer.getSalt()));
            System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
            if (customer.getPassword().equals(passwordHash)) {
                 System.out.println("###########################################################################");
                //May not need to load the enquiries here
                customer.getEnquiries().size();

                customer.getSubscriptions().size();
                customer.getOrders().size();
                customer.getBookedmarkedRecipes().size();
                customer.getFoods().size();
                customer.getCreditCard();
                return customer;
            } else 
            {
                throw new InvalidLoginCredentialException("Customer does not exist or invalid password!");
            }
        } catch (CustomerNotFoundException ex) 
        {
            throw new InvalidLoginCredentialException("Customer does not exist or invalid password!");
        }
    }
    
    //Ask angely do we need to stop when customer can update some profile stuff?
    @Override
    public void updateCustomer(Customer customer) throws CustomerNotFoundException, UpdateCustomerException, InputDataValidationException
    {
        if(customer != null && customer.getCustomerId() != null)
        {
            Set<ConstraintViolation<Customer>>constraintViolations = validator.validate(customer);
        
            if(constraintViolations.isEmpty())
            {
                Customer customerToUpdate = retrieveCustomerByCustomerId(customer.getCustomerId());

                if(customerToUpdate.getEmail().equals(customer.getEmail()))
                {
                   customerToUpdate.setFirstName(customer.getFirstName());
                   customerToUpdate.setLastName(customer.getLastName());
                   customerToUpdate.setUserName(customer.getUserName());
                   //Cannot update email
                   //customerToUpdate.setEmail(customer.getEmail());
                   customerToUpdate.setContactNumber(customer.getContactNumber());
                   customerToUpdate.setAddress(customer.getAddress());
                   customerToUpdate.setProfilePicture(customer.getProfilePicture());
                   customerToUpdate.setDob(customer.getDob());
                   customerToUpdate.setActivityLevel(customer.getActivityLevel());
                   customerToUpdate.setDietType(customer.getDietType());
                }
                else
                {
                    throw new UpdateCustomerException("Email of customer record to be updated does not match the existing record");
                }
            }
            else
            {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        }
        else
        {
            throw new CustomerNotFoundException("Customer ID not provided for customer to be updated");
        }
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Customer>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
}
