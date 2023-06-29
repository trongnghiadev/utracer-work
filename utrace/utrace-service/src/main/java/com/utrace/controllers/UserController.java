/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utrace.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.urvega.framework.crypto.Rijndael;
import com.urvega.framework.util.JSONUtil;
import com.utrace.model.LoginRespData;
import com.utrace.model.UserBC;
import com.utrace.model.UserEnt;
import spark.Request;
import spark.Response;
import com.utrace.utils.MD5;
import com.utrace.utils.APIResponseUtil;
import com.utrace.utils.TokenHelper;

/**
 *
 * @author Admin
 */
public class UserController {

    public UserController() {

    }

    public static String getByEmail(Request req, Response res) throws JsonProcessingException {
        String email = req.params("email");
        UserEnt userEnt = UserBC.getByEmail(email);
        if (userEnt != null) {
            return APIResponseUtil.successResponse(userEnt);
        } else {
            return APIResponseUtil.errorResponse("User not found");
        }
    }

    public static String login(Request req, Response res) throws JsonProcessingException {
        String email = req.queryParams("email");
        String password = req.queryParams("password");
        UserEnt userEnt = UserBC.login(email, MD5.md5(password));
        if (userEnt != null) {
            // Serialize User Info 
            String info = JSONUtil.serialize(userEnt);
            // Create token base on UserInfo
            String token = TokenHelper.CreateToken(info);
            // Create login response data
            LoginRespData respData = new LoginRespData();
            respData.token = token;
            respData.user = userEnt;
            // Return login success
            // Client will get the token and set in Authorization header
            // on every request
            return APIResponseUtil.successResponse(respData);
        } else {
            return APIResponseUtil.errorResponse("Login found");
        }
    }

    public static String forgotPass(Request req, Response res) throws JsonProcessingException {
        String email = req.queryParams("email");
        UserEnt userEnt = UserBC.forgotPass(email);

        if (userEnt != null) {
            return APIResponseUtil.successResponse(email);
        } else {
            return APIResponseUtil.errorResponse("User not found");
        }
    }


    public static String checkOtp(Request req, Response res) throws JsonProcessingException, Exception {
        String email = req.queryParams("email");
        String otp = req.queryParams("otp");

        if (UserBC.checkOtp(email, otp)) {
            return APIResponseUtil.successResponse("OTP authentication successful",email);
        } else {
            return APIResponseUtil.errorResponse("Invalid OTP");
        }
    }
    

    public static String register(Request req, Response res) throws JsonProcessingException {
        String email = req.queryParams("email");
        if (UserBC.register(email)) {
            return APIResponseUtil.successResponse(email);
        } else {
            return APIResponseUtil.errorResponse("User registration failed");
        }
    }
    
    public static String changeOTP(Request req, Response res) throws JsonProcessingException {
        String email = req.queryParams("email");
        if (UserBC.changeOTP(email)) {
            return APIResponseUtil.successResponse(email);
        } else {
            return APIResponseUtil.errorResponse("Can not send OTP");
        }
    }

    public static String setNewPass(Request req, Response res) throws JsonProcessingException {
        String email = req.queryParams("email");
        String pass = req.queryParams("pass");
        if (UserBC.setNewPassword(email, MD5.md5(pass))) {
            return APIResponseUtil.successResponse(email);
        } else {
            return APIResponseUtil.errorResponse("Invalid PassWord");
        }
    }

    public static String changePassword(Request req, Response res) throws JsonProcessingException {
        String email = req.queryParams("email");
        String oldPassword = req.queryParams("oldPassword");
        String newPassword = req.queryParams("newPassword");
        boolean passwordChanged = UserBC.changePassword(email, oldPassword, newPassword);
        if (passwordChanged) {
            return APIResponseUtil.successResponse("Password changed successfully");
        } else {
            return APIResponseUtil.errorResponse("Invalid password or user not found");
        }
    }
}
