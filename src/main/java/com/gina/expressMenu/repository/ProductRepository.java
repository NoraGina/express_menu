package com.gina.expressMenu.repository;

import com.gina.expressMenu.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
