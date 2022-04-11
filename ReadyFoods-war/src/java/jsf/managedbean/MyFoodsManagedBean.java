/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.CustomerSessionBeanLocal;
import ejb.session.stateless.FoodSessionBeanLocal;
import ejb.session.stateless.RecipeSessionBeanLocal;
import entity.Customer;
import entity.Food;
import entity.Recipe;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import util.exception.CustomerNotFoundException;
import util.exception.DeleteFoodException;
import util.exception.FoodNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.RecipeNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author ngcas
 */
@Named(value = "myFoodsManagedBean")
@ViewScoped
public class MyFoodsManagedBean implements Serializable {

    @EJB
    private RecipeSessionBeanLocal recipeSessionBeanLocal;

    @EJB
    private CustomerSessionBeanLocal customerSessionBeanLocal;

    @EJB
    private FoodSessionBeanLocal foodSessionBeanLocal;

    private List<Food> foods;
    private Customer currentCustomer;
    private Boolean isNotFromRecipe;
    private List<Recipe> recipes;
    private Recipe selectedRecipe;
    private Food foodToCreate;
    private int numServings;

    public MyFoodsManagedBean() {
        isNotFromRecipe = true;
        foodToCreate = new Food();
    }

    @PostConstruct
    public void postConstruct() {
        try {
            Long currentCustomerId = ((Customer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentCustomer")).getCustomerId();
            currentCustomer = customerSessionBeanLocal.retrieveCustomerByCustomerId(currentCustomerId);
            foods = foodSessionBeanLocal.retrieveAllFoodsByCustomerId(currentCustomerId);
            setRecipes(recipeSessionBeanLocal.retrieveAllRecipes());
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("recipes", recipes);
        } catch (CustomerNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Customer not found" + ex.getMessage(), null));
        }
    }

    public void deleteFood(ActionEvent event) {
        try {
            Food selectedFood = (Food) event.getComponent().getAttributes().get("foodToDelete");
            foods.remove(selectedFood);
            foodSessionBeanLocal.deleteFoodByFoodId(selectedFood.getFoodId(), currentCustomer.getCustomerId());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Food Deleted Successfully!", null));
        } catch (FoodNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Food not found!: " + ex.getMessage(), null));
        } catch (DeleteFoodException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error occurred while deleting food!: " + ex.getMessage(), null));
        } catch (CustomerNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Customer Not Found: " + ex.getMessage(), null));
        }
    }

    public void createANewFood(ActionEvent event) {
        try {
            if (getSelectedRecipe() != null) {
                foodToCreate.setCalories(new Double(getSelectedRecipe().getCaloriesPerServing() * numServings));
                foodToCreate.setCarbs(new Double(getSelectedRecipe().getCarbsPerServing() * numServings));
                foodToCreate.setFats(new Double(getSelectedRecipe().getFatsPerServing() * numServings));
                foodToCreate.setProtein(new Double(getSelectedRecipe().getProteinsPerServing() * numServings));
                foodToCreate.setSugar(new Double(getSelectedRecipe().getSugarPerServing() * numServings));
                foodToCreate.setName(getSelectedRecipe().getRecipeTitle());

                foods.add(foodToCreate);
                foodSessionBeanLocal.createNewFood(foodToCreate, currentCustomer.getCustomerId());
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO,
                                "New Food Created Successfully: ", null));
                foodToCreate = new Food();
            } else {
                foodSessionBeanLocal.createNewFood(foodToCreate, currentCustomer.getCustomerId());
                foods.add(foodToCreate);
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO,
                                "New Food Created Successfully: ", null));
                foodToCreate = new Food();
            }
        } catch (CustomerNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Customer Not Found: " + ex.getMessage(), null));
        } catch (UnknownPersistenceException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unknown error occurred while creating new food: " + ex.getMessage(), null));
        } catch (InputDataValidationException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Inputs for new food are not correct!: " + ex.getMessage(), null));
        }
    }

    /**
     * @return the foods
     */
    public List<Food> getFoods() {
        return foods;
    }

    /**
     * @param foods the foods to set
     */
    public void setFoods(List<Food> foods) {
        this.foods = foods;
    }

    /**
     * @return the currentCustomer
     */
    public Customer getCurrentCustomer() {
        return currentCustomer;
    }

    /**
     * @param currentCustomer the currentCustomer to set
     */
    public void setCurrentCustomer(Customer currentCustomer) {
        this.currentCustomer = currentCustomer;
    }

    /**
     * @return the isNotFromRecipe
     */
    public Boolean getIsNotFromRecipe() {
        return isNotFromRecipe;
    }

    /**
     * @param isNotFromRecipe the isNotFromRecipe to set
     */
    public void setIsNotFromRecipe(Boolean isNotFromRecipe) {
        this.isNotFromRecipe = isNotFromRecipe;
    }

    /**
     * @return the recipes
     */
    public List<Recipe> getRecipes() {
        return recipes;
    }

    /**
     * @param recipes the recipes to set
     */
    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    /**
     * @return the foodToCreate
     */
    public Food getFoodToCreate() {
        return foodToCreate;
    }

    /**
     * @param foodToCreate the foodToCreate to set
     */
    public void setFoodToCreate(Food foodToCreate) {
        this.foodToCreate = foodToCreate;
    }

    /**
     * @return the numServings
     */
    public int getNumServings() {
        return numServings;
    }

    /**
     * @param numServings the numServings to set
     */
    public void setNumServings(int numServings) {
        this.numServings = numServings;
    }

    /**
     * @return the selectedRecipe
     */
    public Recipe getSelectedRecipe() {
        return selectedRecipe;
    }

    /**
     * @param selectedRecipe the selectedRecipe to set
     */
    public void setSelectedRecipe(Recipe selectedRecipe) {
        this.selectedRecipe = selectedRecipe;
    }
}
