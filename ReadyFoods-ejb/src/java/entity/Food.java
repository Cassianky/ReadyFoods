package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Food implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long foodId;
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
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    @NotNull
    private Date foodCreatedDate;

    public Food() {
    }

    public Food(String name, Double calories, Double carbs, Double protein, Double fats, Double sugar, Date foodCreatedDate) {
        this.name = name;
        this.calories = calories;
        this.carbs = carbs;
        this.protein = protein;
        this.fats = fats;
        this.sugar = sugar;
        this.foodCreatedDate = foodCreatedDate;
    }

    public Long getFoodId() {
        return foodId;
    }

    public void setFoodId(Long foodId) {
        this.foodId = foodId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (foodId != null ? foodId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the foodId fields are not set
        if (!(object instanceof Food)) {
            return false;
        }
        Food other = (Food) object;
        if ((this.foodId == null && other.foodId != null) || (this.foodId != null && !this.foodId.equals(other.foodId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Food[ id=" + foodId + " ]";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getCalories() {
        return calories;
    }

    public void setCalories(Double calories) {
        this.calories = calories;
    }

    public Double getCarbs() {
        return carbs;
    }

    public void setCarbs(Double carbs) {
        this.carbs = carbs;
    }

    public Double getProtein() {
        return protein;
    }

    public void setProtein(Double protein) {
        this.protein = protein;
    }

    public Double getFats() {
        return fats;
    }

    public void setFats(Double fats) {
        this.fats = fats;
    }

    public Double getSugar() {
        return sugar;
    }

    public void setSugar(Double sugar) {
        this.sugar = sugar;
    }

    public Date getFoodCreatedDate() {
        return foodCreatedDate;
    }

    public void setFoodCreatedDate(Date foodCreatedDate) {
        this.foodCreatedDate = foodCreatedDate;
    }
    
}
