package com.demon.auth.controller;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import com.demon.auth.service.OAuth2Service;
import com.demon.auth.service.UserService;
import com.jarvis.frmk.core.I18N;
import com.jarvis.frmk.core.model.http.response.ResponseJEntity;
import com.jarvis.frmk.core.util.AppContextUtil;
import com.jarvis.frmk.core.util.StringUtil;
import com.jarvis.frmk.security.RSASecurityProvider;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(value = "/system", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class SystemController {

    @Autowired
    private RSASecurityProvider rsaSecurity;

    @Autowired
    private OAuth2Service auth2Service;

    @Autowired
    private UserService userService;

    /*@PutMapping(value = "/user", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseJEntity updateUser() {

    }*/

    @PostMapping("/encrypt")
    public ResponseJEntity encryptRSAPassword(@RequestParam("pwd") String rawPassword) throws Exception {
        return ResponseJEntity.ok("Encrypt successful").jsonObject("pwd", rsaSecurity.encryptText(rawPassword)).build();
    }

    @GetMapping("/tokens")
    public ResponseJEntity listTokens(@RequestParam(value = "client_id") String clientId,
                                      @RequestParam(value = "username", required = false) String username) {
        return ResponseJEntity.ok(I18N.getMessage("successful"),
                StringUtil.isEmpty(username) ?
                        auth2Service.listTokens(clientId) :
                        auth2Service.listTokens(clientId, username));
    }

    @DeleteMapping("/revoke-token")
    public ResponseJEntity revokeToken(@RequestParam("token") String token) {
        Optional<OAuth2AccessToken> auth = auth2Service.revokeToken(token);
        return ResponseJEntity.ok(I18N.getMessage(auth == null ? "error.token.not.found" : "successful"), auth);
    }

    /*@GetMapping(value = "/user")
    public ResponseJEntity getUserById(@RequestParam(value = "id", required = false) Integer userId) {
        return userService.getUserById(userId);
    }*/

    @GetMapping("/test")
    public ResponseJEntity test(@RequestBody @Valid Map map) throws GeneralSecurityException, UnsupportedEncodingException {
        String a = "ED12siKsx+f5OpzRpEuR07mKkLs7i66r5KBNBtr0Qwgh4JFzx5q3Fu84DwuhGWXhrlHaaJM2s9+a\n" +
                "RL/JvueIq2W3KD9I6S+CUOaOlqsi8tNT3DAX80oIelsRj7bv6LDlzo7KIsQYdyLzF04rVO++WrEk\n" +
                "a8c8MGwDMACmjd8Dn18=\n";
        // a = StringUtils.normalizeSpace(a);
        // a = new String(rsaSecurity.decrypt(a.getBytes()));
        // byte[]x= rsaSecurity.encrypt("hello".getBytes(ICore.UTF_8));
        // byte[]y= rsaSecurity.decrypt(x);
        byte[] x = "Hello".getBytes();
        System.out.println(new String(x));
        System.out.println(a);
        return ResponseJEntity.ok("{}");
    }

    @PostMapping("/changeloglevel")
    public void changeloglevel(@RequestParam String loglevel, @RequestParam String name) {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        loggerContext.getLogger(name).setLevel(Level.toLevel(loglevel));
    }

    @GetMapping("/cup")
    public ResponseJEntity testProp(@RequestParam String key, @RequestParam Object value) {
        AppContextUtil.putProperty(key, value);
        return ResponseJEntity.ok("Success");
    }
}
