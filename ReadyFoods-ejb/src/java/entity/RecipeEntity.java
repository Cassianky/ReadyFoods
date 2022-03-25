package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class RecipeEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recipeId;

    @Column(nullable = false, length = 64)
    @NotNull
    @Size(max = 64)
    private String recipeTitle;
    @Column(nullable = false)
    @NotNull
    @Min(15)
    private Integer cookingTime;
    @Column(nullable = false)
    @NotNull
    @OneToMany(mappedBy = "recipe")
    private List<IngredientSpecifcationEntity> ingredientSpecifcations;
    @Column(nullable = false)
    @NotNull
    private List<String> recipeSteps;
    @Column(nullable = false)
    @NotNull
    @Min(0)
    private Integer reorderQuantity;
    @Column(nullable = false)
    @NotNull
    @Min(0)
    private Integer caloriesPerServing; // changed to Integer due to double constraint not supported.
    @Column(nullable = false)
    @NotNull
    @Min(0)
    private Integer carbsPerServing;
    @Column(nullable = false)
    @NotNull
    @Min(0)
    private Integer fatsPerServing;
    @Column(nullable = false)
    @NotNull
    @Min(0)
    private Integer proteinsPerServing;
    @Column(nullable = false)
    @NotNull
    @Min(0)
    private Integer sugarPerServing;
    @Column(nullable = false)
    @NotNull
    private String videoURL;
    private Boolean recipeDisabled;
    
    @OneToMany(mappedBy = "recipe")
    private List<ReviewEntity> reviews;

    @OneToMany(mappedBy = "recipe")
    private List<IngredientSpecifcationEntity> ingredientSpecificationList;
    @ManyToMany
    private List<CategoryEntity> categories;//seperate or combine categories?
//    private CategoryEntity cuisineCategory;
//    private CategoryEntity dietCategory;
//    private CategoryEntity mealTypeCategory;
//    private CategoryEntity prepTimeCategory;
//    private CategoryEntity cookMethodCategory;
    
    public RecipeEntity() {
        this.ingredientSpecifcations = new ArrayList<>();
        this.recipeSteps = new ArrayList<>();
        this.categories = new ArrayList<>();
        this.recipeDisabled = false;
    }

    public RecipeEntity(String recipeTitle, Integer cookingTime, Integer reorderQuantity, Integer caloriesPerServing, Integer carbsPerServing, Integer fatsPerServing, Integer proteinsPerServing, Integer sugarPerServing, String videoURL) {
        this();
        this.recipeTitle = recipeTitle;
        this.cookingTime = cookingTime;
        this.reorderQuantity = reorderQuantity;
        this.caloriesPerServing = caloriesPerServing;
        this.carbsPerServing = carbsPerServing;
        this.fatsPerServing = fatsPerServing;
        this.proteinsPerServing = proteinsPerServing;
        this.sugarPerServing = sugarPerServing;
        this.videoURL = videoURL;
    }
    
    
    public Long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (recipeId != null ? recipeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the recipeId fields are not set
        if (!(object instanceof RecipeEntity)) {
            return false;
        }
        RecipeEntity other = (RecipeEntity) object;
        if ((this.recipeId == null && other.recipeId != null) || (this.recipeId != null && !this.recipeId.equals(other.recipeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RecipeEntity[ id=" + recipeId + " ]";
    }

    public String getRecipeTitle() {
        return recipeTitle;
    }

    public void setRecipeTitle(String recipeTitle) {
        this.recipeTitle = recipeTitle;
    }

    public Integer getCookingTime() {
        return cookingTime;
    }

    public void setCookingTime(Integer cookingTime) {
        this.cookingTime = cookingTime;
    }

    public List<IngredientSpecifcationEntity> getIngredientSpecifcations() {
        return ingredientSpecifcations;
    }

    public void setIngredientSpecifcations(List<IngredientSpecifcationEntity> ingredientSpecifcations) {
        this.ingredientSpecifcations = ingredientSpecifcations;
    }

    public List<String> getRecipeSteps() {
        return recipeSteps;
    }

    public void setRecipeSteps(List<String> recipeSteps) {
        this.recipeSteps = recipeSteps;
    }

    public Integer getReorderQuantity() {
        return reorderQuantity;
    }

    public void setReorderQuantity(Integer reorderQuantity) {
        this.reorderQuantity = reorderQuantity;
    }

    public Integer getCaloriesPerServing() {
        return caloriesPerServing;
    }

    public void setCaloriesPerServing(Integer caloriesPerServing) {
        this.caloriesPerServing = caloriesPerServing;
    }

    public Integer getCarbsPerServing() {
        return carbsPerServing;
    }

    public void setCarbsPerServing(Integer carbsPerServing) {
        this.carbsPerServing = carbsPerServing;
    }

    public Integer getFatsPerServing() {
        return fatsPerServing;
    }

    public void setFatsPerServing(Integer fatsPerServing) {
        this.fatsPerServing = fatsPerServing;
    }

    public Integer getProteinsPerServing() {
        return proteinsPerServing;
    }

    public void setProteinsPerServing(Integer proteinsPerServing) {
        this.proteinsPerServing = proteinsPerServing;
    }

    public Integer getSugarPerServing() {
        return sugarPerServing;
    }

    public void setSugarPerServing(Integer sugarPerServing) {
        this.sugarPerServing = sugarPerServing;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public Boolean getRecipeDisabled() {
        return recipeDisabled;
    }

    public void setRecipeDisabled(Boolean recipeDisabled) {
        this.recipeDisabled = recipeDisabled;
    }

    public List<CategoryEntity> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryEntity> categories) {
        this.categories = categories;
    }

}
