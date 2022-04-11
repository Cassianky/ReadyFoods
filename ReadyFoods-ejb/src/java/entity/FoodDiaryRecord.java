/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author ngcas
 */
@Entity
public class FoodDiaryRecord implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long foodDiaryRecordId;
    @NotNull
    @Column(nullable = false, length = 50)
    @Size(min = 4, max = 50)
    private String title;
    @NotNull
    @Column(nullable = false, length = 50)
    @Size(min = 0, max = 50)
    private String description;
    @NotNull
    private LocalDateTime startDate;
    @NotNull
    private LocalDateTime endDate;
    @Column(nullable = false, length = 32)
    @NotNull
    @Size(max = 32)
    private String name;
    @Column(nullable = false, length = 32)
    @NotNull
    private Double calories;
    @Column(nullable = false, length = 32)
    @NotNull
    private Double carbs;
    @Column(nullable = false, length = 32)
    @NotNull
    private Double protein;
    @Column(nullable = false, length = 32)
    @NotNull
    private Double fats;
    @Column(nullable = false, length = 32)
    @NotNull
    private Double sugar;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Customer customer;
    
    public FoodDiaryRecord() {
    }

    public FoodDiaryRecord(String title, String description, LocalDateTime startDate, LocalDateTime endDate) {
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public FoodDiaryRecord(String title, String description, LocalDateTime startDate, LocalDateTime endDate, String name, Double calories, Double carbs, Double protein, Double fats, Double sugar) {
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.name = name;
        this.calories = calories;
        this.carbs = carbs;
        this.protein = protein;
        this.fats = fats;
        this.sugar = sugar;
    }

    public Long getFoodDiaryRecordId() {
        return foodDiaryRecordId;
    }

    public void setFoodDiaryRecordId(Long foodDiaryRecordId) {
        this.foodDiaryRecordId = foodDiaryRecordId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (foodDiaryRecordId != null ? foodDiaryRecordId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the foodDiaryRecordId fields are not set
        if (!(object instanceof FoodDiaryRecord)) {
            return false;
        }
        FoodDiaryRecord other = (FoodDiaryRecord) object;
        if ((this.foodDiaryRecordId == null && other.foodDiaryRecordId != null) || (this.foodDiaryRecordId != null && !this.foodDiaryRecordId.equals(other.foodDiaryRecordId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.FoodDiaryRecord[ id=" + foodDiaryRecordId + " ]";
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the startDate
     */
    public LocalDateTime getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the endDate
     */
    public LocalDateTime getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
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

    /**
     * @return the customer
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * @param customer the customer to set
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
