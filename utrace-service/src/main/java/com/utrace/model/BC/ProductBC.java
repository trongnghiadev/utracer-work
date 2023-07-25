/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utrace.model.BC;

import com.utrace.model.Ent.ProductEnt;
import com.urvega.framework.util.LogUtil;
import com.utrace.model.CA.ProductCA;
import com.utrace.model.DA.ProductDA;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Admin
 */
public class ProductBC {
    private static final Logger logger = LogUtil.getLogger(ProductBC.class);

    private static ProductEnt validate(ProductEnt value) {
        if (value == null || value.name == null || value.name.isEmpty() || value.companyId <= 0) {
            return null;
        }

        value.name = value.name.trim();
        value.description = value.description != null ? value.description.trim() : null;
        value.content = value.content != null ? value.content.trim() : null;
        value.productionUnitCode = value.productionUnitCode != null ? value.productionUnitCode.trim() : null;
        value.rawMaterialArea = value.rawMaterialArea != null ? value.rawMaterialArea.trim() : null;

        return value;
    }

    public static boolean insert(ProductEnt item) {
        boolean result = false;
        item = validate(item);

        if (item == null) {
            return result;
        }

        try {
            long currentTime = System.currentTimeMillis();
            item.createdAt = currentTime;
            item.updatedAt = currentTime;
            item.status = true;

            if (!ProductDA.insert(item)) {
                return result;
            }

            if (ProductCA.set(item) > 0) {
                result = true;
            }
        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
        }

        return result;
    }

//    public static boolean update(ProductEnt item) {
//        boolean result = false;
//        item = validate(item);
//
//        if (item.id <= 0) {
//            return result;
//        }
//
//        try {
//            item.updatedAt = System.currentTimeMillis();
//            if (ProductCA.update(item)) {
//                result = ProductDA.update(item);
//            }
//        } catch (Exception e) {
//            logger.error(LogUtil.stackTrace(e));
//        }
//
//        return result;
//    }

    public static List<ProductEnt> getListByCompanyId(int companyId) {
        List<ProductEnt> result = new ArrayList<>();

        try {
            result = ProductCA.getListByCompanyId(companyId);
        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
        }

        return result;
    }
}
