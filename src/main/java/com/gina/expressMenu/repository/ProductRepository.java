package com.gina.expressMenu.repository;

import com.gina.expressMenu.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("Select p from Product p where restaurant.idRestaurant=:idRestaurant ")
    List<Product> findAllByRestaurantId(@Param("idRestaurant")Long idRestaurant);
    @Query("Select p from Product p where restaurant.idRestaurant=:idRestaurant and restaurant.manager.idManager=:idManager")
    List<Product>findAllByReststaurantIdAndManagerId(@Param("idRestaurant")Long idRestaurant, @Param("idManager")Long idManager);
}
