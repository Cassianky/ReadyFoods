/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Category;
import java.util.List;
import javax.ejb.Local;
import util.exception.CategoryNotFoundException;
import util.exception.CreateCategoryException;
import util.exception.InputDataValidationException;

/**
 *
 * @author Eugene Chua
 */
@Local
public interface CategorySessionBeanLocal {

    public Category createNewCategory(Category newCategory, Long parentCategoryId) throws CreateCategoryException, InputDataValidationException;

    public Category retrieveCategoryByCategoryId(Long categoryId) throws CategoryNotFoundException;

    public List<Category> retrieveSubCategoriesByParentId(Long parentId) throws CategoryNotFoundException;

    public List<Category> retrieveAllParentCategories() throws CategoryNotFoundException;

    public List<Category> searchSubCategoriesByName(String searchString);

    public List<Category> retrieveAllSubCategories();

}
