package com.gina.expressMenu.repository;

import com.gina.expressMenu.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    @Query("Select r from Restaurant r where manager.idManager=:idManager ")
    List<Restaurant> findAllByManagerId(@Param("idManager")Long idManager);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "delete from restaurants where id_restaurant = ?1", nativeQuery = true)
    void deleteById(Long idRestaurant);
}
