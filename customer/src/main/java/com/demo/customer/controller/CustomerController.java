package com.demo.customer.controller;

import com.demo.customer.domain.request.Register;
import com.demo.customer.service.CustomerService;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class CustomerController {

    @Autowired
    private CustomerService service;

    @PreAuthorize("#oauth2.hasScope('read')")
    @GetMapping("/me")
    public ResponseEntity<?> getCustomer() {
        return service.getCustomer();
    }

    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> register(@RequestPart(required = false) MultipartFile photo, @RequestPart String json) throws IOException {
        return service.register(photo, new ObjectMapper().readValue(json, Register.class));
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> register(@RequestBody Register request) throws IOException {
        return service.register(null, request);
    }

    @PutMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> update(@RequestPart(required = false) MultipartFile photo, @RequestPart String json) throws IOException {
        return service.update(photo, new ObjectMapper().readValue(json, Register.class));
    }

    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> update(@RequestBody Register request) throws IOException {
        return service.update(null, request);
    }

    @PreAuthorize("#oauth2.hasScope('write')")
    @PutMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updatePhoto(@RequestPart MultipartFile photo) throws IOException {
        return service.updatePhoto(photo);
    }

    @GetMapping(value = "/photo", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public HttpEntity<byte[]> viewPhotoProfile() {
        return service.viewPhotoProfile();
    }
}
