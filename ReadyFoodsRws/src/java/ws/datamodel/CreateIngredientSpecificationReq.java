package ws.datamodel;

import entity.IngredientSpecification;

public class CreateIngredientSpecificationReq {

    private String username;
    private String password;
    private IngredientSpecification ingredientSpecification;
    private Long ingredientId;

    public CreateIngredientSpecificationReq() {
    }

    public CreateIngredientSpecificationReq(String username, String password, IngredientSpecification ingredientSpecification, Long ingredientSpecificationId) {
        this.username = username;
        this.password = password;
        this.ingredientSpecification = ingredientSpecification;
        this.ingredientId = ingredientSpecificationId;
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

    public Long getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(Long ingredientId) {
        this.ingredientId = ingredientId;
    }

}
