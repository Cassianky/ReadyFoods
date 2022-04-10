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
public class FoodDiaryRecordNotFoundException extends Exception{

    /**
     * Creates a new instance of <code>FoodDiaryRecordNotFoundException</code>
     * without detail message.
     */
    public FoodDiaryRecordNotFoundException() {
    }

    /**
     * Constructs an instance of <code>FoodDiaryRecordNotFoundException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public FoodDiaryRecordNotFoundException(String msg) {
        super(msg);
    }
}
