package com.utrace.config;
import com.urvega.framework.util.Config;
import com.urvega.framework.util.ConvertUtil;

/**
 *
 * @author superman
 */
public class ConfigInfo {

    public static final String CACHE_USER = "user_cache";
    public static final String CACHE_COMPANY = "company_cache";
    // Mail
    public static final String MAIL_SERVICE = "trongnghiajs@gmail.com";
    public static final String MAIL_SERVICE_PASSWORD = "sacdutagtmldrmfv";
    
    public static final String DB_NAME = "tracer";
    public static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/tracer";
    public static final String DB_USER = "root";
    public static final String DB_PASSWORD = "";
    public static final String TABLE_USER = ConvertUtil.toString(Config.getParam(DB_NAME, "user_tablename"), "user");
    public static final String TABLE_COMPANY = ConvertUtil.toString(Config.getParam(DB_NAME, "company_tablename"), "company");
}
    