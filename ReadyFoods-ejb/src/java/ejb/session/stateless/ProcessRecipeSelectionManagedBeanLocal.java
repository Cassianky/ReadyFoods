/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import javax.ejb.Local;

/**
 *
 * @author angler
 */
@Local
public interface ProcessRecipeSelectionManagedBeanLocal {

    public Integer process();

    public void weeklyProcess();
    
}
