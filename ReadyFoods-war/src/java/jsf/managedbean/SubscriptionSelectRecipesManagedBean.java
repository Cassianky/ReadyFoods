/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.CategorySessionBeanLocal;
import ejb.session.stateless.CustomerSessionBeanLocal;
import ejb.session.stateless.OrderEntitySessionBeanLocal;
import ejb.session.stateless.ProcessRecipeSelectionManagedBeanLocal;
import ejb.session.stateless.RecipeSessionBeanLocal;
import ejb.session.stateless.SubscriptionSessionBeanLocal;
import entity.Category;
import entity.Customer;
import entity.OrderEntity;
import entity.OrderLineItem;
import entity.Recipe;
import entity.Subscription;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import util.enumeration.Status;
import util.exception.CategoryNotFoundException;
import util.exception.CreateNewOrderException;
import util.exception.CustomerNotFoundException;
import util.exception.NoOngoingSubscriptionException;
import util.exception.OrderNotFoundException;
import util.exception.RecipeNotFoundException;

/**
 *
 * @author angler
 */
@Named(value = "subscriptionSelectRecipesManagedBean")
@ViewScoped
public class SubscriptionSelectRecipesManagedBean implements Serializable {

    @EJB(name = "CategorySessionBeanLocal")
    private CategorySessionBeanLocal categorySessionBeanLocal;

    @EJB
    private ProcessRecipeSelectionManagedBeanLocal processRecipeSelectionManagedBeanLocal;

    @EJB(name = "OrderEntitySessionBeanLocal")
    private OrderEntitySessionBeanLocal orderEntitySessionBeanLocal;

    @EJB(name = "SubscriptionSessionBeanLocal")
    private SubscriptionSessionBeanLocal subscriptionSessionBeanLocal;

    @EJB(name = "CustomerSessionBeanLocal")
    private CustomerSessionBeanLocal customerSessionBeanLocal;

    @EJB(name = "RecipeSessionBeanLocal")
    private RecipeSessionBeanLocal recipeSessionBeanLocal;

    @Inject
    private RecipeViewSummarisedManagedBean recipeViewSummarisedManagedBean;

    private List<Recipe> allRecipes;

    private List<OrderLineItem> orderLineItems;
    private List<OrderLineItem> recommendedLineItems;
    private List<OrderLineItem> otherLineItems;
    private Recipe recipeToView;

    private Customer currentCustomerEntity;
    private Subscription ongoingSubscription;
    private OrderEntity currentOrder;
    private String additionalNotes;

    private Integer remaining;
    private String nextWk;
    private Date dateForDelivery;

    /**
     * Creates a new instance of SubscriptionSelectRecipesManagedBean
     */
    public SubscriptionSelectRecipesManagedBean() {
        this.setOrderLineItems(new ArrayList<>());
        this.recommendedLineItems = new ArrayList<>();
        this.otherLineItems = new ArrayList<>();
        additionalNotes = "";
        nextWk = getNextWeek();

    }

    @PostConstruct
    public void postConstruct() {
        setCurrentCustomerEntity((Customer) FacesContext.getCurrentInstance().
                getExternalContext().getSessionMap().get("currentCustomer"));
        try {
            setAllRecipes(recipeSessionBeanLocal.retrieveAllRecipes());
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("recipeToView", getRecipeToView());
            this.currentCustomerEntity = customerSessionBeanLocal.retrieveCustomerByCustomerId(getCurrentCustomerEntity().getCustomerId());

            // This line throws NoONgoingSubscriptionException
            this.setOngoingSubscription(subscriptionSessionBeanLocal.retrieveOngoingSubscriptionForCustomer(getCurrentCustomerEntity().getCustomerId()));

            initCustomerOrderInformation();

        } catch (CustomerNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Customer not found" + ex.getMessage(), null));
        } catch (NoOngoingSubscriptionException ex) {
            this.setOngoingSubscription(null);

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Create new" + ex.getMessage(), null));
        }

    }

    public void resetSelection(ActionEvent event) {

        for (OrderLineItem oli : this.orderLineItems) {
            oli.setQuantity(0);
        }

        Integer selected = 0;
        for (OrderLineItem oli : orderLineItems) {

            selected += oli.getQuantity();
            //System.out.println(selected);
        }

        setRemaining((Integer) ongoingSubscription.getNumOfRecipes() - selected);

    }

