/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.datamodel;

import entity.Enquiry;

/**
 *
 * @author angler
 */
public class UpdateEnquiryReq {

    private String username;
    private String password;
    private Enquiry enquiry;
    private String response;
    private Boolean resolved;

    public UpdateEnquiryReq() {
    }
    
    

    public UpdateEnquiryReq(String username, String password,
            Enquiry enquiry, String response, Boolean resolved) {
        this.username = username;
        this.password = password;
        this.enquiry = enquiry;
        this.response = response;
        this.resolved = resolved;
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
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the enquiry
     */
    public Enquiry getEnquiry() {
        return enquiry;
    }

    /**
     * @param enquiry the enquiry to set
     */
    public void setEnquiry(Enquiry enquiry) {
        this.enquiry = enquiry;
    }

    /**
     * @return the response
     */
    public String getResponse() {
        return response;
    }

    /**
     * @param response the response to set
     */
    public void setResponse(String response) {
        this.response = response;
    }

    /**
     * @return the resolved
     */
    public Boolean getResolved() {
        return resolved;
    }

    /**
     * @param resolved the resolved to set
     */
    public void setResolved(Boolean resolved) {
        this.resolved = resolved;
    }

}
