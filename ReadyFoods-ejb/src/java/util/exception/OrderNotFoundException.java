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
public class OrderNotFoundException extends Exception{

    /**
     * Creates a new instance of <code>OrderNotFoundException</code> without
     * detail message.
     */
    public OrderNotFoundException() {
    }

    /**
     * Constructs an instance of <code>OrderNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public OrderNotFoundException(String msg) {
        super(msg);
    }
}
