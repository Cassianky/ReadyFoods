package ejb.session.stateless;

import entity.Customer;
import entity.FoodDiaryRecord;
import entity.Recipe;
import static java.time.DayOfWeek.MONDAY;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import static java.time.temporal.TemporalAdjusters.next;
import static java.time.temporal.TemporalAdjusters.previousOrSame;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;
import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
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
import util.email.EmailManager;
import util.enumeration.ActivityLevel;
import util.enumeration.Gender;
import util.exception.CustomerEmailExistsException;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateCustomerException;
import util.security.CryptographicHelper;
import wrapper.FoodWrapper;

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
    public Long createNewCustomer(Customer newCustomer) throws CustomerEmailExistsException, UnknownPersistenceException, InputDataValidationException {
        Set<ConstraintViolation<Customer>> constraintViolations = validator.validate(newCustomer);

        if (constraintViolations.isEmpty()) {
            try {
                em.persist(newCustomer);
                em.flush();

                return newCustomer.getCustomerId();
            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new CustomerEmailExistsException();
                    } else {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    @Override
    public void updatePassword(Customer customer, String oldPassword, String newPassword) throws InputDataValidationException, CustomerNotFoundException, InvalidLoginCredentialException {
        if (customer != null && customer.getCustomerId() != null) {
            Set<ConstraintViolation<Customer>> constraintViolations = validator.validate(customer);

            if (constraintViolations.isEmpty()) {
                Customer customerToUpdate = retrieveCustomerByCustomerId(customer.getCustomerId());
                String oldPasswordHash = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(oldPassword + customer.getSalt()));

                if (customer.getPassword().equals(oldPasswordHash)) {
                    customerToUpdate.setSalt(CryptographicHelper.getInstance().generateRandomString(32));
                    customerToUpdate.setPassword(newPassword);
                } else {
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
    public List<Customer> retrieveAllCustomers() {
        Query query = em.createQuery("SELECT c FROM Customer c");

        return query.getResultList();
    }

    @Override
    public Customer retrieveCustomerByEmail(String email) throws CustomerNotFoundException {
        Query query = em.createQuery("SELECT c FROM Customer c WHERE c.email = :inEmail");
        query.setParameter("inEmail", email);

        try {
            return (Customer) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new CustomerNotFoundException("Customer Email " + email + " does not exist!");
        }
    }

    @Override
    public Customer customerLogin(String email, String password) throws InvalidLoginCredentialException {
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
            } else {
                throw new InvalidLoginCredentialException("Customer does not exist or invalid password!");
            }
        } catch (CustomerNotFoundException ex) {
            throw new InvalidLoginCredentialException("Customer does not exist or invalid password!");
        }
    }

    @Override
    public void addBookmarkedRecipe(Recipe recipeToAdd, Long customerId) {
        Customer customerToUpdate = em.find(Customer.class, customerId);
        customerToUpdate.getBookedmarkedRecipes().add(recipeToAdd);
    }

    @Override
    public void removeBookmarkedRecipe(Recipe recipeToRemove, Long customerId) {
        Customer customerToUpdate = em.find(Customer.class, customerId);
        customerToUpdate.getBookedmarkedRecipes().remove(recipeToRemove);
    }

    @Override
    public void banCustomer(Long customerId) {
        Customer customerToBan = em.find(Customer.class, customerId);
        customerToBan.setIsBanned(true);
    }

    @Override
    public void unbanCustomer(Long customerId) {
        Customer customerToBan = em.find(Customer.class, customerId);
        customerToBan.setIsBanned(false);
    }

    //Ask angely do we need to stop when customer can update some profile stuff?
    @Override
    public void updateCustomer(Customer customer) throws CustomerNotFoundException, UpdateCustomerException, InputDataValidationException {
        if (customer != null && customer.getCustomerId() != null) {
            Set<ConstraintViolation<Customer>> constraintViolations = validator.validate(customer);

            if (constraintViolations.isEmpty()) {
                Customer customerToUpdate = retrieveCustomerByCustomerId(customer.getCustomerId());

                if (customerToUpdate.getEmail().equals(customer.getEmail())) {
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
                } else {
                    throw new UpdateCustomerException("Email of customer record to be updated does not match the existing record");
                }
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } else {
            throw new CustomerNotFoundException("Customer ID not provided for customer to be updated");
        }
    }

    @Override
    public HashMap<String, Double> customerMacroGoals(Long customerId) throws CustomerNotFoundException {
        Customer currentCustomer = retrieveCustomerByCustomerId(customerId);
        LocalDate todayDate = LocalDate.now();
        int age = 0;
        double calories = 0;
        ActivityLevel activityLevel = currentCustomer.getActivityLevel();
        if (currentCustomer.getDob() != null && todayDate != null) {
            age = Period.between(currentCustomer.getDob(), todayDate).getYears();
        }

        if (age <= 30) {
            if (currentCustomer.getGender().equals(Gender.FEMALE)) {
                //Customer is female
                switch (activityLevel) {
                    case HIGH:
                        calories = 2400;
                        break;
                    case MODERATE:
                        calories = 2200;
                        break;
                    case LOW:
                        calories = 2000;
                        break;
                }
            } else {
                //Customer is male
                switch (activityLevel) {
                    case HIGH:
                        calories = 3200;
                        break;
                    case MODERATE:
                        calories = 2800;
                        break;
                    case LOW:
                        calories = 2600;
                        break;
                }
            }
        } else if (age <= 50) {
            if (currentCustomer.getGender().equals(Gender.FEMALE)) {
                //Customer is female
                switch (activityLevel) {
                    case HIGH:
                        calories = 2200;
                        break;
                    case MODERATE:
                        calories = 2000;
                        break;
                    case LOW:
                        calories = 1800;
                        break;
                }
            } else {
                //Customer is male
                switch (activityLevel) {
                    case HIGH:
                        calories = 3000;
                        break;
                    case MODERATE:
                        calories = 2600;
                        break;
                    case LOW:
                        calories = 2400;
                        break;
                }
            }
        } else {
            //Customer is older than 50 years old

            if (currentCustomer.getGender().equals(Gender.FEMALE)) {
                //Customer is female
                switch (activityLevel) {
                    case HIGH:
                        calories = 2200;
                        break;
                    case MODERATE:
                        calories = 1800;
                        break;
                    case LOW:
                        calories = 1600;
                        break;
                }
            } else {
                //Customer is male
                switch (activityLevel) {
                    case HIGH:
                        calories = 2800;
                        break;
                    case MODERATE:
                        calories = 2400;
                        break;
                    case LOW:
                        calories = 2200;
                        break;
                }
            }
        }

        HashMap<String, Double> map = new HashMap<String, Double>();
        double fats = (0.35 * calories) / 9.0;
        double sugar = (0.10 * calories) / 4.0;
        double protein = (0.35 * calories) / 4.0;
        double carbs = (0.65 * calories) / 4.0;
        map.put("calories", calories);
        map.put("fats", fats);
        map.put("sugar", sugar);
        map.put("protein", protein);
        map.put("carbs", carbs);
        return map;
    }

    @Override
    public FoodWrapper customerConsumedMacro(Long customerId) {
        Query query = em.createQuery("SELECT df from FoodDiaryRecord df WHERE df.customer.customerId = :inCustomerId AND df.startDate >= :inStartDate1 AND df.startDate < :inStartDate2");
        query.setParameter("inCustomerId", customerId);
        query.setParameter("inStartDate1", LocalDate.now().atTime(0, 0));
        query.setParameter("inStartDate2", LocalDate.now().plusDays(1).atTime(0, 0));
        List<FoodDiaryRecord> records = query.getResultList();

        FoodWrapper consumed = new FoodWrapper();
        for (FoodDiaryRecord record : records) {
            consumed.addCalories(record.getCalories());
            consumed.addCarbs(record.getCarbs());
            consumed.addFats(record.getFats());
            consumed.addProtein(record.getProtein());
            consumed.addSugar(record.getSugar());
        }

        return consumed;
    }

    @Override
    public FoodWrapper customerConsumedMacroForTheWeek(Long customerId) {
        Query query = em.createQuery("SELECT df from FoodDiaryRecord df WHERE df.customer.customerId = :inCustomerId AND df.startDate >= :inStartDate1 AND df.startDate < :inStartDate2");
        query.setParameter("inCustomerId", customerId);
        query.setParameter("inStartDate1", LocalDate.now().with(previousOrSame(MONDAY)).atTime(0, 0));
        query.setParameter("inStartDate2", LocalDate.now().with(next(MONDAY)).atTime(0, 0));
        List<FoodDiaryRecord> records = query.getResultList();

        FoodWrapper consumed = new FoodWrapper();
        for (FoodDiaryRecord record : records) {
            System.out.println("*******************************RecordID   " + record.getFoodDiaryRecordId());
            consumed.addCalories(record.getCalories());
            consumed.addCarbs(record.getCarbs());
            consumed.addFats(record.getFats());
            consumed.addProtein(record.getProtein());
            consumed.addSugar(record.getSugar());
        }

        return consumed;
    }

    @Override
    public List<FoodDiaryRecord> topFoodForEachMacro(Long customerId, String typeMacro) {
        List<FoodDiaryRecord> records = new ArrayList<>();
        if (typeMacro.equals("calories")) {
            Query query = em.createQuery("SELECT df from FoodDiaryRecord df WHERE df.customer.customerId = :inCustomerId AND df.startDate >= :inStartDate1"
                    + " AND df.calories = (SELECT MAX(df2.calories) FROM FoodDiaryRecord df2 WHERE df2.customer.customerId = :inCustomerId)");
            query.setParameter("inCustomerId", customerId);
            query.setParameter("inStartDate1", LocalDate.now().with(previousOrSame(MONDAY)).atTime(0, 0));
            records = query.getResultList();
        } else if (typeMacro.equals("carbs")) {
            Query query = em.createQuery("SELECT df from FoodDiaryRecord df WHERE df.customer.customerId = :inCustomerId AND df.startDate >= :inStartDate1"
                    + " AND df.carbs = (SELECT MAX(df2.carbs) FROM FoodDiaryRecord df2 WHERE df2.customer.customerId = :inCustomerId)");
            query.setParameter("inCustomerId", customerId);
            query.setParameter("inStartDate1", LocalDate.now().with(previousOrSame(MONDAY)).atTime(0, 0));
            records = query.getResultList();
        } else if (typeMacro.equals("protein")) {
            Query query = em.createQuery("SELECT df from FoodDiaryRecord df WHERE df.customer.customerId = :inCustomerId AND df.startDate >= :inStartDate1"
                    + " AND df.protein = (SELECT MAX(df2.protein) FROM FoodDiaryRecord df2 WHERE df2.customer.customerId = :inCustomerId)");
            query.setParameter("inCustomerId", customerId);
            query.setParameter("inStartDate1", LocalDate.now().with(previousOrSame(MONDAY)).atTime(0, 0));
            records = query.getResultList();
        } else if (typeMacro.equals("fats")) {
            Query query = em.createQuery("SELECT df from FoodDiaryRecord df WHERE df.customer.customerId = :inCustomerId AND df.startDate >= :inStartDate1"
                    + " AND df.fats = (SELECT MAX(df2.fats) FROM FoodDiaryRecord df2 WHERE df2.customer.customerId = :inCustomerId)");
            query.setParameter("inCustomerId", customerId);
            query.setParameter("inStartDate1", LocalDate.now().with(previousOrSame(MONDAY)).atTime(0, 0));
            records = query.getResultList();
        } else if (typeMacro.equals("sugar")) {
            Query query = em.createQuery("SELECT df from FoodDiaryRecord df WHERE df.customer.customerId = :inCustomerId AND df.startDate >= :inStartDate1"
                    + " AND df.sugar = (SELECT MAX(df2.sugar) FROM FoodDiaryRecord df2 WHERE df2.customer.customerId = :inCustomerId)");
            query.setParameter("inCustomerId", customerId);
            query.setParameter("inStartDate1", LocalDate.now().with(previousOrSame(MONDAY)).atTime(0, 0));
            records = query.getResultList();
        }

        return records;

    }

    @Override
    @Asynchronous
    public Future<Boolean> sendWelcomeEmail(String name, String email, String path) throws InterruptedException {
        EmailManager emailManager = new EmailManager("readyfoodscorporation@gmail.com", "fgdlhkfsl4648795");
        Boolean result = emailManager.email(name, "readyfoodscorporation@gmail.com", email, path);
        return new AsyncResult<>(result);
    }

    @Override
    @Asynchronous
    public Future<Boolean> sendOrderInvoiceEmail(String name, String email, String path) throws InterruptedException {
        EmailManager emailManager = new EmailManager("readyfoodscorporation@gmail.com", "fgdlhkfsl4648795");
        Boolean result = emailManager.emailOrderInvoice(name, "readyfoodscorporation@gmail.com", email, path);
        return new AsyncResult<>(result);
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Customer>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
}
