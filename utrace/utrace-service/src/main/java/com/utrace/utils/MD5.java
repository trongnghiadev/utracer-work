/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utrace.API.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author asus
 */
public class MD5 {
    public static String md5(String str) {
        MessageDigest md;
        String result = "";
        try {
                md = MessageDigest.getInstance("MD5");
                md.update(str.getBytes());
                BigInteger bi = new BigInteger(1, md.digest());

                result = bi.toString(16);
        } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
        }
        return result;
    }
}
