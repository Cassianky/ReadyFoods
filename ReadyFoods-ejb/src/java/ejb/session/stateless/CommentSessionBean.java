/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CommentEntity;
import entity.Recipe;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import util.exception.CreateCommentException;

/**
 *
 * @author PYT
 */
@Stateless
public class CommentSessionBean implements CommentSessionBeanLocal {

    @EJB(name = "RecipeSessionBeanLocal")
    private RecipeSessionBeanLocal recipeSessionBeanLocal;

    @PersistenceContext(unitName = "ReadyFoods-ejbPU")
    private EntityManager em;

    public CommentSessionBean() {
    }
    
    @Override
    public Long createNewCommentForRecipe(Long recipeId, CommentEntity newComment) throws CreateCommentException {
        try{
        Recipe recipeToUpdate = em.find(Recipe.class,recipeId);
        em.persist(newComment);
        recipeToUpdate.getComments().add(newComment);
        em.flush();
        }catch(PersistenceException ex){
            throw new CreateCommentException("Error occurred while adding new comment");
        }
        return newComment.getCommentEntityId();
    }

    
}
