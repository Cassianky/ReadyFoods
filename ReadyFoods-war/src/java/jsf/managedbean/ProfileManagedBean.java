/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.CustomerSessionBeanLocal;
import entity.Customer;
import entity.Recipe;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import org.primefaces.event.FileUploadEvent;
import util.enumeration.ActivityLevel;
import util.enumeration.DietType;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UpdateCustomerException;
import util.security.CryptographicHelper;

/**
 *
 * @author ngcas
 */
/**
 *
 * @author ngcas
 */
@Named(value = "profileManagedBean")
@ViewScoped
public class ProfileManagedBean implements Serializable {

    @EJB
    private CustomerSessionBeanLocal customerSessionBeanLocal;
    
    @Inject
    private RecipeViewManagedBean recipeViewManagedBean;

    private Customer currentCustomer;
    private String uploadedFilePath;
    private Boolean showUploadedFile;
    private ActivityLevel[] activityLevels;
    private DietType[] dietTypes;
    private LocalDate maxDate;
    private String newPassword;
    private String oldPassword;
    private String confirmPassword;
    
    private List<Recipe>bookmarkedRecipes;

    public ProfileManagedBean() {
        showUploadedFile = false;
        LocalDate today = LocalDate.now();
        int yearToday = today.getYear();
        int monthToday = today.getMonthValue();
        int dateToday = today.getDayOfMonth();
        maxDate = LocalDate.of(yearToday - 18, monthToday, dateToday);
    }

