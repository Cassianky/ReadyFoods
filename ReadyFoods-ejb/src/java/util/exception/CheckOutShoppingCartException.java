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
public class CheckOutShoppingCartException extends Exception{

    /**
     * Creates a new instance of <code>CheckOutShoppingCartException</code>
     * without detail message.
     */
    public CheckOutShoppingCartException() {
    }

    /**
     * Constructs an instance of <code>CheckOutShoppingCartException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public CheckOutShoppingCartException(String msg) {
        super(msg);
    }
}
