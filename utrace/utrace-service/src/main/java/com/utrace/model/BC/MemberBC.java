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
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Admin
 */
public class MemberBC {
    private static final Logger logger = LogUtil.getLogger(MemberBC.class);
    
    private static MemberEnt valid(MemberEnt value){
        if(value == null ||
                value.name == null || value.name.length() <= 0 ||
                value.companyId <= 0) {
            return null;
        } 
        
        value.name = ConvertUtil.toString(value.name).trim();
        value.location = ConvertUtil.toString(value.location).trim();
        
        return value;
    }
    
    public static boolean insert(MemberEnt item){
        long startTime = System.currentTimeMillis();
        boolean result = false;
        item = valid(item);
        
        if(item == null){
            return result;
        }
        
        try {
            item.createdAt = System.currentTimeMillis();
            item.updatedAt = item.createdAt;
            item.status = true;
            
            if(!MemberDA.insert(item)) {
                return result;
            }
            
            if(!MemberCA.set(item)) {
              return result;
            }
            
        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
        } finally {
        }
        return result;
    }
    
    public static boolean update(MemberEnt item){
        long startTime = System.currentTimeMillis();
        
        boolean result = false;
        
        item = valid(item);
        
        if(item.id <= 0){
            return result;
        }
        
        try {
            MemberEnt member = MemberCA.get(item.companyId,item.id);
            if(member == null) {
                return result;
            }
            
            item.createdAt = member.createdAt;
            item.updatedAt = System.currentTimeMillis();
            
            if(!MemberCA.set(item)){
                return result;
            }
            
            result = MemberDA.update(item);
            
        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
        } finally {
        }
        return result;
    }
    
    public static MemberEnt get(int idCom, int idMem){
        long startTime = System.currentTimeMillis();
        
        MemberEnt result = null;
        
        try {
            if(idMem <= 0){
                return result;
            }
            
            result = MemberCA.get(idCom, idMem);
            
        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
        } finally {
        }
        return result;
    }
}

