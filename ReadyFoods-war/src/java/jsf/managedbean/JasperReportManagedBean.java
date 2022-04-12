/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.OrderEntity;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.HashMap;
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
    
    

    public JasperReportManagedBean() {
    }

    public void generateReport(ActionEvent event) {
        OrderEntity order = (OrderEntity) event.getComponent().getAttributes().get("orderIdToGenerate");
        try {
            HashMap parameters = new HashMap();
            parameters.put("orderId", order.getOrderEntityId());

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

}
