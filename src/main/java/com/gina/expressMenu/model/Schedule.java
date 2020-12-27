package com.gina.expressMenu.model;

import org.apache.tomcat.jni.Local;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.Locale;
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

    public Schedule(Restaurant restaurant) {
        this.restaurant = restaurant;

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

    public String getDayValue(){
        String dayName = "";
        switch (day){
            case 1:
                dayName = "Luni";
                break;
            case 2:
                dayName = "Marti";
                break;
            case 3:
                dayName = "Miercuri";
                break;
            case 4:
                dayName = "Joi";
               break;
            case 5:
                dayName = "Vineri";
                break;
            case 6:
                dayName = "Sambata";
                break;
            case 7:
                dayName = "Duminica";
                break;
            default:
                dayName="Inchis";
        }
        return dayName;
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

