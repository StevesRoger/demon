package com.demo.auth.controller;

import com.demo.auth.domain.request.Login;
import com.demo.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping(value = "/logout")
    public ResponseEntity<?> logout(@RequestParam("token") String token) {
        return userService.logout(token);
    }

    @PutMapping(value = "/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String authorization,
                                          @RequestParam("token") String token) throws Exception {
        return userService.refreshToken(token, authorization);
    }
}
