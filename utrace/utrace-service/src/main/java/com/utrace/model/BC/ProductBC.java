/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utrace.model.BC;

import com.utrace.model.Ent.ProductEnt;
import com.urvega.framework.util.ConvertUtil;
import com.urvega.framework.util.LogUtil;
import com.utrace.model.CA.ProductCA;
import com.utrace.model.DA.ProductDA;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Admin
 */
public class ProductBC {
    private static final Logger logger = LogUtil.getLogger(ProductBC.class);
    
    private static ProductEnt valid(ProductEnt value) {
        if (value == null ||
                value.name == null || value.name.length() <= 0) {
            return null;
        }
        
        value.name = ConvertUtil.toString(value.name).trim();
        value.description = ConvertUtil.toString(value.description).trim();
        value.content = ConvertUtil.toString(value.content).trim();
        value.productionUnitCode = ConvertUtil.toString(value.productionUnitCode).trim();
        value.rawMaterialArea = ConvertUtil.toString(value.rawMaterialArea).trim();
        
        return value;
    }
    
    public static boolean insert(ProductEnt item) {
        long startTime = System.currentTimeMillis();
        boolean result = false;
        item = valid(item);
        
        if (item == null) {
            return result;
        }
        
        try {
            item.createdAt = System.currentTimeMillis();
            item.updatedAt = item.createdAt;
            item.status = true;
            
            if (!ProductDA.insert(item)) {
                return result;
            }
            
            if (!ProductCA.set(item)) {
                return result;
            }
            
            result = ProductCA.setMap(item.name, item.id);
        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
        } finally {
        }
        return result;
    }
    
    public static boolean update(ProductEnt item) {
        long startTime = System.currentTimeMillis();
        boolean result = false;
        item = valid(item);
        
        if (item.id <= 0) {
            return result;
        }
        
        try {
            ProductEnt product = ProductCA.get(item.id);
            if (product == null) {
                return result;
            }
            
            item.createdAt = product.createdAt;
            item.updatedAt = System.currentTimeMillis();
            
            if (!ProductCA.set(item)) {
                return result;
            }
            
            if (!ProductDA.update(item)) {
                ProductCA.set(product);
                return result;
            }
            
            result = true;
        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
        } finally {
        }
        return result;
    }
    
    public static ProductEnt get(int id) {
        long startTime = System.currentTimeMillis();
        ProductEnt result = null;
        
        try {
            if (id <= 0) {
                return result;
            }
            
            result = ProductCA.get(id);
        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
        } finally {
        }
        return result;
    }
    
    public static ProductEnt getByName(String name) {
        long startTime = System.currentTimeMillis();
        ProductEnt result = null;
        
        try {
            if (ConvertUtil.toString(name).length() <= 0) {
                return result;
            }
            
            int id = ProductCA.getMap(name.trim());
            result = get(id);
        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
        } finally {
        }
        return result;
    }
}
