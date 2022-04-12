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

    private List<Ingredient> ingredients;
    private List<Long> ingredientIds;
    private List<SelectItem> selectItems;
    private List<Recipe> recipes;
    private String condition;

    public FilterRecipeByIngredientManagedBean() {
        setCondition("NO");
    }

    @PostConstruct
    public void postConstruct() {
        System.err.print("post constructing==================================");
        System.err.print(condition + " condition==================================");
        setIngredients(ingredientSessionBeanLocal.retrieveAllIngredients());

        setSelectItems(new ArrayList<>());

        for (Ingredient i : ingredients) {
            selectItems.add(new SelectItem(i.getIngredientId(), i.getName(), i.getDescription()));
        }

        System.err.print("ingredients quantity:" + ingredients.size() + "==================================");

        setCondition((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("conditionIngredients"));
        setIngredientIds((List<Long>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("selectedIngredients"));

        filterRecipe();
        System.err.print("finish post constructing==================================");

    }

    public void filterRecipe() {
        if (ingredientIds != null && ingredientIds.size() > 0) {
            recipes = recipeSessionBeanLocal.searchRecipesByIngredients(ingredientIds, condition);
            System.err.print("retrieve filtered recipes==================================");
        } else {
            recipes = recipeSessionBeanLocal.retrieveAllRecipes();
            System.err.print("retrieve all recipes==================================");
        }
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Long> getIngredientIds() {
        return ingredientIds;
    }

    public void setIngredientIds(List<Long> ingredientIds) {
        this.ingredientIds = ingredientIds;
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
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("selectedIngredients", this.selectItems);
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }

}
