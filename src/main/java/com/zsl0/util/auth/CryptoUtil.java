package com.zsl0.util.auth;

import org.apache.commons.codec.digest.DigestUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * @author zsl0
 * create on 2022/10/17 9:15
 */
public class CryptoUtil {

    private static final String ALGORITHM = "DESede"; // 使用的加密算法
    private static final String SECRET = "PUBLICSTATICVOIDMAINSTRINGARGS";    // 加密的盐

    /**
     * md5加密
     * @param encode 明文
     * @return 长度32的字符结果（16进制所表示字符串）
     */
    public static String md5Hex(String encode) {
        byte[] digest = md5(encode.getBytes(StandardCharsets.UTF_8));
        //16是表示转换为16进制数
        return new BigInteger(1, digest).toString(16);
    }

    /**
     * md5加密
     * @param encode 明文
     * @return 长度16的字节结果
     */
    public static byte[] md5(byte[] encode) {
        MessageDigest md5;
        byte[] digest;
        try {
            md5 = MessageDigest.getInstance("md5");
            digest = md5.digest(encode);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        //16是表示转换为16进制数
        return digest;
    }

    /**
     * 对明文加密
     * @param encode 明文
     * @return 加密后的密文字节数组
     */
    public static byte[] encode(byte[] encode) {
        // 加密后的密文
        byte[] result = new byte[0];
        try {
            // 获取实例，设置算法
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            // 初始化，设置加密模式、盐
            cipher.init(Cipher.ENCRYPT_MODE, getKey(SECRET));
            result = cipher.doFinal(encode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 对明文加密并转Base64
     * @param encode 待加密的明文
     * @return 加密后的Base64密文
     */
    public static String encode2Base64(String encode) {
        return Base64.getEncoder().encodeToString(encode(encode.getBytes()));
    }

    /**
     * 获取密钥
     * @param key 盐
     * @return key对象
     */
    public static Key getKey(String key) {
//        return new SecretKeySpec(key.getBytes(), ALGORITHM);
        return getDESedeKey(key.getBytes());
    }

    /**
     * 获取DESede密钥
     * @param key 盐
     * @return DESede密钥
     */
    public static Key getDESedeKey(byte[] key) {
        Key secretKey = null;
        try {
            DESedeKeySpec spec = new DESedeKeySpec(key);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
            secretKey = keyFactory.generateSecret(spec);
        }catch (Exception e){
            e.printStackTrace();
        }
        return secretKey;
    }

    /**
     * 对密文解密
     * @param decode 密文
     * @return 解码后字节数组
     */
    public static byte[] decode(byte[] decode) {
        byte[] result = new byte[0];
        try {
            // 获取实例，设置算法
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            // 初始化，设置解密模式、盐
            cipher.init(Cipher.DECRYPT_MODE, getKey(SECRET));
            // 解密
            result = cipher.doFinal(decode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Base64密文解密
     * @param baseDecode Base64编码密文
     * @return 解密后字符串
     */
    public static String decodeBase64(String baseDecode) {
        // Base64解码 (转String会出现填充的问题导致解密出错)
        byte[] decode = Base64.getDecoder().decode(baseDecode.getBytes());
        // 解密
        byte[] bytes = decode(decode);
        return new String(bytes);
    }

    public static void main(String[] args) {
        String content = "root";
        // 对明文进行加密
        System.out.println("encode: " + new String(CryptoUtil.encode(content.getBytes())));
        // 对明文进行加密并转base64编码
        System.out.println("encode2Base64: " + CryptoUtil.encode2Base64(content));

        String secret = "0s7WyjEXZNzgJwHoBsmtvQ==";
        // Base64密文解密
        System.out.println("decodeBase64: " + CryptoUtil.decodeBase64(secret));

        System.out.println(DigestUtils.md5Hex("admin".getBytes()));


        System.out.println(md5Hex("zsl0"));
        System.out.println(DigestUtils.md5Hex("zsl0".getBytes()));


    }
}
