package com.sky.happyf.util;

import com.orhanobut.logger.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public final class RSAUtils {
    private static String RSA = "RSA";


    /**
     * 用公钥加密 <br>
     * 每次加密的字节数，不能超过密钥的长度值减去11
     *
     * @return 加密后的byte型数据
     */
//    public static byte[] encryptData(byte[] data, PublicKey publicKey) {
//        try {
//            //该密钥能够加密的最大字节长度
//            int maxLength = ((RSAPublicKey) publicKey).getModulus().bitLength() / 8 - 11;
//            Logger.d("RSA encryptData maxLength=" + maxLength);
//
//            Cipher cipher = Cipher.getInstance(RSA);
//            // 编码前设定编码方式及密钥
//            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
//
//
//            // 传入编码数据并返回编码结果
//            return cipher.doFinal(data);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    /**
     * 从字符串中加载公钥
     *
     * @param publicKeyStr 公钥数据字符串
     * @throws Exception 加载公钥时产生的异常
     */
    public static PublicKey loadPublicKey(String publicKeyStr) throws Exception {
        try {
            byte[] buffer = Base64Utils.decode(publicKeyStr);
            KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("公钥非法");
        } catch (NullPointerException e) {
            throw new Exception("公钥数据为空");
        }
    }


    /**
     * 从文件中输入流中加载公钥
     *
     * @param in 公钥输入流
     * @throws Exception 加载公钥时产生的异常
     */
    public static PublicKey loadPublicKey(InputStream in) throws Exception {
        try {
            return loadPublicKey(readKey(in));
        } catch (IOException e) {
            throw new Exception("公钥数据流读取错误");
        } catch (NullPointerException e) {
            throw new Exception("公钥输入流为空");
        }
    }

    /**
     * 读取密钥信息
     *
     * @param in
     * @return
     * @throws IOException
     */
    private static String readKey(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String readLine = null;
        StringBuilder sb = new StringBuilder();
        while ((readLine = br.readLine()) != null) {
            if (readLine.charAt(0) == '-') {
                continue;
            } else {
                sb.append(readLine);
                sb.append('\r');
            }
        }

        return sb.toString();
    }


    /**
     * RSA公钥加密
     *
     * @param content   等待加密的数据
     * @param publicKey RSA 公钥 if null then getPublicKey()
     * @return 加密后的密文(16进制的字符串)
     */
    public static byte[] encryptByPublic(byte[] content, PublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            //该密钥能够加密的最大字节长度
            int splitLength = ((RSAPublicKey) publicKey).getModulus().bitLength() / 8 - 11;
            byte[][] arrays = splitBytes(content, splitLength);
            StringBuffer stringBuffer = new StringBuffer();
            for (byte[] array : arrays) {
                stringBuffer.append(bytesToHexString(cipher.doFinal(array)));
            }
            return stringBuffer.toString().getBytes();
        } catch (NoSuchAlgorithmException e) {
            Logger.e("encrypt()#NoSuchAlgorithmException" + e.getMessage());
            return null;
        } catch (NoSuchPaddingException e) {
            Logger.e("encrypt()#NoSuchPaddingException" + e.getMessage());
            return null;
        } catch (InvalidKeyException e) {
            Logger.e("encrypt()#InvalidKeyException" + e.getMessage());
            return null;
        } catch (BadPaddingException e) {
            Logger.e("encrypt()#BadPaddingException" + e.getMessage());
            return null;
        } catch (IllegalBlockSizeException e) {
            Logger.e("encrypt()#IllegalBlockSizeException" + e.getMessage());
            return null;
        }
    }

    /**
     * 根据限定的每组字节长度，将字节数组分组
     *
     * @param bytes       等待分组的字节组
     * @param splitLength 每组长度
     * @return 分组后的字节组
     */
    public static byte[][] splitBytes(byte[] bytes, int splitLength) {
        //bytes与splitLength的余数
        int remainder = bytes.length % splitLength;
        //数据拆分后的组数，余数不为0时加1
        int quotient = remainder != 0 ? bytes.length / splitLength + 1 : bytes.length / splitLength;
        byte[][] arrays = new byte[quotient][];
        byte[] array = null;
        for (int i = 0; i < quotient; i++) {
            //如果是最后一组（quotient-1）,同时余数不等于0，就将最后一组设置为remainder的长度
            if (i == quotient - 1 && remainder != 0) {
                array = new byte[remainder];
                System.arraycopy(bytes, i * splitLength, array, 0, remainder);
            } else {
                array = new byte[splitLength];
                System.arraycopy(bytes, i * splitLength, array, 0, splitLength);
            }
            arrays[i] = array;
        }
        return arrays;
    }

    /**
     * 将字节数组转换成16进制字符串
     *
     * @param bytes 即将转换的数据
     * @return 16进制字符串
     */
    public static String bytesToHexString(byte[] bytes) {
        StringBuffer sb = new StringBuffer(bytes.length);
        String temp = null;
        for (int i = 0; i < bytes.length; i++) {
            temp = Integer.toHexString(0xFF & bytes[i]);
            if (temp.length() < 2) {
                sb.append(0);
            }
            sb.append(temp);
        }
        return sb.toString();
    }

}