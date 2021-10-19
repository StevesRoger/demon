package com.jarvis.app.auth.controller;

import com.jarvis.app.auth.model.request.*;
import com.jarvis.app.auth.service.OtpService;
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

    @Autowired
    private OtpService otpService;

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseJEntity register(@RequestBody @Valid RequestPlain<UserRegister> request) throws Exception {
        return userService.register(request.getData());
    }

    @PutMapping(value = "/activate")
    public ResponseJEntity activate(@RequestParam("otp_refer") String otpRefer, @RequestParam("otp_code") String otpCode) throws Exception {
        return userService.activate(otpRefer, otpCode);
    }

    @PostMapping(value = "/forget-password", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseJEntity forgetPassword(@RequestBody RequestPlain<ForgetPassword> request) {
        return userService.forgetPassword(request.getData());
    }

    @PutMapping(value = "/reset-password", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseJEntity resetPassword(@RequestBody RequestPlain<RestPassword> request) throws Exception {
        return userService.resetPassword(request.getData());
    }

    @PutMapping(value = "/change-password", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseJEntity changePassword(@RequestBody RequestPlain<ChangePassword> request) throws Exception {
        return userService.changePassword(request.getData());
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseJEntity login(@RequestHeader("Authorization") String authorization,
                                 @RequestBody @Valid RequestPlain<Login> request) throws Exception {
        return userService.login(request.getData(), authorization);
    }

    @PutMapping(value = "/logout")
    public ResponseJEntity logout(@RequestParam("token") String token) {
        return userService.logout(token);
    }

    @PutMapping(value = "/refresh-token")
    public ResponseJEntity refreshToken(@RequestHeader("Authorization") String authorization,
                                        @RequestParam("token") String token) throws Exception {
        return userService.refreshToken(token, authorization);
    }

    @PostMapping(value = "/resend-otp")
    public ResponseJEntity resendOtp(@RequestParam("otp_refer") String otpRefer) {
        return otpService.resendOtp(otpRefer);
    }
}
