/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import entity.Enquiry;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.CreateNewEnquiryException;
import util.exception.CustomerNotFoundException;
import util.exception.EnquiryNotFoundException;

/**
 *
 * @author angler
 */
@Stateless
public class EnquirySessionBean implements EnquirySessionBeanLocal {

    @EJB(name = "CustomerSessionBeanLocal")
    private CustomerSessionBeanLocal customerSessionBeanLocal;

    @PersistenceContext(unitName = "ReadyFoods-ejbPU")
    private EntityManager em;

    @Override
    public Enquiry createNewEnquiry(Long customerId, Enquiry newEnquiry)
            throws CustomerNotFoundException, CreateNewEnquiryException {

        if (newEnquiry != null) {

            Customer customer = customerSessionBeanLocal.retrieveCustomerByCustomerId(customerId);

            customer.getEnquiries().add(newEnquiry);

            em.persist(newEnquiry);

            em.flush();

            return newEnquiry;

        } else {
            throw new CreateNewEnquiryException("Enquiry information not provided");
        }
    }

    @Override
    public List<Enquiry> retrieveAllEnquires() {
        Query query = em.createQuery("SELECT e FROM Enquiry e");

        return query.getResultList();
    }

    @Override
    public Enquiry retrieveEnquiryByEnquiryId(Long enId) throws EnquiryNotFoundException {
        Enquiry enquiry = em.find(Enquiry.class, enId);

        if (enquiry != null) {
            return enquiry;
        } else {
            throw new EnquiryNotFoundException("Enquiry ID " + enId + " does not exist!");
        }
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
