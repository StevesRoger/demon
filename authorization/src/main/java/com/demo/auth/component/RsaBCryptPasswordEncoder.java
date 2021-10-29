package com.demo.auth.component;

import com.demo.auth.util.SecurityUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

public class RsaBCryptPasswordEncoder extends BCryptPasswordEncoder {

    private static final Logger LOG = LoggerFactory.getLogger(RsaBCryptPasswordEncoder.class);

    private PrivateKey privateKey;

    private PublicKey publicKey;

    public RsaBCryptPasswordEncoder() {
        super();
    }

    public RsaBCryptPasswordEncoder(int strength) {
        super(strength);
    }

    public RsaBCryptPasswordEncoder(int strength, SecureRandom random) {
        super(strength, random);
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public String encryptText(String data) throws GeneralSecurityException, UnsupportedEncodingException {
        return SecurityUtil.encrypt(data, publicKey);
    }

    public String decryptText(String encrypt) throws GeneralSecurityException, UnsupportedEncodingException {
        return SecurityUtil.decrypt(encrypt, privateKey);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        try {
            if (StringUtils.isEmpty(rawPassword) || StringUtils.isEmpty(encodedPassword)) {
                LOG.warn("Empty password");
                return false;
            }
            String rawPwd = rawPassword.toString();
            if (Base64.isBase64(rawPwd)) rawPwd = decryptText(rawPwd);
            return super.matches(rawPwd, encodedPassword);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return false;
        }
    }
}