    public void initCustomerOrderInformation() {
        // Generate list of order line items
        System.out.println("Generating order line items for use in selectRecipes.xhtml");
        this.setRemaining(ongoingSubscription.getNumOfRecipes());

        currentOrder = ongoingSubscription.getCurrentOrder();

        List<Long> usedRecipeIds = new ArrayList<Long>();

        if (currentOrder != null) {

            setAdditionalNotes(currentOrder.getAdditionalNotes());
            dateForDelivery = currentOrder.getDateForDelivery();
            for (OrderLineItem orderLineItem : currentOrder.getOrderLineItems()) {
                orderLineItems.add(orderLineItem);
                orderLineItem.getRecipe().getCategories().size();
                usedRecipeIds.add(orderLineItem.getRecipe().getRecipeId());
                this.remaining -= orderLineItem.getQuantity();

            }
        }

        for (Recipe recipe : allRecipes) {
            if (!usedRecipeIds.contains(recipe.getRecipeId())) {
                OrderLineItem oli = new OrderLineItem();
                oli.setRecipeSubTotal(BigDecimal.ZERO); // no need to update as amount user is charged is predetermined
                oli.setRecipe(recipe);
                oli.setQuantity(0);
                this.getOrderLineItems().add(oli);
            }
        }

        //System.out.println(currentCustomerEntity.getDietType());
        for (OrderLineItem oli : orderLineItems) {
            Category dietType = getDietTypeForRecipe(oli.getRecipe());
            if (dietType != null && dietType.getName().toLowerCase().equals(currentCustomerEntity.getDietType().name().toLowerCase())) {
                getRecommendedLineItems().add(oli);
            } else {
                this.otherLineItems.add(oli);
            }
        }

    }

