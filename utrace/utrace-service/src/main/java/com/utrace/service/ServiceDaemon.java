package com.utrace.service;

import com.utrace.controllers.UserController;
import com.urvega.framework.util.LogUtil;
import com.utrace.utils.TokenHelper;
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
        // exec before every request
        before((request, response) -> {
            String path = request.pathInfo();

            // check path
            if (!path.equals("/users/login") && !path.matches("^/users/[^/]+$") && !path.equals("/users/checkOtp")) {
                // Get token from Authorization header
                String token = request.headers("Authorization");

                // validate token
                if (!TokenHelper.IsValidToken(token)) {
                    // token is not valid, return 401 Unauthorized
                    halt(401, "Unauthorized access blocked!");
                }
            }
        });

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
