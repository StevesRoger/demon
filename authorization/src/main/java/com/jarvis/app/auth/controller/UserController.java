package com.jarvis.app.auth.controller;

import com.jarvis.app.auth.model.entity.ref.OtpType;
import com.jarvis.app.auth.model.request.RegisterUser;
import com.jarvis.app.auth.model.request.ForgetPassword;
import com.jarvis.app.auth.service.UserService;
import com.jarvis.frmk.core.model.http.request.RequestPlain;
import com.jarvis.frmk.core.model.http.response.ResponseJEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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

    @PostMapping(value = "/register")
    public ResponseJEntity registerUser(@RequestBody @Valid RequestPlain<RegisterUser> request) throws Exception {
        return userService.registerUser(request.getData());
    }

    @PostMapping(value = "/send-otp")
    public ResponseJEntity sendOTP(@RequestParam("email") String email, @RequestParam("type") OtpType otpType) {
        return userService.sendOTP(email, otpType);
    }

    @PostMapping(value = "/verify-otp")
    public ResponseJEntity verifyOTP(@RequestParam("otp_refer") String otpRefer, @RequestParam("otp_code") String code) throws Exception {
        return userService.verifyOTP(otpRefer, code);
    }

    @PostMapping(value = "/forget-password")
    public ResponseJEntity resetPassword(@RequestBody ForgetPassword restPasswordRequest) throws Exception {
        return userService.resetPassword(restPasswordRequest);
    }

    @PostMapping(value = "/change-password")
    public ResponseJEntity changePassword(@RequestBody ChangePasswordRequest request) throws Exception {
        return userService.changePassword(request);
    }

    @PostMapping(value = "/login")
    public ResponseJEntity login(@RequestBody @Valid LoginRequest body) {
        return userService.login(body);
    }

    @PostMapping(value = "/logout")
    public ResponseJEntity logout(@RequestBody @Valid LogoutRequest body) {
        return userService.logout(body);
    }

    @PostMapping(value = "/refresh-token")
    public ResponseJEntity refreshToken(@RequestBody @Valid RefreshTokenRequest request) {
        return userService.refreshToken(request);
    }
}
