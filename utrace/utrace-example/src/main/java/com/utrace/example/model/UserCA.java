package com.utrace.example.model;

import com.urvega.framework.redis.RedisClient;
import com.urvega.framework.util.ConvertUtil;
import com.urvega.framework.util.JSONUtil;
import com.urvega.framework.util.LogUtil;
import com.utrace.example.config.ConfigInfo;
import org.apache.logging.log4j.Logger;

// User Cache
/**
 *
 * @author superman
 */
public class UserCA {
    private static final Logger logger = LogUtil.getLogger(UserCA.class);
    
    private static final String USER_KEY = "usr:%d";
    private static final String USER_MAP = "usm:%s";
    
    public static boolean set(UserEnt item) {
        boolean result = false;
        
        try {
            RedisClient client = RedisClient.getInstance(ConfigInfo.CACHE_USER);
            String key = String.format(USER_KEY, item.getId());
            result = client.set(key, JSONUtil.serialize(item));
        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
        }
        
        return result;
    }
    
    public static UserEnt get(int id){
        UserEnt result = null;
        
        try {
            RedisClient client = RedisClient.getInstance(ConfigInfo.CACHE_USER);
            String key = String.format(USER_KEY, id);
            String data = ConvertUtil.toString(client.get(key));
            if(data.length() == 0){
                return null;
            }
            
            result = JSONUtil.deserialize(data, UserEnt.class);
        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
        }
        
        return result;
    }
    
    public static boolean del(int id) {
        boolean result = false;
        
        try {
            RedisClient client = RedisClient.getInstance(ConfigInfo.CACHE_USER);
            String key = String.format(USER_KEY, id);
            result = client.del(key) > 0;
        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
        }
        
        return result;
    }
    
    public static boolean setMap(String email, int id){
        boolean result = false;
        
        try {
            RedisClient client = RedisClient.getInstance(ConfigInfo.CACHE_USER);
            String key = String.format(USER_MAP, email);
            result = client.set(key, ConvertUtil.toString(id));
        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
        }
        
        return result;
    }
    
    public static int getMap(String email){
        int result = 0;
        
        try {
            RedisClient client = RedisClient.getInstance(ConfigInfo.CACHE_USER);
            String key = String.format(USER_MAP, email);
            result = ConvertUtil.toInt(client.get(key));           
            
        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
            result = -1;
        }
        
        return result;
    }
    
    public static boolean delMap(String email) {
        boolean result = false;
        
        try {
            RedisClient client = RedisClient.getInstance(ConfigInfo.CACHE_USER);
            String key = String.format(USER_MAP, email);
            result = client.del(key) > 0;
        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
        }
        
        return result;
    }
}
