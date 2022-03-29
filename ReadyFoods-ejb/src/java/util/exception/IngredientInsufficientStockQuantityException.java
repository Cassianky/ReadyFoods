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
public class IngredientInsufficientStockQuantityException extends Exception{

    /**
     * Creates a new instance of
     * <code>IngredientInsufficientStockQuantityException</code> without detail
     * message.
     */
    public IngredientInsufficientStockQuantityException() {
    }

    /**
     * Constructs an instance of
     * <code>IngredientInsufficientStockQuantityException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public IngredientInsufficientStockQuantityException(String msg) {
        super(msg);
    }
}
