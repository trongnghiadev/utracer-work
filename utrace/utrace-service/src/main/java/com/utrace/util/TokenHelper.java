/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utrace.util;

import com.urvega.framework.crypto.Rijndael;

/**
 *
 * @author asus
 */
public class TokenHelper {
    
    private static String secret = "0123456789ABCDEF";
    
    private static Rijndael rijndael = new Rijndael(secret);
    
    public static String CreateToken(String info) {
        return rijndael.encrypt(info);
    }
    
    public static Boolean IsValidToken(String token) {
            if(token.length() <= 1) {
                return false;
            }
                
            return rijndael.decrypt(token) != "";
    }
}
