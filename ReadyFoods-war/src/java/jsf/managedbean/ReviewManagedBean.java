/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.CustomerSessionBeanLocal;
import ejb.session.stateless.ReviewSessionBeanLocal;
import entity.Customer;
import entity.Recipe;
import entity.Review;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.persistence.Column;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author PYT
 */
@Named(value = "reviewManagedBean")
@ViewScoped
public class ReviewManagedBean implements Serializable {

    @EJB(name = "ReviewSessionBeanLocal")
    private ReviewSessionBeanLocal reviewSessionBeanLocal;
    
    

    @Column(nullable = false, length = 64)
    @NotNull(message = "Title is required!")
    @Size(max = 64)
    private String title;
    
    @Column(nullable = false, length = 320)
    @NotNull(message = "Description is required!")
    @Size(max = 320)
    private String description;
    
    @Column(nullable = false)
    @NotNull(message = "Rating is required!")
    @Positive
    @Min(1)
    @Max(5)
    private Integer rating;
    
    @Inject
    private RecipeViewManagedBean recipeViewManagedBean;
    
    public ReviewManagedBean() {
    }
    
    public void createNewReview(ActionEvent event){
        Customer customer = (Customer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentCustomer");
        Recipe recipe = (Recipe)event.getComponent().getAttributes().get("recipeToReview");
        Date currentDate = new Date();
        Review newReview = new Review(currentDate, getTitle(), getDescription(), getRating());
        try {
            Long reviewId = reviewSessionBeanLocal.createNewReview(newReview, customer.getCustomerId(),recipe.getRecipeId());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Review added succesfully!", "Review ID: " + reviewId));
            setTitle(null);
            setDescription(null);
            recipeViewManagedBean.updateRating(getRating());
            setRating(null);
            
                    
        } catch (CustomerNotFoundException ex) {
            Logger.getLogger(ReviewManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnknownPersistenceException ex) {
            Logger.getLogger(ReviewManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InputDataValidationException ex) {
            Logger.getLogger(ReviewManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the rating
     */
    public Integer getRating() {
        return rating;
    }

    /**
     * @param rating the rating to set
     */
    public void setRating(Integer rating) {
        this.rating = rating;
    }
    
}
