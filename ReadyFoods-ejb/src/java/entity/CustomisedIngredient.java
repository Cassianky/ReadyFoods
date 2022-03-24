/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

/**
 *
 * @author PYT
 */
@Entity
public class CustomisedIngredient implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customisedIngredientId;
    
    @Column(nullable = false)
    @NotNull
    @Min(1)
    private Integer quantityOfIngredient;
    
    @Column(nullable = false)
    @NotNull
    private Boolean prepared;
    
    @Column(nullable = false)
    @NotNull
    private Long ingredientId;
    
    @Column(nullable = false, unique = true, length = 32)
    @NotNull
    @Size(max = 32)
    private String ingredientName;
    
    @Column(nullable = false, precision = 11, scale = 2)
    @NotNull
    @DecimalMin("0.00")
    @Digits(integer = 9, fraction = 2)
    private BigDecimal unitPrice;
    
    @Column(nullable = false, precision = 11, scale = 2)
    @NotNull
    @DecimalMin("0.00")
    @Digits(integer = 9, fraction = 2)
    private BigDecimal subtotal;
    
    

    public Long getCustomisedIngredientId() {
        return customisedIngredientId;
    }

    public void setCustomisedIngredientId(Long customisedIngredientId) {
        this.customisedIngredientId = customisedIngredientId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (customisedIngredientId != null ? customisedIngredientId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the customisedIngredientId fields are not set
        if (!(object instanceof CustomisedIngredient)) {
            return false;
        }
        CustomisedIngredient other = (CustomisedIngredient) object;
        if ((this.customisedIngredientId == null && other.customisedIngredientId != null) || (this.customisedIngredientId != null && !this.customisedIngredientId.equals(other.customisedIngredientId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CustomisedIngredient[ id=" + customisedIngredientId + " ]";
    }

    /**
     * @return the quantityOfIngredient
     */
    public Integer getQuantityOfIngredient() {
        return quantityOfIngredient;
    }

    /**
     * @param quantityOfIngredient the quantityOfIngredient to set
     */
    public void setQuantityOfIngredient(Integer quantityOfIngredient) {
        this.quantityOfIngredient = quantityOfIngredient;
    }

    /**
     * @return the prepared
     */
    public Boolean getPrepared() {
        return prepared;
    }

    /**
     * @param prepared the prepared to set
     */
    public void setPrepared(Boolean prepared) {
        this.prepared = prepared;
    }

    /**
     * @return the ingredientId
     */
    public Long getIngredientId() {
        return ingredientId;
    }

    /**
     * @param ingredientId the ingredientId to set
     */
    public void setIngredientId(Long ingredientId) {
        this.ingredientId = ingredientId;
    }

    /**
     * @return the ingredientName
     */
    public String getIngredientName() {
        return ingredientName;
    }

    /**
     * @param ingredientName the ingredientName to set
     */
    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    /**
     * @return the unitPrice
     */
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    /**
     * @param unitPrice the unitPrice to set
     */
    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    /**
     * @return the subtotal
     */
    public BigDecimal getSubtotal() {
        return subtotal;
    }

    /**
     * @param subtotal the subtotal to set
     */
    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
    
}
