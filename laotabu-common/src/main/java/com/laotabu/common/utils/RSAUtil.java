package com.laotabu.common.utils;

import org.apache.commons.codec.binary.Base64;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;

@Component
public class RSAUtil {
    public static String publicKey;
    public static String privateKey;


//    /**
//     * 公钥解密
//     *
//     * @param publicKeyString 公钥
//     * @param text            待解密的信息
//     * @return 解密后的文本
//     */
//    public static String decryptByPublicKey(String publicKeyString, String text) throws Exception {
//        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKeyString));
//        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//        PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
//        Cipher cipher = Cipher.getInstance("RSA");
//        cipher.init(Cipher.DECRYPT_MODE, publicKey);
//        byte[] result = cipher.doFinal(Base64.decodeBase64(text));
//        return new String(result);
//    }

//    /**
//     * 私钥加密
//     *
//     * @param privateKeyString 私钥
//     * @param text             待加密的信息
//     * @return 加密后的文本
//     */
//    public String encryptByPrivateKey(String privateKeyString, String text) throws Exception {
//        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKeyString));
//        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
//        Cipher cipher = Cipher.getInstance("RSA");
//        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
//        byte[] result = cipher.doFinal(text.getBytes());
//        return Base64.encodeBase64String(result);
//    }

    /**
     * 私钥解密
     *
     * @param text             待解密的文本
     * @return 解密后的文本
     */
    public static String decryptByPrivateKey(String text) throws Exception {
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec5 = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKeyStr = keyFactory.generatePrivate(pkcs8EncodedKeySpec5);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKeyStr);
        byte[] result = cipher.doFinal(Base64.decodeBase64(text));
        return new String(result);
    }

//    /**
//     * 公钥加密
//     *
//     * @param publicKeyString 公钥
//     * @param text            待加密的文本
//     * @return 加密后的文本
//     */
//    public static String encryptByPublicKey(String publicKeyString, String text) throws Exception {
//        X509EncodedKeySpec x509EncodedKeySpec2 = new X509EncodedKeySpec(Base64.decodeBase64(publicKeyString));
//        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//        PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec2);
//        Cipher cipher = Cipher.getInstance("RSA");
//        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
//        byte[] result = cipher.doFinal(text.getBytes());
//        return Base64.encodeBase64String(result);
//    }

    /**
     * 构建RSA密钥对
     *
     * @return 生成后的公私钥信息
     */
    @Bean
    public void generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(1024, new SecureRandom());
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
        publicKey =  Base64.encodeBase64String(rsaPublicKey.getEncoded());;
        privateKey = Base64.encodeBase64String(rsaPrivateKey.getEncoded());
    }




}