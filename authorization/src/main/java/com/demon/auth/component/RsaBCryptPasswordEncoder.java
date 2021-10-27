package com.demon.auth.component;

import com.demon.auth.utils.RSAUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.PrivateKey;
import java.security.SecureRandom;

/**
 * Created: KimChheng
 * Date: 27-Oct-2021 Wed
 * Time: 9:55 PM
 */
public class RsaBCryptPasswordEncoder extends BCryptPasswordEncoder {

    private static final Logger LOG = LoggerFactory.getLogger(RsaBCryptPasswordEncoder.class);

    private PrivateKey privateKey;

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

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        try {
            if (StringUtils.isEmpty(rawPassword) || StringUtils.isEmpty(encodedPassword)) {
                LOG.warn("Empty password");
                return false;
            }
            String rawPwd = rawPassword.toString();
            if (Base64.isBase64(rawPwd)) rawPwd = RSAUtil.decrypt(rawPwd, privateKey);
            return super.matches(rawPwd, encodedPassword);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return false;
        }
    }
}