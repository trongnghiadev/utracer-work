/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utrace.model;

/**
 *
 * @author Admin
 */
public class OtpData {
    public long created;
    public String OTP;
    public long expiration;
    public int countSend;
    public boolean status;

    public OtpData() {
        this.created = 0;
        this.OTP = "";
        this.expiration = 0;
        this.countSend = 1;
        this.status = false;
    }
    
}