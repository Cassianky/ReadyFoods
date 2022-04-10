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
public class CreateCommentException extends Exception{

    /**
     * Creates a new instance of <code>CreateCommentException</code> without
     * detail message.
     */
    public CreateCommentException() {
    }

    /**
     * Constructs an instance of <code>CreateCommentException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public CreateCommentException(String msg) {
        super(msg);
    }
}
