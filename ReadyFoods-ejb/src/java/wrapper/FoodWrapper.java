/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wrapper;

import java.util.Objects;

/**
 *
 * @author ngcas
 */
public class FoodWrapper {
    private Long foodDiaryRecordId;
    private String name;
    private Double calories;
    private Double carbs;
    private Double protein;
    private Double fats;
    private Double sugar;

    public FoodWrapper() {
        calories = 0.0;
        carbs = 0.0;
        protein = 0.0;
        fats = 0.0;
        sugar = 0.0;
    }

    public FoodWrapper(String name, Double calories, Double carbs, Double protein, Double fats, Double sugar) {
        this.name = name;
        this.calories = calories;
        this.carbs = carbs;
        this.protein = protein;
        this.fats = fats;
        this.sugar = sugar;
    }

    public FoodWrapper(Long foodDiaryRecordId, String name, Double calories, Double carbs, Double protein, Double fats, Double sugar) {
        this.foodDiaryRecordId = foodDiaryRecordId;
        this.name = name;
        this.calories = calories;
        this.carbs = carbs;
        this.protein = protein;
        this.fats = fats;
        this.sugar = sugar;
    }

    public FoodWrapper(Double calories, Double carbs, Double protein, Double fats, Double sugar) {
        this.calories = calories;
        this.carbs = carbs;
        this.protein = protein;
        this.fats = fats;
        this.sugar = sugar;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FoodWrapper other = (FoodWrapper) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.calories, other.calories)) {
            return false;
        }
        if (!Objects.equals(this.carbs, other.carbs)) {
            return false;
        }
        if (!Objects.equals(this.protein, other.protein)) {
            return false;
        }
        if (!Objects.equals(this.fats, other.fats)) {
            return false;
        }
        if (!Objects.equals(this.sugar, other.sugar)) {
            return false;
        }
        return true;
    }

    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the calories
     */
    public Double getCalories() {
        return calories;
    }

    /**
     * @param calories the calories to set
     */
    public void setCalories(Double calories) {
        this.calories = calories;
    }
    
    public void addCalories(Double newCalories) {
        this.calories = this.calories + newCalories;
    }

    /**
     * @return the carbs
     */
    public Double getCarbs() {
        return carbs;
    }

    /**
     * @param carbs the carbs to set
     */
    public void setCarbs(Double carbs) {
        this.carbs = carbs;
    }
    
    public void addCarbs(Double newCarbs) {
        this.carbs = this.carbs + newCarbs;
    }

    /**
     * @return the protein
     */
    public Double getProtein() {
        return protein;
    }

    /**
     * @param protein the protein to set
     */
    public void setProtein(Double protein) {
        this.protein = protein;
    }

    public void addProtein(Double newProtein) {
        this.protein = this.protein + newProtein;
    }
    /**
     * @return the fats
     */
    public Double getFats() {
        return fats;
    }

    /**
     * @param fats the fats to set
     */
    public void setFats(Double fats) {
        this.fats = fats;
    }
    
    public void addFats(Double newFats) {
        this.fats = this.fats + newFats;
    }

    /**
     * @return the sugar
     */
    public Double getSugar() {
        return sugar;
    }

    /**
     * @param sugar the sugar to set
     */
    public void setSugar(Double sugar) {
        this.sugar = sugar;
    }
    
    public void addSugar(Double newSugar) {
        this.sugar = this.carbs + newSugar;
    }

    /**
     * @return the foodDiaryRecordId
     */
    public Long getFoodDiaryRecordId() {
        return foodDiaryRecordId;
    }

    /**
     * @param foodDiaryRecordId the foodDiaryRecordId to set
     */
    public void setFoodDiaryRecordId(Long foodDiaryRecordId) {
        this.foodDiaryRecordId = foodDiaryRecordId;
    }
    
    
}
