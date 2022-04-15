package ws.datamodel;

import entity.Category;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ngcas
 */
public class UpdateCategoryReq {
    private String username;
    private String password;
    private Category category;

    public UpdateCategoryReq() {
    }

    public UpdateCategoryReq(String username, String password, Category category) {
        this.username = username;
        this.password = password;
        this.category = category;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @return the category
     */
    public Category getCategory() {
        return category;
    }
    
}
