/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utrace.example.controllers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.utrace.example.model.UserBC;
import com.utrace.example.model.UserEnt;
import spark.Request;
import spark.Response;
import com.utrace.example.utils.MD5;
import com.urvega.framework.util.JSONUtil;
/**
 *
 * @author Admin
 */
public class UserController {
    

    
    public UserController() {
 
    }
    
    public static String getByEmail(Request req, Response res) {
        UserBC userBC = new UserBC();
        String email = req.params("email");
        UserEnt userEnt = UserBC.getByEmail(email);
        return JSONUtil.serialize(userEnt);
    }
    
    public static String login(Request req, Response res) {
        UserBC userBC = new UserBC();
        String email = req.queryParams("email");
        String password = req.queryParams("password");
        UserEnt userEnt = UserBC.login(email, MD5.md5(password));
        return JSONUtil.serialize(userEnt);
    }
    
    public static String forgotPass(Request req, Response res) {
        UserBC userBC = new UserBC();
        String email = req.queryParams("email");
        UserEnt userEnt = UserBC.forgotPass(email);
        return JSONUtil.serialize(userEnt);
    }
    
    public static String checkOtp(Request req, Response res) {
        UserBC userBC = new UserBC();
        String email = req.queryParams("email");
        String otp = req.queryParams("otp");
        UserEnt userEnt = UserBC.checkOtp(email, otp);
        return JSONUtil.serialize(userEnt);
    }
    
    
    public static String register(Request req, Response res) throws JsonProcessingException {
        UserBC userBC = new UserBC();
        String email = req.queryParams("email");
        UserEnt userEnt = UserBC.register(email);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(userEnt);
    }
    
    public static String setNewPass(Request req, Response res) {
        UserBC userBC = new UserBC();
        String email = req.queryParams("email");
        String pass = req.queryParams("pass");
        String repass = req.queryParams("repass");
        UserEnt userEnt = UserBC.setNewPassword(email, pass, repass);
        return JSONUtil.serialize(userEnt);
    }
}
