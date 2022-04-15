/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import entity.Enquiry;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CreateNewEnquiryException;
import util.exception.CustomerNotFoundException;
import util.exception.EnquiryNotFoundException;
import util.exception.EnquiryResolveException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author angler
 */
@Stateless
public class EnquirySessionBean implements EnquirySessionBeanLocal {

    @EJB(name = "CustomerSessionBeanLocal")
    private CustomerSessionBeanLocal customerSessionBeanLocal;

    @PersistenceContext(unitName = "ReadyFoods-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public EnquirySessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Enquiry createNewEnquiry(Long customerId, Enquiry newEnquiry)
            throws CustomerNotFoundException, CreateNewEnquiryException, InputDataValidationException, UnknownPersistenceException {
        newEnquiry.setDateOfEnquiry(new Date());

        Set<ConstraintViolation<Enquiry>> constraintViolations = validator.validate(newEnquiry);

        if (constraintViolations.isEmpty()) {

            if (newEnquiry != null) {

                Customer customer = customerSessionBeanLocal.retrieveCustomerByCustomerId(customerId);
                newEnquiry.setCustomer(customer);

                customer.getEnquiries().add(newEnquiry);

                em.persist(newEnquiry);

                em.flush();

                return newEnquiry;

            } else {
                throw new CreateNewEnquiryException("Enquiry information not provided");
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    @Override
    public List<Enquiry> retrieveAllEnquires() {
        Query query = em.createQuery("SELECT e FROM Enquiry e");

        return query.getResultList();
    }

    @Override
    public Enquiry retrieveEnquiryByEnquiryId(Long enId) throws EnquiryNotFoundException {
        Enquiry enquiry = em.find(Enquiry.class, enId);

        if (enquiry != null) {
            return enquiry;
        } else {
            throw new EnquiryNotFoundException("Enquiry ID " + enId + " does not exist!");
        }
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Enquiry>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - "
                    + constraintViolation.getInvalidValue() + "; "
                    + constraintViolation.getMessage();
        }

        return msg;
    }

    @Override
    public void deleteEnquiry(Long enquiryId) throws EnquiryNotFoundException {
        Enquiry toDel = retrieveEnquiryByEnquiryId(enquiryId);

        Customer customer = toDel.getCustomer();

        customer.getEnquiries().remove(toDel);
        em.remove(toDel);

    }

    @Override
    public void resolveEnquiry(Long enquiryId) throws EnquiryNotFoundException, EnquiryResolveException {
        Enquiry toResolve = retrieveEnquiryByEnquiryId(enquiryId);
        if (toResolve.getResolved()) {
            throw new EnquiryResolveException("Enquiry is already resolved.");
        }
        toResolve.setResolved(Boolean.TRUE);
    }

    public void updateEnquiry(Long enquiryId, String response, Boolean resolved) throws EnquiryNotFoundException {
        System.out.println("Updating enquiry...." + response + resolved);
        Enquiry toUpdate = retrieveEnquiryByEnquiryId(enquiryId);
        
        
        
        if (response != null) {
            toUpdate.setResponse(response);
        } else if (resolved != null && resolved && !toUpdate.getResolved()) {
            toUpdate.setResolved(resolved);
        } 

    }

}
