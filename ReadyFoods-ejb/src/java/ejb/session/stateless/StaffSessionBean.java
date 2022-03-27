/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Staff;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.InputDataValidationException;
import util.exception.StaffUsernameExistException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author angler
 */
@Stateless
public class StaffSessionBean implements StaffSessionBeanLocal {

    @PersistenceContext(unitName = "ReadyFoods-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public StaffSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Long createNewStaff(Staff newStaff) throws StaffUsernameExistException,
            UnknownPersistenceException, InputDataValidationException {
        Set<ConstraintViolation<Staff>> constraintViolations = validator.validate(newStaff);

        if (constraintViolations.isEmpty()) {
            try {
                em.persist(newStaff);
                em.flush();

                return newStaff.getStaffId();
            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new StaffUsernameExistException();
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

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Staff>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - "
                    + constraintViolation.getInvalidValue() + "; "
                    + constraintViolation.getMessage();
        }

        return msg;
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
