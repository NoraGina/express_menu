package com.gina.expressMenu.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.Objects;

@Entity
@Table(name = "schedules")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_schedule")
    private Long idSchedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @Column(name = "day")
    private int day;

    @Column(name = "start_time")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime openTime;

    @Column(name = "end_time")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime closeTime;

    public Schedule() {
    }

    public Schedule(Restaurant restaurant, int day, LocalTime openTime, LocalTime closeTime) {
        this.restaurant = restaurant;
        this.day = day;
        this.openTime = openTime;
        this.closeTime = closeTime;
    }

    public Long getIdSchedule() {
        return idSchedule;
    }

    public void setIdSchedule(Long idSchedule) {
        this.idSchedule = idSchedule;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public LocalTime getOpenTime() {
        return openTime;
    }

    public void setOpenTime(LocalTime openTime) {
        this.openTime = openTime;
    }

    public LocalTime getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(LocalTime closeTime) {
        this.closeTime = closeTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Schedule)) return false;
        Schedule schedule = (Schedule) o;
        if(idSchedule != null){
            return idSchedule.equals(schedule.idSchedule) &&
                    restaurant.equals(schedule.restaurant);
        }else {
            return restaurant.equals(schedule.restaurant);
        }

    }

    @Override
    public int hashCode() {
        return Objects.hash(idSchedule, restaurant);
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "idSchedule=" + idSchedule +
                ", restaurant=" + restaurant +
                ", day=" + day +
                ", openTime=" + openTime +
                ", closeTime=" + closeTime +
                '}';
    }

    public void displaySchedule(){
        System.out.println("Day: "+day);
        System.out.println("Open: "+ openTime);
        System.out.println("Close: "+closeTime);
    }
}

