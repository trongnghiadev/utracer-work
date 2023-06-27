package com.utrace.model;
import com.urvega.framework.dbconn.ManagerIF;
import com.urvega.framework.util.LogUtil;
import com.utrace.utils.MD5;
import com.utrace.config.ConfigInfo;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import org.apache.logging.log4j.Logger;

// User Data Acces
/**
 *
 * @author superman
 */
public class UserDA {

    private static final Logger logger = LogUtil.getLogger(UserDA.class);

    static final String QUERY_INSERT = String.format("INSERT INTO `%s` (`email`, `email_verified`, `fullname`, `phone`, `status`, `created_at`, `updated_at`) VALUES (?, ?, ?, ?, ?, ?, ?)", ConfigInfo.TABLE_USER);
    static final String QUERY_UPDATE = String.format("UPDATE `%s` SET `email_verified` = ?, `fullname` = ?, `phone` = ?, `status` = ?, `updated_at` = ? WHERE  `id` = ?", ConfigInfo.TABLE_USER);

    public static UserEnt getFromResultSet(ResultSet rs) throws SQLException {
        UserEnt result = new UserEnt();
        result.id = rs.getInt("id");
        result.email = rs.getString("email");
        result.emailVerified = rs.getBoolean("email_verified");
        result.fullname = rs.getString("fullname");
        result.phone = rs.getString("phone");
        result.status = rs.getBoolean("status");
        result.createdAt = rs.getTimestamp("created_at");
        result.updatedAt = rs.getTimestamp("updated_at");
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

    public static UserEnt getByEmail(String email) {
        UserEnt userEntity = null;
        ResultSet rs = null;
        ManagerIF cm = null;
        Connection con = null;
        try {
            con = DriverManager.getConnection(ConfigInfo.DB_URL,
                    ConfigInfo.DB_USER, ConfigInfo.DB_PASSWORD);
            try ( PreparedStatement stmt = con.prepareStatement("SELECT * FROM user WHERE `email`= ?")) {
                stmt.setString(1, email);
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
                stmt.setTimestamp(6, item.createdAt);
                stmt.setTimestamp(7, item.updatedAt);

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
                stmt.setBoolean(6, item.status);
                stmt.setTimestamp(7, item.updatedAt);
                stmt.setInt(8, item.id);
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

    public static UserEnt login(String email, String md5) {
    UserEnt userEntity = null;

    try (Connection con = DriverManager.getConnection(ConfigInfo.DB_URL, ConfigInfo.DB_USER, ConfigInfo.DB_PASSWORD);
         PreparedStatement stmt = con.prepareStatement("SELECT * FROM user WHERE `email` = ? AND `password` = ? AND `email_verified` = 1 AND `status` = 1")) {

        stmt.setString(1, email);
        stmt.setString(2, md5);

        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                userEntity = getFromResultSet(rs);
            }
        }
    } catch (Exception e) {
        logger.error(LogUtil.stackTrace(e));
    }

    return userEntity;
}

    public static UserEnt forgotPass(String email) {
        UserEnt userEntity = null;
        ResultSet rs = null;
        ManagerIF cm = null;
        Connection con = null;
        try {
            con = DriverManager.getConnection(ConfigInfo.DB_URL,
                    ConfigInfo.DB_USER, ConfigInfo.DB_PASSWORD);
            try ( PreparedStatement stmt = con.prepareStatement("SELECT * FROM user WHERE `email`= ? AND email_verified = 1")) {
                stmt.setString(1, email);
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

    public static void updateVerifyOtp(String email, String randomOtp) {
        boolean result = false;
        ManagerIF cm = null;
        Connection con = null;
        try {
            con = DriverManager.getConnection(ConfigInfo.DB_URL,
                    ConfigInfo.DB_USER, ConfigInfo.DB_PASSWORD);
            try ( PreparedStatement stmt = con.prepareStatement("UPDATE `user` SET `otp` = ? WHERE `email` = ?")) {
                stmt.setString(1, randomOtp);
                stmt.setString(2, email);
                result = stmt.executeUpdate() > 0;
            }
        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
        } finally {
            if (cm != null && con != null) {
                cm.returnClient(con);
            }
        }
    }

    public static void updateVerifyStatus(String email) {
        ManagerIF cm = null;
        Connection con = null;
        try {
            con = DriverManager.getConnection(ConfigInfo.DB_URL, ConfigInfo.DB_USER, ConfigInfo.DB_PASSWORD);
            try ( PreparedStatement stmt = con.prepareStatement("UPDATE `user` SET `status` = ?, `email_verified` = ?, `updated_at` = ? WHERE `email` = ?")) {
                stmt.setBoolean(1, true);  // Cập nhật trạng thái thành true
                stmt.setBoolean(2, true);  // Cập nhật trạng thái xác minh email thành true
                stmt.setTimestamp(3, new Timestamp(new Date().getTime()));  // Cập nhật thời gian hiện tại
                stmt.setString(4, email);
                stmt.executeUpdate();
            }
        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
        } finally {
            if (cm != null && con != null) {
                cm.returnClient(con);
            }
        }
    }

    public static Boolean verifyOtp(String email, String otp) {
        UserEnt userEntity = null;
        ResultSet rs = null;
        ManagerIF cm = null;
        Connection con = null;
        try {
            con = DriverManager.getConnection(ConfigInfo.DB_URL,
                    ConfigInfo.DB_USER, ConfigInfo.DB_PASSWORD);
            try ( PreparedStatement stmt = con.prepareStatement("SELECT * FROM user WHERE `email`= ? AND `otp` = ?")) {
                stmt.setString(1, email);
                stmt.setString(2, otp);
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
        return (userEntity != null);
    }

    public static UserEnt register(String email) {
        boolean result = false;
        ManagerIF cm = null;
        Connection con = null;
        try {
            con = DriverManager.getConnection(ConfigInfo.DB_URL,
                    ConfigInfo.DB_USER, ConfigInfo.DB_PASSWORD);
            try ( PreparedStatement stmt = con.prepareStatement("INSERT INTO `user`(`email`, `email_verified`) VALUES(?, ?)", Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, email);
                stmt.setInt(2, 0);

                result = stmt.executeUpdate() > 0;
                if (result) {
                    try ( ResultSet rs = stmt.getGeneratedKeys()) {
                        rs.next();
                        int id = rs.getInt(1);
                        return getById(id);
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

        return null;
    }

    public static UserEnt setNewPassword(String email, String pass) {
        ManagerIF cm = null;
        Connection con = null;
        try {
            con = DriverManager.getConnection(ConfigInfo.DB_URL, ConfigInfo.DB_USER, ConfigInfo.DB_PASSWORD);
            try ( PreparedStatement stmt = con.prepareStatement("UPDATE `user` SET `password` = ? WHERE `email` = ?")) {
                stmt.setString(1, MD5.md5(pass));
                stmt.setString(2, email);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    return getByEmail(email);
                }
            }
        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
        } finally {
            if (cm != null && con != null) {
                cm.returnClient(con);
            }
        }
        return null;
    }

}