    public void updateRemaningRecipes(ValueChangeEvent event) {
        System.out.println("Update remaining recipes");
        Integer selected = 0;
        for (OrderLineItem oli : orderLineItems) {

            selected += oli.getQuantity();
            //System.out.println(selected);
        }

        setRemaining((Integer) ongoingSubscription.getNumOfRecipes() - selected);
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Remaining recipes: " + remaining,
                null));
    }

    public void updateSelection(ActionEvent event) {
        System.out.println("Date for delivery" + dateForDelivery);

        if (remaining != 0) {

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "You have " + remaining + " recipe(s) remaining!", null));
            return;

        } else if (dateForDelivery == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Please select a delivery date!", null));
            return;

        }
        try {
            List<OrderLineItem> lineItemsToBuy = new ArrayList<>();

            for (OrderLineItem oli : orderLineItems) {
                if (oli.getQuantity() != 0) {
                    lineItemsToBuy.add(oli);
                }
            }

            OrderEntity newOrder = new OrderEntity(ongoingSubscription.getNumOfPeople(), ongoingSubscription.getWeeklyPrice(), false,
                    new Date(), Status.PENDING, lineItemsToBuy);
            newOrder.setAdditionalNotes(getAdditionalNotes());
            newOrder.setDateForDelivery(dateForDelivery);
            
            if (currentOrder == null) {
                newOrder = orderEntitySessionBeanLocal.createNewSubscriptionOrder(currentCustomerEntity.getCustomerId(), newOrder);
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO,
                                "Recipe selection has been saved!", null));
                currentOrder = newOrder;

            } else {
                OrderEntity oldOrder = orderEntitySessionBeanLocal.deleteSubscriptionOrder(currentCustomerEntity.getCustomerId(), currentOrder.getOrderEntityId());

                newOrder = orderEntitySessionBeanLocal.createNewSubscriptionOrder(currentCustomerEntity.getCustomerId(), newOrder);

                currentOrder = newOrder;

                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO,
                                "Recipe selection has been updated!", null));

            }
        } catch (CustomerNotFoundException | CreateNewOrderException | NoOngoingSubscriptionException | OrderNotFoundException ex) {

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Error when updating recipe selection: " + ex.getMessage(), null));
        }

    }

    public String getNextWeek() {
        ZoneId TZ = ZoneId.of("Asia/Singapore");
        final DayOfWeek firstDayOfWeek = WeekFields.of(Locale.FRANCE).getFirstDayOfWeek();
        final DayOfWeek lastDayOfWeek = DayOfWeek.of(((firstDayOfWeek.getValue() + 5) % DayOfWeek.values().length) + 1);
        LocalDate start = LocalDate.now(TZ).with(TemporalAdjusters.previousOrSame(firstDayOfWeek)).plusDays(7); // first day
        LocalDate end = LocalDate.now(TZ).with(TemporalAdjusters.nextOrSame(lastDayOfWeek)).plusDays(7);      // last day

        return start.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)) + " - " + end.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG));

        // return start + " TO " + end;
    }

    public Date getMinDate() {
        ZoneId TZ = ZoneId.of("Asia/Singapore");
        final DayOfWeek firstDayOfWeek = WeekFields.of(Locale.FRANCE).getFirstDayOfWeek();

        LocalDate start = LocalDate.now(TZ).with(TemporalAdjusters.previousOrSame(firstDayOfWeek)).plusDays(7); // first day

        return Date.from(start.atStartOfDay(TZ).toInstant());

    }

    public Date getMaxDate() {
        ZoneId TZ = ZoneId.of("Asia/Singapore");
        final DayOfWeek firstDayOfWeek = WeekFields.of(Locale.FRANCE).getFirstDayOfWeek();
        final DayOfWeek lastDayOfWeek = DayOfWeek.of(((firstDayOfWeek.getValue() + 5) % DayOfWeek.values().length) + 1);
        LocalDate start = LocalDate.now(TZ).with(TemporalAdjusters.previousOrSame(firstDayOfWeek)).plusDays(7); // first day
        LocalDate end = LocalDate.now(TZ).with(TemporalAdjusters.nextOrSame(lastDayOfWeek)).plusDays(7);      // last day

        return Date.from(end.atStartOfDay(TZ).toInstant());

        // return start + " TO " + end;
    }

    public String getCurrentOrderDeliveryDate() {
        ZoneId TZ = ZoneId.of("Asia/Singapore");

        if (currentOrder == null || currentOrder.getDateForDelivery() == null) {
            return "Not selected.";
        }

        LocalDate date = currentOrder.getDateForDelivery().toInstant().atZone(TZ).toLocalDate();
        return date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG));

    }

    public void onDateSelect(SelectEvent<Date> event) {
        System.out.println("Date select");

        FacesContext facesContext = FacesContext.getCurrentInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, format.format(dateForDelivery) + " selected. Remember to save your changes!",
                format.format(event.getObject())));
    }

    public void click() {
        System.out.println("Click");
        PrimeFaces.current().ajax().update("form");
        PrimeFaces.current().executeScript("PF('dlg').show()");
    }

    public void process() {
        processRecipeSelectionManagedBeanLocal.process();
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Processed for every customer!", null));
    }

    public Category getDietTypeForRecipe(Recipe recipe) {

        try {
            return categorySessionBeanLocal.retrieveRecipeDietType(recipe.getRecipeId());
        } catch (CategoryNotFoundException | RecipeNotFoundException ex) {
            return null;

        }

    }

    /**
     * @return the allRecipes
     */
    public List<Recipe> getAllRecipes() {
        return allRecipes;
    }

    /**
     * @param allRecipes the allRecipes to set
     */
    public void setAllRecipes(List<Recipe> allRecipes) {
        this.allRecipes = allRecipes;
    }

    /**
     * @return the recipeToView
     */
    public Recipe getRecipeToView() {
        return recipeToView;
    }

    /**
     * @param recipeToView the recipeToView to set
     */
    public void setRecipeToView(Recipe recipeToView) {
        this.recipeToView = recipeToView;
    }

    /**
     * @return the currentCustomerEntity
     */
    public Customer getCurrentCustomerEntity() {
        return currentCustomerEntity;
    }

    /**
     * @param currentCustomerEntity the currentCustomerEntity to set
     */
    public void setCurrentCustomerEntity(Customer currentCustomerEntity) {
        this.currentCustomerEntity = currentCustomerEntity;
    }

    /**
     * @return the ongoingSubscription
     */
    public Subscription getOngoingSubscription() {
        return ongoingSubscription;
    }

    /**
     * @param ongoingSubscription the ongoingSubscription to set
     */
    public void setOngoingSubscription(Subscription ongoingSubscription) {
        this.ongoingSubscription = ongoingSubscription;
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
     * @return the remaining
     */
    public Integer getRemaining() {
        return remaining;
    }

    /**
     * @param remaining the remaining to set
     */
    public void setRemaining(Integer remaining) {
        this.remaining = remaining;
    }

    /**
     * @return the nextWk
     */
    public String getNextWk() {
        return nextWk;
    }

    /**
     * @param nextWk the nextWk to set
     */
    public void setNextWk(String nextWk) {
        this.nextWk = nextWk;
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
     * @return the recipeViewSummarisedManagedBean
     */
    public RecipeViewSummarisedManagedBean getRecipeViewSummarisedManagedBean() {
        return recipeViewSummarisedManagedBean;
    }

    /**
     * @param recipeViewSummarisedManagedBean the
     * recipeViewSummarisedManagedBean to set
     */
    public void setRecipeViewSummarisedManagedBean(RecipeViewSummarisedManagedBean recipeViewSummarisedManagedBean) {
        this.recipeViewSummarisedManagedBean = recipeViewSummarisedManagedBean;
    }

    /**
     * @return the recommendedLineItems
     */
    public List<OrderLineItem> getRecommendedLineItems() {
        return recommendedLineItems;
    }

    /**
     * @param recommendedLineItems the recommendedLineItems to set
     */
    public void setRecommendedLineItems(List<OrderLineItem> recommendedLineItems) {
        this.recommendedLineItems = recommendedLineItems;
    }

    /**
     * @return the otherLineItems
     */
    public List<OrderLineItem> getOtherLineItems() {
        return otherLineItems;
    }

    /**
     * @param otherLineItems the otherLineItems to set
     */
    public void setOtherLineItems(List<OrderLineItem> otherLineItems) {
        this.otherLineItems = otherLineItems;
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

}
