package com.utrace.service;

import com.utrace.controllers.UserController;
import com.urvega.framework.util.LogUtil;
import com.utrace.controllers.CompanyController;
import com.utrace.controllers.IndexController;
import com.utrace.controllers.LogbookController;
import com.utrace.controllers.MemberController;
import com.utrace.controllers.ProductController;
import com.utrace.util.ConfigInfo;
import com.utrace.util.Path;
import org.apache.logging.log4j.Logger;
import spark.Spark;
import static spark.Spark.*;

/**
 *
 * @author superman
 */
public class ServiceDaemon {

    private static final Logger logger = LogUtil.getLogger(ServiceDaemon.class);

    public static void main(String[] args) throws ClassNotFoundException {
        //port(ConfigInfo.PORT);
        ipAddress(ConfigInfo.HOST);
        Spark.threadPool(ConfigInfo.MAX_THREAD, ConfigInfo.MIN_THREAD, 30000);
        Class.forName("com.mysql.cj.jdbc.Driver");
        staticFiles.location("/public");
        staticFiles.expireTime(600L);
        logger.info(String.format("start: %s:%s", ConfigInfo.HOST, ConfigInfo.PORT));
        get(Path.Web.INDEX, IndexController.serveIndex);

        //enableDebugScreen();
        // exec before every request
//        before((request, response) -> {
//            String path = request.pathInfo();
//
//            // Kiểm tra path có nằm trong excludedPaths không
//            if (!path.matches("/users/register") 
//                    && !path.matches("^/users/[^/]+$") 
//                    && !path.matches("/users/setNewPass")
//                    && !path.matches("/users/forgotPass")
//                    && !path.equals("/login")
//                    && !path.matches("/users/checkOtp")) 
//            {
//                // Get token from Authorization header
//                String token = request.headers("Authorization");
//                // validate token
//                if (!TokenHelper.IsValidToken(token)) {
//                    // token is not valid, return 401 Unauthorized
//                    halt(401, "Unauthorized access blocked!");
//                }
//            }
//        });
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

        path("/companies", () -> {
            get("/:iduser", CompanyController::getCompanyByIdUser);
            post("/addCompany", CompanyController::addOrUpdateCompany);

        });

        path("/members", () -> {
            get("/getListByCompanyId/:idCom", MemberController::getListByCompanyId);
            post("/addMember", MemberController::addMember);
            post("/updateMember", MemberController::updateMember);

        });

        path("/logbooks", () -> {
            get("/:productId", LogbookController::getLogbookByProductId);
            post("/addlogbook", LogbookController::addLogbook);
            post("/updateLogbook", LogbookController::updateLogbook);
        });

        path("/products", () -> {
            get("/:companyId", ProductController::getListByCompanyId);
            post("/addProduct", ProductController::addProduct);
        });

    }
}
