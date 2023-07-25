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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Admin
 */
public class MemberCA {

    private static final Logger logger = LogUtil.getLogger(MemberCA.class);

    private static final String MEMBER_KEY = "mbr:%s";
    private static final String MEMBER_MAP = "mbm:%s";

    public static Long set(MemberEnt item) {
        Long result = 0L;

        try {
            RedisClient client = RedisClient.getInstance(ConfigInfo.CACHE_MEMBER);
            String key = String.format(MEMBER_KEY, item.companyId);
            double score = System.currentTimeMillis(); // Sử dụng thời gian hiện tại làm điểm số
            result = client.zadd(key, score, JSONUtil.serialize(item));
        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
        }

        return result;
    }

    public static List<MemberEnt> getListByCompanyId(int idCom) {
        List<MemberEnt> result = new ArrayList<>();

        try {
            RedisClient client = RedisClient.getInstance(ConfigInfo.CACHE_MEMBER);
            String key = String.format(MEMBER_KEY, idCom);
            List<String> members = client.zrange(key, 0, -1);

            for (String memberData : members) {
                MemberEnt member = JSONUtil.deserialize(memberData, MemberEnt.class);
                result.add(member);
            }
        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
        }

        return result;
    }

    public static boolean del(int idCom, String idMem) {
        boolean result = false;

        try {
            RedisClient client = RedisClient.getInstance(ConfigInfo.CACHE_MEMBER);
            String key = String.format(MEMBER_KEY, idCom);
            result = client.zrem(key, new String[]{idMem}) > 0;
        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
        }

        return result;
    }

    public static boolean setMap(String k, int id) {
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

    public static MemberEnt get(int idCom, int idMem) {
        MemberEnt result = null;

        try {
            List<MemberEnt> members = getListByCompanyId(idCom);

            for (MemberEnt member : members) {
                if (member.id == idMem) {
                    result = member;
                    break;
                }
            }
        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
        }

        return result;
    }

 public static boolean update(MemberEnt updatedMember) {
    boolean result = false;
    MemberEnt member = get(updatedMember.companyId, updatedMember.id);

    if (member != null) {
        try {
            RedisClient client = RedisClient.getInstance(ConfigInfo.CACHE_MEMBER);
            String key = String.format(MEMBER_KEY, updatedMember.companyId);
            
            // Remove the member from the sorted set using the member's id
            String[] memberJsonArray = member.toString(new String[0]);
            client.zrem(key, memberJsonArray);
            // Update the member's information
            member.name = updatedMember.name;
            member.acreage = updatedMember.acreage;
            member.location = updatedMember.location;
            member.status = updatedMember.status;
            member.updatedAt = updatedMember.updatedAt;
            
            // Add the updated member back to the sorted set
            double newScore = updatedMember.updatedAt;
            client.zadd(key, newScore, JSONUtil.serialize(member));
            
            result = true;
        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
        }
    }

    return result;
}





    public static int getMap(String k) {
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
