package jsf.managedbean;

import ejb.session.stateless.IngredientSessionBeanLocal;
import ejb.session.stateless.RecipeSessionBeanLocal;
import entity.Ingredient;
import entity.Recipe;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

@Named(value = "filterRecipeByIngredientManagedBean")
@ViewScoped
public class FilterRecipeByIngredientManagedBean implements Serializable {

    @EJB
    RecipeSessionBeanLocal recipeSessionBeanLocal;
    @EJB
    IngredientSessionBeanLocal ingredientSessionBeanLocal;

    private List<Long> selectedIngredientIds;
    private List<SelectItem> selectItems;
    private List<Recipe> recipes;
    private String condition;

    public FilterRecipeByIngredientManagedBean() {
        setCondition("NO");
    }

    @PostConstruct
    public void postConstruct() {
        List <Ingredient> ingredients = ingredientSessionBeanLocal.retrieveAllIngredients();

        setSelectItems(new ArrayList<>());

        for (Ingredient i : ingredients) {
            selectItems.add(new SelectItem(i.getIngredientId(), i.getName(), i.getDescription()));
        }

        setCondition((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("conditionIngredients"));
        setSelectedIngredientIds((List<Long>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("selectedIngredients"));

        filterRecipe();
    }
    
    public void filterRecipe() {
        if (selectedIngredientIds != null && selectedIngredientIds.size() > 0) {
            recipes = recipeSessionBeanLocal.searchRecipesByIngredients(selectedIngredientIds, condition);
            System.err.print("retrieve filtered recipes==================================");
        } else {
            recipes = recipeSessionBeanLocal.retrieveAllRecipes();
            System.err.print("retrieve all recipes==================================");
        }
    }

    public List<Long> getSelectedIngredientIds() {
        return selectedIngredientIds;
    }

    public void setSelectedIngredientIds(List<Long> selectedIngredientIds) {
        this.selectedIngredientIds = selectedIngredientIds;
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("selectedIngredients", selectedIngredientIds);
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("conditionIngredients", condition);
    }

    public List<SelectItem> getSelectItems() {
        return selectItems;
    }

    public void setSelectItems(List<SelectItem> selectItems) {
        this.selectItems = selectItems;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }

}
