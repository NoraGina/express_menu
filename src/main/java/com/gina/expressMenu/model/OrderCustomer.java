package com.gina.expressMenu.model;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "orders_customer")
public class OrderCustomer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_order_customer")
    private Long idOrderCustomer;

    @Column(name = "date")
    @DateTimeFormat(pattern = "MM-dd-yyyy")
    private LocalDate date;

    @Column(name = "time")
    private LocalTime time;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    @NotFound(action = NotFoundAction.IGNORE)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    @NotFound(action = NotFoundAction.IGNORE)
    private Restaurant restaurant;

    @OneToMany(mappedBy = "orderCustomer", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<OrderItem> orderItemList ;


    @Enumerated(EnumType.STRING)
    @Column(name = "status_type", length = 9)
    private Status status;

    public OrderCustomer() {
    }

    public OrderCustomer(Long idOrderCustomer, LocalDate date, LocalTime time, Customer customer,
                         Restaurant restaurant, List<OrderItem> orderItemList, Status status) {
        this.idOrderCustomer = idOrderCustomer;
        this.date = date;
        this.time = time;
        this.customer = customer;
        this.restaurant = restaurant;
        this.orderItemList = orderItemList;
        this.status = status;
    }

    public Long getIdOrderCustomer() {
        return idOrderCustomer;
    }

    public void setIdOrderCustomer(Long idOrderCustomer) {
        this.idOrderCustomer = idOrderCustomer;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public List<OrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }



    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getQuantityForEachOrderItem(){
        int quantity = 0;
        for(OrderItem orderItem:this.orderItemList){

           quantity= orderItem.getQuantity();
        }
        return quantity;
    }

    public boolean quantityZero(){
        if(getQuantityForEachOrderItem() == 0){
            return false;
        }
        return true;
    }

    public double getTotal(){
        double total = 0;
        for(OrderItem orderItem:orderItemList){
            total += orderItem.getQuantity()* orderItem.getProduct().getPrice();
        }
        return total;
    }

    public double getAmount(){
        double total = 0;
        for(OrderItem orderItem:this.orderItemList){
            total += orderItem.getQuantity()* orderItem.getProduct().getPrice();
        }
        return total;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderCustomer)) return false;
        OrderCustomer that = (OrderCustomer) o;
        if(idOrderCustomer != null){
            return idOrderCustomer.equals(that.idOrderCustomer) &&
                    date.equals(that.date);
        }else{
            return  date.equals(that.date);
        }

    }

    @Override
    public int hashCode() {
        return Objects.hash(idOrderCustomer, date);
    }

    @Override
    public String toString() {
        return "OrderCustomer{" +
                "idOrderCustomer=" + idOrderCustomer +
                ", date=" + date +
                ", time=" + time +
                ", customer=" + customer +
                ", restaurant=" + restaurant +
                ", orderItemList=" + orderItemList +
                ", status=" + status +
                '}';
    }

    public void displayOrderCustomer(){
        System.out.println("Id order Customer: "+ idOrderCustomer);
        System.out.println("Date: "+ date);
        System.out.println("Customer: "+ customer.getCustomerName());
        System.out.println("Restaurant: "+ restaurant.getRestaurantName());
    }
}
