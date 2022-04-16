package ws.datamodel;

import entity.Recipe;
import java.util.List;

public class CreateRecipeReq {

    private String username;
    private String password;
    private Recipe recipe;
    private Long recipeId;
    private List<Long> categoryIds;
    private List<Long> ingredientSpecificationIds;

    public CreateRecipeReq() {
    }

    public CreateRecipeReq(String username, String password, Recipe recipe, Long recipeId, List<Long> categoryIds, List<Long> ingredientSpecificationIds) {
        this.username = username;
        this.password = password;
        this.recipe = recipe;
        this.recipeId = recipeId;
        this.categoryIds = categoryIds;
        this.ingredientSpecificationIds = ingredientSpecificationIds;
    }
    
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public Long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
    }

    public List<Long> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<Long> categoryIds) {
        this.categoryIds = categoryIds;
    }

    public List<Long> getIngredientSpecificationIds() {
        return ingredientSpecificationIds;
    }

    public void setIngredientSpecificationIds(List<Long> ingredientSpecificationIds) {
        this.ingredientSpecificationIds = ingredientSpecificationIds;
    }

}
