package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
public class IngredientSpecifcationEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ingredientSpecificationId;
    @Column(nullable = false)
    @NotNull
    @Min(1)
    private Integer quantity;
    @Column(nullable = false)
    @NotNull
    private IngredientEntity ingredient;
    
    @ManyToOne(optional = false)
    private RecipeEntity recipe;

    
    public IngredientSpecifcationEntity(Integer quantity, IngredientEntity ingredient) {
        this.quantity = quantity;
        this.ingredient = ingredient;
    }
    
    
    public Long getIngredientSpecificationId() {
        return ingredientSpecificationId;
    }

    public void setIngredientSpecificationId(Long ingredientSpecificationId) {
        this.ingredientSpecificationId = ingredientSpecificationId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ingredientSpecificationId != null ? ingredientSpecificationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the ingredientSpecificationId fields are not set
        if (!(object instanceof IngredientSpecifcationEntity)) {
            return false;
        }
        IngredientSpecifcationEntity other = (IngredientSpecifcationEntity) object;
        if ((this.ingredientSpecificationId == null && other.ingredientSpecificationId != null) || (this.ingredientSpecificationId != null && !this.ingredientSpecificationId.equals(other.ingredientSpecificationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.IngredientSpecifcationEntity[ id=" + ingredientSpecificationId + " ]";
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.setQuantity(quantity);
    }

    public IngredientEntity getIngredient() {
        return ingredient;
    }

    public void setIngredient(IngredientEntity ingredient) {
        this.ingredient = ingredient;
    }

    public RecipeEntity getRecipe() {
        return recipe;
    }

    public void setRecipe(RecipeEntity recipe) {
        this.recipe = recipe;
    }
    
}
