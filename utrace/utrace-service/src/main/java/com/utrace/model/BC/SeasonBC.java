/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utrace.model.BC;

import com.utrace.model.Ent.SeasonEnt;
import com.urvega.framework.util.ConvertUtil;
import com.urvega.framework.util.LogUtil;
import com.utrace.model.CA.SeasonCA;
import com.utrace.model.DA.SeasonDA;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Admin
 */
public class SeasonBC {
    private static final Logger logger = LogUtil.getLogger(SeasonBC.class);

    private static SeasonEnt valid(SeasonEnt value) {
        if (value == null || value.lotNumber == null || value.lotNumber.length() <= 0
                || value.name == null || value.name.length() <= 0) {
            return null;
        }

        value.lotNumber = ConvertUtil.toString(value.lotNumber).trim();
        value.name = ConvertUtil.toString(value.name).trim();
        value.logbook = ConvertUtil.toString(value.logbook).trim();
        value.logistic = ConvertUtil.toString(value.logistic).trim();

        return value;
    }

    public static boolean insert(SeasonEnt item) {
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

            if (!SeasonDA.insert(item)) {
                return result;
            }

            if (!SeasonCA.set(item)) {
                return result;
            }

            result = SeasonCA.setMap(item.lotNumber, item.id);
        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
        } finally {
        }
        return result;
    }

    public static boolean update(SeasonEnt item) {
        long startTime = System.currentTimeMillis();

        boolean result = false;

        item = valid(item);

        if (item.id <= 0) {
            return result;
        }

        try {
            SeasonEnt season = SeasonCA.get(item.id);
            if (season == null) {
                return result;
            }

            item.lotNumber = season.lotNumber;
            item.createdAt = season.createdAt;
            item.updatedAt = System.currentTimeMillis();

            if (!SeasonCA.set(item)) {
                return result;
            }

            if (!SeasonDA.update(item)) {
                SeasonCA.set(season);
                return result;
            }

            result = true;
        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
        } finally {
        }
        return result;
    }

    public static SeasonEnt get(int id) {
        long startTime = System.currentTimeMillis();

        SeasonEnt result = null;

        try {
            if (id <= 0) {
                return result;
            }

            result = SeasonCA.get(id);
        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
        } finally {
        }
        return result;
    }

    public static SeasonEnt getByLotNumber(String lotNumber) {
        long startTime = System.currentTimeMillis();

        SeasonEnt result = null;

        try {
            if (ConvertUtil.toString(lotNumber).length() <= 0) {
                return result;
            }

            int id = SeasonCA.getMap(lotNumber.trim());
            result = get(id);
        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
        } finally {
        }
        return result;
    }
}