package com.utrace.service;
import com.utrace.controllers.UserController;
import com.urvega.framework.util.LogUtil;
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
        
        // API User
        path("/users", () -> {
            get("/:email", UserController::getByEmail);
            post("/login", UserController::login);
            post("/forgotPass", UserController::forgotPass);
            post("/checkOtp", UserController::checkOtp);
            post("/register", UserController::register);
            post("/setNewPass", UserController::setNewPass);
            post("/changePass", UserController::changePassword);
            post("/changeOtp", UserController::changeOTP);
        });
        
    }    
}
