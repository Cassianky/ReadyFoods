/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import util.enumeration.PreparationMethod;

/**
 *
 * @author PYT
 */
@Entity
public class ConfirmationEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    private OrderEntity orderEntity;
    
    

    
    
    public ConfirmationEntity() {
    }
  
    public ConfirmationEntity(OrderEntity orderEntity) {
        this.orderEntity = orderEntity;
    }
   
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ConfirmationEntity)) {
            return false;
        }
        ConfirmationEntity other = (ConfirmationEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ConfirmationEntity[ id=" + id + " ]";
    }

    /**
     * @return the orderEntity
     */
    public OrderEntity getOrderEntity() {
        return orderEntity;
    }

    /**
     * @param orderEntity the orderEntity to set
     */
    public void setOrderEntity(OrderEntity orderEntity) {
        this.orderEntity = orderEntity;
    }
    
}
