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
import util.enumeration.PreparationMethod;
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
            Ingredient ingredient2 = new Ingredient("Salt", "Salt by 10 grams.unit", IngredientUnit.Gram, new BigDecimal("0.05"), 100 * 1000, 20 * 1000);
            Ingredient ingredient3 = new Ingredient("Scallion", "Scallion by bunch", IngredientUnit.Whole, new BigDecimal("0.50"), 50, 10);
            Ingredient ingredient4 = new Ingredient("Sesame Oil", "Sesame Oil by 100 ml/unit", IngredientUnit.Millitre, new BigDecimal("0.80"), 50 * 1000, 10 * 1000);
            Ingredient ingredient5 = new Ingredient("Ginger", "Whole Ginger between 15-30g", IngredientUnit.Whole, new BigDecimal("0.50"), 50, 10);
            Ingredient ingredient6 = new Ingredient("Rice", "Rice 1kg per pack", IngredientUnit.Kilogram, new BigDecimal("2.50"), 200, 50);
            Ingredient ingredient7 = new Ingredient("Spaghetti", "Dried bunch of Spaghetti", IngredientUnit.Gram, new BigDecimal("3.50"), 200, 50);
            Ingredient ingredient8 = new Ingredient("Olive Oil", "Olive Oil by 100 ml/unit", IngredientUnit.Millitre, new BigDecimal("1.00"), 50 * 1000, 10 * 1000);
            Ingredient ingredient9 = new Ingredient("Beef", "100g per piece", IngredientUnit.Piece, new BigDecimal("10.00"), 75, 15);
            Ingredient ingredient10 = new Ingredient("Tomato", "20g per piece", IngredientUnit.Piece, new BigDecimal("1.50"), 100, 20);
            Ingredient ingredient11 = new Ingredient("Sugar", "Sugar by 10 grams/unit", IngredientUnit.Gram, new BigDecimal("0.03"), 100 * 1000, 10 * 1000);
            Ingredient ingredient12 = new Ingredient("Pepper", "Pepper by 10 grams/unit", IngredientUnit.Gram, new BigDecimal("0.01"), 100 * 1000, 10 * 1000);
            Ingredient ingredient13 = new Ingredient("Garlic", "1 Piece Garlic ~20 gram", IngredientUnit.Piece, new BigDecimal("2.50"), 200, 50);

            Long ingredient1Id = ingredientSessionBeanLocal.createNewIngredient(ingredient1);
            Long ingredient2Id = ingredientSessionBeanLocal.createNewIngredient(ingredient2);
            Long ingredient3Id = ingredientSessionBeanLocal.createNewIngredient(ingredient3);
            Long ingredient4Id = ingredientSessionBeanLocal.createNewIngredient(ingredient4);
            Long ingredient5Id = ingredientSessionBeanLocal.createNewIngredient(ingredient5);
            Long ingredient6Id = ingredientSessionBeanLocal.createNewIngredient(ingredient6);
            Long ingredient7Id = ingredientSessionBeanLocal.createNewIngredient(ingredient7);
            Long ingredient8Id = ingredientSessionBeanLocal.createNewIngredient(ingredient8);
            Long ingredient9Id = ingredientSessionBeanLocal.createNewIngredient(ingredient9);
            Long ingredient10Id = ingredientSessionBeanLocal.createNewIngredient(ingredient10);
            Long ingredient11Id = ingredientSessionBeanLocal.createNewIngredient(ingredient11);
            Long ingredient12Id = ingredientSessionBeanLocal.createNewIngredient(ingredient12);
            Long ingredient13Id = ingredientSessionBeanLocal.createNewIngredient(ingredient13);

            IngredientSpecification ingredientSpecifcation1 = new IngredientSpecification(1);
            IngredientSpecification ingredientSpecifcation2 = new IngredientSpecification(50);
            IngredientSpecification ingredientSpecifcation3 = new IngredientSpecification(1, PreparationMethod.Chopped);
            IngredientSpecification ingredientSpecifcation4 = new IngredientSpecification(30);
            IngredientSpecification ingredientSpecifcation5 = new IngredientSpecification(1, PreparationMethod.Sliced);
            IngredientSpecification ingredientSpecifcation6 = new IngredientSpecification(1);
            
            IngredientSpecification ingredientSpecifcation7 = new IngredientSpecification(1);
            IngredientSpecification ingredientSpecifcation8 = new IngredientSpecification(1);
            IngredientSpecification ingredientSpecifcation9 = new IngredientSpecification(1, PreparationMethod.Minced);
            IngredientSpecification ingredientSpecifcation10 = new IngredientSpecification(1, PreparationMethod.Crushed);
            IngredientSpecification ingredientSpecifcation11 = new IngredientSpecification(1);
            IngredientSpecification ingredientSpecifcation12 = new IngredientSpecification(1);
            IngredientSpecification ingredientSpecifcation13 = new IngredientSpecification(1);

            Long ingredientSpecification1Id = ingredientSpecificationSessionBeanLocal.createNewIngredientSpecification(ingredientSpecifcation1, 1L);
            Long ingredientSpecification2Id = ingredientSpecificationSessionBeanLocal.createNewIngredientSpecification(ingredientSpecifcation2, ingredient2Id);
            Long ingredientSpecification3Id = ingredientSpecificationSessionBeanLocal.createNewIngredientSpecification(ingredientSpecifcation3, ingredient3Id);
            Long ingredientSpecification4Id = ingredientSpecificationSessionBeanLocal.createNewIngredientSpecification(ingredientSpecifcation4, ingredient4Id);
            Long ingredientSpecification5Id = ingredientSpecificationSessionBeanLocal.createNewIngredientSpecification(ingredientSpecifcation5, ingredient5Id);
            Long ingredientSpecification6Id = ingredientSpecificationSessionBeanLocal.createNewIngredientSpecification(ingredientSpecifcation6, ingredient6Id);
            Long ingredientSpecification7Id = ingredientSpecificationSessionBeanLocal.createNewIngredientSpecification(ingredientSpecifcation7, ingredient7Id);
            Long ingredientSpecification8Id = ingredientSpecificationSessionBeanLocal.createNewIngredientSpecification(ingredientSpecifcation8, ingredient8Id);
            Long ingredientSpecification9Id = ingredientSpecificationSessionBeanLocal.createNewIngredientSpecification(ingredientSpecifcation9, ingredient9Id);
            Long ingredientSpecification10Id = ingredientSpecificationSessionBeanLocal.createNewIngredientSpecification(ingredientSpecifcation10, ingredient10Id);
            Long ingredientSpecification11Id = ingredientSpecificationSessionBeanLocal.createNewIngredientSpecification(ingredientSpecifcation11, ingredient11Id);
            Long ingredientSpecification12Id = ingredientSpecificationSessionBeanLocal.createNewIngredientSpecification(ingredientSpecifcation12, ingredient12Id);
            Long ingredientSpecification13Id = ingredientSpecificationSessionBeanLocal.createNewIngredientSpecification(ingredientSpecifcation13, ingredient13Id);

            List<Long> recipe1IngredientSpecicationsId = new ArrayList<>();
            recipe1IngredientSpecicationsId.add(ingredientSpecification1Id);
            recipe1IngredientSpecicationsId.add(ingredientSpecification2Id);
            recipe1IngredientSpecicationsId.add(ingredientSpecification3Id);
            recipe1IngredientSpecicationsId.add(ingredientSpecification4Id);
            recipe1IngredientSpecicationsId.add(ingredientSpecification5Id);
            recipe1IngredientSpecicationsId.add(ingredientSpecification6Id);
            
            List<Long> recipe2IngredientSpecicationsId = new ArrayList<>();
            recipe2IngredientSpecicationsId.add(ingredientSpecification7Id);
            recipe2IngredientSpecicationsId.add(ingredientSpecification8Id);
            recipe2IngredientSpecicationsId.add(ingredientSpecification9Id);
            recipe2IngredientSpecicationsId.add(ingredientSpecification10Id);
            recipe2IngredientSpecicationsId.add(ingredientSpecification11Id);
            recipe2IngredientSpecicationsId.add(ingredientSpecification12Id);
            recipe2IngredientSpecicationsId.add(ingredientSpecification13Id);

            //purpose used /n for replacement
            String recipe1Steps = "Preparation" + "/n"
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

            //different formatting to test
            String recipe2Steps = "Heat oil in a large pot or deep skillet over medium high heat. Add onion and garlic, cook for 3 minutes or until light golden and softened.\n"
                    + "Turn heat up to high and add beef. Cook, breaking it up as your go, until browned.\n"
                    + "Add red wine. Bring to simmer and cook for 1 minute, scraping the bottom of the pot, until the alcohol smell is gone.\n"
                    + "Add remaining ingredients except salt and pepper. Stir, bring to a simmer then turn down to medium so it bubbles gently. Cook for 20 – 30 minutes (no lid), adding water if the sauce gets too thick for your taste. Stir occasionally.\n"
                    + "Slow simmer option: really takes this to another level, if you have the time! Add 3/4 cup of water, cover with lid and simmer on very low for 2 – 2.5 hours, stirring every 30 minutes or so. (Note 5) Uncover, simmer 20 minutes to thicken sauce. (Note 6 for slow cooker)\n"
                    + "Adjust salt and pepper to taste right at the end. Serve over spaghetti – though if you have the time, I recommend tossing the sauce and pasta per steps below.";

            Recipe recipe1 = new Recipe("Hainanese Chicken Rice", "Chicken Rice King", 45, recipe1Steps, 1248, 87, 76, 47, 9, "https://www.youtube.com/embed/XPA3rn1XImY");
            Recipe recipe2 = new Recipe("Spaghetti Bolognese", "Gotten Ramshy", 30, recipe2Steps, 510, 53, 12, 40, 9, "https://www.youtube.com/embed/yShBC-G-jrQ");

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
            
            List<Long> recipe2Categories = new ArrayList<>();
            recipe2Categories.add(childCategory12.getCategoryId());
            recipe2Categories.add(childCategory23.getCategoryId());
            recipe2Categories.add(childCategory25.getCategoryId());

            recipeSessionBeanLocal.createNewRecipe(recipe1, recipe1Categories, recipe1IngredientSpecicationsId);
            recipeSessionBeanLocal.createNewRecipe(recipe2, recipe2Categories, recipe2IngredientSpecicationsId);

        } catch (InputDataValidationException | UnknownPersistenceException | CustomerEmailExistsException | CategoryNotFoundException
                | CreateCategoryException | CreateRecipeException | RecipeTitleExistException | IngredientExistsException ex) {
            ex.printStackTrace();
        } catch (IngredientNotFoundException ex) {
            Logger.getLogger(DataInitSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
