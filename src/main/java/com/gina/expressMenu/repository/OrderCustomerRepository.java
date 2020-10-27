package com.gina.expressMenu.repository;

import com.gina.expressMenu.model.OrderCustomer;
import com.gina.expressMenu.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderCustomerRepository extends JpaRepository<OrderCustomer, Long> {

}
