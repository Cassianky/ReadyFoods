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

/**
 *
 * @author angler
 */
@Local
public interface EnquirySessionBeanLocal {

    public Enquiry createNewEnquiry(Long customerId, Enquiry newEnquiry) throws CustomerNotFoundException, CreateNewEnquiryException;

    public List<Enquiry> retrieveAllEnquires();

    public Enquiry retrieveEnquiryByEnquiryId(Long enId) throws EnquiryNotFoundException;
    
}
