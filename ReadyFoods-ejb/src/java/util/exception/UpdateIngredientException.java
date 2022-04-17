/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author ngcas
 */
public class UpdateIngredientException extends Exception{

    /**
     * Creates a new instance of <code>UpdateIngredientException</code> without
     * detail message.
     */
    public UpdateIngredientException() {
    }

    /**
     * Constructs an instance of <code>UpdateIngredientException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public UpdateIngredientException(String msg) {
        super(msg);
    }
}
