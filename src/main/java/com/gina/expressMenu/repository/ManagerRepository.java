package com.gina.expressMenu.repository;

import com.gina.expressMenu.model.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ManagerRepository extends JpaRepository<Manager, Long> {
    Manager findByEmailAndPassword(String email, String password);
    @Query("select count(*) from Manager m where m.email =:email and m.password =:password")
    long countByEmailAndPassword(@Param("email") String email, @Param("password") String password);
}
