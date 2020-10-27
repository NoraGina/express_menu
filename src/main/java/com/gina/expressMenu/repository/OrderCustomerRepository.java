package com.gina.expressMenu.repository;

import com.gina.expressMenu.model.OrderCustomer;
import com.gina.expressMenu.model.OrderItem;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderCustomerRepository extends JpaRepository<OrderCustomer, Long> {

    @Query("Select o from OrderCustomer o where restaurant.idRestaurant=:idRestaurant")
     List<OrderCustomer>findAllByIdRestaurant(@Param("idRestaurant") Long idRestaurant);
}
