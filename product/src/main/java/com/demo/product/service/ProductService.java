package com.demo.product.service;

import com.demo.product.domain.entity.Policy;
import com.demo.product.domain.entity.Product;
import com.demo.product.domain.entity.Status;
import com.demo.product.domain.response.ResponseBody;
import com.demo.product.repository.PolicyRepository;
import com.demo.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private PolicyRepository policyRepo;

    public ResponseEntity<?> listProducts() {
        List<Product> all = productRepo.findAllByStatus(Status.ACTIVE);
        return ResponseEntity.ok(new ResponseBody("Successful", all));
    }

    public ResponseEntity<?> getProduct(int id) {
        Optional<Product> optProduct = productRepo.findById(id);
        return optProduct.map(product -> ResponseEntity.ok(new ResponseBody("Successful", product)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ResponseBody.fail("E400", "Product id " + id + " not found")));
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> purchase(int id) {
        Product product = productRepo.findById(id).orElseThrow(() -> new RuntimeException("Product id " + id + " not found"));
        Policy policy = new Policy(product, SecurityContextHolder.getContext().getAuthentication().getName());
        policyRepo.save(policy);
        if (Objects.isNull(policy.getProduct())) throw new RuntimeException("Purchase failed");
        Map<String, Object> data = new HashMap<>();
        data.put("policy_id", policy.getId());
        data.put("product_id", product.getId());
        data.put("owner", policy.getOwner());
        return ResponseEntity.ok(new ResponseBody("Successful", data));
    }


    public ResponseEntity<?> listPurchasedPolicy() {
        List<Policy> policies = policyRepo.findAllByOwner(SecurityContextHolder.getContext().getAuthentication().getName());
        Map<String, Object> data = new HashMap<>();
        data.put("policies", policies);
        return ResponseEntity.ok(new ResponseBody("Successful", data));
    }
}
