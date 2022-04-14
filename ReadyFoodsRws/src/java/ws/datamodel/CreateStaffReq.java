/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.datamodel;

import entity.Staff;

/**
 *
 * @author PYT
 */
public class CreateStaffReq {
  
    private String username;
    private String password;
    private Staff newStaff;

    public CreateStaffReq() {
    }

    public CreateStaffReq(String username, String password, Staff newStaff) {
        this.username = username;
        this.password = password;
        this.newStaff = newStaff;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the newStaff
     */
    public Staff getNewStaff() {
        return newStaff;
    }

    /**
     * @param newStaff the newStaff to set
     */
    public void setNewStaff(Staff newStaff) {
        this.newStaff = newStaff;
    }

}
