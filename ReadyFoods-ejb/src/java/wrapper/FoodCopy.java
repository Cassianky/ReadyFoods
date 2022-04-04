package wrapper;

import java.io.Serializable;

public class FoodCopy implements Serializable{
    
    private String name;

    public FoodCopy() {
    }

    public FoodCopy(String name) {
        this.name = name;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
    
}
