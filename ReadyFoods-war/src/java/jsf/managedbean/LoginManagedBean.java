/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import javax.inject.Named;
import javax.enterprise.context.Dependent;

/**
 *
 * @author ngcas
 */
@Named(value = "loginManagedBean")
@Dependent
public class LoginManagedBean {

    /**
     * Creates a new instance of LoginManagedBean
     */
    public LoginManagedBean() {
    }
    
}
