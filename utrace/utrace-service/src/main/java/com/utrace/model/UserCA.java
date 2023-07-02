package com.utrace.model;

import com.urvega.framework.redis.RedisClient;
import com.urvega.framework.util.ConvertUtil;
import com.urvega.framework.util.JSONUtil;
import com.urvega.framework.util.LogUtil;
import com.utrace.config.ConfigInfo;
import com.utrace.utils.SendMail;
import java.io.UnsupportedEncodingException;
import javax.mail.MessagingException;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

// User Cache
/**
 *
 * @author superman
 */
public class UserCA {

    private static final Logger logger = LogUtil.getLogger(UserCA.class);

    private static final String USER_KEY = "usr:%d";
    private static final String USER_OTP = "otp:%s";
    private static final String USER_MAP = "usm:%s";

    public static boolean set(UserEnt item) {
        boolean result = false;

        try {
            RedisClient client = RedisClient.getInstance(ConfigInfo.CACHE_USER);
            String key = String.format(USER_KEY, item.id);
            result = client.set(key, JSONUtil.serialize(item));
        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
        }

        return result;
    }

    public static boolean setOTP(String email, String OTP) {
        boolean result = false;
        try {
            RedisClient client = RedisClient.getInstance(ConfigInfo.CACHE_USER);
            String key = String.format(USER_OTP, email);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("otp", OTP);
            jsonObject.put("countSend", 1);
            result = client.set(key, JSONUtil.serialize(jsonObject));
            client.expireAt(key, System.currentTimeMillis() + 3600000);
        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
        }
        return result;
    }

    public static boolean UpdateOTP(String email, String OTP) {
        boolean result = false;
        try {
            RedisClient client = RedisClient.getInstance(ConfigInfo.CACHE_USER);
            String key = String.format(USER_OTP, email);
            String data = client.get(key);

            if (data == null) {
                return UserCA.setOTP(email, OTP);
            }
            JSONObject jsonObject = new JSONObject(data);
            JSONObject mapObject = jsonObject.getJSONObject("map");
            int countSend = mapObject.getInt("countSend");

            if (countSend >= 4) {
                // Điều kiện không thỏa mãn, không thể cập nhật
                return result;
            }
            result = client.del(key) > 0;
            mapObject.put("countSend", countSend++);
            mapObject.put("otp", OTP);
            jsonObject.put("map", mapObject);
          result = client.set(key, JSONUtil.serialize(mapObject));
        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
        }
        return result;
    }

    public static UserEnt get(int id) {
        UserEnt result = null;

        try {
            RedisClient client = RedisClient.getInstance(ConfigInfo.CACHE_USER);
            String key = String.format(USER_KEY, id);
            String data = ConvertUtil.toString(client.get(key));
            if (data.length() == 0) {
                return null;
            }

            result = JSONUtil.deserialize(data, UserEnt.class);
        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
        }

        return result;
    }

    public static boolean verifyOtp(String email, String otp) throws Exception {
        boolean result = false;
        try {
            RedisClient client = RedisClient.getInstance(ConfigInfo.CACHE_USER);
            String key = String.format(USER_OTP, email);
            String data = client.get(key);

            if (data == null) {
                return result;
            }
            JSONObject jsonObject = new JSONObject(data);
            JSONObject mapObject = jsonObject.getJSONObject("map");
            String otpdata = mapObject.getString("otp");
            int countSend = mapObject.getInt("countSend");

            if (countSend >= 4) {
                return result;
            }

            if (otpdata.length() < 0) {
                return result;
            }

            if (!otpdata.equals(otp)) {
                return result;
            }

            result = client.del(key) > 0;

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

    public static boolean setMap(String email, int id) {
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

    public static int getMap(String email) {
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
