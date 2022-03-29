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
public class ShoppingCartIsEmptyException extends Exception{

    /**
     * Creates a new instance of <code>ShoppingCartIsEmptyException</code>
     * without detail message.
     */
    public ShoppingCartIsEmptyException() {
    }

    /**
     * Constructs an instance of <code>ShoppingCartIsEmptyException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public ShoppingCartIsEmptyException(String msg) {
        super(msg);
    }
}
