package com.demon.auth.controller;

import com.demon.auth.domain.request.*;
import com.demon.auth.service.OtpService;
import com.demon.auth.service.UserService;
import com.jarvis.frmk.core.model.http.request.RequestPlain;
import com.jarvis.frmk.core.model.http.response.ResponseJEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created: kim chheng
 * Date: 29-Sep-2019 Sun
 * Time: 1:33 PM
 */
@RestController
@RequestMapping(value = "/user", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> login(@RequestHeader("Authorization") String authorization,
                                @RequestBody @Valid RequestPlain<Login> request) throws Exception {
        return userService.login(request.getData(), authorization);
    }
}
