/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CommentEntity;
import javax.ejb.Local;
import util.exception.CreateCommentException;

/**
 *
 * @author PYT
 */
@Local
public interface CommentSessionBeanLocal {

   public Long createNewCommentForRecipe(Long recipeId, CommentEntity newComment) throws CreateCommentException;

    public void deleteComment(Long commentId, Long recipeId);


    
}
