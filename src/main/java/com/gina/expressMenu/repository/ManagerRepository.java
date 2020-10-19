package com.gina.expressMenu.repository;

import com.gina.expressMenu.model.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagerRepository extends JpaRepository<Manager, Long> {
    Manager findByEmailAndPassword(String email, String password);
}
