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
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import wrapper.FoodCopy;

@Entity
public class FoodDiaryRecord implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long foodDiaryRecordId;
    @NotNull
    @Column(nullable = false, length = 50)
    @Size(min=4, max=50)
    private String title;
    @NotNull
    @Column(nullable = false, length = 50)
    @Size(min=0, max=50)
    private String description;
    @NotNull
    private LocalDateTime startDate;
    @NotNull
    private LocalDateTime endDate;
    //@OneToOne(fetch = FetchType.LAZY, optional = false)
    // @JoinColumn(nullable = false)
    //private Food food;
    @NotNull
    private FoodCopy foodCopy;
    
    public FoodDiaryRecord() {
    }

    public FoodDiaryRecord(String title, String description, LocalDateTime startDate, LocalDateTime endDate) {
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        //this.food = food;
    }

   @Override
    public int hashCode() {
        int hash = 0;
        hash += (foodDiaryRecordId != null ? foodDiaryRecordId.hashCode() : 0);
        return hash;
    }
    
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the customerId fields are not set
        if (!(object instanceof Customer)) {
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
    
    public Long getFoodDiaryRecordId() {
        return foodDiaryRecordId;
    }

    public void setFoodDiaryRecordId(Long foodDiaryRecordId) {
        this.foodDiaryRecordId = foodDiaryRecordId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    //public Food getFood() {
    //    return food;
    //}

    //public void setFood(Food food) {
    //    this.food = food;
    //}

    /**
     * @return the foodCopy
     */
    public FoodCopy getFoodCopy() {
        return foodCopy;
    }

    /**
     * @param foodCopy the foodCopy to set
     */
    public void setFoodCopy(FoodCopy foodCopy) {
        this.foodCopy = foodCopy;
    }
}
