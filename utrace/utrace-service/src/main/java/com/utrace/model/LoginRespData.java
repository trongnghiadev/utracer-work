/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utrace.model;

/**
 * Return to client while login success include 
 * token and userInfo
 * @author asus
 */
public class LoginRespData {
    public String token;
    public UserEnt user;
    public LoginRespData() {
        this.token = "";
        this.user = null;
    }
}
