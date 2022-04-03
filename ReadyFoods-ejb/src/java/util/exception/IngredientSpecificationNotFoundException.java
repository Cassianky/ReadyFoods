/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author PYT
 */
public class IngredientSpecificationNotFoundException extends Exception{

    /**
     * Creates a new instance of
     * <code>IngredientSpecificationNotFoundException</code> without detail
     * message.
     */
    public IngredientSpecificationNotFoundException() {
    }

    /**
     * Constructs an instance of
     * <code>IngredientSpecificationNotFoundException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public IngredientSpecificationNotFoundException(String msg) {
        super(msg);
    }
}
