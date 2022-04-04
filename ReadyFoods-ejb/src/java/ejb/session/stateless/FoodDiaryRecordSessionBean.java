package ejb.session.stateless;

import entity.Customer;
import entity.FoodDiaryRecord;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CustomerNotFoundException;
import util.exception.DeleteFoodDiaryRecordException;
import util.exception.FoodDiaryRecordNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

@Stateless
public class FoodDiaryRecordSessionBean implements FoodDiaryRecordSessionBeanLocal {

    @EJB(name = "CustomerSessionBeanLocal")
    private CustomerSessionBeanLocal customerSessionBeanLocal;

    @PersistenceContext(unitName = "ReadyFoods-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    
    public FoodDiaryRecordSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    @Override
    public Long createNewFoodDiaryRecord (FoodDiaryRecord newFoodDiaryRecord, Long customerId) throws CustomerNotFoundException, UnknownPersistenceException, InputDataValidationException {
        Set<ConstraintViolation<FoodDiaryRecord>> constraintViolations = validator.validate(newFoodDiaryRecord);
        Customer customer = customerSessionBeanLocal.retrieveCustomerByCustomerId(customerId);
        
        if (constraintViolations.isEmpty()) {
            try {
                //Association
                customer.addFoodDiaryRecord(newFoodDiaryRecord);
                em.persist(newFoodDiaryRecord);
                em.flush();

                return newFoodDiaryRecord.getFoodDiaryRecordId();
            } catch (PersistenceException ex) {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
     @Override
    public FoodDiaryRecord retrieveFoodDiaryRecordByFoodDiaryRecordId(Long foodDiaryRecordId) throws FoodDiaryRecordNotFoundException 
    {
        FoodDiaryRecord foodDiaryRecord = em.find(FoodDiaryRecord.class, foodDiaryRecordId);

        if (foodDiaryRecord != null) {

            return foodDiaryRecord;
        } else {
            throw new FoodDiaryRecordNotFoundException("Food Diary Record ID " + foodDiaryRecordId + " does not exist!");
        }
    }
    
    @Override
    public void deleteFoodDiaryRecordByFoodDiaryRecordId(Long foodDiaryRecordId, Long customerId) throws FoodDiaryRecordNotFoundException, DeleteFoodDiaryRecordException, CustomerNotFoundException
    {
        FoodDiaryRecord foodDiaryRecord = retrieveFoodDiaryRecordByFoodDiaryRecordId(foodDiaryRecordId);
        Customer customer = customerSessionBeanLocal.retrieveCustomerByCustomerId(customerId);
        
        //Disassociation
        customer.removeFoodDiaryRecord(foodDiaryRecord);
        
        em.remove(foodDiaryRecord);
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<FoodDiaryRecord>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
}
