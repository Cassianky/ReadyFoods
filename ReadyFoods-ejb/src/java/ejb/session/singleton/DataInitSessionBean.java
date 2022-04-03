package ejb.session.singleton;

import ejb.session.stateless.CategorySessionBeanLocal;
import ejb.session.stateless.CustomerSessionBeanLocal;
import ejb.session.stateless.IngredientSessionBean;
import ejb.session.stateless.IngredientSessionBeanLocal;
import ejb.session.stateless.IngredientSpecificaitonSessionBeanLocal;
import ejb.session.stateless.RecipeSessionBeanLocal;
import entity.Category;
import entity.Customer;
import entity.Ingredient;
import entity.IngredientSpecification;
import entity.Recipe;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.ActivityLevel;
import util.enumeration.DietType;
import util.enumeration.Gender;
import util.enumeration.IngredientUnit;
import util.exception.CategoryNotFoundException;
import util.exception.CreateCategoryException;
import util.exception.CreateRecipeException;
import util.exception.CustomerEmailExistsException;
import util.exception.CustomerNotFoundException;
import util.exception.IngredientExistsException;
import util.exception.IngredientNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.RecipeNotFoundException;
import util.exception.RecipeTitleExistException;
import util.exception.UnknownPersistenceException;

@Singleton
@LocalBean
@Startup

public class DataInitSessionBean {

    @EJB
    private IngredientSpecificaitonSessionBeanLocal ingredientSpecificationSessionBeanLocal;

    @EJB
    private CustomerSessionBeanLocal customerSessionBeanLocal;
    @EJB
    RecipeSessionBeanLocal recipeSessionBeanLocal;
    @EJB
    CategorySessionBeanLocal categorySessionBeanLocal;
    @EJB
    IngredientSessionBeanLocal ingredientSessionBeanLocal;

    @PersistenceContext(unitName = "ReadyFoods-ejbPU")
    private EntityManager em;

    @PostConstruct
    public void postConstruct() {
        try {
            customerSessionBeanLocal.retrieveCustomerByEmail("customer1@gmail.com");
            recipeSessionBeanLocal.retrieveRecipeByRecipeId(new Long("1"));
        } catch (CustomerNotFoundException | RecipeNotFoundException ex) {
            initializeData();
        }
    }

