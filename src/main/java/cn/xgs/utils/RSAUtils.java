package cn.xgs.utils;


import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import java.io.IOException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@Component
public class RSAUtils {

    /**
     * Rsa 私钥 也可固定秘钥对
     */
    public static String privateKeys = "";
    private static String backendPublicKey = "";
    private static String backendPrivateKey = "";
    private static final RSAKeyPair rsaKeyPair = new RSAKeyPair();

    /**
     * 私钥解密
     *
     * @param text 待解密的文本
     * @return 解密后的文本
     */
    public static String decryptByPrivateKey(String text) throws Exception {
        return decryptByPrivateKey(getPrivateKey(), text);
    }

    /**
     * 公钥解密
     *
     * @param publicKeyString 公钥
     * @param text            待解密的信息
     * @return 解密后的文本
     */
    public static String decryptByPublicKey(String publicKeyString, String text) throws Exception {
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKeyString));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        byte[] result = cipher.doFinal(Base64.decodeBase64(text));
        return new String(result);
    }

    /**
     * 私钥加密
     *
     * @param privateKeyString 私钥
     * @param text             待加密的信息
     * @return 加密后的文本
     */
    public static String encryptByPrivateKey(String privateKeyString, String text) throws Exception {
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKeyString));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        byte[] result = cipher.doFinal(text.getBytes());
        return Base64.encodeBase64String(result);
    }

    /**
     * 私钥解密
     *
     * @param privateKeyString 私钥
     * @param text             待解密的文本
     * @return 解密后的文本
     */
    public static String decryptByPrivateKey(String privateKeyString, String text) throws Exception {
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec5 = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKeyString));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec5);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] result = cipher.doFinal(Base64.decodeBase64(text));
        return new String(result);
    }

    /**
     * 公钥加密
     *
     * @param publicKeyString 公钥
     * @param text            待加密的文本
     * @return 加密后的文本
     */
    public static String encryptByPublicKey(String publicKeyString, String text) throws Exception {
        X509EncodedKeySpec x509EncodedKeySpec2 = new X509EncodedKeySpec(Base64.decodeBase64(publicKeyString));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec2);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] result = cipher.doFinal(text.getBytes());
        return Base64.encodeBase64String(result);
    }

    /**
     * 构建RSA密钥对
     *
     * @return 生成后的公私钥信息
     */
    @Bean
    public void generateKeyPair() throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(1024);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
        String publicKeyString = Base64.encodeBase64String(rsaPublicKey.getEncoded());
        String privateKeyString = Base64.encodeBase64String(rsaPrivateKey.getEncoded());
        rsaKeyPair.setBackendPrivateKey(privateKeyString);
        rsaKeyPair.setBackendPublicKey(publicKeyString);
        backendPublicKey = publicKeyString;
        backendPrivateKey = privateKeyString;
    }

    /**
     * 生成RSA
     * @param dirPathFromRoot 基于当前项目的路径
     * @param keySize 密钥长度
     */
    void generate(String dirPathFromRoot, int keySize) throws NoSuchAlgorithmException, IOException {
//        KeyPairGenerator rsa = KeyPairGenerator.getInstance("RSA");
//        rsa.initialize(keySize, new SecureRandom());
//        KeyPair keyPair = rsa.generateKeyPair();
//        PemWriter priPemWriter = new PemWriter(new FileWriter(dirPathFromRoot + "/app.key"));
//        priPemWriter.writeObject(new PemObject("PRIVATE KEY", keyPair.getPrivate().getEncoded()));
//        priPemWriter.close();
//
//        PemWriter pubPemWriter = new PemWriter(new FileWriter(dirPathFromRoot + "/app.pub"));
//        pubPemWriter.writeObject(new PemObject("PUBLIC KEY", keyPair.getPublic().getEncoded()));
//        pubPemWriter.close();
    }


    public static String getPublicKey() {
        return backendPublicKey;
    }

    public static String getPrivateKey() {
        return backendPrivateKey;
    }

    public static RSAKeyPair rsaKeyPair() {
        return rsaKeyPair;
    }

    /**
     * RSA密钥对对象
     */
    public static class RSAKeyPair {
        private String backendPublicKey;
        private String backendPrivateKey;

        public String getBackendPublicKey() {
            return backendPublicKey;
        }

        public void setBackendPublicKey(String backendPublicKey) {
            this.backendPublicKey = backendPublicKey;
        }

        public String getBackendPrivateKey() {
            return backendPrivateKey;
        }

        public void setBackendPrivateKey(String backendPrivateKey) {
            this.backendPrivateKey = backendPrivateKey;
        }

        public RSAKeyPair() {
        }

        public RSAKeyPair(String backendPublicKey, String backendPrivateKey) {
            this.backendPublicKey = backendPublicKey;
            this.backendPrivateKey = backendPrivateKey;
        }
    }
}
