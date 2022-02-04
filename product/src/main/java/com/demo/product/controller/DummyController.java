package com.demo.product.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * Created: KimChheng
 * Date: 07-Jan-2022 Fri
 * Time: 3:47 PM
 */
@RestController
@RequestMapping("/dummy")
public class DummyController {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${customer.url}")
    private String customerUrl;

    @GetMapping
    public ResponseEntity<?> test(@RequestHeader("Authorization") String auth) {
        HttpHeaders header = new HttpHeaders();
        header.set("Authorization", auth);
        ResponseEntity<Map> exchange = restTemplate.exchange(customerUrl + "/dummy", HttpMethod.GET, new HttpEntity<>(header), Map.class);
        return ResponseEntity.ok(exchange.getBody());
    }
}
