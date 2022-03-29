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
public class IngredientExistsException extends Exception{

    /**
     * Creates a new instance of <code>IngredientExistsException</code> without
     * detail message.
     */
    public IngredientExistsException() {
    }

    /**
     * Constructs an instance of <code>IngredientExistsException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public IngredientExistsException(String msg) {
        super(msg);
    }
}
