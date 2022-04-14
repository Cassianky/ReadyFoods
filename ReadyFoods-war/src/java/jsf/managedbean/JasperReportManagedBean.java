/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.CustomerSessionBeanLocal;
import entity.Customer;
import entity.OrderEntity;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.concurrent.Future;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.sql.DataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperRunManager;

/**
 *
 * @author PYT
 */
@Named(value = "jasperReportManagedBean")
@RequestScoped
public class JasperReportManagedBean {

    @EJB(name = "CustomerSessionBeanLocal")
    private CustomerSessionBeanLocal customerSessionBeanLocal;

    @Resource(name = "readyFoodsDataSource")
    private DataSource readyFoodsDataSource;

    private OrderEntity order;

    private Customer customer;

    
    @Inject
    private ShoppingCartManagedBean shoppingCartManagedBean;

    private String path;

    public JasperReportManagedBean() {

    }

    @PostConstruct
    public void postConstruct() {
        order = shoppingCartManagedBean.getOrderToGenerate();
        FacesContext ctx = FacesContext.getCurrentInstance();
        customer = (Customer) ctx.getExternalContext().getSessionMap().get("currentCustomer");
        path = ctx.getExternalContext().getInitParameter("ORDER_INVOICE_PATH");
        System.out.println("jsf.managedbean.JasperReportManagedBean.postConstruct()" + customer.getUserName());
    }

    public void generateReport() {

        try {
            HashMap parameters = new HashMap();
            parameters.put("ORDERENTITYID", order.getOrderEntityId());
            parameters.put("customerFirstName", order.getCustomer().getFirstName());
            parameters.put("IMAGE_PATH", "http://localhost:8080/ReadyFoods-war/jasperreport/jasper.png");
            parameters.put("customerLastName", order.getCustomer().getLastName());
            parameters.put("customerAddress", order.getCustomer().getAddress());
            parameters.put("customerNumber", order.getCustomer().getContactNumber());

            System.out.println(readyFoodsDataSource.getConnection());
            InputStream reportStream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/jasperreport/orderInvoice.jasper");
            //JasperRunManager.runReportToPdfStream(reportStream, outputStream, parameters, readyFoodsDataSource.getConnection());
            String newFilePath = FacesContext.getCurrentInstance().getExternalContext().getInitParameter("alternatedocroot_1")
                    + System.getProperty("file.separator") + "reports"
                    + System.getProperty("file.separator") + customer.getEmail() + order.getOrderEntityId().toString() + ".pdf";
            JasperPrint jasperPrint = JasperFillManager.fillReport(reportStream, parameters, readyFoodsDataSource.getConnection());
            JasperExportManager.exportReportToPdfFile(jasperPrint, newFilePath);
            
            Future<Boolean> asyncResult = customerSessionBeanLocal.sendOrderInvoiceEmail(customer.getFirstName(), customer.getEmail(), newFilePath);
            System.out.println("" + newFilePath);
        } catch (JRException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
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

    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }

}
