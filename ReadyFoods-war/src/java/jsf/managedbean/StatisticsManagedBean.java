/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.CustomerSessionBeanLocal;
import entity.Customer;
import entity.FoodDiaryRecord;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import static java.time.DayOfWeek.MONDAY;
import java.time.LocalDate;
import static java.time.temporal.TemporalAdjusters.previousOrSame;
import java.util.HashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import util.exception.CustomerNotFoundException;
import wrapper.FoodWrapper;

/**
 *
 * @author ngcas
 */
@Named(value = "statisticsManagedBean")
@ViewScoped
public class StatisticsManagedBean implements Serializable {

    @EJB
    private CustomerSessionBeanLocal customerSessionBeanLocal;
    private Customer currentCustomer;
    private BarChartModel weeklyCaloriesModel;
    private BarChartModel weeklyMacroModel;
    private HashMap<String, Double> macro;
    private List<FoodDiaryRecord> topCaloriesMeals;
    private List<FoodDiaryRecord> topCarbsMeals;
    private List<FoodDiaryRecord> topSugarMeals;
    private List<FoodDiaryRecord> topFatsMeals;
    private List<FoodDiaryRecord> topProteinMeals;
    private FoodWrapper goals;
    private LocalDate todayDate;
    private LocalDate lastMonDate;
    private FoodWrapper consumed;
    private FoodWrapper weekConsumption;

    public StatisticsManagedBean() {
        weeklyCaloriesModel = new BarChartModel();
        weeklyMacroModel = new BarChartModel();
    }

