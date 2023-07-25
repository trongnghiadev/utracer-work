/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utrace.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.utrace.model.BC.LogbookBC;
import com.utrace.model.Ent.LogbookEnt;
import com.utrace.util.APIResponseUtil;
import spark.Request;
import spark.Response;

/**
 *
 * @author Admin
 */
public class LogbookController {

    public static String addLogbook(Request req, Response res) throws JsonProcessingException {
        String productId = req.queryParams("productId");
        String content = req.queryParams("content");

        int productIdInt = Integer.parseInt(productId);

        // Thêm logbook mới
        LogbookEnt newLogbook = new LogbookEnt();
        newLogbook.productId = productIdInt;
        newLogbook.content = content;

        boolean success = LogbookBC.insert(newLogbook);

        if (success) {
            return APIResponseUtil.successResponse("Logbook added successfully", newLogbook);
        } else {
            return APIResponseUtil.errorResponse("Failed to add Logbook");
        }
    }

    public static String updateLogbook(Request req, Response res) throws JsonProcessingException {
        String logbookId = req.queryParams("logbookId");
        String content = req.queryParams("content");

        int logbookIdInt = Integer.parseInt(logbookId);

        // Kiểm tra xem logbook đã tồn tại hay chưa
        LogbookEnt existingLogbook = LogbookBC.get(logbookIdInt);
        if (existingLogbook != null) {
            // Cập nhật thông tin logbook
            existingLogbook.content = content;

            boolean success = LogbookBC.update(existingLogbook);

            if (success) {
                return APIResponseUtil.successResponse("Logbook updated successfully", existingLogbook);
            } else {
                return APIResponseUtil.errorResponse("Failed to update Logbook");
            }
        } else {
            return APIResponseUtil.errorResponse("Logbook does not exist");
        }
    }

    public static String getLogbookById(Request req, Response res) throws JsonProcessingException {
        String logbookId = req.params("logbookId");
        int logbookIdInt = Integer.parseInt(logbookId);

        // Lấy thông tin logbook từ CSDL
        LogbookEnt logbook = LogbookBC.get(logbookIdInt);
        if (logbook != null) {
            return APIResponseUtil.successResponse(logbook);
        } else {
            return APIResponseUtil.errorResponse("Logbook not found");
        }
    }
    
    public static String getLogbookByProductId(Request req, Response res) throws JsonProcessingException {
        String productId = req.params("productId");
        int productIdInt = Integer.parseInt(productId);

        // Lấy thông tin logbook từ CSDL dựa vào productId
        LogbookEnt logbook = LogbookBC.getLogbookByProductId(productIdInt);
        if (logbook != null) {
            return APIResponseUtil.successResponse(logbook);
        } else {
            return APIResponseUtil.errorResponse("Logbook not found for the product");
        }
    }
    
    
}

