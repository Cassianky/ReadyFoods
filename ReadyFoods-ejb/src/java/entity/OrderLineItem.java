/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

/**
 *
 * @author PYT
 */
@Entity
public class OrderLineItem implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderLineItemId;
    
    @Column(nullable = false, precision = 11, scale = 2)
    @NotNull
    @DecimalMin("0.00")
    @Digits(integer = 9, fraction = 2)
    private BigDecimal recipeSubTotal;
    
    @OneToMany
    private List<CustomisedIngredient> customisedIngredients;
    
    @ManyToOne(optional = true)
    private Recipe recipe;

    public Long getOrderLineItemId() {
        return orderLineItemId;
    }

    public void setOrderLineItemId(Long orderLineItemId) {
        this.orderLineItemId = orderLineItemId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (orderLineItemId != null ? orderLineItemId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the orderLineItemId fields are not set
        if (!(object instanceof OrderLineItem)) {
            return false;
        }
        OrderLineItem other = (OrderLineItem) object;
        if ((this.orderLineItemId == null && other.orderLineItemId != null) || (this.orderLineItemId != null && !this.orderLineItemId.equals(other.orderLineItemId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.OrderLineItem[ id=" + orderLineItemId + " ]";
    }

    /**
     * @return the recipeSubTotal
     */
    public BigDecimal getRecipeSubTotal() {
        return recipeSubTotal;
    }

    /**
     * @param recipeSubTotal the recipeSubTotal to set
     */
    public void setRecipeSubTotal(BigDecimal recipeSubTotal) {
        this.recipeSubTotal = recipeSubTotal;
    }

    /**
     * @return the customisedIngredients
     */
    public List<CustomisedIngredient> getCustomisedIngredients() {
        return customisedIngredients;
    }

    /**
     * @param customisedIngredients the customisedIngredients to set
     */
    public void setCustomisedIngredients(List<CustomisedIngredient> customisedIngredients) {
        this.customisedIngredients = customisedIngredients;
    }
    
}