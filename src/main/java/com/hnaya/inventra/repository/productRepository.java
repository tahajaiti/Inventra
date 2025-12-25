package com.hnaya.inventra.repository;

import com.hnaya.inventra.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface productRepository extends JpaRepository<Product, Long> {
}
