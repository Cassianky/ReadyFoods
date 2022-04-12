/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.CustomerSessionBeanLocal;
import ejb.session.stateless.OrderEntitySessionBeanLocal;
import ejb.session.stateless.RecipeSessionBeanLocal;
import entity.CommentEntity;
import entity.Customer;
import entity.OrderEntity;
import entity.OrderLineItem;
import entity.Recipe;
import entity.Review;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.xml.stream.events.Comment;
import util.exception.CustomerNotFoundException;
import util.exception.OrderNotFoundException;
import util.exception.RecipeNotFoundException;

/**
 *
 * @author Eugene Chua
 */
@Named(value = "recipeViewManagedBean")
@ViewScoped
public class RecipeViewManagedBean implements Serializable {

    @EJB(name = "OrderEntitySessionBeanLocal")
    private OrderEntitySessionBeanLocal orderEntitySessionBeanLocal;

    @EJB(name = "CustomerSessionBeanLocal")
    private CustomerSessionBeanLocal customerSessionBeanLocal;

    @EJB(name = "RecipeSessionBeanLocal")
    private RecipeSessionBeanLocal recipeSessionBeanLocal;
    
    

    private List<Review> reviews;
    private List<CommentEntity> comments;

    private Recipe recipe;
    private String formattedRecipeSteps;
    private Long recipeId;
    private Boolean isBookmarked;

    private Boolean customerHasBoughtRecipe;

    public RecipeViewManagedBean() {
        recipeId = (Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("recipeToView");
        customerHasBoughtRecipe = false;
    }

    @PostConstruct
    public void postConstruct() {
        try {
            recipe = recipeSessionBeanLocal.retrieveRecipeByRecipeId(getRecipeId());
            comments = recipe.getComments();
            reviews = recipe.getReviews();
            Customer currentCustomer = (Customer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentCustomer");
            Customer customerRetrieved = customerSessionBeanLocal.retrieveCustomerByCustomerId(currentCustomer.getCustomerId());
            for (Recipe bookmarkedRecipe : customerRetrieved.getBookedmarkedRecipes()) {
                if (bookmarkedRecipe.getRecipeId() == recipe.getRecipeId()) {
                    setIsBookmarked(true);
                }
            }
        } catch (RecipeNotFoundException | CustomerNotFoundException ex) {
            ex.printStackTrace();
        }
        System.out.println("Test recipe: " + recipe.getRecipeTitle());
        System.out.println(recipe.getRecipeSteps());
        System.out.println(formattedRecipeSteps);
    }

    public void addReview() {

    }

    public Recipe getRecipe() {
        System.out.println("Recipe: " + recipe.getRecipeTitle());
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public String getFormattedRecipeSteps() {
        return formattedRecipeSteps;
    }

    public void setFormattedRecipeSteps(String formattedRecipeSteps) {
        this.formattedRecipeSteps = formattedRecipeSteps;
    }

    /**
     * @return the recipeId
     */
    public Long getRecipeId() {
        return recipeId;
    }

    /**
     * @param recipeId the recipeId to set
     */
    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
    }

    /**
     * @return the isBookmarked
     */
    public Boolean getIsBookmarked() {
        return isBookmarked;
    }

    /**
     * @param isBookmarked the isBookmarked to set
     */
    public void setIsBookmarked(Boolean isBookmarked) {
        this.isBookmarked = isBookmarked;
    }

    /**
     * @return the reviews
     */
    public List<Review> getReviews() {
        return reviews;
    }

    /**
     * @param reviews the reviews to set
     */
    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    /**
     * @return the comments
     */
    public List<CommentEntity> getComments() {
        return recipeSessionBeanLocal.getAllComments(recipe);
    }

    /**
     * @param comments the comments to set
     */
    public void setComments(List<CommentEntity> comments) {
        this.comments = comments;
    }

    /**
     * @return the customerHasBoughtRecipe
     */
    public Boolean getCustomerHasBoughtRecipe() {
        Customer currentCustomer = (Customer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentCustomer");
        try {
            Customer customerRetrieved = customerSessionBeanLocal.retrieveCustomerByCustomerId(currentCustomer.getCustomerId());
            for(OrderEntity order:customerRetrieved.getOrders()){
                for(OrderLineItem orderLineItem:order.getOrderLineItems()){
                    if(orderLineItem.getRecipe().getRecipeId() == recipe.getRecipeId()){
                        setCustomerHasBoughtRecipe(true);
                    }
                }
            }
            
        } catch (CustomerNotFoundException ex) {
            Logger.getLogger(RecipeViewManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return customerHasBoughtRecipe;
    }

    /**
     * @param customerHasBoughtRecipe the customerHasBoughtRecipe to set
     */
    public void setCustomerHasBoughtRecipe(Boolean customerHasBoughtRecipe) {
        this.customerHasBoughtRecipe = customerHasBoughtRecipe;
    }

}
