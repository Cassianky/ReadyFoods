package ws.rest;

import ejb.session.stateless.IngredientSessionBeanLocal;
import ejb.session.stateless.RecipeSessionBeanLocal;
import ejb.session.stateless.StaffSessionBeanLocal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class SessionBeanLookup {

    private final String ejbModuleJndiPath;

    public SessionBeanLookup() {
        this.ejbModuleJndiPath = "java:global/ReadyFoods/ReadyFoods-ejb/";
    }

    public IngredientSessionBeanLocal lookupIngredientSessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (IngredientSessionBeanLocal) c.lookup(ejbModuleJndiPath + "IngredientSessionBean!ejb.session.stateless.IngredientSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    public StaffSessionBeanLocal lookupStaffSessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (StaffSessionBeanLocal) c.lookup(ejbModuleJndiPath + "StaffSessionBean!ejb.session.stateless.StaffSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    
    public RecipeSessionBeanLocal lookupRecipeSessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (RecipeSessionBeanLocal) c.lookup(ejbModuleJndiPath + "RecipeSessionBean!ejb.session.stateless.RecipeSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

}
