package com.demo.auth.controller;

import com.demo.auth.domain.request.Register;
import com.demo.auth.service.OAuth2Service;
import com.demo.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/system", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class SystemController {

    @Autowired
    private UserService userService;

    @Autowired
    private OAuth2Service auth2Service;

    @PostMapping(value = "/account", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> createOrUpdateUserAccount(@RequestBody @Valid Register request) throws Exception {
        return userService.createOrUpdateUserAccount(request);
    }

    @GetMapping("/{clientId}")
    public ResponseEntity<?> loadClientDetail(@PathVariable String clientId) {
        return auth2Service.loadClientDetail(clientId);
    }

    @PostMapping(value = "/check_password", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<?> verifyPassword(@RequestParam("raw") String rawPassword, @RequestParam("encode") String encodedPassword) {
        return auth2Service.verifyPassword(rawPassword, encodedPassword);
    }
}
