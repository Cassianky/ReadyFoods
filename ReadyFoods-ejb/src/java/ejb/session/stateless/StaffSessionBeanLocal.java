/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Staff;
import javax.ejb.Local;
import util.exception.InputDataValidationException;
import util.exception.StaffUsernameExistException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author angler
 */
@Local
public interface StaffSessionBeanLocal {

    public Long createNewStaff(Staff newStaff) throws StaffUsernameExistException, UnknownPersistenceException, InputDataValidationException;
    
}
