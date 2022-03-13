package com.geek.geekstudio.util;

import org.bouncycastle.util.encoders.Hex;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Security;

/**
 * 对用户密码加密解密用到（数据库中存储的也应该是AES加密后的文本）
 * 比较好的思路：
 * 前端对密码做第一次加盐MD5（防止密码明文传输），后端对md5后的字符串进行第二次加盐MD5（AES等等也行）.
 * 然后再把结果拿去和数据库比较。
 *
 * 对称加密 AES的工具类
 */
public class AESUtil {


    //初始向量（偏移）
    public static final String IV = "ImH@ckErabcdefgh";   //AES 为16bytes. DES 为8bytes

    //编码方式
    public static final String bm = "UTF-8";

    //私钥  （密钥）
    private static final String ASE_KEY="GeEk_1s_AwEs0Me!";   //AES固定格式为128/192/256 bits.即：16/24/32bytes。DES固定格式为128bits，即8bytes。

    /**
     * 加密
     * @param cleartext 加密前的字符串
     * @return 加密后的字符串
     */
    public static String encrypt(String cleartext) {

        //------------------------------------------AES加密-------------------------------------
        //加密方式： AES128(CBC/PKCS7Padding) + Base64, 私钥：
        try {
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            IvParameterSpec zeroIv = new IvParameterSpec(IV.getBytes(bm));
            //两个参数，第一个为私钥字节数组， 第二个为加密方式 AES或者DES
            SecretKeySpec key = new SecretKeySpec(ASE_KEY.getBytes(bm), "AES");
            //实例化加密类，参数为加密方式，要写全
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding"); //PKCS5Padding比PKCS7Padding效率高，PKCS7Padding可支持IOS加解密
            //初始化，此方法可以采用三种方式，按加密算法要求来添加。（1）无第三个参数（2）第三个参数为SecureRandom random = new SecureRandom();中random对象，随机数。(AES不可采用这种方法)（3）采用此代码中的IVParameterSpec
            cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
            //------------------------------------------base64编码-------------------------------------
            //加密操作,返回加密后的字节数组，然后需要编码。主要编解码方式有Base64, HEX, UUE,7bit等等。此处看服务器需要什么编码方式
            //byte[] encryptedData = cipher.doFinal(cleartext.getBytes(bm));
            //return new BASE64Encoder().encode(encryptedData);
            //上下等同，只是导入包不同
            //加密后的字节数组
            byte[] encryptedData = cipher.doFinal(cleartext.getBytes(bm));
            //对加密后的字节数组进行base64编码
            //byte[] base64Data = org.apache.commons.codec.binary.Base64.encodeBase64(encryptedData);
            //将base64编码后的字节数组转化为字符串并返回
            return Hex.toHexString(encryptedData).toUpperCase();
            //------------------------------------------/base64编码-------------------------------------
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 解密
     * @param encrypted 解密前的字符串（也就是加密后的字符串）
     * @return 解密后的字符串（也就是加密前的字符串）
     */
    public static String decrypt(String encrypted) {
        //---------------------------------------AES解密----------------------------------------
        try {
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            //---------------------------------------base64解码---------------------------------------
            //byte[] byteMi = new BASE64Decoder().decodeBuffer(encrypted);
            //上下等同，只是导入包不同
            //将字符串转化为base64编码的字节数组
//            byte[] encryptedBase64Bytes = encrypted.getBytes();
            //将base64编码的字节数组转化为在加密之后的字节数组
            byte[] byteMi = Hex.decode(encrypted);
            //---------------------------------------/base64解码---------------------------------------
            IvParameterSpec zeroIv = new IvParameterSpec(IV.getBytes(bm));
            SecretKeySpec key = new SecretKeySpec(
                    ASE_KEY.getBytes(bm), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            //与加密时不同MODE:Cipher.DECRYPT_MODE
            cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
            byte[] decryptedData = cipher.doFinal(byteMi);
            return new String(decryptedData, bm);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void main(String[] args) {
        System.out.println(decrypt("6D257F9D3882DB6553E1A929746C7D38"));
    }

}