/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utrace.model.BC;

import com.utrace.model.Ent.CompanyEnt;
import com.urvega.framework.util.ConvertUtil;
import com.urvega.framework.util.LogUtil;
import com.utrace.model.CA.CompanyCA;
import com.utrace.model.DA.CompanyDA;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Admin
 */
public class CompanyBC {
    private static final Logger logger = LogUtil.getLogger(CompanyBC.class);
    
    private static CompanyEnt valid(CompanyEnt value){
        
        if(value == null ||
                value.name == null || value.name.length() <= 0) {
            return null;
        } 
        
        value.name = ConvertUtil.toString(value.name).trim();
        value.phone = ConvertUtil.toString(value.phone).trim();
        value.address = ConvertUtil.toString(value.address).trim();
        value.logo = ConvertUtil.toString(value.logo).trim();
        value.website = ConvertUtil.toString(value.website).trim();
        
        return value;
    }
    
    public static boolean insert(CompanyEnt item){
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
            
            if(!CompanyDA.insert(item)) {
                return result;
            }
            
            if(!CompanyCA.set(item)){
                return result;
            }
            result = CompanyCA.setMap(item.userId, item.id);
            
        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
        } finally {
        }
        return result;
    }
    
    public static boolean update(CompanyEnt item){
        long startTime = System.currentTimeMillis();
        
        boolean result = false;
        
        item = valid(item);
        
        if(item.id <= 0){
            return result;
        }
        
        try {
            
            CompanyEnt company = CompanyCA.get(item.id);
            if(company == null) {
                return result;
            }
            
            item.createdAt = company.createdAt;
            item.updatedAt = System.currentTimeMillis();
            
            if(!CompanyCA.set(item)){
                return result;
            }
            
            if(!CompanyDA.update(item)){
                CompanyCA.set(company);             
                
                return result;
            }
            
            result = true;
        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
        } finally {
        }
        return result;
    }
    
    public static CompanyEnt get(int id){
        long startTime = System.currentTimeMillis();
        
        CompanyEnt result = null;
        
        try {
            
            if(id <= 0){
                return result;
            }
            
            result = CompanyCA.get(id);
            
        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
        } finally {
        }
        return result;
    }
    
    public static CompanyEnt getByUserID(int ID){
        long startTime = System.currentTimeMillis();
        
        CompanyEnt result = null;
        
        try {
            
            if(ConvertUtil.toString(ID).length() <= 0){
                return result;
            }
            
            int id = CompanyCA.getMap(ID);
            result = get(id);
            
        } catch (Exception e) {
            logger.error(LogUtil.stackTrace(e));
        } finally {
        }
        return result;
    }
    
    
}

