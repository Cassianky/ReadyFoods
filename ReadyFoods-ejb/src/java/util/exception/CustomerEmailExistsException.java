package util.exception;

public class CustomerEmailExistsException extends Exception{
    public CustomerEmailExistsException() 
    {
    }
    
    public CustomerEmailExistsException(String msg) 
    {
        super(msg);
    }
}
