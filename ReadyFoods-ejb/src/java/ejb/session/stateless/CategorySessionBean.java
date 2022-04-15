package ejb.session.stateless;

import entity.Category;
import entity.Recipe;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CategoryNotFoundException;
import util.exception.CreateCategoryException;
import util.exception.InputDataValidationException;
import util.exception.RecipeNotFoundException;
import util.exception.UpdateCategoryException;

@Stateless

public class CategorySessionBean implements CategorySessionBeanLocal {

    @EJB(name = "RecipeSessionBeanLocal")
    private RecipeSessionBeanLocal recipeSessionBeanLocal;

    @PersistenceContext(unitName = "ReadyFoods-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    

    public CategorySessionBean() {
        this.validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }
    
    @Override
    public Category createNewCategory(Category newCategory, Long parentCategoryId) throws CreateCategoryException, InputDataValidationException {
        Set<ConstraintViolation<Category>> constraintViolations = validator.validate(newCategory);

        if (constraintViolations.isEmpty()) {
            try {
                if (parentCategoryId != null) {
                    Category parentCategory = retrieveCategoryByCategoryId(parentCategoryId);
                    newCategory.setParentCategory(parentCategory);

                    if (!parentCategory.getRecipes().isEmpty()) {
                        throw new CreateCategoryException("Parent category cannot be associated with any recipe!");
                    }
                }

                em.persist(newCategory);
                em.flush();
                return newCategory;
            } catch (PersistenceException ex) {
                if (ex.getCause() != null
                        && ex.getCause().getCause() != null
                        && ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException")) {
                    throw new CreateCategoryException("Same category already exist!");
                } else {
                    throw new CreateCategoryException("An unexpected error has occurred: " + ex.getMessage());
                }
            } catch (Exception ex) {
                throw new CreateCategoryException("An unexpected error has occurred: " + ex.getMessage());
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    @Override
    public void updateCategory(Category category) throws UpdateCategoryException, CategoryNotFoundException, InputDataValidationException
    {
        if(category != null && category.getCategoryId() != null && (category.getParentCategory().getName()).equals("Diet Type") ==  false && (category.getName()).equals("Diet Type") == false)
        {
            Set<ConstraintViolation<Category>>constraintViolations = validator.validate(category);
            if(constraintViolations.isEmpty())
            {
                Category categoryToUpdate = retrieveCategoryByCategoryId(category.getCategoryId());
                
                if(categoryToUpdate.getCategoryId().equals(category.getCategoryId()))
                {
                    categoryToUpdate.setName(category.getName());
                    categoryToUpdate.setDescription(categoryToUpdate.getDescription());
                } else {
                    throw new UpdateCategoryException("An error occur during updating category of ID" + categoryToUpdate.getCategoryId());
                }
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } else {
            throw new CategoryNotFoundException("Category ID is not found!");
        }
    }

    @Override
    public Category retrieveCategoryByCategoryId(Long categoryId) throws CategoryNotFoundException {

        Category retrievedCategory = em.find(Category.class, categoryId);

        if (retrievedCategory != null) {
            if (retrievedCategory.getParentCategory() == null) {
                retrievedCategory.getSubCategories().size(); //parent category will have no recipe
            } else {
                retrievedCategory.getParentCategory();
                retrievedCategory.getRecipes().size();
            }
            return retrievedCategory;
        } else {
            throw new CategoryNotFoundException("Category ID " + categoryId + " does not exists!");
        }
    }

    @Override
    public List<Category> retrieveAllParentCategories() throws CategoryNotFoundException {
        List<Category> parentCategories = new ArrayList<>();

        Query query = em.createQuery("SELECT c FROM Category c WHERE c.parentCategory IS NULL");
        try {
            parentCategories = query.getResultList();
            parentCategories.size();
            parentCategories.forEach(result -> result.getSubCategories().size());
            parentCategories.forEach(pc -> pc.getSubCategories().forEach(sc -> sc.getRecipes().size()));
            //parent category will have no recipe(s)
            return parentCategories;
        } catch (NoResultException ex) {
            throw new CategoryNotFoundException("No categories are available at the moment!");
        }
    }

    @Override
    public List<Category> retrieveAllSubCategories() {
        Query query = em.createQuery("SELECT c FROM Category c WHERE c.parentCategory IS NOT NULL");

        List<Category> subCategories = query.getResultList();
        subCategories.size();
        subCategories.forEach(result -> result.getRecipes());
        return subCategories;
    }

    @Override
    public List<Category> retrieveSubCategoriesByParentId(Long parentId) throws CategoryNotFoundException {
        Query query = em.createQuery("SELECT c FROM Category c WHERE c.parentCategory.categoryId =:inCategoryID ");
        query.setParameter("inCategoryId", parentId);

        try {
            query.getSingleResult();
            List<Category> subCategories = query.getResultList();
            subCategories.size();
            subCategories.forEach(result -> result.getRecipes().size());
            return subCategories;
        } catch (NoResultException ex) {
            throw new CategoryNotFoundException("Please select a parent category!");
        }
    }
    
    @Override
    public Category retrieveRecipeDietType(Long recipeId) throws CategoryNotFoundException, RecipeNotFoundException {
        Recipe recipe = recipeSessionBeanLocal.retrieveRecipeByRecipeId(recipeId);
        List<Category> cats = recipe.getCategories();
        
        for (Category cat : cats) {
            //System.out.println(cat);
            if (cat.getParentCategory() != null && cat.getParentCategory().getName().equals("Diet Type")) {
                return cat;
            }
        }
        
        throw new CategoryNotFoundException("Recipe has no diet type.");
        
    }

    @Override
    public List<Category> searchSubCategoriesByName(String searchString) {
        Query query = em.createQuery("SELECT c FROM Category c WHERE c.parentCategory IS NOT NULL AND c.name LIKE :inSearchString ORDER BY c.name ASC");
        query.setParameter("inSearchString", "%" + searchString + "%");

        List<Category> subCategories = query.getResultList();
        subCategories.size();
        subCategories.forEach(result -> result.getRecipes().size());

        return subCategories;
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Category>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
}
