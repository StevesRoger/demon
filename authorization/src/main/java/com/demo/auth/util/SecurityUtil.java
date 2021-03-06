package com.demo.auth.util;

import io.micrometer.core.instrument.util.IOUtils;
import org.apache.commons.codec.binary.Base64;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import javax.crypto.Cipher;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public interface SecurityUtil {

    String RSA = "RSA";

    String BEGIN_PUBLIC_KEY = "-----BEGIN PUBLIC KEY-----\n";
    String END_PUBLIC_KEY = "\n-----END PUBLIC KEY-----\n";

    String BEGIN_PRIVATE_KEY = "-----BEGIN PRIVATE KEY-----\n";
    String END_PRIVATE_KEY = "\n-----END PRIVATE KEY-----\n";

    static PublicKey base64ToPublicKey(String keyAlgorithm, InputStream inputStream) throws InvalidKeySpecException, IOException, NoSuchAlgorithmException {
        return base64ToPublicKey(keyAlgorithm, IOUtils.toString(inputStream, StandardCharsets.UTF_8));
    }

    static PublicKey base64ToPublicKey(String keyAlgorithm, String base64PublicKey) throws InvalidKeySpecException, NoSuchAlgorithmException {
        String temp = base64PublicKey;
        temp = temp.replace(BEGIN_PUBLIC_KEY, "");
        temp = temp.replace(END_PUBLIC_KEY, "");
        return byteToPublicKey(keyAlgorithm, Base64.decodeBase64(temp));
    }

    static PrivateKey base64ToPrivateKey(String keyAlgorithm, InputStream inputStream) throws InvalidKeySpecException, IOException, NoSuchAlgorithmException {
        return base64ToPrivateKey(keyAlgorithm, IOUtils.toString(inputStream, StandardCharsets.UTF_8));
    }

    static PrivateKey base64ToPrivateKey(String keyAlgorithm, String base64PrivateKey) throws InvalidKeySpecException, NoSuchAlgorithmException {
        String temp = base64PrivateKey;
        temp = temp.replace(BEGIN_PRIVATE_KEY, "");
        temp = temp.replace(END_PRIVATE_KEY, "");
        return byteToPrivateKey(keyAlgorithm, Base64.decodeBase64(temp));
    }

    static PrivateKey byteToPrivateKey(String keyAlgorithm, byte[] bytes) throws InvalidKeySpecException, NoSuchAlgorithmException {
        KeyFactory keyFactory = KeyFactory.getInstance(keyAlgorithm);
        return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(bytes));
    }

    static PublicKey byteToPublicKey(String keyAlgorithm, byte[] bytes) throws InvalidKeySpecException, NoSuchAlgorithmException {
        KeyFactory keyFactory = KeyFactory.getInstance(keyAlgorithm);
        return keyFactory.generatePublic(new X509EncodedKeySpec(bytes));
    }

    static String encrypt(String plainText, PublicKey Key) throws GeneralSecurityException, UnsupportedEncodingException {
        return encrypt(RSA, plainText, Key);
    }

    static String encrypt(String keyAlgorithm, String plainText, PublicKey Key) throws GeneralSecurityException, UnsupportedEncodingException {
        Cipher cipherEncrypt = Cipher.getInstance(keyAlgorithm);
        cipherEncrypt.init(Cipher.ENCRYPT_MODE, Key);
        byte[] bytes = cipherEncrypt.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        return Base64.encodeBase64String(bytes);
    }

    static String decrypt(String text, PrivateKey privateKey) throws GeneralSecurityException, UnsupportedEncodingException {
        return decrypt(RSA, text, privateKey);
    }

    static String decrypt(String keyAlgorithm, String text, PrivateKey privateKey) throws GeneralSecurityException, UnsupportedEncodingException {
        byte[] bytes = Base64.decodeBase64(text);
        Cipher cipherDecrypt = Cipher.getInstance(keyAlgorithm);
        cipherDecrypt.init(Cipher.DECRYPT_MODE, privateKey);
        return new String(cipherDecrypt.doFinal(bytes), StandardCharsets.UTF_8);
    }

    static RSAPublicKey readPublicKey(InputStream inputStream) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        return readPublicKey(IOUtils.toString(inputStream, StandardCharsets.UTF_8));
    }

    static RSAPrivateKey readPrivateKey(InputStream inputStream) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        return readPrivateKey(IOUtils.toString(inputStream, StandardCharsets.UTF_8));
    }

    static RSAPublicKey readPublicKey(String content) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String KeyPEM = content
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replaceAll(System.lineSeparator(), "")
                .replace("-----END PUBLIC KEY-----", "");
        byte[] encoded = Base64.decodeBase64(KeyPEM);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
        return (RSAPublicKey) keyFactory.generatePublic(keySpec);
    }

    static RSAPrivateKey readPrivateKey(String content) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String privateKeyPEM = content
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replaceAll(System.lineSeparator(), "")
                .replace("-----END PRIVATE KEY-----", "");
        byte[] encoded = Base64.decodeBase64(privateKeyPEM);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
        return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
    }

    static String encodeBasicHeader(String header) throws UnsupportedEncodingException {
        return Base64.encodeBase64String(header.getBytes(StandardCharsets.UTF_8));
    }

    static String[] extractAndDecodeBasicHeader(String header) throws IOException {
        try {
            byte[] base64Token = header.substring(6).getBytes(StandardCharsets.UTF_8);
            byte[] decoded = Base64.decodeBase64(base64Token);
            String token = new String(decoded, StandardCharsets.UTF_8);
            return token.split(":");
        } catch (Exception e) {
            throw new BadCredentialsException("Failed to decode basic authentication token");
        }
    }

    static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    static String getAuthenticateUsername() {
        Authentication authentication = getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            User principal = (User) authentication.getPrincipal();
            return principal.getUsername();
        }
        return "anonymous";
    }

}