    @PostConstruct
    public void postConstruct() {
        try {
            //Today's date
            todayDate = LocalDate.now();  
            //Last or nearest Monday's date
            lastMonDate = todayDate.with(previousOrSame(MONDAY));
           
            //initialise current customer, macro, goals
            Long currentCustomerId = ((Customer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentCustomer")).getCustomerId();
            setCurrentCustomer(customerSessionBeanLocal.retrieveCustomerByCustomerId(currentCustomerId));
            setMacro(customerSessionBeanLocal.customerMacroGoals(currentCustomerId));
            
            //initialising top meals for each macronutrient
            topCaloriesMeals = customerSessionBeanLocal.topFoodForEachMacro(currentCustomerId, "calories"); 
            topCarbsMeals = customerSessionBeanLocal.topFoodForEachMacro(currentCustomerId, "carbs");
            topFatsMeals = customerSessionBeanLocal.topFoodForEachMacro(currentCustomerId, "fats");
            topProteinMeals = customerSessionBeanLocal.topFoodForEachMacro(currentCustomerId, "protein");
            topSugarMeals = customerSessionBeanLocal.topFoodForEachMacro(currentCustomerId, "sugar");
            
            goals = new FoodWrapper(macro.get("calories"), macro.get("carbs"), macro.get("protein"), macro.get("fats"), macro.get("sugar"));
            consumed = customerSessionBeanLocal.customerConsumedMacro(currentCustomerId);
            weekConsumption = customerSessionBeanLocal.customerConsumedMacroForTheWeek(currentCustomerId);
            
            ChartSeries goalCalories = new ChartSeries();
            goalCalories.setLabel("Weekly Calories Goal");
            goalCalories.set("Calories", macro.get("calories") * 7);
            ChartSeries achievedCalories = new ChartSeries();
            achievedCalories.setLabel("Weekly Consumed Calories");
            System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^" + weekConsumption.getCalories());
            achievedCalories.set("Calories", weekConsumption.getCalories()); 
            weeklyCaloriesModel.addSeries(goalCalories);
            weeklyCaloriesModel.addSeries(achievedCalories);
            weeklyCaloriesModel.setTitle("Weekly Calories Goals VS Consumed");
            weeklyCaloriesModel.setLegendPosition("ne");
            Axis xAxis = weeklyCaloriesModel.getAxis(AxisType.X);
            xAxis.setLabel("Consumed or Goals Calories");
            Axis yAxis = weeklyCaloriesModel.getAxis(AxisType.Y);
            yAxis.setLabel("Calories");
            yAxis.setMin(7000);
            yAxis.setMax(21000);

            ChartSeries goalMacro = new ChartSeries();
            goalMacro.setLabel("Weekly Macronutrients Goals");
            goalMacro.set("Carbs", macro.get("carbs") * 7);
            goalMacro.set("Fats", macro.get("fats") * 7);
            goalMacro.set("Protein", macro.get("protein") * 7);
            goalMacro.set("Sugar", macro.get("sugar") * 7);
            ChartSeries achievedMacro = new ChartSeries();
            achievedMacro.setLabel("Weekly Consumed Macronutrients");
            achievedMacro.set("Carbs", weekConsumption.getCarbs());
            achievedMacro.set("Fats", weekConsumption.getFats());
            achievedMacro.set("Protein", weekConsumption.getProtein());
            achievedMacro.set("Sugar", weekConsumption.getSugar());
            weeklyMacroModel.addSeries(goalMacro);
            weeklyMacroModel.addSeries(achievedMacro);
           weeklyMacroModel.setTitle("Weekly Macronutrients Goals VS Consumed");
            weeklyMacroModel.setLegendPosition("ne");
            Axis xAxis2 = weeklyMacroModel.getAxis(AxisType.X);
            xAxis2.setLabel("Consumed or Goals Macronutrients");
            Axis yAxis2 = weeklyMacroModel.getAxis(AxisType.Y);
            yAxis2.setLabel("grams");
            yAxis2.setMin(0);
            yAxis2.setMax(3000);
        } catch (CustomerNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Customer not found" + ex.getMessage(), null));
        }
    }

    /**
     * @return the currentCustomer
     */
    public Customer getCurrentCustomer() {
        return currentCustomer;
    }
    
    public void test(ActionEvent event)
    {
        customerSessionBeanLocal.topFoodForEachMacro(currentCustomer.getCustomerId(), "calories");
    }
    public void setCurrentCustomer(Customer currentCustomer) {
        this.currentCustomer = currentCustomer;
    }

    /**
     * @return the weeklyCaloriesModel
     */
    public BarChartModel getWeeklyCaloriesModel() {
        return weeklyCaloriesModel;
    }

    /**
     * @param weeklyCaloriesModel the weeklyCaloriesModel to set
     */
    public void setWeeklyCaloriesModel(BarChartModel weeklyCaloriesModel) {
        this.weeklyCaloriesModel = weeklyCaloriesModel;
    }

    /**
     * @return the weeklyMacroModel
     */
    public BarChartModel getWeeklyMacroModel() {
        return weeklyMacroModel;
    }

    /**
     * @param weeklyMacroModel the weeklyMacroModel to set
     */
    public void setWeeklyMacroModel(BarChartModel weeklyMacroModel) {
        this.weeklyMacroModel = weeklyMacroModel;
    }

    /**
     * @return the macro
     */
    public HashMap<String, Double> getMacro() {
        return macro;
    }

    /**
     * @param macro the macro to set
     */
    public void setMacro(HashMap<String, Double> macro) {
        this.macro = macro;
    }

    /**
     * @return the todayDate
     */
    public LocalDate getTodayDate() {
        return todayDate;
    }

    /**
     * @param todayDate the todayDate to set
     */
    public void setTodayDate(LocalDate todayDate) {
        this.todayDate = todayDate;
    }

    /**
     * @return the lastMonDate
     */
    public LocalDate getLastMonDate() {
        return lastMonDate;
    }

    /**
     * @param lastMonDate the lastMonDate to set
     */
    public void setLastMonDate(LocalDate lastMonDate) {
        this.lastMonDate = lastMonDate;
    }

    /**
     * @return the goals
     */
    public FoodWrapper getGoals() {
        return goals;
    }

    /**
     * @param goals the goals to set
     */
    public void setGoals(FoodWrapper goals) {
        this.goals = goals;
    }

    /**
     * @return the consumed
     */
    public FoodWrapper getConsumed() {
        return consumed;
    }

    /**
     * @param consumed the consumed to set
     */
    public void setConsumed(FoodWrapper consumed) {
        this.consumed = consumed;
    }

    /**
     * @return the weekConsumption
     */
    public FoodWrapper getWeekConsumption() {
        return weekConsumption;
    }

    /**
     * @param weekConsumption the weekConsumption to set
     */
    public void setWeekConsumption(FoodWrapper weekConsumption) {
        this.weekConsumption = weekConsumption;
    }

    /**
     * @return the topCaloriesMeals
     */
    public List<FoodDiaryRecord> getTopCaloriesMeals() {
        return topCaloriesMeals;
    }

    /**
     * @param topCaloriesMeals the topCaloriesMeals to set
     */
    public void setTopCaloriesMeals(List<FoodDiaryRecord> topCaloriesMeals) {
        this.topCaloriesMeals = topCaloriesMeals;
    }

    /**
     * @return the topCarbsMeals
     */
    public List<FoodDiaryRecord> getTopCarbsMeals() {
        return topCarbsMeals;
    }

    /**
     * @param topCarbsMeals the topCarbsMeals to set
     */
    public void setTopCarbsMeals(List<FoodDiaryRecord> topCarbsMeals) {
        this.topCarbsMeals = topCarbsMeals;
    }

    /**
     * @return the topSugarMeals
     */
    public List<FoodDiaryRecord> getTopSugarMeals() {
        return topSugarMeals;
    }

    /**
     * @param topSugarMeals the topSugarMeals to set
     */
    public void setTopSugarMeals(List<FoodDiaryRecord> topSugarMeals) {
        this.topSugarMeals = topSugarMeals;
    }

    /**
     * @return the topFatsMeals
     */
    public List<FoodDiaryRecord> getTopFatsMeals() {
        return topFatsMeals;
    }

    /**
     * @param topFatsMeals the topFatsMeals to set
     */
    public void setTopFatsMeals(List<FoodDiaryRecord> topFatsMeals) {
        this.topFatsMeals = topFatsMeals;
    }

    /**
     * @return the topProteinMeals
     */
    public List<FoodDiaryRecord> getTopProteinMeals() {
        return topProteinMeals;
    }

    /**
     * @param topProteinMeals the topProteinMeals to set
     */
    public void setTopProteinMeals(List<FoodDiaryRecord> topProteinMeals) {
        this.topProteinMeals = topProteinMeals;
    }
}

