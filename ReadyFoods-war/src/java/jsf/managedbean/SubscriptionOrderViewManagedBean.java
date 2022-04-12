/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.OrderEntitySessionBeanLocal;
import ejb.session.stateless.SubscriptionSessionBeanLocal;
import entity.OrderEntity;
import entity.Subscription;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import util.enumeration.Status;
import util.exception.OrderNotFoundException;
import util.exception.SubscriptionNotFoundException;

/**
 *
 * @author angler
 */
@Named(value = "subscriptionOrderViewManagedBean")
@ViewScoped
public class SubscriptionOrderViewManagedBean implements Serializable {

    @EJB(name = "OrderEntitySessionBeanLocal")
    private OrderEntitySessionBeanLocal orderEntitySessionBeanLocal;

    @EJB(name = "SubscriptionSessionBeanLocal")
    private SubscriptionSessionBeanLocal subscriptionSessionBeanLocal;

    /**
     * Creates a new instance of SubscriptionOrderViewManagedBean
     */
    private Long subId;
    private Subscription subscription;

    private List<OrderEntity> orders;

    private ZoneId TZ = ZoneId.of("Asia/Singapore");

    public SubscriptionOrderViewManagedBean() {
    }

    @PostConstruct
    public void postConstruct() {
        try {

            subId = (Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("subIdToView");

            subscription = subscriptionSessionBeanLocal.retrieveSubscriptionBySubscriptionId(subId);

            setOrders(subscription.getSubscriptionOrders());

        } catch (NumberFormatException ex) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "ID not provided", null));
        } catch (SubscriptionNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "An error has occurred while retrieving the subscription record: " + ex.getMessage(), null));
        }
    }

    public LocalDate getMinDate(Date date) {
        LocalDate today = date.toInstant().atZone(TZ).toLocalDate();
        LocalDate monday = today;
        while (monday.getDayOfWeek() != DayOfWeek.MONDAY) {
            monday = monday.minusDays(1);
        }

        return monday;

        //return Date.from(monday.atStartOfDay(TZ).toInstant());
    }

    public LocalDate getMaxDate(Date date) {
        LocalDate today = date.toInstant().atZone(TZ).toLocalDate();

        LocalDate sunday = today;
        while (sunday.getDayOfWeek() != DayOfWeek.SUNDAY) {
            sunday = sunday.plusDays(1);
        }
        return sunday;

        //return Date.from(sunday.atStartOfDay(TZ).toInstant());
    }

    public String getWeek(Date orderDate) {
        LocalDate monday = getMinDate(orderDate);
        LocalDate sunday = getMaxDate(orderDate);

        return monday.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG))
                + " - " + sunday.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG));

    }

    public void updateOrderStatus(ActionEvent event) {
        try {
            Long id = (Long) event.getComponent().getAttributes().get("idToUpdate");
 

            orderEntitySessionBeanLocal.updateOrderStatusReceieved(id);
            subscription = subscriptionSessionBeanLocal.retrieveSubscriptionBySubscriptionId(subscription.getSubscriptionId());

            setOrders(subscription.getSubscriptionOrders());
            
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Updated status!", null));
        } catch (OrderNotFoundException | SubscriptionNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "An error has occurred while retrieving the subscription record: " + ex.getMessage(), null));
        }

    }

    public boolean canUpdateOrderStatus(OrderEntity order) {
        return order.getStatus() == Status.PROCESSED;

    }

    /**
     * @return the subscription
     */
    public Subscription getSubscription() {
        return subscription;
    }

    /**
     * @param subscription the subscription to set
     */
    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    /**
     * @return the orders
     */
    public List<OrderEntity> getOrders() {
        return orders;
    }

    /**
     * @param orders the orders to set
     */
    public void setOrders(List<OrderEntity> orders) {
        this.orders = orders;
    }

}
