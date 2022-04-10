package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import util.enumeration.ActivityLevel;
import util.enumeration.DietType;
import util.enumeration.Gender;
import util.exception.EntityInstanceExistsInCollectionException;
import util.exception.EntityInstanceMissingInCollectionException;
import util.security.CryptographicHelper;

@Entity
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;
    @Column(nullable = false, unique = true, length = 64)
    @NotNull
    @Size(min=5, max = 64)
    private String userName;
    @NotNull
    @Size(min=2, max = 32)
    private String firstName;
    @NotNull
    @Size(min=2, max = 32)
    private String lastName;
    @NotNull
    @Column(nullable = false, length = 8)
    @Size(min=8, max=8)
    private String contactNumber;
    // Updated in v4.5 to use CHAR instead of VARCHAR
    // @Column(nullable = false, length = 32)
    @Column(columnDefinition = "CHAR(32) NOT NULL")
    @NotNull
    // The following bean validation constraint is not applicable since we are only storing the password hashsum which is always 128 bit represented as 32 characters (16 hexadecimal digits)
    // @Size(min = 8, max = 32)
    private String password;
    @Column(columnDefinition = "CHAR(32) NOT NULL")
    private String salt;
    @Column(nullable = false, unique = true, length = 64)
    @NotNull
    @Size(min=5, max = 64)
    @Email
    private String email;
    @Column(nullable = false)
    @NotNull
    private Boolean isBanned;
    @NotNull
    @Column(nullable = false, unique = true, length = 50)
    @Size(min=5, max=50)
    private String address;
    //@Column(nullable = false)
    //@NotNull
    //@Min(18) //To create an account, you must be 18 years old or older
    //@Max(120)
    //private Integer age;
    @Column(nullable = false)
    @NotNull
    private LocalDate dob;
    @Column(nullable = false, precision = 11, scale = 2)
    @NotNull
    @DecimalMin("0.00")
    @Digits(integer = 9, fraction = 2) // 11 - 2 digits to the left of the decimal point
    private BigDecimal amountSpent;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private DietType dietType;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private Gender gender;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private ActivityLevel activityLevel;
    
    @OneToMany(fetch = FetchType.LAZY)
    private List<Subscription> subscriptions;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer")
    private List<Enquiry> enquiries;

    @OneToMany(mappedBy = "customer",fetch = FetchType.LAZY)
    private List<OrderEntity> orders;
    
    @OneToMany(fetch = FetchType.LAZY)
    private List<Food> foods;
    
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Recipe> bookmarkedRecipes;
    
    @OneToOne(fetch = FetchType.LAZY)
    private CreditCard creditCard;

    @Column(nullable = false)
    private String profilePicture;
    
    @OneToMany(fetch = FetchType.LAZY)
    private List<FoodDiaryRecord> foodDiaryRecords;
    
    public Customer() {
        bookmarkedRecipes = new ArrayList<>();
        foods = new ArrayList<>();
        orders = new ArrayList<>();
        enquiries = new ArrayList<>();
        subscriptions = new ArrayList<>();
        foodDiaryRecords = new ArrayList<>();
        amountSpent = new BigDecimal(0);
        isBanned = false;
        this.salt = CryptographicHelper.getInstance().generateRandomString(32);
        profilePicture = "";
    }

    public Customer(String userName, String firstName, String lastName, String contactNumber, String password, String email, String address, DietType dietType, Gender gender, ActivityLevel activityLevel) {
        this();
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.contactNumber = contactNumber;
        this.email = email;
        this.address = address;
        //this.age = age;
        this.dietType = dietType;
        this.gender = gender;
        this.activityLevel = activityLevel;
        setPassword(password);
    }

    
    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (customerId != null ? customerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the customerId fields are not set
        if (!(object instanceof Customer)) {
            return false;
        }
        Customer other = (Customer) object;
        if ((this.customerId == null && other.customerId != null) || (this.customerId != null && !this.customerId.equals(other.customerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Customer[ id=" + customerId + " ]";
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if(password != null)
        {
            this.password = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(password + this.salt));
        }
        else
        {
            this.password = null;
        }
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getIsBanned() {
        return isBanned;
    }

    public void setIsBanned(Boolean isBanned) {
        this.isBanned = isBanned;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

//    public Integer getAge() {
//        return age;
//    }
//
//    public void setAge(Integer age) {
//        this.age = age;
//    }

    public BigDecimal getAmountSpent() {
        return amountSpent;
    }

    public void setAmountSpent(BigDecimal amountSpent) {
        this.amountSpent = amountSpent;
    }

    public DietType getDietType() {
        return dietType;
    }

    public void setDietType(DietType dietType) {
        this.dietType = dietType;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public ActivityLevel getActivityLevel() {
        return activityLevel;
    }

    public void setActivityLevel(ActivityLevel activityLevel) {
        this.activityLevel = activityLevel;
    }

    public List<Subscription> getSubscriptions() {
        return subscriptions;
    }

    public void addSubscription(Subscription subscription) {
        if(!this.subscriptions.contains(subscription))
        {
            this.getSubscriptions().add(subscription);
        }
    }
    
    public void removeSubscription(Subscription subscription) {
        if(this.getSubscriptions().contains(subscription))
        {
            this.getSubscriptions().remove(subscription);
        }
    }

    public List<Enquiry> getEnquiries() {
        return enquiries;
    }

    public void addEnquiry(Enquiry enquiry) {
        if(!this.enquiries.contains(enquiry))
        {
            this.getEnquiries().add(enquiry);
        }
    }
    
    public void removeEnquiry(Enquiry enquiry) {
        if(this.getEnquiries().contains(enquiry))
        {
            this.getEnquiries().remove(enquiry);
        }
    }

    public List<OrderEntity> getOrders() {
        return orders;
    }

    public void addOrder(OrderEntity order) throws EntityInstanceExistsInCollectionException
    {
        if(!this.orders.contains(order))
        {
            this.orders.add(order);
        }
        else
        {
            throw new EntityInstanceExistsInCollectionException("Order already exists");
        }
    }
    
    
    
    public void removeOrder(OrderEntity order) throws EntityInstanceMissingInCollectionException
    {
        if(this.orders.contains(order))
        {
            this.orders.remove(order);
        }
        else
        {
            throw new EntityInstanceMissingInCollectionException("Order missing");
        }
    }

    public List<Food> getFoods() {
        return foods;
    }

    public void addFood(Food food) {
        if(!this.foods.contains(food))
        {
            this.getFoods().add(food);
        }
    }
    
    public void removeFood(Food food) {
        if(this.getFoods().contains(food))
        {
            this.getFoods().remove(food);
        }
    }

    public List<Recipe> getBookedmarkedRecipes() {
        return bookmarkedRecipes;
    }

     public void addBookedmarkedRecipe(Recipe bookmarkedRecipe) {
        if(!this.getBookedmarkedRecipes().contains(bookmarkedRecipe))
        {
            this.getBookedmarkedRecipes().add(bookmarkedRecipe);
        }
    }
    
    public void removeBookedmarkedRecipe(Recipe bookmarkedRecipe) {
        if(this.getBookedmarkedRecipes().contains(bookmarkedRecipe))
        {
            this.getBookedmarkedRecipes().remove(bookmarkedRecipe);
        }
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }
    
    /**
     * @return the profilePicture
     */
    public String getProfilePicture() {
        return profilePicture;
    }

    /**
     * @param profilePicture the profilePicture to set
     */
    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    /**
     * @return the dob
     */
    public LocalDate getDob() {
        return dob;
    }

    /**
     * @param dob the dob to set
     */
    public void setDob(LocalDate dob) {
        this.dob = dob;
    }
    
    public List<FoodDiaryRecord> getFoodDiaryRecords() {
        return this.foodDiaryRecords;
    }

    public void addFoodDiaryRecord(FoodDiaryRecord foodDiaryRecord) {
        if(!this.foodDiaryRecords.contains(foodDiaryRecord))
        {
            this.getFoodDiaryRecords().add(foodDiaryRecord);
        }
    }
    
    public void removeFoodDiaryRecord(FoodDiaryRecord foodDiaryRecord) {
        if(this.getFoodDiaryRecords().contains(foodDiaryRecord))
        {
            this.getFoodDiaryRecords().remove(foodDiaryRecord);
        }
    }
}
