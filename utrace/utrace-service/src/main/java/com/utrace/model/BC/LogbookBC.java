/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utrace.model.BC;

import com.utrace.model.Ent.LogbookEnt;
import com.urvega.framework.util.ConvertUtil;
import com.urvega.framework.util.LogUtil;
import com.utrace.model.CA.LogbookCA;
import com.utrace.model.DA.LogbookDA;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Admin
 */
public class LogbookBC {
    private static final Logger logger = LogUtil.getLogger(LogbookBC.class);

    private static LogbookEnt valid(LogbookEnt value) {
        if (value == null || value.content == null || value.content.length() <= 0) {
            return null;
        }

        value.content = ConvertUtil.toString(value.content).trim();

        return value;
    }

    public static boolean insert(LogbookEnt item) {
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

            if (!LogbookDA.insert(item)) {
                return result;
            }

            if (!LogbookCA.set(item)) {
                return result;
            }

            result = LogbookCA.setMap(String.valueOf(item.productId), item.id);
        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
        }

        return result;
    }

    public static boolean update(LogbookEnt item) {
        long startTime = System.currentTimeMillis();

        boolean result = false;

        item = valid(item);

        if (item.id <= 0) {
            return result;
        }

        try {
            LogbookEnt logbook = LogbookCA.get(item.id);
            if (logbook == null) {
                return result;
            }

            item.productId = logbook.productId;
            item.createdAt = logbook.createdAt;
            item.updatedAt = System.currentTimeMillis();

            if (!LogbookCA.set(item)) {
                return result;
            }

            if (!LogbookDA.update(item)) {
                LogbookCA.set(logbook);
                return result;
            }

            result = true;
        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
        }

        return result;
    }

    public static LogbookEnt get(int id) {
        long startTime = System.currentTimeMillis();

        LogbookEnt result = null;

        try {
            if (id <= 0) {
                return result;
            }

            result = LogbookCA.get(id);
        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
        }

        return result;
    }
}