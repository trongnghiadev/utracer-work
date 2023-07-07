/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utrace.model.DA;

import com.utrace.model.Ent.MemberEnt;
import com.urvega.framework.dbconn.ClientManager;
import com.urvega.framework.dbconn.ManagerIF;
import com.urvega.framework.util.LogUtil;
import com.utrace.config.ConfigInfo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Admin
 */
public class MemberDA {
    private static final Logger logger = LogUtil.getLogger(MemberDA.class);
    static final String QUERY_INSERT = String.format("INSERT INTO `%s` (`name`, `company_id`, `acreage`, `location`, `status`, `created_at`, `updated_at`) VALUES (?, ?, ?, ?, ?, ?, ?)", ConfigInfo.TABLE_MEMBER);
    static final String QUERY_UPDATE = String.format("UPDATE `%s` SET `name` = ?, `company_id` = ?, `acreage` = ?, `location` = ?, `status` = ?, `updated_at` = ? WHERE  `id` = ?", ConfigInfo.TABLE_MEMBER);
    
    public static MemberEnt getFromResultSet(ResultSet rs) throws SQLException {
        MemberEnt result = new MemberEnt();
        result.id = rs.getInt("id");
        result.name = rs.getString("name");
        result.companyId = rs.getInt("company_id");
        result.acreage = rs.getFloat("acreage");
        result.location = rs.getString("location");
        result.status = rs.getBoolean("status");
        result.createdAt = rs.getTimestamp("created_at").getTime();
        result.updatedAt = rs.getTimestamp("updated_at").getTime();
        return result;
    }

    public static boolean insert(MemberEnt item) {
        boolean result = false;
        ManagerIF cm = null;
        Connection con = null;
        try {
            cm = ClientManager.getInstance(ConfigInfo.DB_NAME);
            con = cm.borrowClient();
            
            try (PreparedStatement stmt = con.prepareStatement(QUERY_INSERT, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, item.name);
                stmt.setInt(2, item.companyId);
                stmt.setFloat(3, item.acreage);
                stmt.setString(4, item.location);
                stmt.setBoolean(5, item.status);
                stmt.setTimestamp(6, new java.sql.Timestamp(item.createdAt));
                stmt.setTimestamp(7, new java.sql.Timestamp(item.updatedAt));
                result = stmt.executeUpdate() > 0;
                if(result){
                    try (ResultSet rs = stmt.getGeneratedKeys()) {
                        rs.next();  
                        item.id = rs.getInt(1);
                    }
                }
            }
        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
        } finally {
            if (cm != null && con != null) {
                cm.returnClient(con);
            }
        }
        return result;
    }
    
    public static boolean update(MemberEnt item) {
        boolean result = false;
        ManagerIF cm = null;
        Connection con = null;
        try {
            cm = ClientManager.getInstance(ConfigInfo.DB_NAME);
            con = cm.borrowClient();
            try (PreparedStatement stmt = con.prepareStatement(QUERY_UPDATE)) {
                stmt.setString(1, item.name);
                stmt.setInt(2, item.companyId);
                stmt.setFloat(3, item.acreage);
                stmt.setString(4, item.location);
                stmt.setBoolean(5, item.status);
                stmt.setTimestamp(6, new java.sql.Timestamp(item.updatedAt));
                stmt.setInt(7, item.id);
                result = stmt.executeUpdate() > 0;
            }
        }
        catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
        } finally {
            if (cm != null && con != null) {
                cm.returnClient(con);
            }
        }
        return result;
    }
}

