/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utrace.model.DA;

import com.utrace.model.Ent.LogbookEnt;
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
public class LogbookDA {
    private static final Logger logger = LogUtil.getLogger(LogbookDA.class);
    private static final String QUERY_INSERT = String.format("INSERT INTO `%s` (`product_id`, `content`, `status`, `created_at`, `updated_at`) VALUES (?, ?, ?, ?, ?)", ConfigInfo.TABLE_LOGBOOK);
    private static final String QUERY_UPDATE = String.format("UPDATE `%s` SET `product_id` = ?, `content` = ?, `status` = ?, `updated_at` = ? WHERE `id` = ?", ConfigInfo.TABLE_LOGBOOK);

    public static LogbookEnt getFromResultSet(ResultSet rs) throws SQLException {
        LogbookEnt result = new LogbookEnt();
        result.id = rs.getInt("id");
        result.productId = rs.getInt("product_id");
        result.content = rs.getString("content");
        result.status = rs.getBoolean("status");
        result.createdAt = rs.getDate("created_at").getTime();
        result.updatedAt = rs.getDate("updated_at").getTime();
        return result;
    }

    public static boolean insert(LogbookEnt item) {
        boolean result = false;
        ManagerIF cm = null;
        Connection con = null;
        try {
            cm = ClientManager.getInstance(ConfigInfo.DB_NAME);
            con = cm.borrowClient();

            try (PreparedStatement stmt = con.prepareStatement(QUERY_INSERT, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, item.productId);
                stmt.setString(2, item.content);
                stmt.setBoolean(3, item.status);
                stmt.setDate(4, new java.sql.Date(item.createdAt));
                stmt.setDate(5, new java.sql.Date(item.updatedAt));
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
                cm.returnClient(con);
            }
        }
        return result;
    }

    public static boolean update(LogbookEnt item) {
        boolean result = false;
        ManagerIF cm = null;
        Connection con = null;
        try {
            cm = ClientManager.getInstance(ConfigInfo.DB_NAME);
            con = cm.borrowClient();
            try (PreparedStatement stmt = con.prepareStatement(QUERY_UPDATE)) {
                stmt.setInt(1, item.productId);
                stmt.setString(2, item.content);
                stmt.setBoolean(3, item.status);
                stmt.setDate(4, new java.sql.Date(item.updatedAt));
                stmt.setInt(5, item.id);
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
