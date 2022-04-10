/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.CustomerSessionBeanLocal;
import ejb.session.stateless.FoodDiarySessionBeanLocal;
import ejb.session.stateless.FoodSessionBeanLocal;
import entity.Customer;
import entity.Food;
import entity.FoodDiaryRecord;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
import util.exception.CustomerNotFoundException;
import util.exception.DeleteFoodDiaryRecordException;
import util.exception.FoodDiaryRecordNotFoundException;
import util.exception.FoodNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import wrapper.FoodWrapper;

/**
 *
 * @author ngcas
 */
@Named(value = "foodDiaryManagedBean")
@ViewScoped
public class FoodDiaryManagedBean implements Serializable {

    @EJB
    private CustomerSessionBeanLocal customerSessionBeanLocal;
    @EJB
    private FoodDiarySessionBeanLocal foodDiaryRecordSessionBeanLocal;
    @EJB
    private FoodSessionBeanLocal foodSessionBeanLocal;
    
    
    private ScheduleModel scheduleModel;
    private DefaultScheduleEvent<FoodWrapper> scheduleEvent;
    private List<Food> foods;
    private Customer currentCustomer;
    private Long currentFoodId;
    private List<FoodDiaryRecord> foodDiaryRecords;

    public FoodDiaryManagedBean() {
        scheduleModel = new DefaultScheduleModel();
        scheduleEvent = new DefaultScheduleEvent<FoodWrapper>();
    }

    @PostConstruct
    public void postConstruct() {
        try {
            scheduleEvent.setData(new FoodWrapper());

            Long currentCustomerId = ((Customer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentCustomer")).getCustomerId();
            currentCustomer = customerSessionBeanLocal.retrieveCustomerByCustomerId(currentCustomerId);
            foodDiaryRecords = currentCustomer.getFoodDiaryRecords();
            
            for(FoodDiaryRecord foodDiaryRecord : foodDiaryRecords) {
               DefaultScheduleEvent<FoodWrapper> event = DefaultScheduleEvent.builder()
                        .startDate(foodDiaryRecord.getStartDate())
                       .endDate(foodDiaryRecord.getEndDate())
                       .title(foodDiaryRecord.getTitle())
                       .description(foodDiaryRecord.getDescription())
                       .data(new FoodWrapper(foodDiaryRecord.getFoodDiaryRecordId(), foodDiaryRecord.getName(), foodDiaryRecord.getCalories(), foodDiaryRecord.getCarbs(), foodDiaryRecord.getProtein(), foodDiaryRecord.getFats(), foodDiaryRecord.getSugar()))
                       .editable(false)
                       .build();
               
               scheduleModel.addEvent(event);
            }
            
            this.foods = foodSessionBeanLocal.retrieveAllFoodsByCustomerId(getCurrentCustomer().getCustomerId());
            
        } catch (CustomerNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Customer Not Found: " + ex.getMessage(), null));
        }

    }

    public ScheduleModel getScheduleModel() {
        return scheduleModel;
    }

    public void setScheduleModel(ScheduleModel scheduleModel) {
        this.scheduleModel = scheduleModel;
    }

    public ScheduleEvent getScheduleEvent() {
        return scheduleEvent;
    }

    public void setScheduleEvent(DefaultScheduleEvent scheduleEvent) {
        this.scheduleEvent = scheduleEvent;
    }

    public void addEvent(ActionEvent actionEvent) throws CustomerNotFoundException, UnknownPersistenceException {
        try {
            if (scheduleEvent.getId() == null) {
                Food foodSelect = foodSessionBeanLocal.retrieveFoodByFoodId(currentFoodId);
                FoodWrapper dataAdded = new FoodWrapper(foodSelect.getName(), foodSelect.getCalories(), foodSelect.getCarbs(), foodSelect.getProtein(), foodSelect.getFats(), foodSelect.getSugar());
                //Creation of new Food Diary Record and save to database
                FoodDiaryRecord newFoodDiaryRecord = new FoodDiaryRecord(scheduleEvent.getTitle(), scheduleEvent.getDescription(), scheduleEvent.getStartDate(), scheduleEvent.getEndDate());
                newFoodDiaryRecord.setName(dataAdded.getName());
                newFoodDiaryRecord.setCalories(dataAdded.getCalories());
                newFoodDiaryRecord.setCarbs(dataAdded.getCarbs());
                newFoodDiaryRecord.setProtein(dataAdded.getProtein());
                newFoodDiaryRecord.setFats(dataAdded.getFats());
                newFoodDiaryRecord.setSugar(dataAdded.getSugar());
                
                Long foodDiaryRecordId = foodDiaryRecordSessionBeanLocal.createNewFoodDiaryRecord(newFoodDiaryRecord, currentCustomer.getCustomerId());
                dataAdded.setFoodDiaryRecordId(foodDiaryRecordId);
                scheduleEvent.setData(dataAdded);
                scheduleModel.addEvent(scheduleEvent);
            }

            currentFoodId = 1l;

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New Diary Record Added Successfully!", null));
        } catch (CustomerNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Customer Not Found: " + ex.getMessage(), null));
        } catch (InputDataValidationException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid inputs for diary record: " + ex.getMessage(), null));
        } catch (UnknownPersistenceException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while creating the new food diary record: " + ex.getMessage(), null));
        } catch (FoodNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Food not found!: " + ex.getMessage(), null));
        } 
    }

    public void deleteEvent(ActionEvent actionEvent) {
       try {
        if (scheduleEvent.getId() != null) {
           
            foodDiaryRecordSessionBeanLocal.deleteFoodDiaryRecordByFoodDiaryRecordId(scheduleEvent.getData().getFoodDiaryRecordId(), currentCustomer.getCustomerId());
            scheduleModel.deleteEvent(scheduleEvent);
        }
        scheduleEvent = new DefaultScheduleEvent<FoodWrapper>();
        scheduleEvent.setData(new FoodWrapper());
       } catch (FoodDiaryRecordNotFoundException ex) {
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Food Diary Record Not Found!: " + ex.getMessage(), null));
       } catch (DeleteFoodDiaryRecordException ex) {
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting the food diary record: " + ex.getMessage(), null));
       } catch (CustomerNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Customer Not Found: " + ex.getMessage(), null));
        }
    }

    public void onEventSelect(SelectEvent selectEvent) {
        scheduleEvent = (DefaultScheduleEvent) selectEvent.getObject();
        scheduleEvent.getData();
    }

    public void onDateSelect(SelectEvent selectEvent) {
        scheduleEvent = new DefaultScheduleEvent("", (LocalDateTime) selectEvent.getObject(), (LocalDateTime) selectEvent.getObject());
        scheduleEvent.setEditable(false);
        scheduleEvent.setData(new FoodWrapper());
    }

    private void addMessage(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public List<Food> getFoods() {
        return foods;
    }

    public void setFoods(List<Food> foods) {
        this.foods = foods;
    }

    public Customer getCurrentCustomer() {
        return currentCustomer;
    }

    public void setCurrentCustomer(Customer currentCustomer) {
        this.currentCustomer = currentCustomer;
    }

    public Long getCurrentFoodId() {
        return currentFoodId;
    }

    public void setCurrentFoodId(Long currentFoodId) {
        this.currentFoodId = currentFoodId;
    }
}
