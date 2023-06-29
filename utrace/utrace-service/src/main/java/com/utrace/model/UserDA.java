package com.utrace.model;

import com.urvega.framework.dbconn.ManagerIF;
import com.urvega.framework.util.LogUtil;
import com.utrace.config.ConfigInfo;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.logging.log4j.Logger;

// User Data Acces
/**
 *
 * @author superman
 */
public class UserDA {

    private static final Logger logger = LogUtil.getLogger(UserDA.class);

    static final String QUERY_INSERT = String.format("INSERT INTO `%s` (`email`, `email_verified`, `fullname`, `phone`, `status`, `created_at`, `updated_at`) VALUES (?, ?, ?, ?, ?, ?, ?)", ConfigInfo.TABLE_USER);
    static final String QUERY_UPDATE = String.format("UPDATE `%s` SET `email_verified` = ?, `fullname` = ?, `phone` = ?, `status` = ?, `updated_at` = ?, `password` = ? WHERE `id` = ?", ConfigInfo.TABLE_USER);

    public static UserEnt getFromResultSet(ResultSet rs) throws SQLException {
        UserEnt result = new UserEnt();
        result.id = rs.getInt("id");
        result.email = rs.getString("email");
        result.emailVerified = rs.getBoolean("email_verified");
        result.fullname = rs.getString("fullname");
        result.phone = rs.getString("phone");
        result.status = rs.getBoolean("status");
        result.createdAt = rs.getDate("created_at").getTime();
        result.updatedAt = rs.getDate("updated_at").getTime();
        return result;
    }

    public static UserEnt getById(int id) {
        UserEnt userEntity = null;
        ResultSet rs = null;
        ManagerIF cm = null;
        Connection con = null;
        try {
            con = DriverManager.getConnection(ConfigInfo.DB_URL,
                    ConfigInfo.DB_USER, ConfigInfo.DB_PASSWORD);
            try ( PreparedStatement stmt = con.prepareStatement("SELECT * FROM user WHERE `id`= ?")) {
                stmt.setInt(1, id);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    userEntity = getFromResultSet(rs);
                    break;
                }
                rs.close();
                stmt.close();
            }
        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
        } finally {
            if (cm != null && con != null) {
                cm.returnClient(con);
            }
        }
        return userEntity;
    }

    public static boolean insert(UserEnt item) {
        boolean result = false;
        ManagerIF cm = null;
        Connection con = null;
        try {
            con = DriverManager.getConnection(ConfigInfo.DB_URL,
                    ConfigInfo.DB_USER, ConfigInfo.DB_PASSWORD);
            try ( PreparedStatement stmt = con.prepareStatement(QUERY_INSERT, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, item.email);
                stmt.setBoolean(2, item.emailVerified);
                stmt.setString(3, item.fullname);
                stmt.setString(4, item.phone);
                stmt.setBoolean(5, item.status);
                stmt.setDate(6, new java.sql.Date(item.createdAt));
                stmt.setDate(7, new java.sql.Date(item.updatedAt));

                result = stmt.executeUpdate() > 0;
                if (result) {
                    try ( ResultSet rs = stmt.getGeneratedKeys()) {
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

    public static boolean update(UserEnt item) {
        boolean result = false;
        ManagerIF cm = null;
        Connection con = null;
        try {
            con = DriverManager.getConnection(ConfigInfo.DB_URL,
                    ConfigInfo.DB_USER, ConfigInfo.DB_PASSWORD);
            try ( PreparedStatement stmt = con.prepareStatement(QUERY_UPDATE)) {
                stmt.setBoolean(1, item.emailVerified);
                stmt.setString(2, item.fullname);
                stmt.setString(3, item.phone);
                stmt.setBoolean(4, item.status);
                stmt.setDate(5, new java.sql.Date(item.updatedAt));
                stmt.setString(6, item.passwordUser);
                stmt.setInt(7, item.id);

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

    public static UserEnt login(String email, String password) {
        UserEnt userEntity = null;

        try ( Connection con = DriverManager.getConnection(ConfigInfo.DB_URL, ConfigInfo.DB_USER, ConfigInfo.DB_PASSWORD);  PreparedStatement stmt = con.prepareStatement("SELECT * FROM user WHERE `email` = ? AND `password` = ? AND `email_verified` = 1 AND `status` = 1")) {

            stmt.setString(1, email);
            stmt.setString(2, password);

            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    userEntity = getFromResultSet(rs);
                }
            }
        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
        }

        return userEntity;
    }
}
