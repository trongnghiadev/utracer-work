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
    public static final String CACHE_MEMBER = "member_cache";
    public static final String CACHE_SEASON = "season_cache";
    public static final String CACHE_LOGBOOK = "logbook_cache";
    public static final String CACHE_PRODUCT = "product_cache";
    
    // Mail
    public static final String MAIL_SERVICE = "trongnghiajs@gmail.com";
    public static final String MAIL_SERVICE_PASSWORD = "sacdutagtmldrmfv";
    
    public static final String DB_NAME = "db";
    public static final String DB_URL = "jdbc:mysql://localhost:3306/tracer";
    public static final String DB_USER = "root";
    public static final String DB_PASSWORD = "12345678a";
    public static final String TABLE_USER = ConvertUtil.toString(Config.getParam(DB_NAME, "user_tablename"), "user");
    public static final String TABLE_COMPANY = ConvertUtil.toString(Config.getParam(DB_NAME, "company_tablename"), "company");
    public static final String TABLE_MEMBER = ConvertUtil.toString(Config.getParam(DB_NAME, "member_tablename"), "member");
    public static final String TABLE_SEASON = ConvertUtil.toString(Config.getParam(DB_NAME, "season_tablename"), "season");
    public static final String TABLE_LOGBOOK = ConvertUtil.toString(Config.getParam(DB_NAME, "logbook_tablename"), "logbook");
    public static final String TABLE_PRODUCT = ConvertUtil.toString(Config.getParam(DB_NAME, "product_tablename"), "product");
    

}
    