    @PostConstruct
    public void postConstruct() {
        currentCustomer = (Customer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentCustomer");
    }

    public void handleFileUpload(FileUploadEvent event) {
        try {
            System.err.println("********** Demo03ManagedBean.handleFileUpload(): alternatedocroot_1: " + (String) FacesContext.getCurrentInstance().getExternalContext().getInitParameter("alternatedocroot_1"));
            String newFilePath = FacesContext.getCurrentInstance().getExternalContext().getInitParameter("alternatedocroot_1") + System.getProperty("file.separator") + "profile" + System.getProperty("file.separator") + event.getFile().getFileName();

            System.err.println("********** Demo03ManagedBean.handleFileUpload(): File name: " + event.getFile().getFileName());
            System.err.println("********** Demo03ManagedBean.handleFileUpload(): newFilePath: " + newFilePath);

            File file = new File(newFilePath);
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            int a;
            int BUFFER_SIZE = 8192;
            byte[] buffer = new byte[BUFFER_SIZE];

            InputStream inputStream = event.getFile().getInputStream();

            while (true) {
                a = inputStream.read(buffer);

                if (a < 0) {
                    break;
                }

                fileOutputStream.write(buffer, 0, a);
                fileOutputStream.flush();
            }

            fileOutputStream.close();
            inputStream.close();

            String newFile = event.getFile().getFileName();
            currentCustomer.setProfilePicture(newFile);
            customerSessionBeanLocal.updateCustomer(currentCustomer);
            setUploadedFilePath(newFile);
            setShowUploadedFile((Boolean) true);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "File uploaded successfully", ""));
        } catch (InputDataValidationException | UpdateCustomerException | CustomerNotFoundException | IOException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "File upload error: " + ex.getMessage(), ""));
        }
    }
    
    public void addRecipeToBookMarks(ActionEvent event){
        Recipe recipeToAdd = (Recipe)event.getComponent().getAttributes().get("recipeToAdd");
        customerSessionBeanLocal.addBookmarkedRecipe(recipeToAdd, currentCustomer.getCustomerId());
        recipeViewManagedBean.setIsBookmarked(true);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Bookmarked recipe successfully!", "" + recipeToAdd.getRecipeTitle()));
        
    }
    
    public void removeRecipeFromBookmarks(ActionEvent event){
        Recipe recipeToRemove = (Recipe)event.getComponent().getAttributes().get("recipeToRemove");
        customerSessionBeanLocal.removeBookmarkedRecipe(recipeToRemove, currentCustomer.getCustomerId());
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Removed recipe from bookmarks successfully!", "" + recipeToRemove.getRecipeTitle()));
    }


    public void updateCustomer(ActionEvent event) {
        try {
            customerSessionBeanLocal.updateCustomer(currentCustomer);
            Customer newCurrentCustomer = customerSessionBeanLocal.retrieveCustomerByCustomerId(currentCustomer.getCustomerId());
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("currentCustomer", newCurrentCustomer);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Profile updated successfully", null));
        } catch (CustomerNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Customer ID is not found! : " + ex.getMessage(), null));
        } catch (InputDataValidationException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating profile: " + ex.getMessage(), null));
        } catch (UpdateCustomerException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating product: " + ex.getMessage(), null));
        }
    }

    public void changePassword(ActionEvent event) {
        try {
            String passwordHash = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(oldPassword + currentCustomer.getSalt()));
            if (passwordHash.equals(currentCustomer.getPassword())) {

                customerSessionBeanLocal.updatePassword(currentCustomer, oldPassword, newPassword);
                Customer newCurrentCustomer = customerSessionBeanLocal.retrieveCustomerByCustomerId(currentCustomer.getCustomerId());
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("currentCustomer", newCurrentCustomer);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Password changed successfully", null));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Old password is incorrect", null));
            }

        } catch (InvalidLoginCredentialException | CustomerNotFoundException | InputDataValidationException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating password: " + ex.getMessage(), null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }

    /**
     * @return the currentCustomer
     */
    public Customer getCurrentCustomer() {
        return currentCustomer;
    }

    /**
     * @param currentCustomer the currentCustomer to set
     */
    public void setCurrentCustomer(Customer currentCustomer) {
        this.currentCustomer = currentCustomer;
    }

    /**
     * @return the uploadedFilePath
     */
    public String getUploadedFilePath() {
        return uploadedFilePath;
    }

    /**
     * @param uploadedFilePath the uploadedFilePath to set
     */
    public void setUploadedFilePath(String uploadedFilePath) {
        this.uploadedFilePath = uploadedFilePath;
    }

    /**
     * @return the showUploadedFile
     */
    public Boolean getShowUploadedFile() {
        return showUploadedFile;
    }

    /**
     * @param showUploadedFile the showUploadedFile to set
     */
    public void setShowUploadedFile(Boolean showUploadedFile) {
        this.showUploadedFile = showUploadedFile;
    }

    /**
     * @return the activityLevels
     */
    public ActivityLevel[] getActivityLevels() {
        return ActivityLevel.values();
    }

    /**
     * @param activityLevels the activityLevels to set
     */
    public void setActivityLevels(ActivityLevel[] activityLevels) {
        this.activityLevels = activityLevels;
    }

    /**
     * @return the dietTypes
     */
    public DietType[] getDietTypes() {
        return DietType.values();
    }

    /**
     * @param dietTypes the dietTypes to set
     */
    public void setDietTypes(DietType[] dietTypes) {
        this.dietTypes = dietTypes;
    }

    /**
     * @return the maxDate
     */
    public LocalDate getMaxDate() {
        return maxDate;
    }

    /**
     * @param maxDate the maxDate to set
     */
    public void setMaxDate(LocalDate maxDate) {
        this.maxDate = maxDate;
    }

    /**
     * @return the newPassword
     */
    public String getNewPassword() {
        return newPassword;
    }

    /**
     * @param newPassword the newPassword to set
     */
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    /**
     * @return the oldPassword
     */
    public String getOldPassword() {
        return oldPassword;
    }

    /**
     * @param oldPassword the oldPassword to set
     */
    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    /**
     * @return the confirmPassword
     */
    public String getConfirmPassword() {
        return confirmPassword;
    }

    /**
     * @param confirmPassword the confirmPassword to set
     */
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    /**
     * @return the bookmarkedRecipes
     */
    public List<Recipe> getBookmarkedRecipes() {
        try {
            Customer customerRetrieved = customerSessionBeanLocal.retrieveCustomerByCustomerId(currentCustomer.getCustomerId());
            return customerRetrieved.getBookedmarkedRecipes();
        } catch (CustomerNotFoundException ex) {
            ex.printStackTrace();
        }
        return bookmarkedRecipes;
    }

    /**
     * @param bookmarkedRecipes the bookmarkedRecipes to set
     */
    public void setBookmarkedRecipes(List<Recipe> bookmarkedRecipes) {
        this.bookmarkedRecipes = bookmarkedRecipes;
    }

}
