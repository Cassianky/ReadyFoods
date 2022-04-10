package jsf.managedbean;

import entity.Recipe;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;



@Named(value = "recipeViewSummarisedManagedBean")
@ViewScoped

public class RecipeViewSummarisedManagedBean implements Serializable
{
    private Recipe recipeToView;
    
    
    
    public RecipeViewSummarisedManagedBean()
    {
        recipeToView = new Recipe();
    }
    
    
    
    @PostConstruct
    public void postConstruct()
    {        
    }

    /**
     * @return the recipeToView
     */
    public Recipe getRecipeToView() {
        return recipeToView;
    }

    /**
     * @param recipeToView the recipeToView to set
     */
    public void setRecipeToView(Recipe recipeToView) {
        this.recipeToView = recipeToView;
    }

    

}
