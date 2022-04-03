package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import util.enumeration.IngredientUnit;

@Entity
public class Ingredient implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ingredientId;
    @Column(nullable = false, length = 64)
    @NotNull
    @Size(max = 64)
    private String name;
    @Column(length = 128)
    @Size(max = 128)
    private String description;
    @Column(nullable = false)
    @NotNull
    IngredientUnit ingredientUnit; //ml, pcs, etc.
    @NotNull
    @DecimalMin("0.00")
    @Digits(integer = 9, fraction = 2) // 11 - 2 digits to the left of the decimal point
    private BigDecimal unitPrice; //1 thing, 10gram, 10ml.
    @Column(nullable = false)
    @NotNull
    @Min(0)
    private Integer reorderQuantity;
    //insufficient stock to fulfil customer's order function
    //implement backend, kiv frontend
    @Column(nullable = false)
    @NotNull
    @Min(0) 
    private Integer stockQuantity;

    
    public Ingredient() {
    }


    public Ingredient(String name, String description, IngredientUnit ingredientUnit, BigDecimal unitPrice, Integer reorderQuantity, Integer stockQuantity) {
        this();
        this.name = name;
        this.description = description;
        this.ingredientUnit = ingredientUnit;
        this.unitPrice = unitPrice;
        this.reorderQuantity = reorderQuantity;
        this.stockQuantity = stockQuantity;
    }
    
    public Long getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(Long ingredientId) {
        this.ingredientId = ingredientId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ingredientId != null ? ingredientId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the ingredientId fields are not set
        if (!(object instanceof Ingredient)) {
            return false;
        }
        Ingredient other = (Ingredient) object;
        if ((this.ingredientId == null && other.ingredientId != null) || (this.ingredientId != null && !this.ingredientId.equals(other.ingredientId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.IngredientEntity[ id=" + ingredientId + " ]";
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }


    public Integer getStockQuantity() {
        return stockQuantity;
    }


    public IngredientUnit getIngredientUnit() {
        return ingredientUnit;
    }

    public Integer getReorderQuantity() {
        return reorderQuantity;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @param ingredientUnit the ingredientUnit to set
     */
    public void setIngredientUnit(IngredientUnit ingredientUnit) {
        this.ingredientUnit = ingredientUnit;
    }

    /**
     * @param unitPrice the unitPrice to set
     */
    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    /**
     * @param reorderQuantity the reorderQuantity to set
     */
    public void setReorderQuantity(Integer reorderQuantity) {
        this.reorderQuantity = reorderQuantity;
    }

    /**
     * @param stockQuantity the stockQuantity to set
     */
    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    
}
