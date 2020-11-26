package com.gina.expressMenu.repository;

import com.gina.expressMenu.model.OrderCustomer;
import com.gina.expressMenu.model.OrderItem;


import com.gina.expressMenu.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface OrderCustomerRepository extends JpaRepository<OrderCustomer, Long> {

    @Query("Select o from OrderCustomer o where restaurant.idRestaurant=:idRestaurant")
     List<OrderCustomer>findAllByIdRestaurant(@Param("idRestaurant") Long idRestaurant);

    @Query("Select o from OrderCustomer o where restaurant.idRestaurant=:idRestaurant and o.date=:date")
    List<OrderCustomer>findAllByIdRestaurantAndDate(@Param("idRestaurant") Long idRestaurant, @Param("date")LocalDate date);

    @Query("Select o from OrderCustomer o where restaurant.idRestaurant=:idRestaurant and o.status=:status")
    List<OrderCustomer>findAllByIdRestaurantAndStatus(@Param("idRestaurant") Long idRestaurant, @Param("status")Status status);

    @Query("Select o from OrderCustomer o where " +
            "restaurant.idRestaurant=:idRestaurant and o.date=:date and o.status=:status")
    List<OrderCustomer>findAllByIdRestaurantDateAndStatus
            (@Param("idRestaurant") Long idRestaurant, @Param("date")LocalDate date, @Param("status")Status status);
}
