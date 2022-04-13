/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import util.enumeration.StaffType;
import util.security.CryptographicHelper;

/**
 *
 * @author angler
 */
@Entity
public class Staff implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long staffId;

    @Column(nullable = false, length = 32)
    @NotNull
    @Size(max = 32)
    private String firstName;

    @Column(nullable = false, length = 32)
    @NotNull
    @Size(max = 32)
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private StaffType staffType;

    @Column(nullable = false, unique = true, length = 32)
    @NotNull
    @Size(min = 4, max = 32)
    private String username;

    @Column(columnDefinition = "CHAR(32) NOT NULL")
    @NotNull
    private String password;

    @Column(columnDefinition = "CHAR(32) NOT NULL")
    private String salt;

    public Staff() {
        // Newly added in v4.5
        this.salt = CryptographicHelper.getInstance().generateRandomString(32);
    }

    public Staff(String firstName, String lastName, StaffType staffType, String username, String password) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
        this.staffType = staffType;
        this.username = username;
        setPassword(password);
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    // Updated in v4.5
    public void setPassword(String password) {
        if (password != null) {
            this.password = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(password + this.salt));
        } else {
            this.password = null;
        }
    }

    // Newly added in v4.5
    public String getSalt() {
        return salt;
    }

    // Newly added in v4.5
    public void setSalt(String salt) {
        this.salt = salt;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (staffId != null ? staffId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the staffId fields are not set
        if (!(object instanceof Staff)) {
            return false;
        }
        Staff other = (Staff) object;
        if ((this.staffId == null && other.staffId != null) || (this.staffId != null && !this.staffId.equals(other.staffId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Employee[ id=" + staffId + " ]";
    }

    /**
     * @return the staffType
     */
    public StaffType getStaffType() {
        return staffType;
    }

    /**
     * @param staffType the staffType to set
     */
    public void setStaffType(StaffType staffType) {
        this.staffType = staffType;
    }

}
