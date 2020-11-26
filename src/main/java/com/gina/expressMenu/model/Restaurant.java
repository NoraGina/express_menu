package com.gina.expressMenu.model;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Entity
@Table(name = "restaurants")
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_restaurant")
    private Long idRestaurant;
    @Column(name = "restaurant_name")
    private String restaurantName;
    @Column(name="restaurant_email")
    private String restaurantEmail;
    @Column(name = "restaurant_phone")
    private String restaurantPhone;
    @Column(name = "restaurant_address")
    private String restaurantAddress;

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<Product> productSet;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_manager")
    @NotFound(action = NotFoundAction.IGNORE)
    private Manager manager;
    @Lob
    @Column(length = 100000,  name="image")
    private byte[] image;
    @Column(name = "description")
    private String description;
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Schedule> scheduleSet;
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Set<OrderCustomer> orderCustomerSet;


    public Restaurant() {
    }

    public Restaurant(Long idRestaurant, String restaurantName, String restaurantEmail, String restaurantPhone,
                      String restaurantAddress, String description, byte[] image, Set<Product> productSet,
                      Set<Schedule> scheduleSet, Set<OrderCustomer> orderCustomerSet, Manager manager) {
        this.idRestaurant = idRestaurant;
        this.restaurantName = restaurantName;
        this.restaurantEmail = restaurantEmail;
        this.restaurantPhone = restaurantPhone;
        this.restaurantAddress = restaurantAddress;
        this.description = description;
        this.image = image;
        this.productSet = productSet;
        this.scheduleSet = scheduleSet;
        this.orderCustomerSet = orderCustomerSet;
        this.manager = manager;
    }

    public Long getIdRestaurant() {
        return idRestaurant;
    }

    public void setIdRestaurant(Long idRestaurant) {
        this.idRestaurant = idRestaurant;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getRestaurantEmail() {
        return restaurantEmail;
    }

    public void setRestaurantEmail(String restaurantEmail) {
        this.restaurantEmail = restaurantEmail;
    }

    public String getRestaurantPhone() {
        return restaurantPhone;
    }

    public void setRestaurantPhone(String restaurantPhone) {
        this.restaurantPhone = restaurantPhone;
    }

    public String getRestaurantAddress() {
        return restaurantAddress;
    }

    public void setRestaurantAddress(String restaurantAddress) {
        this.restaurantAddress = restaurantAddress;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageAsString(){
        String base64EncodedImage = Base64.getEncoder().encodeToString(image);
        return base64EncodedImage;
    }


    public Set<Product> getProductSet() {
        return productSet;
    }

    public void setProductSet(Set<Product> productSet) {
        this.productSet = productSet;
    }

    public Set<Schedule> getScheduleSet() {
        return scheduleSet;
    }

    public void setScheduleSet(Set<Schedule> scheduleSet) {
        this.scheduleSet = scheduleSet;
    }

    public void setSchedule(Long idRestaurant, int day){
        for(Schedule schedule:this.scheduleSet){
            Restaurant restaurant = schedule.getRestaurant();
        }
    }

    @Transient
    public Schedule getSchedule( ){

        for(Schedule schedule:scheduleSet) {
           if(schedule.getDay() == getDayOfWeek(LocalDate.now())){
               LocalTime openTime = schedule.getOpenTime();
               LocalTime closeTime = schedule.getCloseTime();
               schedule.getDayValue();
               return schedule;
           }


        }

        return null;
    }


    public  int getDayOfWeek(LocalDate date) {

        DayOfWeek day = date.getDayOfWeek();
        return day.getValue();
    }

    public  int getNextDay(LocalDate date){

        LocalDate nextDay = date.plusDays(1l);
        DayOfWeek day = nextDay.getDayOfWeek();
        return day.getValue();
    }




    public String getDayName1() {
        int days = getDayOfWeek(LocalDate.now());
        String dayName = "";
        switch (days) {
            case 1:
                dayName = "Luni";
                break;
            case 2:
                dayName ="Marti";
                break;
            case 3:
                dayName= "Miercuri";
                break;
            case 4:
                dayName= "Joi";
                break;
            case 5:
                dayName= "Vineri";
                break;
            case 6:
                dayName= "Sambata";
                break;
            case 7:
                dayName= "Duminica";
                break;
            default:
                throw new IllegalArgumentException("The dayOfWeek (" + days + ") is not valid.");
        }
        return dayName;
    }

    public String getDayName2() {
        int days = getNextDay(LocalDate.now());
        String dayName = "";
        switch (days) {
            case 1:
                dayName = "Luni";
                break;
            case 2:
                dayName ="Marti";
                break;
            case 3:
                dayName= "Miercuri";
                break;
            case 4:
                dayName= "Joi";
                break;
            case 5:
                dayName= "Vineri";
                break;
            case 6:
                dayName= "Sambata";
                break;
            case 7:
                dayName= "Duminica";
                break;
            default:
                throw new IllegalArgumentException("The dayOfWeek (" + days + ") is not valid.");
        }
        return dayName;
    }


    public static  String getNextDayName(LocalDate date){

        LocalDate nextDay = date.plusDays(1l);
        DayOfWeek day = nextDay.getDayOfWeek();
        return day.name();
    }

    public Set<OrderCustomer> getOrderCustomerSet() {
        return orderCustomerSet;
    }

    public void setOrderCustomerSet(Set<OrderCustomer> orderCustomerSet) {
        this.orderCustomerSet = orderCustomerSet;
    }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Restaurant)) return false;
        Restaurant that = (Restaurant) o;
        return idRestaurant.equals(that.idRestaurant) &&
                restaurantName.equals(that.restaurantName) &&
                restaurantEmail.equals(that.restaurantEmail) &&
                restaurantPhone.equals(that.restaurantPhone) &&
                restaurantAddress.equals(that.restaurantAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idRestaurant, restaurantName, restaurantEmail, restaurantPhone, restaurantAddress);
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "idRestaurant=" + idRestaurant +
                ", restaurantName='" + restaurantName + '\'' +
                ", restaurantEmail='" + restaurantEmail + '\'' +
                ", restaurantPhone='" + restaurantPhone + '\'' +
                ", restaurantAddress='" + restaurantAddress + '\'' +
                ", description='" + description + '\'' +

                '}';
    }

    public void displayRestaurant(){
        System.out.println("Restaurant id: "+idRestaurant);
        System.out.println("Restaurant name: "+ restaurantName);
        System.out.println("Restaurant email: "+ restaurantEmail);
        System.out.println("Restaurant phone: "+ restaurantPhone);
        System.out.println("Restaurant address: "+ restaurantAddress);
        //System.out.println("Manager: "+ manager.getUserName());
    }
}

