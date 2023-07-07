/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utrace.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.utrace.model.BC.CompanyBC;
import com.utrace.model.CA.CompanyCA;
import com.utrace.model.Ent.CompanyEnt;
import com.utrace.util.APIResponseUtil;
import spark.Request;
import spark.Response;

/**
 *
 * @author Admin
 */
public class CompanyController {

    public CompanyController() {

    }

    public static String getCompanyByIdUser(Request req, Response res) throws JsonProcessingException {
        int ID = Integer.parseInt(req.params("iduser"));
        CompanyEnt company = CompanyBC.getByUserID(ID);

        if (company != null) {
            return APIResponseUtil.successResponse(company);
        } else {
            return APIResponseUtil.errorResponse("Company not found");
        }
    }

    public static String addOrUpdateCompany(Request req, Response res) throws JsonProcessingException {

    String userId = req.queryParams("userId");
    String name = req.queryParams("name");
    String phone = req.queryParams("phone");
    String address = req.queryParams("address");
    String logo = req.queryParams("logo");
    String website = req.queryParams("website");

    int userIdInt = Integer.parseInt(userId);
    
    // Kiểm tra xem công ty đã tồn tại hay chưa
    CompanyEnt existingCompany = CompanyBC.getByUserID(userIdInt);
    if (existingCompany != null) {
        // Cập nhật thông tin công ty
        existingCompany.name = name;
        existingCompany.phone = phone;
        existingCompany.address = address;
        existingCompany.logo = logo;
        existingCompany.website = website;
        boolean success = CompanyBC.update(existingCompany);

        if (success) {
            return APIResponseUtil.successResponse("Company updated successfully");
        } else {
            return APIResponseUtil.errorResponse("Failed to update Company");
        }
    } else {
        // Thêm công ty mới
        CompanyEnt newCompany = new CompanyEnt();
        newCompany.name = name;
        newCompany.phone = phone;
        newCompany.address = address;
        newCompany.logo = logo;
        newCompany.website = website;
        boolean success = CompanyBC.insert(newCompany);

        if (success) {
            return APIResponseUtil.successResponse("Company added successfully");
        } else {
            return APIResponseUtil.errorResponse("Failed to add Company");
        }
    }
 }
}
