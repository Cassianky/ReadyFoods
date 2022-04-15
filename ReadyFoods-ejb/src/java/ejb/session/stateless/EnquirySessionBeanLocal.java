/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Enquiry;
import java.util.List;
import javax.ejb.Local;
import util.exception.CreateNewEnquiryException;
import util.exception.CustomerNotFoundException;
import util.exception.EnquiryNotFoundException;
import util.exception.EnquiryResolveException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author angler
 */
@Local
public interface EnquirySessionBeanLocal {


    public List<Enquiry> retrieveAllEnquires();

    public Enquiry retrieveEnquiryByEnquiryId(Long enId) throws EnquiryNotFoundException;

    public Enquiry createNewEnquiry(Long customerId, Enquiry newEnquiry) throws CustomerNotFoundException, CreateNewEnquiryException, InputDataValidationException, UnknownPersistenceException;

    public void deleteEnquiry(Long enquiryId) throws EnquiryNotFoundException;

    public void resolveEnquiry(Long enquiryId) throws EnquiryNotFoundException, EnquiryResolveException;

    public void updateEnquiry(Long enquiryId, String response, Boolean resolved) throws EnquiryNotFoundException;
    
}
