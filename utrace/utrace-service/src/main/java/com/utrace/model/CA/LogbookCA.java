/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utrace.model.CA;

import com.utrace.model.Ent.LogbookEnt;
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
public class LogbookCA {
    private static final Logger logger = LogUtil.getLogger(LogbookCA.class);

    private static final String LOGBOOK_KEY = "log:%s";
    private static final String LOGBOOK_MAP = "lom:%s";

    public static boolean set(LogbookEnt item) {
        boolean result = false;

        try {
            RedisClient client = RedisClient.getInstance(ConfigInfo.CACHE_LOGBOOK);
            String key = String.format(LOGBOOK_KEY, item.id);
            result = client.set(key, ConvertUtil.toString(item));
        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
        }

        return result;
    }

    public static LogbookEnt get(int id) {
        LogbookEnt result = null;

        try {
            RedisClient client = RedisClient.getInstance(ConfigInfo.CACHE_LOGBOOK);
            String key = String.format(LOGBOOK_KEY, id);
            String data = client.get(key);
            if (data == null) {
                return null;
            }

            result = JSONUtil.deserialize(data, LogbookEnt.class);
        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
        }

        return result;
    }

    public static boolean del(int id) {
        boolean result = false;

        try {
            RedisClient client = RedisClient.getInstance(ConfigInfo.CACHE_LOGBOOK);
            String key = String.format(LOGBOOK_KEY, id);
            result = client.del(key) > 0;
        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
        }

        return result;
    }

    public static boolean setMap(String k, int id) {
        boolean result = false;

        try {
            RedisClient client = RedisClient.getInstance(ConfigInfo.CACHE_LOGBOOK);
            String key = String.format(LOGBOOK_MAP, k);
            result = client.set(key, ConvertUtil.toString(id));
        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
        }

        return result;
    }

    public static int getMap(String k) {
        int result = 0;

        try {
            RedisClient client = RedisClient.getInstance(ConfigInfo.CACHE_LOGBOOK);
            String key = String.format(LOGBOOK_MAP, k);
            String data = client.get(key);
            result = ConvertUtil.toInt(data);

        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
            result = -1;
        }

        return result;
    }

    public static boolean delMap(String k) {
        boolean result = false;

        try {
            RedisClient client = RedisClient.getInstance(ConfigInfo.CACHE_LOGBOOK);
            String key = String.format(LOGBOOK_MAP, k);
            result = client.del(key) > 0;
        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
        }

        return result;
    }
}