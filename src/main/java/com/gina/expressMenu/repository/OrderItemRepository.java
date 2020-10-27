package com.gina.expressMenu.repository;

import com.gina.expressMenu.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {
    @Query("Select o from OrderItem o where orderCustomer.idOrderCustomer=:idOrderCustomer ")
    List<OrderItem> findAllByOrderCustomerId(@Param("idOrderCustomer")Long idOrderCustomer);
}
