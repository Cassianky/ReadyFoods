/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Ingredient;
import entity.Recipe;
import entity.RecipeOfTheDay;
import static java.lang.Math.random;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author ngcas
 */
@Stateless
public class EjbTimerSessionBean implements EjbTimerSessionBeanLocal {

    @PersistenceContext(unitName = "ReadyFoods-ejbPU")
    private EntityManager em;

    @EJB(name = "RecipeSessionBeanLocal")
    private RecipeSessionBeanLocal recipeSessionBeanLocal;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    public void persist(Object object) {
        em.persist(object);
    }
    
    @Override
    @Schedule(hour = "2", info = "recipeOfTheDay")
    //@Schedule(hour = "*", minute = "*", second = "*/5", info = "recipeOfTheDay")
    public void recipeOfTheDay() {
        
        LocalDate today = LocalDate.now();
        System.out.print("********** EjbTimerSessionBean.recipeOfTheDay(): Timeout at " + today);
        Query query = em.createQuery("SELECT rd FROM RecipeOfTheDay rd");
        List<RecipeOfTheDay> recipes = query.getResultList();
        for(RecipeOfTheDay recipe: recipes)
        {
            em.remove(recipe);
        }
        
        Query query2 = em.createQuery("SELECT r FROM Recipe r");
        List<Recipe> recipesExisting = query2.getResultList();
        Random random = new Random();
        Long newRecipeOfTheDayId = (recipesExisting.get(random.nextInt(recipesExisting.size()))).getRecipeId();
        RecipeOfTheDay newRecipeOfTheDay = new RecipeOfTheDay();
        newRecipeOfTheDay.setRecipeId(newRecipeOfTheDayId);
        System.out.print("Recipe Of The Day" + newRecipeOfTheDayId);
        em.persist(newRecipeOfTheDay);
        em.flush();
    }
    
    @Override
    public void recipeOfTheDayInitialise() { 
        Query query2 = em.createQuery("SELECT r FROM Recipe r");
        List<Recipe> recipesExisting = query2.getResultList();
        Random random = new Random();
        Long newRecipeOfTheDayId = (recipesExisting.get(random.nextInt(recipesExisting.size()))).getRecipeId();
        RecipeOfTheDay newRecipeOfTheDay = new RecipeOfTheDay();
        newRecipeOfTheDay.setRecipeId(newRecipeOfTheDayId);
        System.out.print("Recipe Of The Day" + newRecipeOfTheDayId);
        em.persist(newRecipeOfTheDay);
        em.flush();
    }
    
    @Override
    @Schedule(hour = "2", info = "reorderIngredientQuantity")
    //@Schedule(hour = "*", minute = "*", second = "*/5", info = "recipeOfTheDay")
    public void reorderIngredientQuantity() {
        
        LocalDate today = LocalDate.now();
        System.out.print("********** EjbTimerSessionBean.reorderIngredientQuantity(): Timeout at " + today);
        Query query = em.createQuery("SELECT i FROM Ingredient i");
        List<Ingredient> ingredients = query.getResultList();
        for(Ingredient ingredient: ingredients)
        {
            Integer reorderQty = ingredient.getReorderQuantity();
            Integer stockQty = ingredient.getStockQuantity();
            ingredient.setStockQuantity(reorderQty + stockQty);
        }
    }
    
    @Override
    public List<RecipeOfTheDay> retrieveRecipeOftheDay()
    {
        Query query = em.createQuery("SELECT rd FROM RecipeOfTheDay rd");
        List<RecipeOfTheDay> recipes = query.getResultList();
        return recipes;
    }
}
