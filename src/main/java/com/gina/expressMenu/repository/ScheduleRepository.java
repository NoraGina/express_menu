package com.gina.expressMenu.repository;

import com.gina.expressMenu.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    @Query("Select s from Schedule s where restaurant.idRestaurant=:idRestaurant ")
    List<Schedule> findAllByRestaurantId(@Param("idRestaurant")Long idRestaurant);
}
