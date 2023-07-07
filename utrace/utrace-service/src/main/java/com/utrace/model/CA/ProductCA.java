/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utrace.model.CA;

import com.utrace.model.Ent.ProductEnt;
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
public class ProductCA {
    private static final Logger logger = LogUtil.getLogger(ProductCA.class);
    
    private static final String PRODUCT_KEY = "prd:%s";
    private static final String PRODUCT_MAP = "prm:%s";
    
    public static boolean set(ProductEnt item) {
        boolean result = false;
        
        try {
            RedisClient client = RedisClient.getInstance(ConfigInfo.CACHE_PRODUCT);
            String key = String.format(PRODUCT_KEY, item.id);
            result = client.set(key, JSONUtil.serialize(item));
        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
        }
        
        return result;
    }
    
    public static ProductEnt get(int id){
        ProductEnt result = null;
        
        try {
            RedisClient client = RedisClient.getInstance(ConfigInfo.CACHE_PRODUCT);
            String key = String.format(PRODUCT_KEY, id);
            String data = ConvertUtil.toString(client.get(key));
            if(data.length() == 0){
                return null;
            }
            
            result = JSONUtil.deserialize(data, ProductEnt.class);
        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
        }
        
        return result;
    }
    
    public static boolean del(int id) {
        boolean result = false;
        
        try {
            RedisClient client = RedisClient.getInstance(ConfigInfo.CACHE_PRODUCT);
            String key = String.format(PRODUCT_KEY, id);
            result = client.del(key) > 0;
        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
        }
        
        return result;
    }
    
    public static boolean setMap(String k, int id){
        boolean result = false;
        
        try {
            RedisClient client = RedisClient.getInstance(ConfigInfo.CACHE_PRODUCT);
            String key = String.format(PRODUCT_MAP, k);
            result = client.set(key, ConvertUtil.toString(id));
        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
        }
        
        return result;
    }
    
    public static int getMap(String k){
        int result = 0;
        
        try {
            RedisClient client = RedisClient.getInstance(ConfigInfo.CACHE_PRODUCT);
            String key = String.format(PRODUCT_MAP, k);
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
            RedisClient client = RedisClient.getInstance(ConfigInfo.CACHE_PRODUCT);
            String key = String.format(PRODUCT_MAP, k);
            result = client.del(key) > 0;
        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
        }
        
        return result;
    }
}

