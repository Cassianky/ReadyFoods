/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.FoodDiaryRecord;
import javax.ejb.Local;
import util.exception.CustomerNotFoundException;
import util.exception.DeleteFoodDiaryRecordException;
import util.exception.FoodDiaryRecordNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author ngcas
 */
@Local
public interface FoodDiaryRecordSessionBeanLocal {

    public void deleteFoodDiaryRecordByFoodDiaryRecordId(Long foodDiaryRecordId, Long customerId) throws FoodDiaryRecordNotFoundException, DeleteFoodDiaryRecordException, CustomerNotFoundException;

    public FoodDiaryRecord retrieveFoodDiaryRecordByFoodDiaryRecordId(Long foodDiaryRecordId) throws FoodDiaryRecordNotFoundException;

    public Long createNewFoodDiaryRecord(FoodDiaryRecord newFoodDiaryRecord, Long customerId) throws CustomerNotFoundException, UnknownPersistenceException, InputDataValidationException;
    
}
