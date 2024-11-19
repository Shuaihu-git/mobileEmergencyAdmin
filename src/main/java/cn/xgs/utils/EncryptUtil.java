package cn.xgs.utils;

import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentStringPBEConfig;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;


/**
 * @author Xiaohu
 * Jasypt加密解密工具类
 */
@Slf4j
public class EncryptUtil {
    private static final String PBEWITHMD5ANDDES = "PBEWithMD5AndDES";

    private static final String PBEWITHHMACSHA512ANDAES_256 = "PBEWITHHMACSHA512ANDAES_256";

    public static void urlAndUsernameAndPassword(String url, String username, String password,String salt) {
        //BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        //PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        //// 加密秘钥(盐)
        //encryptor.setPassword("!qaz@wsx#edc");
        // 要加密的数据（数据库的用户名或密码）
        //String username1 = encryptor.encrypt(username);
        //String password1 = encryptor.encrypt(password);
        //String url1 = encryptor.encrypt(url);
        //System.out.println("url:" + url1);
        //System.out.println("username:" + username1);
        //System.out.println("password:" + password1);

        String url1 = EncryptUtil.encryptWithSHA512(url, salt);
        String username1 = EncryptUtil.encryptWithSHA512(username, salt);
        String password1 = EncryptUtil.encryptWithSHA512(password, salt);
        log.info("url1:{}",url1);
        log.info("username1:{}",username1);
        log.info("password1:{}",password1);
    }

    public static void main(String[] args) {
        //生成密文
//        String hyperwaf = decryptWithSHA512("", "hyperwaf");
//        System.out.println(hyperwaf);
        String hyperwaf = encryptWithSHA512("", "hyperwaf");
        log.info(hyperwaf);


    }

    /**
     * @param text  待加密原文
     * @param crack 盐值（密钥）
     * @return 加密后的字符串
     * @Description: Jasypt加密（PBEWithMD5AndDES）
     */
    public static String encryptWithMD5(String text, String crack) {
        //1.创建加解密工具实例
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        //2.加解密配置
        EnvironmentStringPBEConfig config = new EnvironmentStringPBEConfig();
        config.setAlgorithm(PBEWITHMD5ANDDES);
        config.setPassword(crack);
        encryptor.setConfig(config);
        //3.加密
        return encryptor.encrypt(text);
    }

    /**
     * @param text  待解密原文
     * @param crack 盐值（密钥）
     * @return 解密后的字符串
     * @Description: Jasypt解密（PBEWithMD5AndDES）
     */
    public static String decryptWithMD5(String text, String crack) {
        //1.创建加解密工具实例
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        //2.加解密配置
        EnvironmentStringPBEConfig config = new EnvironmentStringPBEConfig();
        config.setAlgorithm(PBEWITHMD5ANDDES);
        config.setPassword(crack);
        encryptor.setConfig(config);
        //解密
        return encryptor.decrypt(text);
    }

    /**
     * @param text  待加密的原文
     * @param crack 盐值（密钥）
     * @return 加密后的字符串
     * @Description: jasypt 加密（PBEWITHHMACSHA512ANDAES_256）
     */
    public static String encryptWithSHA512(String text, String crack) {
        //1.创建加解密工具实例
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        //2.加解密配置
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(crack);
        config.setAlgorithm(PBEWITHHMACSHA512ANDAES_256);
        // 为减少配置文件的书写，以下都是 Jasypt 3.x 版本，配置文件默认配置
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator");
        config.setStringOutputType("base64");
        encryptor.setConfig(config);
        //3.加密
        return encryptor.encrypt(text);
    }

    /**
     * @param text  待解密原文
     * @param crack 盐值（密钥）
     * @return 解密后的字符串
     * @Description: jasypt 解密（PBEWITHHMACSHA512ANDAES_256）
     */
    public static String decryptWithSHA512(String text, String crack) {
        //1.创建加解密工具实例
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        //2.加解密配置
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(crack);
        config.setAlgorithm(PBEWITHHMACSHA512ANDAES_256);
        // 为减少配置文件的书写，以下都是 Jasypt 3.x 版本，配置文件默认配置
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator");
        config.setStringOutputType("base64");
        encryptor.setConfig(config);
        //3.解密
        return encryptor.decrypt(text);
    }

}
