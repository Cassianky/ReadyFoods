/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CreditCard;
import javax.ejb.Local;
import util.exception.CreditCardNotFoundException;
import util.exception.CustomerNotFoundException;
import util.exception.DeleteCreditCardException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author ngcas
 */
@Local
public interface CreditCardSessionBeanLocal {

    public Long createNewCreditCard(CreditCard newCreditCard, Long customerId) throws UnknownPersistenceException, InputDataValidationException, CustomerNotFoundException;

    public void deleteCreditCardByCreditCardId(Long creditCardId, Long customerId) throws CreditCardNotFoundException, DeleteCreditCardException, CustomerNotFoundException;

    public CreditCard retrieveCreditCardByCreditCardId(Long creditCardId) throws CreditCardNotFoundException;
    
}
