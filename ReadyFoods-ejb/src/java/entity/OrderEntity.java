/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import util.enumeration.Status;

/**
 *
 * @author ngcas
 */
@Entity
public class OrderEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderEntityId;
    
    @Column(nullable = false)
    @NotNull
    @Min(1)
    private Integer numPax;
    
    @Column(nullable = false, precision = 11, scale = 2)
    @NotNull
    @DecimalMin("0.00")
    @Digits(integer = 9, fraction = 2) // 11 - 2 digits to the left of the decimal point
    private BigDecimal totalCost;
    
    @Column(nullable = false)
    @NotNull
    private Boolean paid;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    @NotNull
    private Date dateOfOrder;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = true)
    private Date dateForDelivery;
    
    @Column(nullable = false)
    @NotNull
    private Status status;
    
    @Size(max = 128)
    private String additionalNotes;
    
    @OneToMany
    private List<OrderLineItem> orderLineItems;
    
    @ManyToOne(optional = true)
    private Customer customer;

    public OrderEntity() {
        this.orderLineItems = new ArrayList<>();
    }

    public OrderEntity(Integer numPax, BigDecimal totalCost, Boolean paid, Date dateOfOrder, Status status, List<OrderLineItem> orderLineItems, Customer customer) {
        this.numPax = numPax;
        this.totalCost = totalCost;
        this.paid = paid;
        this.dateOfOrder = dateOfOrder;
        this.status = status;
        this.orderLineItems = orderLineItems;
        this.customer = customer;
    }
    
    public Long getOrderEntityId() {
        return orderEntityId;
    }

    public void setOrderEntityId(Long orderEntityId) {
        this.orderEntityId = orderEntityId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (orderEntityId != null ? orderEntityId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the orderId fields are not set
        if (!(object instanceof OrderEntity)) {
            return false;
        }
        OrderEntity other = (OrderEntity) object;
        if ((this.orderEntityId == null && other.orderEntityId != null) || (this.orderEntityId != null && !this.orderEntityId.equals(other.orderEntityId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Order[ id=" + orderEntityId + " ]";
    }

    /**
     * @return the numPax
     */
    public Integer getNumPax() {
        return numPax;
    }

    /**
     * @param numPax the numPax to set
     */
    public void setNumPax(Integer numPax) {
        this.numPax = numPax;
    }

    /**
     * @return the totalCost
     */
    public BigDecimal getTotalCost() {
        return totalCost;
    }

    /**
     * @param totalCost the totalCost to set
     */
    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    /**
     * @return the paid
     */
    public Boolean getPaid() {
        return paid;
    }

    /**
     * @param paid the paid to set
     */
    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    /**
     * @return the dateOfOrder
     */
    public Date getDateOfOrder() {
        return dateOfOrder;
    }

    /**
     * @param dateOfOrder the dateOfOrder to set
     */
    public void setDateOfOrder(Date dateOfOrder) {
        this.dateOfOrder = dateOfOrder;
    }

    /**
     * @return the dateForDelivery
     */
    public Date getDateForDelivery() {
        return dateForDelivery;
    }

    /**
     * @param dateForDelivery the dateForDelivery to set
     */
    public void setDateForDelivery(Date dateForDelivery) {
        this.dateForDelivery = dateForDelivery;
    }


    /**
     * @return the additionalNotes
     */
    public String getAdditionalNotes() {
        return additionalNotes;
    }

    /**
     * @param additionalNotes the additionalNotes to set
     */
    public void setAdditionalNotes(String additionalNotes) {
        this.additionalNotes = additionalNotes;
    }

    /**
     * @return the orderLineItems
     */
    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    /**
     * @param orderLineItems the orderLineItems to set
     */
    public void setOrderLineItems(List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
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
     * @return the status
     */
    public Status getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(Status status) {
        this.status = status;
    }
    
    
}
