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
public class CreateNewOrderException extends Exception{

    /**
     * Creates a new instance of <code>CreateNewOrderException</code> without
     * detail message.
     */
    public CreateNewOrderException() {
    }

    /**
     * Constructs an instance of <code>CreateNewOrderException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public CreateNewOrderException(String msg) {
        super(msg);
    }
}
