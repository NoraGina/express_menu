package com.gina.expressMenu.repository;

import com.gina.expressMenu.model.OrderCustomer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderCustomerRepository extends JpaRepository<OrderCustomer, Long> {
}