    private void initializeData() {
        try {
            Customer customer1 = new Customer("customer1", "customer1", "customer1", "99999999", "password", "customer1@gmail.com", "123 Street", 20, DietType.VEGAN, Gender.FEMALE, ActivityLevel.HIGH);
            customerSessionBeanLocal.createNewCustomer(customer1);

            Ingredient ingredient1 = new Ingredient("Chicken", "Whole Chicken between 1kg to 1.5kg", IngredientUnit.Whole, new BigDecimal("10.00"), 50, 150);
            Ingredient ingredient2 = new Ingredient("Salt", "Salt by grams", IngredientUnit.Gram, new BigDecimal("0.05"), 100 * 1000, 20 * 1000);
            Ingredient ingredient3 = new Ingredient("Scallion", "Scallion by bunch", IngredientUnit.Whole, new BigDecimal("0.50"), 50, 10);
            Ingredient ingredient4 = new Ingredient("Sesame Oil", "Sesame Oil by 100 ml", IngredientUnit.Gram, new BigDecimal("0.80"), 50 * 1000, 10 * 1000);
            Ingredient ingredient5 = new Ingredient("Ginger", "Whole Ginger between 15-30g", IngredientUnit.Whole, new BigDecimal("0.50"), 50, 10);
            Ingredient ingredient6 = new Ingredient("Rice", "Rice 1kg per pack", IngredientUnit.Kilogram, new BigDecimal("2.50"), 200, 50);

            Long ingredient1Id = ingredientSessionBeanLocal.createNewIngredient(ingredient1);
            Long ingredient2Id = ingredientSessionBeanLocal.createNewIngredient(ingredient2);
            Long ingredient3Id = ingredientSessionBeanLocal.createNewIngredient(ingredient3);
            Long ingredient4Id = ingredientSessionBeanLocal.createNewIngredient(ingredient4);
            Long ingredient5Id = ingredientSessionBeanLocal.createNewIngredient(ingredient5);

            IngredientSpecification ingredientSpecifcation1 = new IngredientSpecification(1);
            IngredientSpecification ingredientSpecifcation2 = new IngredientSpecification(50);
            IngredientSpecification ingredientSpecifcation3 = new IngredientSpecification(1);
            IngredientSpecification ingredientSpecifcation4 = new IngredientSpecification(30);
            IngredientSpecification ingredientSpecifcation5 = new IngredientSpecification(1);
       

            Long ingredientSpecification1Id = ingredientSpecificationSessionBeanLocal.createNewIngredientSpecification(ingredientSpecifcation1, 1L);
            Long ingredientSpecification2Id = ingredientSpecificationSessionBeanLocal.createNewIngredientSpecification(ingredientSpecifcation2, ingredient2Id);
            Long ingredientSpecification3Id = ingredientSpecificationSessionBeanLocal.createNewIngredientSpecification(ingredientSpecifcation3, ingredient3Id);
            Long ingredientSpecification4Id = ingredientSpecificationSessionBeanLocal.createNewIngredientSpecification(ingredientSpecifcation4, ingredient4Id);
            Long ingredientSpecification5Id = ingredientSpecificationSessionBeanLocal.createNewIngredientSpecification(ingredientSpecifcation5, ingredient5Id);
            System.out.println("INGREDIENT SPECIFICATION: " + ingredientSpecification1Id);
            List<Long> ingredientSpecicationsId = new ArrayList<>();
            ingredientSpecicationsId.add(ingredientSpecification1Id);
            ingredientSpecicationsId.add(ingredientSpecification2Id);
            ingredientSpecicationsId.add(ingredientSpecification3Id);
            ingredientSpecicationsId.add(ingredientSpecification4Id);
            ingredientSpecicationsId.add(ingredientSpecification5Id);

            String recipeSteps = "Preparation" + "/n"
                    + "To clean the chicken, rub all over with a handful of kosher salt, getting rid of any loose skin. Rinse the chicken well inside and out. Pat dry with paper towels." + "/n"
                    + "Remove any excess fat from the chicken and set aside for later." + "/n"
                    + "Season the chicken generously with salt. Stuff the chicken cavity with the ginger slices and scallions." + "/n"
                    + "Place the chicken in a large stock pot, cover with cold water by 1 inch (2 cm), and season with salt to taste." + "/n"
                    + "Bring to a boil over high heat, then immediately reduce the heat to low to maintain a simmer. Cover and cook for about 30 minutes, or until the internal temperature of the chicken reaches 165°F (75°C). Remove the pot from the heat." + "/n"
                    + "Remove the chicken from the pot, reserving the poaching liquid for later, and transfer to an ice bath for 5 minutes to stop the cooking process and to keep the chicken skin springy. Discard the ginger and green onion." + "/n"
                    + "After it’s cooled, pat the chicken dry with paper towels and rub all over with sesame oil. This will help prevent the chicken from drying out." + "/n"
                    + "In a large wok or skillet, heat ¼ cup (60 ml) of sesame oil over medium-high heat. Add 2 tablespoons of reserved chopped chicken fat, the garlic, ginger, and salt, and fry until aromatic, about 10 minutes." + "/n"
                    + "Reserve ¼ of the fried garlic mixture, then add the rice to the remaining fried garlic and stir to coat. Cook for 3 minutes." + "/n"
                    + "Transfer the rice to a rice cooker and add 2 cups (480 ml) of reserved poaching broth. Steam the rice for 60 minutes, or until tender." + "/n"
                    + "While the rice is cooking, carve the chicken for serving." + "/n"
                    + "Make the chili sauce: combine the sambal, Sriracha, sugar, garlic, ginger, lime juice, and chicken broth in a small bowl and stir to incorporate." + "/n"
                    + "Make the ginger garlic sauce: in a small bowl, combine the ginger, garlic, salt, peanut oil, and rice vinegar, and stir to incorporate." + "/n"
                    + "Make the soy sauce: in a small bowl, combine the reserved fried garlic and ginger with the oyster sauce, dark soy sauce, light soy sauce, and chicken broth, and stir to incorporate." + "/n"
                    + "Serve the sliced chicken with the rice, dipping sauces, sliced cucumbers, and fresh cilantro." + "/n"
                    + "Enjoy!";

            Recipe recipe1 = new Recipe("Hainanese Chicken Rice", "Chicken King", 45, recipeSteps, 1248, 87, 76, 47, 9, "https://youtu.be/XPA3rn1XImY");


            Category parentCategory1 = categorySessionBeanLocal.createNewCategory(new Category("Cuisine", "Cuisine"), null);
            Category childCategory11 = categorySessionBeanLocal.createNewCategory(new Category("Asian", "Asian"), parentCategory1.getCategoryId());
            Category childCategory12 = categorySessionBeanLocal.createNewCategory(new Category("Western", "Western"), parentCategory1.getCategoryId());
            Category childCategory13 = categorySessionBeanLocal.createNewCategory(new Category("Thai", "Thai"), parentCategory1.getCategoryId());
            Category childCategory14 = categorySessionBeanLocal.createNewCategory(new Category("Japanese", "Japanese"), parentCategory1.getCategoryId());
            Category childCategory15 = categorySessionBeanLocal.createNewCategory(new Category("Indian", "Indian"), parentCategory1.getCategoryId());
            Category parentCategory2 = categorySessionBeanLocal.createNewCategory(new Category("Types of Meal", "Types of Meal"), null);
            Category childCategory21 = categorySessionBeanLocal.createNewCategory(new Category("Breakfast", "Breakfast"), parentCategory2.getCategoryId());
            Category childCategory22 = categorySessionBeanLocal.createNewCategory(new Category("Brunch", "Brunch"), parentCategory2.getCategoryId());
            Category childCategory23 = categorySessionBeanLocal.createNewCategory(new Category("Lunch", "Lunch"), parentCategory2.getCategoryId());
            Category childCategory24 = categorySessionBeanLocal.createNewCategory(new Category("Tea", "Tea"), parentCategory2.getCategoryId());
            Category childCategory25 = categorySessionBeanLocal.createNewCategory(new Category("Dinner", "Dinner"), parentCategory2.getCategoryId());

            List<Long> recipe1Categories = new ArrayList<>();
            recipe1Categories.add(childCategory11.getCategoryId());
            recipe1Categories.add(childCategory23.getCategoryId());
            recipe1Categories.add(childCategory25.getCategoryId());

            recipeSessionBeanLocal.createNewRecipe(recipe1, recipe1Categories, ingredientSpecicationsId);

        } catch (InputDataValidationException | UnknownPersistenceException | CustomerEmailExistsException | CategoryNotFoundException
                | CreateCategoryException | CreateRecipeException | RecipeTitleExistException | IngredientExistsException ex) {
            ex.printStackTrace();
        } catch (IngredientNotFoundException ex) {
            Logger.getLogger(DataInitSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
