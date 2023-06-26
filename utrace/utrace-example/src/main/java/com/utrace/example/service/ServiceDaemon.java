package com.utrace.example.service;

import com.urvega.framework.util.LogUtil;
import com.utrace.example.controllers.*;
import org.apache.logging.log4j.Logger;
import static spark.Spark.*;
/**
 *
 * @author superman
 */
public class ServiceDaemon {
  private static final Logger logger = LogUtil.getLogger(ServiceDaemon.class);
    
    public static void main(String[] args) throws ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        port(4567);
        staticFiles.location("/public");
        staticFiles.expireTime(600L);
        //enableDebugScreen();
        
        get("/users/:email", UserController::getByEmail);
        post("users/login", UserController::login);
        post("users/forgotPass", UserController::forgotPass);
        post("/users/checkOtp", UserController::checkOtp);
        post("/users/register", UserController::register);
        post("/users/setNewPass", UserController::setNewPass);
        
    }    
}
