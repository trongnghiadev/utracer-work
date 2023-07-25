/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utrace.model.DA;

import com.utrace.model.Ent.ProductEnt;
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
public class ProductDA {
    private static final Logger logger = LogUtil.getLogger(ProductDA.class);
    static final String QUERY_INSERT = String.format("INSERT INTO `%s` (`company_id`, `product_key`, `name`, `description`, `content`, `production_unit_code`, `acreage`, `raw_material_area`, `photos`, `recipe`, `recipe_photos`, `farming_photos`, `video`, `certification`, `status`, `created_at`, `updated_at`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", ConfigInfo.TABLE_PRODUCT);
    static final String QUERY_UPDATE = String.format("UPDATE `%s` SET `company_id` = ?, `product_key` = ?, `name` = ?, `description` = ?, `content` = ?, `production_unit_code` = ?, `acreage` = ?, `raw_material_area` = ?, `photos` = ?, `recipe` = ?, `recipe_photos` = ?, `farming_photos` = ?, `video` = ?, `certification` = ?, `status` = ?, `updated_at` = ? WHERE  `id` = ?", ConfigInfo.TABLE_PRODUCT);

    public static ProductEnt getFromResultSet(ResultSet rs) throws SQLException {
        ProductEnt result = new ProductEnt();
        result.id = rs.getInt("id");
        result.companyId = rs.getInt("company_id");
        result.productKey = rs.getString("product_key");
        result.name = rs.getString("name");
        result.description = rs.getString("description");
        result.content = rs.getString("content");
        result.productionUnitCode = rs.getString("production_unit_code");
        result.acreage = rs.getFloat("acreage");
        result.rawMaterialArea = rs.getString("raw_material_area");
        result.photos = rs.getString("photos");
        result.recipe = rs.getString("recipe");
        result.recipePhotos = rs.getString("recipe_photos");
        result.farmingPhotos = rs.getString("farming_photos");
        result.video = rs.getString("video");
        result.certification = rs.getString("certification");
        result.status = rs.getBoolean("status");
        result.createdAt = rs.getTimestamp("created_at").getTime();
        result.updatedAt = rs.getTimestamp("updated_at").getTime();
        return result;
    }

    public static boolean insert(ProductEnt item) {
        boolean result = false;
        ManagerIF cm = null;
        Connection con = null;
        try {
            cm = ClientManager.getInstance(ConfigInfo.DB_NAME);
            con = cm.borrowClient();

            try (PreparedStatement stmt = con.prepareStatement(QUERY_INSERT, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, item.companyId);
                stmt.setString(2, item.productKey);
                stmt.setString(3, item.name);
                stmt.setString(4, item.description);
                stmt.setString(5, item.content);
                stmt.setString(6, item.productionUnitCode);
                stmt.setFloat(7, item.acreage);
                stmt.setString(8, item.rawMaterialArea);
                stmt.setString(9, item.photos);
                stmt.setString(10, item.recipe);
                stmt.setString(11, item.recipePhotos);
                stmt.setString(12, item.farmingPhotos);
                stmt.setString(13, item.video);
                stmt.setString(14, item.certification);
                stmt.setBoolean(15, item.status);
                stmt.setTimestamp(16, new java.sql.Timestamp(item.createdAt));
                stmt.setTimestamp(17, new java.sql.Timestamp(item.updatedAt));
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
    
    public static boolean update(ProductEnt item) {
        boolean result = false;
        ManagerIF cm = null;
        Connection con = null;
        try {
            cm = ClientManager.getInstance(ConfigInfo.DB_NAME);
            con = cm.borrowClient();
            try (PreparedStatement stmt = con.prepareStatement(QUERY_UPDATE)) {
                stmt.setInt(1, item.companyId);
                stmt.setString(2, item.productKey);
                stmt.setString(3, item.name);
                stmt.setString(4, item.description);
                stmt.setString(5, item.content);
                stmt.setString(6, item.productionUnitCode);
                stmt.setFloat(7, item.acreage);
                stmt.setString(8, item.rawMaterialArea);
                stmt.setString(9, item.photos);
                stmt.setString(10, item.recipe);
                stmt.setString(11, item.recipePhotos);
                stmt.setString(12, item.farmingPhotos);
                stmt.setString(13, item.video);
                stmt.setString(14, item.certification);
                stmt.setBoolean(15, item.status);
                stmt.setTimestamp(16, new java.sql.Timestamp(item.updatedAt));
                stmt.setInt(17, item.id);
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

