/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utrace.model.DA;

import com.utrace.model.Ent.SeasonEnt;
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
public class SeasonDA {
    private static final Logger logger = LogUtil.getLogger(SeasonDA.class);
    static final String QUERY_INSERT = String.format("INSERT INTO `%s` (`lot_number`, `name`, `member_id`, `product_id`, `logbook`, `harvest`, `package`, `logistic`, `status`, `created_at`, `updated_at`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", ConfigInfo.TABLE_SEASON);
    static final String QUERY_UPDATE = String.format("UPDATE `%s` SET `lot_number` = ?, `name` = ?, `member_id` = ?, `product_id` = ?, `logbook` = ?, `harvest` = ?, `package` = ?, `logistic` = ?, `status` = ?, `updated_at` = ? WHERE  `id` = ?", ConfigInfo.TABLE_SEASON);
    
    public static SeasonEnt getFromResultSet(ResultSet rs) throws SQLException {
        SeasonEnt result = new SeasonEnt();
        result.id = rs.getInt("id");
        result.lotNumber = rs.getString("lot_number");
        result.name = rs.getString("name");
        result.memberId = rs.getInt("member_id");
        result.productId = rs.getInt("product_id");
        result.logbook = rs.getString("logbook");
        result.harvest = rs.getLong("harvest");
        result.pack = rs.getLong("package");
        result.logistic = rs.getString("logistic");
        result.status = rs.getBoolean("status");
        result.createdAt = rs.getDate("created_at").getTime();
        result.updatedAt = rs.getDate("updated_at").getTime();
        return result;
    }

    public static boolean insert(SeasonEnt item) {
        boolean result = false;
        ManagerIF cm = null;
        Connection con = null;
        try {
            cm = ClientManager.getInstance(ConfigInfo.DB_NAME);
            con = cm.borrowClient();
            
            try (PreparedStatement stmt = con.prepareStatement(QUERY_INSERT, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, item.lotNumber);
                stmt.setString(2, item.name);
                stmt.setInt(3, item.memberId);
                stmt.setInt(4, item.productId);
                stmt.setString(5, item.logbook);
                stmt.setLong(6, item.harvest);
                stmt.setLong(7, item.pack);
                stmt.setString(8, item.logistic);
                stmt.setBoolean(9, item.status); 
                stmt.setDate(10, new java.sql.Date(item.createdAt));
                stmt.setDate(11, new java.sql.Date(item.updatedAt));
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
    
    public static boolean update(SeasonEnt item) {
        boolean result = false;
        ManagerIF cm = null;
        Connection con = null;
        try {
            cm = ClientManager.getInstance(ConfigInfo.DB_NAME);
            con = cm.borrowClient();
            try (PreparedStatement stmt = con.prepareStatement(QUERY_UPDATE)) {
                stmt.setString(1, item.lotNumber);
                stmt.setString(2, item.name);
                stmt.setInt(3, item.memberId);
                stmt.setInt(4, item.productId);
                stmt.setString(5, item.logbook);
                stmt.setLong(6, item.harvest);
                stmt.setLong(7, item.pack);
                stmt.setString(8, item.logistic);
                stmt.setBoolean(9, item.status);
                stmt.setDate(10, new java.sql.Date(item.updatedAt));
                stmt.setInt(11, item.id);
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