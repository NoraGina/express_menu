package com.gina.expressMenu.repository;

import com.gina.expressMenu.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByEmailAndPassword(String email, String password);
    @Query("select count(*) from Customer c where c.email =:email and c.password =:password")
    long countByEmailAndPassword(@Param("email") String email, @Param("password") String password);
}
