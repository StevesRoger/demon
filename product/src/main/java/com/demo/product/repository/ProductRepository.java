package com.demo.product.repository;

import com.demo.product.domain.entity.Product;
import com.demo.product.domain.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findAllByStatus(Status status);
}
