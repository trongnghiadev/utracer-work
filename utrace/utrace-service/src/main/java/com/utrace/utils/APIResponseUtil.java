/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utrace.utils;                               
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.urvega.framework.util.JSONUtil;

/**
 *
 * @author Admin
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class APIResponseUtil {

    private static final String SUCCESS_STATUS = "true";
    private static final String ERROR_STATUS = "false";
    
    public static String successResponse(Object data) throws JsonProcessingException {
        APIResponse response = new APIResponse(SUCCESS_STATUS, null, data);
        return JSONUtil.serialize(response);
    }
    
    public static String successResponse(String message, Object data) throws JsonProcessingException {
        APIResponse response = new APIResponse(SUCCESS_STATUS, message, data);
        return JSONUtil.serialize(response);
    }

    public static String errorResponse(String message) throws JsonProcessingException {
        APIResponse response = new APIResponse(ERROR_STATUS, message, null);
        return JSONUtil.serialize(response);
    }

    public static String errorResponse(String message, Object data) throws JsonProcessingException {
        APIResponse response = new APIResponse(ERROR_STATUS, message, data);
        return JSONUtil.serialize(response);
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private static class APIResponse {

        private String status;
        private String message;
        private Object data;

        public APIResponse(String status, String message, Object data) {
            this.status = status;
            this.message = message;
            this.data = data;
        }

        // Getters and setters
        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }
    }
}