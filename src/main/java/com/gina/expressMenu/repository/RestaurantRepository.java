package com.gina.expressMenu.repository;

import com.gina.expressMenu.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    @Query("Select r from Restaurant r where manager.idManager=:idManager ")
    List<Restaurant> findAllByManagerId(@Param("idManager")Long idManager);
}
