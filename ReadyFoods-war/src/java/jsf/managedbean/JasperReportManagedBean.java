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

    public JasperReportManagedBean() {
    }

    @PostConstruct
    public void postConstruct(){
        setOrder((OrderEntity)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("orderToGenerate"));
        setCustomer((Customer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentCustomer"));
    }
    public void generateReport(ActionEvent event) {
        OrderEntity order = (OrderEntity) event.getComponent().getAttributes().get("orderIdToGenerate");
        try {
            HashMap parameters = new HashMap();
            parameters.put("orderEntityId", order.getOrderEntityId());
            parameters.put("customerFirstName",customer.getFirstName());
            parameters.put("customerLastName",customer.getLastName());
            parameters.put("customerAddress",customer.getAddress());
            parameters.put("customerNumber",customer.getContactNumber());

            InputStream reportStream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/jasperreport/orderInvoice.jasper");
            OutputStream outputStream = FacesContext.getCurrentInstance().getExternalContext().getResponseOutputStream();

            JasperRunManager.runReportToPdfStream(reportStream, outputStream, parameters, readyFoodsDataSource.getConnection());
            
            
        } catch (JRException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
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
