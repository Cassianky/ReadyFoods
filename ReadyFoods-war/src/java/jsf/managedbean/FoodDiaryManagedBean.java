package jsf.managedbean;

import ejb.session.stateless.FoodDiaryRecordSessionBeanLocal;
import ejb.session.stateless.FoodSessionBeanLocal;
import entity.Customer;
import entity.Food;
import entity.FoodDiaryRecord;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import wrapper.FoodCopy;

@Named(value = "foodDiaryManagedBean")
@ViewScoped
public class FoodDiaryManagedBean implements Serializable {

    @EJB
    private FoodDiaryRecordSessionBeanLocal foodDiaryRecordSessionBeanLocal;
    @EJB
    private FoodSessionBeanLocal foodSessionBeanLocal;

    private ScheduleModel scheduleModel;
    private DefaultScheduleEvent<FoodCopy> scheduleEvent;
    private List<Food> foods;
    private List<FoodCopy> foodCopies;
    private Customer currentCustomer;

    public FoodDiaryManagedBean() {
        scheduleModel = new DefaultScheduleModel();
        scheduleEvent = new DefaultScheduleEvent<FoodCopy>();
        
        foods = new ArrayList<>();
        foodCopies = new ArrayList<>();
    }

    @PostConstruct
    public void postConstruct() {
        scheduleEvent.setData(new FoodCopy());
        scheduleModel.addEvent(new DefaultScheduleEvent("Champions League Match", previousDay8Pm(), previousDay11Pm()));
        try {
            setCurrentCustomer((Customer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentCustomer"));
            this.foods = foodSessionBeanLocal.retrieveAllFoodsByCustomerId(getCurrentCustomer().getCustomerId());

            for (int i = 0; i < this.foods.size(); i++) {
                getFoodCopies().add(new FoodCopy(foods.get(i).getName()));
            }

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

                scheduleEvent.setData(new FoodCopy());
                scheduleModel.addEvent(scheduleEvent);
                FoodDiaryRecord newFoodDiaryRecord = new FoodDiaryRecord();
                newFoodDiaryRecord.setTitle(scheduleEvent.getTitle());
                newFoodDiaryRecord.setDescription(scheduleEvent.getDescription());
                newFoodDiaryRecord.setStartDate(scheduleEvent.getStartDate());
                newFoodDiaryRecord.setEndDate(scheduleEvent.getEndDate());
                newFoodDiaryRecord.setFoodCopy((FoodCopy) scheduleEvent.getData());
                foodDiaryRecordSessionBeanLocal.createNewFoodDiaryRecord(newFoodDiaryRecord, currentCustomer.getCustomerId());
            }
            scheduleEvent = new DefaultScheduleEvent<FoodCopy>();

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New Diary Record Added Successfully!", null));
        } catch (CustomerNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Customer Not Found: " + ex.getMessage(), null));
        } catch (InputDataValidationException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid inputs for diary record: " + ex.getMessage(), null));
        } catch (UnknownPersistenceException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while creating the new diary record: " + ex.getMessage(), null));
        }
    }

    public void deleteEvent(ActionEvent actionEvent) {
        if (scheduleEvent.getId() != null) {
            scheduleModel.deleteEvent(scheduleEvent);
        }
        scheduleEvent = new DefaultScheduleEvent<FoodCopy>();
        scheduleEvent.setData(new FoodCopy());
    }

    public void onEventSelect(SelectEvent selectEvent) {
        scheduleEvent = (DefaultScheduleEvent) selectEvent.getObject();
        scheduleEvent.getData();
    }

    public void onDateSelect(SelectEvent selectEvent) {
        scheduleEvent = new DefaultScheduleEvent("", (LocalDateTime) selectEvent.getObject(), (LocalDateTime) selectEvent.getObject());
    }

    public void onEventMove(ScheduleEntryMoveEvent scheduleEvent) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event moved", "Day delta:" + scheduleEvent.getDayDelta() + ", Minute delta:" + scheduleEvent.getMinuteDelta());

        addMessage(message);
    }

    public void onEventResize(ScheduleEntryResizeEvent scheduleEvent) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event resized", "Day delta:" + scheduleEvent.getDayDeltaStart() + " to " + scheduleEvent.getDayDeltaEnd() + ", Minute delta:" + scheduleEvent.getMinuteDeltaStart() + " to " + scheduleEvent.getMinuteDeltaEnd());

        addMessage(message);
    }

    private void addMessage(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    private Calendar today() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 0, 0, 0);

        return calendar;
    }

    private LocalDateTime previousDay8Pm() {
        Calendar t = (Calendar) today().clone();
        t.set(Calendar.AM_PM, Calendar.PM);
        t.set(Calendar.DATE, t.get(Calendar.DATE) - 1);
        t.set(Calendar.HOUR, 8);

        return LocalDateTime.ofInstant(t.toInstant(), t.getTimeZone().toZoneId());
    }

    private LocalDateTime previousDay11Pm() {
        Calendar t = (Calendar) today().clone();
        t.set(Calendar.AM_PM, Calendar.PM);
        t.set(Calendar.DATE, t.get(Calendar.DATE) - 1);
        t.set(Calendar.HOUR, 11);

        return LocalDateTime.ofInstant(t.toInstant(), t.getTimeZone().toZoneId());
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

    /**
     * @return the foodCopies
     */
    public List<FoodCopy> getFoodCopies() {
        return foodCopies;
    }

    /**
     * @param foodCopies the foodCopies to set
     */
    public void setFoodCopies(List<FoodCopy> foodCopies) {
        this.foodCopies = foodCopies;
    }
}

