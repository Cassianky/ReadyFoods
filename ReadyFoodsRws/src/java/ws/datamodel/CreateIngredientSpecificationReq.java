package ws.datamodel;

import entity.Ingredient;
import entity.IngredientSpecification;

public class CreateIngredientSpecificationReq {

    private String username;
    private String password;
    private IngredientSpecification ingredientSpecification;
    private Ingredient ingredient;

    public CreateIngredientSpecificationReq() {
    }

    public CreateIngredientSpecificationReq(String username, String password, IngredientSpecification ingredientSpecification) {
        this.username = username;
        this.password = password;
        this.ingredientSpecification = ingredientSpecification;
        this.ingredient = ingredientSpecification.getIngredient();
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

    public IngredientSpecification getIngredientSpecification() {
        return ingredientSpecification;
    }

    public void setIngredientSpecification(IngredientSpecification ingredientSpecification) {
        this.ingredientSpecification = ingredientSpecification;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

}
