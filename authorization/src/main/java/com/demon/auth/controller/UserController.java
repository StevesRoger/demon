package com.demon.auth.controller;

import com.demon.auth.domain.request.Login;
import com.demon.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/user", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> login(@RequestHeader("Authorization") String authorization,
                                   @RequestBody @Valid Login request) throws Exception {
        return userService.login(request, authorization);
    }
}
