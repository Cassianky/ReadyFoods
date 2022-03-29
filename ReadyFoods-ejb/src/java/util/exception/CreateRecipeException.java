/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author Eugene Chua
 */
public class CreateRecipeException extends Exception {

    public CreateRecipeException() {
    }

    public CreateRecipeException(String msg) {
        super(msg);
    }
}
