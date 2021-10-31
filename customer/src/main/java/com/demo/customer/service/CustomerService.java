package com.demo.customer.service;

import com.demo.customer.domain.entity.Customer;
import com.demo.customer.domain.entity.Photo;
import com.demo.customer.domain.request.Register;
import com.demo.customer.domain.response.ResponseBody;
import com.demo.customer.helper.SecurityHelper;
import com.demo.customer.repository.CustomerRepository;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class CustomerService {

    @Value("${oauth2.remote.client-id}")
    private String clientId;

    @Value("${oauth2.remote.client-secret}")
    private String clientSecret;

    @Value("${auth-url}")
    private String authBaseUrl;

    @Value("${default-profile}")
    private String defaultProfile;

    @Autowired
    private CustomerRepository repository;

    public ResponseEntity<?> getCustomer() {
        return ResponseEntity.ok(new ResponseBody("Successful", findById()));
    }

    public HttpEntity<byte[]> viewPhotoProfile() {
        Customer customer = findById();
        Photo photo = customer.getPhoto();
        byte[] bytes = Base64.getDecoder().decode(photo.getContent());
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Disposition", "inline; filename=\"" + photo.getName() + "\"");
        headers.set("Content-Type", photo.getMineType());
        headers.setContentLength(bytes.length);
        return new HttpEntity<>(bytes, headers);
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> register(MultipartFile file, Register request) throws IOException {
        Customer customer = new Customer();
        customer.setEmail(request.getEmail());
        customer.setName(request.getName());
        customer.setPhone(request.getPhone());
        Photo photo = new Photo();
        if (file == null) {
            byte[] bytes = IOUtils.toByteArray(Thread.currentThread().getContextClassLoader().getResourceAsStream(defaultProfile));
            photo.setContent(Base64.getEncoder().encodeToString(bytes));
            photo.setMineType("image/" + FilenameUtils.getExtension(defaultProfile));
            photo.setName(FilenameUtils.getName(defaultProfile));
        } else {
            photo.setMineType(file.getContentType());
            photo.setName(file.getOriginalFilename());
            photo.setContent(Base64.getEncoder().encodeToString(file.getBytes()));
        }
        customer.setPhoto(photo);
        repository.saveAndFlush(customer);
        saveOrUpdateUserAccount(customer, request, "CREATE");
        Map<String, Object> data = new HashMap<>();
        data.put("customer_id", customer.getId());
        return ResponseEntity.ok(new ResponseBody("Successful", data));
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> update(MultipartFile file, Register request) throws IOException {
        Customer customer = findById();
        customer.setEmail(request.getEmail());
        customer.setName(request.getName());
        customer.setPhone(request.getPhone());
        updatePhoto(customer, file);
        repository.saveAndFlush(customer);
        saveOrUpdateUserAccount(customer, request, "UPDATE");
        return ResponseEntity.ok(new ResponseBody("Successful"));
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> updatePhoto(MultipartFile file) throws IOException {
        Customer customer = findById();
        updatePhoto(customer, file);
        repository.save(customer);
        return ResponseEntity.ok(new ResponseBody("Successful"));
    }

    private Customer findById() {
        Integer customerId = SecurityHelper.getCustomerId();
        return repository.findById(customerId).orElseThrow(() -> new RuntimeException("Customer id " + customerId + " not found"));
    }

    private void updatePhoto(Customer customer, MultipartFile file) throws IOException {
        if (file != null) {
            Photo photo = customer.getPhoto() == null ? new Photo() : customer.getPhoto();
            photo.setName(file.getOriginalFilename());
            photo.setMineType(file.getContentType());
            photo.setContent(Base64.getEncoder().encodeToString(file.getBytes()));
            customer.setPhoto(photo);
        }
    }

    private void saveOrUpdateUserAccount(Customer customer, Register request, String method) {
        Map<Object, Object> data = new HashMap<>();
        data.put("username", request.getUsername());
        data.put("password", request.getPassword());
        data.put("customer_id", customer.getId());
        data.put("method", method);
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(clientId, clientSecret);
        headers.setContentType(MediaType.APPLICATION_JSON);
        new RestTemplate().postForObject(authBaseUrl + "/system/account", new HttpEntity<>(data, headers), Map.class);
    }
}
