/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Staff;
import java.util.List;
import javax.ejb.Local;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.StaffNotFoundException;
import util.exception.StaffUsernameExistException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author angler
 */
@Local
public interface StaffSessionBeanLocal {

    public Long createNewStaff(Staff newStaff) throws StaffUsernameExistException, UnknownPersistenceException, InputDataValidationException;

    public List<Staff> retrieveAllStaffs();

    public Staff retrieveStaffByStaffId(Long staffId) throws StaffNotFoundException;

    public Staff retrieveStaffByUsername(String username) throws StaffNotFoundException;

    public Staff staffLogin(String username, String password) throws InvalidLoginCredentialException;

    public void deleteStaff(Long staffId) throws StaffNotFoundException;
    
}
