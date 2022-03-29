package util.exception;

public class RecipeNotFoundException extends Exception{

    /**
     * Creates a new instance of <code>RecipeNotFoundException</code> without
     * detail message.
     */
    public RecipeNotFoundException() {
    }

    public RecipeNotFoundException(String msg) {
        super(msg);
    }
}
