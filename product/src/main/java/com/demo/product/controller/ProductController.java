package com.demo.product.controller;

import com.demo.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ProductController {

    @Autowired
    private ProductService service;

    @GetMapping("/list")
    public ResponseEntity<?> listProducts() {
        return service.listProducts();
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getProduct(@PathVariable int id) {
        return service.getProduct(id);
    }

    @GetMapping("/purchase/{id}")
    public ResponseEntity<?> purchase(@PathVariable int id) {
        return service.purchase(id);
    }

    @GetMapping("/policies")
    public ResponseEntity<?> listPurchasedPolicy() {
        return service.listPurchasedPolicy();
    }
}
