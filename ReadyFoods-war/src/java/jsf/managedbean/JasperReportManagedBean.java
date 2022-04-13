/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.Customer;
import entity.OrderEntity;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.HashMap;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.sql.DataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
/**
 *
 * @author PYT
 */
@Named(value = "jasperReportManagedBean")
@RequestScoped
public class JasperReportManagedBean {

    @Resource(name = "readyFoodsDataSource")
    private DataSource readyFoodsDataSource;

    private OrderEntity order;

    private Customer customer;

    @Inject
    private ShoppingCartManagedBean shoppingCartManagedBean;

    public JasperReportManagedBean() {

    }

    @PostConstruct
    public void postConstruct() {
        order = shoppingCartManagedBean.getOrderToGenerate();
        FacesContext ctx = FacesContext.getCurrentInstance();
        customer = (Customer) ctx.getExternalContext().getSessionMap().get("currentCustomer");
        ctx.responseComplete();
        System.out.println("jsf.managedbean.JasperReportManagedBean.postConstruct()" + customer.getUserName());
    }

    public void generateReport(ActionEvent event) {

        try {
            HashMap parameters = new HashMap();
            System.out.print("((((((((((((((((((((((((" + order.getOrderEntityId());
            parameters.put("orderEntityId", order.getOrderEntityId());
           parameters.put("customerFirstName", customer.getFirstName());
            parameters.put("customerLastName", customer.getLastName());
           parameters.put("customerAddress", customer.getAddress());
           parameters.put("customerNumber", customer.getContactNumber());
           
           System.out.println(readyFoodsDataSource.getConnection());
            InputStream reportStream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/jasperreport/orderInvoice.jasper");
            OutputStream outputStream = FacesContext.getCurrentInstance().getExternalContext().getResponseOutputStream();
            JasperRunManager.runReportToPdfStream(reportStream, outputStream, parameters, readyFoodsDataSource.getConnection());
            
            
        } catch (JRException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @return the order
     */
    public OrderEntity getOrder() {
        return order;
    }

    /**
     * @param order the order to set
     */
    public void setOrder(OrderEntity order) {
        this.order = order;
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

}
