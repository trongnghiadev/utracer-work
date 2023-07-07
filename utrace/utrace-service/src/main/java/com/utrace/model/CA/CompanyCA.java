/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utrace.model.CA;
import com.utrace.model.Ent.CompanyEnt;
import com.urvega.framework.redis.RedisClient;
import com.urvega.framework.util.ConvertUtil;
import com.urvega.framework.util.JSONUtil;
import com.urvega.framework.util.LogUtil;
import com.utrace.config.ConfigInfo;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Admin
 */
public class CompanyCA {
    private static final Logger logger = LogUtil.getLogger(CompanyCA.class);
    
    private static final String COMPANY_KEY = "com:%s";
    private static final String COMPANY_MAP = "com_map:%s";
    
    public static boolean set(CompanyEnt item) {
        boolean result = false;
        
        try {
            RedisClient client = RedisClient.getInstance(ConfigInfo.CACHE_COMPANY);
            String key = String.format(COMPANY_KEY, item.id);
            result = client.set(key, JSONUtil.serialize(item));
        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
        }
        
        return result;
    }
    
    public static CompanyEnt get(int id){
        CompanyEnt result = null;
        
        try {
            RedisClient client = RedisClient.getInstance(ConfigInfo.CACHE_COMPANY);
            String key = String.format(COMPANY_KEY, id);
            String data = ConvertUtil.toString(client.get(key));
            if(data.length() == 0){
                return null;
            }
            
            result = JSONUtil.deserialize(data, CompanyEnt.class);
        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
        }
        
        return result;
    }
    
    public static boolean del(int id) {
        boolean result = false;
        
        try {
            RedisClient client = RedisClient.getInstance(ConfigInfo.CACHE_COMPANY);
            String key = String.format(COMPANY_KEY, id);
            result = client.del(key) > 0;
        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
        }
        
        return result;
    }
    
    public static boolean setMap(int k, int id){
        boolean result = false;
        
        try {
            RedisClient client = RedisClient.getInstance(ConfigInfo.CACHE_COMPANY);
            String key = String.format(COMPANY_MAP, k);
            result = client.set(key, ConvertUtil.toString(id));
        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
        }
        
        return result;
    }
    
    public static int getMap(int k){
        int result = 0;
        
        try {
            RedisClient client = RedisClient.getInstance(ConfigInfo.CACHE_COMPANY);
            String key = String.format(COMPANY_MAP, k);
            result = ConvertUtil.toInt(client.get(key));           
            
        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
            result = -1;
        }
        
        return result;
    }
    
    public static boolean delMap(String k) {
        boolean result = false;
        
        try {
            RedisClient client = RedisClient.getInstance(ConfigInfo.CACHE_COMPANY);
            String key = String.format(COMPANY_MAP, k);
            result = client.del(key) > 0;
        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
        }
        
        return result;
    }
}