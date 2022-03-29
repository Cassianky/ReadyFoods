package util.exception;

/**
 *
 * @author Eugene Chua
 */
public class RecipeTitleExistException extends Exception{

    /**
     * Creates a new instance of <code>RecipeTitleExistException</code> without
     * detail message.
     */
    public RecipeTitleExistException() {
    }

    public RecipeTitleExistException(String msg) {
        super(msg);
    }
}
