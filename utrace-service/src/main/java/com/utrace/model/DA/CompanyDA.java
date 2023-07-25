/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utrace.model.DA;

import com.utrace.model.Ent.CompanyEnt;
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
public class CompanyDA {

    private static final Logger logger = LogUtil.getLogger(CompanyDA.class);
    static final String QUERY_INSERT = String.format("INSERT INTO `%s` (`user_id`, `name`, `phone`, `address`, `logo`, `website`, `status`, `created_at`, `updated_at`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)", ConfigInfo.TABLE_COMPANY);
    static final String QUERY_UPDATE = String.format("UPDATE `%s` SET `user_id` = ?, `name` = ?, `phone` = ?, `address` = ?, `logo` = ?, `website` = ?, `status` = ?, `created_at` = ?, `updated_at` = ? WHERE  `id` = ?", ConfigInfo.TABLE_COMPANY);

    public static CompanyEnt getFromResultSet(ResultSet rs) throws SQLException {
        CompanyEnt result = new CompanyEnt();
        result.id = rs.getInt("id");
        result.userId = rs.getInt("user_id");
        result.name = rs.getString("name");
        result.phone = rs.getString("phone");
        result.address = rs.getString("address");
        result.logo = rs.getString("logo");
        result.website = rs.getString("website");
        result.status = rs.getBoolean("status");
        result.createdAt = rs.getTimestamp("created_at").getTime();
        result.updatedAt = rs.getTimestamp("updated_at").getTime();
        return result;
    }

    public static boolean insert(CompanyEnt item) {
        boolean result = false;
        ManagerIF cm = null;
        Connection con = null;
        try {
            cm = ClientManager.getInstance(ConfigInfo.DB_NAME);
            con = cm.borrowClient();
            try (PreparedStatement stmt = con.prepareStatement(QUERY_INSERT, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, item.userId);
                stmt.setString(2, item.name);
                stmt.setString(3, item.phone);
                stmt.setString(4, item.address);
                stmt.setString(5, item.logo);
                stmt.setString(6, item.website);
                stmt.setBoolean(7, item.status);
                stmt.setTimestamp(8, new java.sql.Timestamp(item.createdAt));
                stmt.setTimestamp(9, new java.sql.Timestamp(item.updatedAt));
                result = stmt.executeUpdate() > 0;
                if (result) {
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
                try {
                    cm.returnClient(con);
                } catch (Exception e) {
                    logger.error(LogUtil.stackTrace(e));
                }
            }
        }
        return result;
    }

    public static boolean update(CompanyEnt item) {
        boolean result = false;
        ManagerIF cm = null;
        Connection con = null;
        try {
            cm = ClientManager.getInstance(ConfigInfo.DB_NAME);
            con = cm.borrowClient();
            try (PreparedStatement stmt = con.prepareStatement(QUERY_UPDATE)) {
                stmt.setInt(1, item.userId);
                stmt.setString(2, item.name);
                stmt.setString(3, item.phone);
                stmt.setString(4, item.address);
                stmt.setString(5, item.logo);
                stmt.setString(6, item.website);
                stmt.setBoolean(7, item.status);
                stmt.setTimestamp(8, new java.sql.Timestamp(item.createdAt));
                stmt.setTimestamp(9, new java.sql.Timestamp(item.updatedAt));
                stmt.setInt(10, item.id);
                result = stmt.executeUpdate() > 0;
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
}
