/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utrace.model.CA;

import com.utrace.model.Ent.MemberEnt;
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
public class MemberCA {
    private static final Logger logger = LogUtil.getLogger(MemberCA.class);
    
        private static final String MEMBER_KEY = "mbr:%s:%s";
    private static final String MEMBER_MAP = "mbm:%s";
    
    public static boolean set(MemberEnt item) {
        boolean result = false;
        
        try {
            RedisClient client = RedisClient.getInstance(ConfigInfo.CACHE_MEMBER);
            String key = String.format(MEMBER_KEY,item.companyId, item.id);
            result = client.set(key, JSONUtil.serialize(item));
        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
        }
        
        return result;
    }
    
    public static MemberEnt get(int idCom, int idMem){
        MemberEnt result = null;
        
        try {
            RedisClient client = RedisClient.getInstance(ConfigInfo.CACHE_MEMBER);
            String key = String.format(MEMBER_KEY, idCom, idMem);
            String data = ConvertUtil.toString(client.get(key));
            if(data.length() == 0){
                return null;
            }
            
            result = JSONUtil.deserialize(data, MemberEnt.class);
        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
        }
        
        return result;
    }
    
    public static boolean del(int idCom, String idMem) {
        boolean result = false;
        
        try {
            RedisClient client = RedisClient.getInstance(ConfigInfo.CACHE_MEMBER);
             String key = String.format(MEMBER_KEY, idCom, idMem);
            result = client.del(key) > 0;
        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
        }
        
        return result;
    }
    
    public static boolean setMap(String k, int id){
        boolean result = false;
        
        try {
            RedisClient client = RedisClient.getInstance(ConfigInfo.CACHE_MEMBER);
            String key = String.format(MEMBER_MAP, k);
            result = client.set(key, ConvertUtil.toString(id));
        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
        }
        
        return result;
    }
    
    public static int getMap(String k){
        int result = 0;
        
        try {
            RedisClient client = RedisClient.getInstance(ConfigInfo.CACHE_MEMBER);
            String key = String.format(MEMBER_MAP, k);
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
            RedisClient client = RedisClient.getInstance(ConfigInfo.CACHE_MEMBER);
            String key = String.format(MEMBER_KEY, k);
            result = client.del(key) > 0;
        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
        }
        
        return result;
    }
    
    
    
    
    

}

