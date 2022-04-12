package ejb.session.stateless;

import entity.Customer;
import entity.Recipe;
import entity.Review;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.RecipeNotFoundException;
import util.exception.ReviewNotFoundException;
import util.exception.UnknownPersistenceException;

@Stateless
public class ReviewSessionBean implements ReviewSessionBeanLocal {

    @EJB
    private RecipeSessionBeanLocal recipeSessionBeanLocal;

    @EJB
    private CustomerSessionBeanLocal customerSessionBeanLocal;
    
    @PersistenceContext(unitName = "ReadyFoods-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public ReviewSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Review>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

    @Override
    public Long createNewReview(Review newReview, Long customerId, Long recipeId) throws CustomerNotFoundException, UnknownPersistenceException, InputDataValidationException {
        Set<ConstraintViolation<Review>> constraintViolations = validator.validate(newReview);
        Customer customer = customerSessionBeanLocal.retrieveCustomerByCustomerId(customerId);
        
        if (constraintViolations.isEmpty()) {
            try {
                Recipe recipe = recipeSessionBeanLocal.retrieveRecipeByRecipeId(recipeId);
                
                newReview.setCustomer(customer);
                    
                em.persist(newReview);
                recipe.getReviews().add(newReview);
                em.flush();

                return newReview.getReviewId();
            } catch (PersistenceException ex) {
                throw new UnknownPersistenceException(ex.getMessage());
            } catch (RecipeNotFoundException ex) {
                Logger.getLogger(ReviewSessionBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
        return null;
    }

    @Override
    public List<Review> retrieveAllReviews() {
        Query query = em.createQuery("SELECT r FROM Review r");

        return query.getResultList();
    }

    @Override
    public List<Review> retrieveReviewsByCustomerId(Long customerId) throws CustomerNotFoundException {

        Query query = em.createQuery("SELECT r FROM Review r WHERE r.customer.customerId =:inCustomerId");
        query.setParameter("inCustomerId", customerId);

        return query.getResultList();

    }
    
    @Override
    public Review retrieveReviewByReviewId(Long reviewId) throws ReviewNotFoundException {
        Review review = em.find(Review.class, reviewId);

        if (review != null) {
            review.getCustomer();

            return review;
        } else {
            throw new ReviewNotFoundException("Review ID " + reviewId + " does not exist!");
        }
    }
}
