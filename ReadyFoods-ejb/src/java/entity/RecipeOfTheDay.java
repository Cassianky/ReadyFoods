/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

/**
 *
 * @author ngcas
 */
@Entity
public class RecipeOfTheDay implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recipeOfTheDayId;
    
    @NotNull
    @Column(nullable = false)
    private Long recipeId;

    public RecipeOfTheDay() {
    }

    public Long getRecipeOfTheDayId() {
        return recipeOfTheDayId;
    }

    public void setRecipeOfTheDayId(Long recipeOfTheDayId) {
        this.recipeOfTheDayId = recipeOfTheDayId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (recipeOfTheDayId != null ? recipeOfTheDayId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the recipeOfTheDayId fields are not set
        if (!(object instanceof RecipeOfTheDay)) {
            return false;
        }
        RecipeOfTheDay other = (RecipeOfTheDay) object;
        if ((this.recipeOfTheDayId == null && other.recipeOfTheDayId != null) || (this.recipeOfTheDayId != null && !this.recipeOfTheDayId.equals(other.recipeOfTheDayId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RecipeOfTheDay[ id=" + recipeOfTheDayId + " ]";
    }

    /**
     * @return the recipeId
     */
    public Long getRecipeId() {
        return recipeId;
    }

    /**
     * @param recipeId the recipeId to set
     */
    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
    }
    
}
