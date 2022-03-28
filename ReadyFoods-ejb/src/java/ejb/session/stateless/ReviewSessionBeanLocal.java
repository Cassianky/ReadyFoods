package ejb.session.stateless;

import entity.Review;
import java.util.List;
import javax.ejb.Local;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.ReviewNotFoundException;
import util.exception.UnknownPersistenceException;

@Local
public interface ReviewSessionBeanLocal {

    public List<Review> retrieveAllReviews();

    public List<Review> retrieveReviewsByCustomerId(Long customerId) throws CustomerNotFoundException;

    public Review retrieveReviewByReviewId(Long reviewId) throws ReviewNotFoundException;

    public Long createNewReview(Review newReview, Long customerId) throws CustomerNotFoundException, UnknownPersistenceException, InputDataValidationException;
    
}
