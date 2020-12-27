package com.gina.expressMenu.repository;

import com.gina.expressMenu.model.OrderItem;
import com.gina.expressMenu.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("Select p from Product p where restaurant.idRestaurant=:idRestaurant ")
    List<Product> findAllByRestaurantId(@Param("idRestaurant")Long idRestaurant);//id IN (:ids)

   @Query("Select p from Product p where restaurant.idRestaurant IN (:idsRestaurant)")
    List<Product>findAllByRestaurantIds(@Param("idsRestaurant")List<Long>idsRestaurant);

   List<Product>findAllByProductName(String productName);

    @Modifying
    @Query(value = "delete from products where id_product=:idProduct",nativeQuery = true)
    void deleteById(@Param("idProduct")Long idProduct);
}
