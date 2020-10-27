package com.gina.expressMenu.repository;

import com.gina.expressMenu.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    @Query("Select s from Schedule s where restaurant.idRestaurant=:idRestaurant ")
    List<Schedule> findAllByRestaurantId(@Param("idRestaurant")Long idRestaurant);
    @Query("Select s from Schedule s where s.day=:day and  restaurant.idRestaurant=:idRestaurant ")
    Schedule findByDayAndIdRestaurant(int day, Long idRestaurant);
}
