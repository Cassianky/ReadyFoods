/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CreditCard;
import entity.Customer;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CreditCardNotFoundException;
import util.exception.CustomerNotFoundException;
import util.exception.DeleteCreditCardException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;


@Stateless
public class CreditCardSessionBean implements CreditCardSessionBeanLocal {

    @EJB(name = "CustomerSessionBeanLocal")
    private CustomerSessionBeanLocal customerSessionBeanLocal;

     @PersistenceContext(unitName = "ReadyFoods-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public CreditCardSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Long createNewCreditCard(CreditCard newCreditCard, Long customerId) throws UnknownPersistenceException, InputDataValidationException, CustomerNotFoundException {
        Set<ConstraintViolation<CreditCard>> constraintViolations = validator.validate(newCreditCard);
        if (constraintViolations.isEmpty()) {
            try {
                //Association
                Customer customer = customerSessionBeanLocal.retrieveCustomerByCustomerId(customerId);
                customer.setCreditCard(newCreditCard);
                
                em.persist(newCreditCard);
                em.flush();

                return newCreditCard.getCreditCardId();
            } catch (PersistenceException ex) {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
  
    @Override
    public void deleteCreditCardByCreditCardId(Long creditCardId, Long customerId) throws CreditCardNotFoundException, DeleteCreditCardException, CustomerNotFoundException
    {
        CreditCard creditCardToDelete = retrieveCreditCardByCreditCardId(creditCardId);
        Customer customer = customerSessionBeanLocal.retrieveCustomerByCustomerId(customerId);
        
        //Disassociation
        customer.setCreditCard(null);
        
        em.remove(creditCardToDelete);
    }
    
    @Override
    public CreditCard retrieveCreditCardByCreditCardId(Long creditCardId) throws CreditCardNotFoundException 
    {
        CreditCard creditCard = em.find(CreditCard.class, creditCardId);

        if (creditCardId != null) {

            return creditCard;
        } else {
            throw new CreditCardNotFoundException("Credit Card ID " + creditCardId + " does not exist!");
        }
    }
    
    @Override
    public CreditCard retrieveCreditCardByCustomerId(Long customerId){
        Customer customer = em.find(Customer.class, customerId);
        
        return customer.getCreditCard();
    }
    

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<CreditCard>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
}
