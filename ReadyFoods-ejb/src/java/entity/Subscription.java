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
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author angler
 */
@Entity
public class Subscription implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subscriptionId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    @NotNull
    private Date subscriptionStartDate;

    @Column(nullable = false)
    @NotNull
    private Integer duration; 

    @Column(nullable = false)
    @NotNull
    private Integer numOfRecipes;

    @Column(nullable = false)
    @NotNull
    private Integer numOfPeople;

    @Column(nullable = false, precision = 11, scale = 2)
    @NotNull
    @DecimalMin("0.00")
    @Digits(integer = 9, fraction = 2)
    private BigDecimal weeklyPrice;

    @Column(nullable = false)
    @NotNull
    private Boolean ongoing;

    @OneToMany
    private List<OrderEntity> subscriptionOrders;

    public Subscription() {
        this.subscriptionOrders = new ArrayList<>();
    }

    public Subscription(Date subscriptionStartDate, Integer duration, 
            Integer numOfRecipes, Integer numOfPeople, BigDecimal weeklyPrice, Boolean ongoing) {
        this();
        this.subscriptionStartDate = subscriptionStartDate;
        this.duration = duration;
        this.numOfRecipes = numOfRecipes;
        this.numOfPeople = numOfPeople;
        this.weeklyPrice = weeklyPrice;
        this.ongoing = ongoing;
    }

    public Subscription(Date subscriptionStartDate, Integer duration,
            Integer numOfRecipes, Integer numOfPeople, BigDecimal weeklyPrice, Boolean ongoing, List<OrderEntity> subscriptionOrders) {
        this.subscriptionStartDate = subscriptionStartDate;
        this.duration = duration;
        this.numOfRecipes = numOfRecipes;
        this.numOfPeople = numOfPeople;
        this.weeklyPrice = weeklyPrice;
        this.ongoing = ongoing;
        this.subscriptionOrders = subscriptionOrders;
    }

    public Long getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(Long subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (subscriptionId != null ? subscriptionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the subscriptionId fields are not set
        if (!(object instanceof Subscription)) {
            return false;
        }
        Subscription other = (Subscription) object;
        if ((this.subscriptionId == null && other.subscriptionId != null) || (this.subscriptionId != null && !this.subscriptionId.equals(other.subscriptionId))) {
            return false;
        }
        return true;
        
                
    }

    @Override
    public String toString() {
        return "entity.Subscription[ id=" + subscriptionId + " ]";
    }

    /**
     * @return the subscriptionStartDate
     */
    public Date getSubscriptionStartDate() {
        return subscriptionStartDate;
    }

    /**
     * @param subscriptionStartDate the subscriptionStartDate to set
     */
    public void setSubscriptionStartDate(Date subscriptionStartDate) {
        this.subscriptionStartDate = subscriptionStartDate;
    }

    /**
     * @return the duration
     */
    public Integer getDuration() {
        return duration;
    }

    /**
     * @param duration the duration to set
     */
    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    /**
     * @return the numOfRecipes
     */
    public Integer getNumOfRecipes() {
        return numOfRecipes;
    }

    /**
     * @param numOfRecipes the numOfRecipes to set
     */
    public void setNumOfRecipes(Integer numOfRecipes) {
        this.numOfRecipes = numOfRecipes;
    }

    /**
     * @return the numOfPeople
     */
    public Integer getNumOfPeople() {
        return numOfPeople;
    }

    /**
     * @param numOfPeople the numOfPeople to set
     */
    public void setNumOfPeople(Integer numOfPeople) {
        this.numOfPeople = numOfPeople;
    }

    /**
     * @return the weeklyPrice
     */
    public BigDecimal getWeeklyPrice() {
        return weeklyPrice;
    }

    /**
     * @param weeklyPrice the weeklyPrice to set
     */
    public void setWeeklyPrice(BigDecimal weeklyPrice) {
        this.weeklyPrice = weeklyPrice;
    }

    /**
     * @return the ongoing
     */
    public Boolean getOngoing() {
        return ongoing;
    }

    /**
     * @param ongoing the ongoing to set
     */
    public void setOngoing(Boolean ongoing) {
        this.ongoing = ongoing;
    }

    /**
     * @return the subscriptionOrders
     */
    public List<OrderEntity> getSubscriptionOrders() {
        return subscriptionOrders;
    }

    /**
     * @param subscriptionOrders the subscriptionOrders to set
     */
    public void setSubscriptionOrders(List<OrderEntity> subscriptionOrders) {
        this.subscriptionOrders = subscriptionOrders;
    }

}
