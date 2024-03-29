package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity()
public class Enquiry implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long enquiryId;

    @Column(nullable = false)
    @Size(max = 128)
    @NotNull
    private String title;

    @Column(nullable = false)
    @Size(max = 256)
    @NotNull
    private String description;

    @Column(nullable = false)
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateOfEnquiry;

    @ManyToOne(optional = false)
    private Customer customer;

    @Column(nullable = false)
    private Boolean resolved = false; // either admin or customer can toggle this

    @Column(nullable = true)
    @Size(max = 256)
    private String response; // admin response

    public Enquiry() {
    }

    public Enquiry(String title, String description, Date dateOfEnquiry) {
        this.title = title;
        this.description = description;
        this.dateOfEnquiry = dateOfEnquiry;
       
    }

    public Enquiry(String title, String description, Date dateOfEnquiry, Boolean resolved) {
        this.title = title;
        this.description = description;
        this.dateOfEnquiry = dateOfEnquiry;

        this.resolved = resolved;
    }

    public Long getEnquiryId() {
        return enquiryId;
    }

    public void setEnquiryId(Long enquiryId) {
        this.enquiryId = enquiryId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (enquiryId != null ? enquiryId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the enquiryId fields are not set
        if (!(object instanceof Enquiry)) {
            return false;
        }
        Enquiry other = (Enquiry) object;
        if ((this.enquiryId == null && other.enquiryId != null) || (this.enquiryId != null && !this.enquiryId.equals(other.enquiryId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Enquiry[ id=" + enquiryId + " ]";
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the dateOfEnquiry
     */
    public Date getDateOfEnquiry() {
        return dateOfEnquiry;
    }

    /**
     * @param dateOfEnquiry the dateOfEnquiry to set
     */
    public void setDateOfEnquiry(Date dateOfEnquiry) {
        this.dateOfEnquiry = dateOfEnquiry;
    }

    /**
     * @return the customer
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * @param customer the customer to set
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
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
