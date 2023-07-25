/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utrace.model.BC;

import com.utrace.model.Ent.MemberEnt;
import com.urvega.framework.util.ConvertUtil;
import com.urvega.framework.util.LogUtil;
import com.utrace.model.CA.MemberCA;
import com.utrace.model.DA.MemberDA;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Admin
 */
public class MemberBC {

    private static final Logger logger = LogUtil.getLogger(MemberBC.class);

    private static MemberEnt valid(MemberEnt value) {
        if (value == null
                || value.name == null || value.name.length() <= 0
                || value.companyId <= 0) {
            return null;
        }

        value.name = ConvertUtil.toString(value.name).trim();
        value.location = ConvertUtil.toString(value.location).trim();

        return value;
    }

    public static boolean insert(MemberEnt item) {
        long startTime = System.currentTimeMillis();
        boolean result = false;
        item = valid(item);

        if (item == null) {
            return result;
        }

        try {
            item.createdAt = startTime;
            item.updatedAt = item.createdAt;
            item.status = true;

            if (!MemberDA.insert(item)) {
                return result;
            }

            if (MemberCA.set(item) > 0) {
                result = true;
            }

        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
        } finally {
        }
        return result;
    }

    public static boolean update(MemberEnt item) {
        long startTime = System.currentTimeMillis();

        boolean result = false;

        item = valid(item);

        if (item.id <= 0) {
            return result;
        }

        try {
            item.updatedAt = startTime;
            if (MemberCA.update(item)) {
                result = MemberDA.update(item);
            }

        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
        } finally {
        }
        return result;
    }

    public static List<MemberEnt> getListByCompanyId(int idCom) {
        long startTime = System.currentTimeMillis();

        List<MemberEnt> result = new ArrayList<>();

        try {
            result = MemberCA.getListByCompanyId(idCom);
        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
        } finally {
        }
        return result;
    }
}
