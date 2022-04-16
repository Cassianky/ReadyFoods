/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.CommentSessionBeanLocal;
import entity.CommentEntity;
import entity.Customer;
import entity.Recipe;
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
import util.exception.CreateCommentException;

/**
 *
 * @author PYT
 */
@Named(value = "commentsManagedBean")
@ViewScoped
public class CommentsManagedBean implements Serializable {

    @EJB(name = "CommentSessionBeanLocal")
    private CommentSessionBeanLocal commentSessionBeanLocal;

    private Date commentDate;

    private String description;

    private String customerName;

    public CommentsManagedBean() {
    }

    public void createNewComment(ActionEvent event) {
        Recipe recipe = (Recipe) event.getComponent().getAttributes().get("recipeToComment");
        Customer customer = (Customer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentCustomer");
        customerName = customer.getUserName();
        Date currentDate = new Date();
        CommentEntity newComment = new CommentEntity(currentDate, description, customerName);
        try {
            Long commentId = commentSessionBeanLocal.createNewCommentForRecipe(recipe.getRecipeId(), newComment);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Comment added succesfully!", "Comment ID: " + commentId));
            commentDate = null;
            description = null;
            customerName = null;
        } catch (CreateCommentException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error occured: " + ex.getMessage(), null));
        }

    }

    /**
     * @return the commentDate
     */
    public Date getCommentDate() {
        return commentDate;
    }

    /**
     * @param commentDate the commentDate to set
     */
    public void setCommentDate(Date commentDate) {
        this.commentDate = commentDate;
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
     * @return the customerName
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * @param customerName the customerName to set
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

}
