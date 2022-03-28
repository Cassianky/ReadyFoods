package ejb.session.stateless;

import entity.Customer;
import entity.Food;
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
import util.exception.DeleteFoodException;
import util.exception.FoodNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

@Stateless
public class FoodSessionBean implements FoodSessionBeanLocal {

    @EJB
    private CustomerSessionBeanLocal customerSessionBeanLocal;
    
    @PersistenceContext(unitName = "ReadyFoods-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public FoodSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Long createNewFood(Food newFood, Long customerId) throws CustomerNotFoundException, UnknownPersistenceException, InputDataValidationException {
        Set<ConstraintViolation<Food>> constraintViolations = validator.validate(newFood);
        Customer customer = customerSessionBeanLocal.retrieveCustomerByCustomerId(customerId);
        
        if (constraintViolations.isEmpty()) {
            try {
                //Association
                customer.addFood(newFood);
                em.persist(newFood);
                em.flush();

                return newFood.getFoodId();
            } catch (PersistenceException ex) {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    @Override
    public Food retrieveFoodByFoodId(Long foodId) throws FoodNotFoundException 
    {
        Food food = em.find(Food.class, foodId);

        if (food != null) {

            return food;
        } else {
            throw new FoodNotFoundException("Food ID " + foodId + " does not exist!");
        }
    }
    
    @Override
    public void deleteFoodByFoodId(Long foodId, Long customerId) throws FoodNotFoundException, DeleteFoodException, CustomerNotFoundException
    {
        Food foodToDelete = retrieveFoodByFoodId(foodId);
        Customer customer = customerSessionBeanLocal.retrieveCustomerByCustomerId(customerId);
        
        //Disassociation
        customer.removeFood(foodToDelete);
        
        em.remove(foodToDelete);
    }
    

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Food>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
}
