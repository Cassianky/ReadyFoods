/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Ingredient;
import entity.IngredientSpecification;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.IngredientExistsException;
import util.exception.IngredientNotFoundException;
import util.exception.IngredientSpecificationNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author PYT
 */
@Stateless
public class IngredientSpecificationSessionBean implements IngredientSpecificaitonSessionBeanLocal {

    @EJB(name = "IngredientSessionBeanLocal")
    private IngredientSessionBeanLocal ingredientSessionBeanLocal;

    @PersistenceContext(unitName = "ReadyFoods-ejbPU")
    private EntityManager entityManager;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public IngredientSpecificationSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Long createNewIngredientSpecification(IngredientSpecification newIngredientSpecifcation, Long ingredientId) throws UnknownPersistenceException, InputDataValidationException, IngredientNotFoundException {

        try {
            Ingredient ingredientRetrieved = ingredientSessionBeanLocal.retrieveIngredientByIngredientId(ingredientId);
            newIngredientSpecifcation.setIngredient(ingredientRetrieved);
            entityManager.persist(newIngredientSpecifcation);
            entityManager.flush();
            System.out.println("********ejb.session.stateless.IngredientSessionBean.createNewIngredientSpecification()");

            return newIngredientSpecifcation.getIngredientSpecificationId();
        } catch (PersistenceException ex) {
            throw new UnknownPersistenceException(ex.getMessage());

        }
    }

    @Override
    public IngredientSpecification retrieveIngredientSpecificationById(Long id) throws IngredientSpecificationNotFoundException {

        IngredientSpecification is = entityManager.find(IngredientSpecification.class, id);

        if (is == null) {
            throw new IngredientSpecificationNotFoundException();
        }

        return is;

    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<IngredientSpecification>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

}
