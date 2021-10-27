package com.demon.auth.utils;

import io.micrometer.core.instrument.util.IOUtils;
import org.apache.commons.codec.binary.Base64;

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

/**
 * Created: KimChheng
 * Date: 27-Oct-2021 Wed
 * Time: 9:58 PM
 */
public final class RSAUtil {

    public static final int LENGTH_256 = 256;
    public static final int LENGTH_512 = 512;
    public static final int LENGTH_576 = 576;
    public static final int LENGTH_1024 = 1024;
    public static final int LENGTH_2048 = 2048;

    public static final String RSA = "RSA";
    public static final String DSA = "DSA";
    public static final String DH = "DH";

    public static final String BEGIN_PUBLIC_KEY = "-----BEGIN PUBLIC KEY-----\n";
    public static final String END_PUBLIC_KEY = "\n-----END PUBLIC KEY-----\n";

    public static final String BEGIN_PRIVATE_KEY = "-----BEGIN PRIVATE KEY-----\n";
    public static final String END_PRIVATE_KEY = "\n-----END PRIVATE KEY-----\n";

    public static PublicKey base64ToPublicKey(String keyAlgorithm, InputStream inputStream) throws InvalidKeySpecException, IOException, NoSuchAlgorithmException {
        return base64ToPublicKey(keyAlgorithm, IOUtils.toString(inputStream, StandardCharsets.UTF_8));
    }

    public static PublicKey base64ToPublicKey(String keyAlgorithm, String base64PublicKey) throws InvalidKeySpecException, NoSuchAlgorithmException {
        String temp = base64PublicKey;
        temp = temp.replace(BEGIN_PUBLIC_KEY, "");
        temp = temp.replace(END_PUBLIC_KEY, "");
        return byteToPublicKey(keyAlgorithm, Base64.decodeBase64(temp));
    }

    public static PrivateKey base64ToPrivateKey(String keyAlgorithm, InputStream inputStream) throws InvalidKeySpecException, IOException, NoSuchAlgorithmException {
        return base64ToPrivateKey(keyAlgorithm, IOUtils.toString(inputStream, StandardCharsets.UTF_8));
    }

    public static PrivateKey base64ToPrivateKey(String keyAlgorithm, String base64PrivateKey) throws InvalidKeySpecException, NoSuchAlgorithmException {
        String temp = base64PrivateKey;
        temp = temp.replace(BEGIN_PRIVATE_KEY, "");
        temp = temp.replace(END_PRIVATE_KEY, "");
        return byteToPrivateKey(keyAlgorithm, Base64.decodeBase64(temp));
    }

    public static PrivateKey byteToPrivateKey(String keyAlgorithm, byte[] bytes) throws InvalidKeySpecException, NoSuchAlgorithmException {
        KeyFactory keyFactory = KeyFactory.getInstance(keyAlgorithm);
        return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(bytes));
    }

    public static PublicKey byteToPublicKey(String keyAlgorithm, byte[] bytes) throws InvalidKeySpecException, NoSuchAlgorithmException {
        KeyFactory keyFactory = KeyFactory.getInstance(keyAlgorithm);
        return keyFactory.generatePublic(new X509EncodedKeySpec(bytes));
    }

    public static String encrypt(String plainText, PublicKey publicKey) throws GeneralSecurityException, UnsupportedEncodingException {
        return encrypt(RSA, plainText, publicKey);
    }

    public static String encrypt(String keyAlgorithm, String plainText, PublicKey publicKey) throws GeneralSecurityException, UnsupportedEncodingException {
        Cipher cipherEncrypt = Cipher.getInstance(keyAlgorithm);
        cipherEncrypt.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] bytes = cipherEncrypt.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        return Base64.encodeBase64String(bytes);
    }

    public static String decrypt(String text, PrivateKey privateKey) throws GeneralSecurityException, UnsupportedEncodingException {
        return decrypt(RSA, text, privateKey);
    }

    public static String decrypt(String keyAlgorithm, String text, PrivateKey privateKey) throws GeneralSecurityException, UnsupportedEncodingException {
        byte[] bytes = Base64.decodeBase64(text);
        Cipher cipherDecrypt = Cipher.getInstance(keyAlgorithm);
        cipherDecrypt.init(Cipher.DECRYPT_MODE, privateKey);
        return new String(cipherDecrypt.doFinal(bytes), StandardCharsets.UTF_8);
    }

    public static RSAPublicKey readPublicKey(InputStream inputStream) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        return readPublicKey(IOUtils.toString(inputStream, StandardCharsets.UTF_8));
    }

    public static RSAPrivateKey readPrivateKey(InputStream inputStream) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        return readPrivateKey(IOUtils.toString(inputStream, StandardCharsets.UTF_8));
    }

    public static RSAPublicKey readPublicKey(String content) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String publicKeyPEM = content
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replaceAll(System.lineSeparator(), "")
                .replace("-----END PUBLIC KEY-----", "");
        byte[] encoded = Base64.decodeBase64(publicKeyPEM);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
        return (RSAPublicKey) keyFactory.generatePublic(keySpec);
    }

    public static RSAPrivateKey readPrivateKey(String content) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String privateKeyPEM = content
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replaceAll(System.lineSeparator(), "")
                .replace("-----END PRIVATE KEY-----", "");
        byte[] encoded = Base64.decodeBase64(privateKeyPEM);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
        return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
    }
}