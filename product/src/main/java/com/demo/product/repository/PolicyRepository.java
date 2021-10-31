package com.demo.product.repository;

import com.demo.product.domain.entity.Policy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PolicyRepository extends JpaRepository<Policy, Integer> {

    List<Policy> findAllByOwner(String owner);
}
