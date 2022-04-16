/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.datamodel;

import entity.OrderEntity;
import util.enumeration.Status;

/**
 *
 * @author angler
 */
public class UpdateOrderReq {

    private String username;
    private String password;
    private OrderEntity order;
    private Status status;

    public UpdateOrderReq() {
    }

    public UpdateOrderReq(String username, String password, OrderEntity order, Status status) {
        this.username = username;
        this.password = password;
        this.order = order;
        this.status = status;
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
     * @return the order
     */
    public OrderEntity getOrder() {
        return order;
    }

    /**
     * @param order the order to set
     */
    public void setOrder(OrderEntity order) {
        this.order = order;
    }

    /**
     * @return the status
     */
    public Status getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(Status status) {
        this.status = status;
    }
    
    

}
