package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class CreditCard implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long creditCardId;
    @Column(nullable = false)
    @NotNull
    @Min(13) //Researched and found ccNumber can be as long as 19 digits and as short as 13 digits
    @Max(19)
    private Integer ccNumber;
    @Column(nullable = false)
    @NotNull
    //CVV can only have 3 numbers
    @Min(100)
    @Max(999)
    private Integer CVV;
    @Column(nullable = false, length = 64)
    @NotNull
    @Size(min=5, max = 64)
    private String nameOnCard;
    @Column(nullable = false)
    @NotNull
    @Size(min=7, max = 7)
    //Sample expiryDate: 10/2023 (length = 7)
    private String expiryDate;

    public CreditCard() {
    }

    public CreditCard(Integer ccNumber, Integer CVV, String nameOnCard, String expiryDate) {
        this.ccNumber = ccNumber;
        this.CVV = CVV;
        this.nameOnCard = nameOnCard;
        this.expiryDate = expiryDate;
    }

    public Long getCreditCardId() {
        return creditCardId;
    }

    public void setCreditCardId(Long creditCardId) {
        this.creditCardId = creditCardId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (creditCardId != null ? creditCardId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the creditCardId fields are not set
        if (!(object instanceof CreditCard)) {
            return false;
        }
        CreditCard other = (CreditCard) object;
        if ((this.creditCardId == null && other.creditCardId != null) || (this.creditCardId != null && !this.creditCardId.equals(other.creditCardId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CreditCard[ id=" + creditCardId + " ]";
    }

    public Integer getCcNumber() {
        return ccNumber;
    }

    public void setCcNumber(Integer ccNumber) {
        this.ccNumber = ccNumber;
    }

    public Integer getCVV() {
        return CVV;
    }

    public void setCVV(Integer CVV) {
        this.CVV = CVV;
    }

    public String getNameOnCard() {
        return nameOnCard;
    }

    public void setNameOnCard(String nameOnCard) {
        this.nameOnCard = nameOnCard;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }
    
}
