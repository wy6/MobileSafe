package com.epan.mobilesafe.untils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by WY on 2017/5/5.
 */

public class MD5Utils {
    public static String digestPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] digest2 = digest.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < digest2.length; i++) {
                //通过按位与操作，把负数提升成正数
                int result = digest2[i] & 0xff;
                //也可以进行不规则的加密，称为加盐加密方法
                String hexString = Integer.toHexString(result);
                if (hexString.length() < 2) {
                    sb.append("0");
                }
                sb.append(hexString);
            }
            String string = sb.toString();
            //System.out.println(string);
            return string;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
