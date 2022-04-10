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
public class DeleteFoodDiaryRecordException extends Exception{

    /**
     * Creates a new instance of <code>DeleteFoodDiaryRecordException</code>
     * without detail message.
     */
    public DeleteFoodDiaryRecordException() {
    }

    /**
     * Constructs an instance of <code>DeleteFoodDiaryRecordException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public DeleteFoodDiaryRecordException(String msg) {
        super(msg);
    }
}
