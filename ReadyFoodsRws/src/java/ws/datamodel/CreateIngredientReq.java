
package ws.datamodel;

import entity.Ingredient;

public class CreateIngredientReq {
    
    private String username;
    private String password;
    private Ingredient ingredient;
    private Long ingredientId;

    public CreateIngredientReq() {
    }

    public CreateIngredientReq(String username, String password, Ingredient ingredient, Long ingredientId) {
        this.username = username;
        this.password = password;
        this.ingredient = ingredient;
        this.ingredientId = ingredientId;
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

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public Long getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(Long ingredientId) {
        this.ingredientId = ingredientId;
    }
    
}
