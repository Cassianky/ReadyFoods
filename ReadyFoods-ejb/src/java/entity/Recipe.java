package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Recipe implements Serializable {

    public List<IngredientSpecification> getIngredientSpecificationList() {
        return ingredientSpecificationList;
    }

    public void setIngredientSpecificationList(List<IngredientSpecification> ingredientSpecificationList) {
        this.ingredientSpecificationList = ingredientSpecificationList;
    }

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recipeId;

    @Column(nullable = false, length = 64, unique = true)
    @NotNull
    @Size(min = 2 , max = 64)
    private String recipeTitle;
    //kiv recipe chef for search and filter?
    //kiv recipe customer for display?
    @Column(nullable = false, length = 64)
    @NotNull
    @Size(min = 2, max = 64)
    private String recipeChef;
    @Column(nullable = false)
    @NotNull
    @Min(15)
    private Integer cookingTime;
    @Column(nullable = false)
    @NotNull
    @Lob
    private String recipeSteps;
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
    //allowed to be nullable at start
    //constructing recipe initially doesnt require picUrl
    //to be added in subsequently
    @Column
    private String picUrl;
    @Column
    private String videoURL;

    @OneToMany(mappedBy = "recipe", fetch = FetchType.LAZY)
    private List<Review> reviews;
    
    @OneToMany
    private List<CommentEntity>comments;

    @OneToMany(fetch = FetchType.LAZY)
    private List<IngredientSpecification> ingredientSpecificationList;
    @ManyToMany
    private List<Category> categories;//seperate or combine categories?
    //5 parent category - cuisine, diet, mealType, prepTime,cookMethod;

    public Recipe() {
        this.ingredientSpecificationList = new ArrayList<>();
        this.categories = new ArrayList<>();
        this.reviews = new ArrayList<>();
    } 
    

    public Recipe(String recipeTitle, String recipeChef, Integer cookingTime, String recipeSteps,
            Integer caloriesPerServing, Integer carbsPerServing,
            Integer fatsPerServing, Integer proteinsPerServing, Integer sugarPerServing,
            String videoURL) {
        this();
        this.recipeTitle = recipeTitle;
        this.recipeChef = recipeChef;
        this.cookingTime = cookingTime;
        this.recipeSteps = recipeSteps; // how to input by admin?
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
        if (!(object instanceof Recipe)) {
            return false;
        }
        Recipe other = (Recipe) object;
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


    public Integer getCookingTime() {
        return cookingTime;
    }

    public String getRecipeSteps() {
        return recipeSteps;
    }

    public Integer getCaloriesPerServing() {
        return caloriesPerServing;
    }

    public Integer getCarbsPerServing() {
        return carbsPerServing;
    }

    public Integer getFatsPerServing() {
        return fatsPerServing;
    }


    public Integer getProteinsPerServing() {
        return proteinsPerServing;
    }

    public Integer getSugarPerServing() {
        return sugarPerServing;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public String getRecipeChef() {
        return recipeChef;
    }


    /**
     * @param reviews the reviews to set
     */
    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    /**
     * @param recipeTitle the recipeTitle to set
     */
    public void setRecipeTitle(String recipeTitle) {
        this.recipeTitle = recipeTitle;
    }

    /**
     * @param recipeChef the recipeChef to set
     */
    public void setRecipeChef(String recipeChef) {
        this.recipeChef = recipeChef;
    }

    /**
     * @param cookingTime the cookingTime to set
     */
    public void setCookingTime(Integer cookingTime) {
        this.cookingTime = cookingTime;
    }

    /**
     * @param recipeSteps the recipeSteps to set
     */
    public void setRecipeSteps(String recipeSteps) {
        this.recipeSteps = recipeSteps;
    }

    /**
     * @param caloriesPerServing the caloriesPerServing to set
     */
    public void setCaloriesPerServing(Integer caloriesPerServing) {
        this.caloriesPerServing = caloriesPerServing;
    }

    /**
     * @param carbsPerServing the carbsPerServing to set
     */
    public void setCarbsPerServing(Integer carbsPerServing) {
        this.carbsPerServing = carbsPerServing;
    }

    /**
     * @param fatsPerServing the fatsPerServing to set
     */
    public void setFatsPerServing(Integer fatsPerServing) {
        this.fatsPerServing = fatsPerServing;
    }

    /**
     * @param proteinsPerServing the proteinsPerServing to set
     */
    public void setProteinsPerServing(Integer proteinsPerServing) {
        this.proteinsPerServing = proteinsPerServing;
    }

    /**
     * @param sugarPerServing the sugarPerServing to set
     */
    public void setSugarPerServing(Integer sugarPerServing) {
        this.sugarPerServing = sugarPerServing;
    }

    /**
     * @param videoURL the videoURL to set
     */
    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    /**
     * @return the reviews
     */
    public List<Review> getReviews() {
        return reviews;
    }

    /**
     * @return the comments
     */
    public List<CommentEntity> getComments() {
        return comments;
    }

    /**
     * @param comments the comments to set
     */
    public void setComments(List<CommentEntity> comments) {
        this.comments = comments;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

}
