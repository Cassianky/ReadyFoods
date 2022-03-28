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
public class DeleteFoodException extends Exception{

    /**
     * Creates a new instance of <code>DeleteFoodException</code> without detail
     * message.
     */
    public DeleteFoodException() {
    }

    /**
     * Constructs an instance of <code>DeleteFoodException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public DeleteFoodException(String msg) {
        super(msg);
    }
}
