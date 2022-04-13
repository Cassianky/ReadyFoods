package jsf.converter;

import entity.Ingredient;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;



@FacesConverter(value = "ingredientConverter", forClass = Ingredient.class)

public class IngredientConverter implements Converter 
{
    public Object getAsObject(FacesContext context, UIComponent component, String value)
    {
        if (value == null || value.length() == 0 || value.equals("null")) 
        {
            return null;
        }

        try
        {            
            Long objLong = Long.parseLong(value);
            
            List<Long> ingredientIds = (List<Long>)context.getExternalContext().getSessionMap().get("selectedIngredients");
            
            for(Long id : ingredientIds)
            {
                if(id.equals(objLong))
                {
                    return id;
                }
            }
        }
        catch(Exception ex)
        {
            throw new IllegalArgumentException("Please select a valid value");
        }
        
        return null;
    }

    
    
    public String getAsString(FacesContext context, UIComponent component, Object value)
    {
        if (value == null) 
        {
            return "";
        }
        
        if (value instanceof String)
        {
            return "";
        }

        if (value instanceof Long) {
            Long id = (Long) value;
            
            return String.valueOf(id);
            
        }
        
        if (value instanceof Ingredient)
        {            
            Ingredient recipe = (Ingredient) value;                        
            
            try
            {
                return recipe.getIngredientId().toString();
            }
            catch(Exception ex)
            {
                throw new IllegalArgumentException("Invalid value");
            }
        }
        else 
        {
            throw new IllegalArgumentException("Invalid value");
        }
    }
}