/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utrace.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.urvega.framework.util.JSONUtil;
import com.utrace.model.BC.ProductBC;
import com.utrace.model.CA.ProductCA;
import com.utrace.model.Ent.ProductEnt;
import com.utrace.util.APIResponseUtil;
import java.util.List;
import spark.Request;
import spark.Response;

/**
 *
 * @author Admin
 */
public class ProductController {

//    public static String updateProduct(Request req, Response res) throws JsonProcessingException {
//        String productId = req.queryParams("productId");
//        String name = req.queryParams("name");
//        String companyId = req.queryParams("companyId");
//        String acreage = req.queryParams("acreage");
//        String location = req.queryParams("location");
//        // Update other properties based on your requirements
//
//        int productIdInt = Integer.parseInt(productId);
//        int companyIdInt = Integer.parseInt(companyId);
//        float acreageFloat = Float.parseFloat(acreage);
//
//        // Kiểm tra xem sản phẩm đã tồn tại hay chưa
//        ProductEnt existingProduct = ProductCA.get(companyIdInt, productIdInt);
//        if (existingProduct != null) {
//            // Cập nhật thông tin sản phẩm
//            existingProduct.name = name;
//            existingProduct.acreage = acreageFloat;
//            existingProduct.location = location;
//            // Update other properties based on your requirements
//            boolean success = ProductBC.update(existingProduct);
//
//            if (success) {
//                return APIResponseUtil.successResponse("Product updated successfully", existingProduct);
//            } else {
//                return APIResponseUtil.errorResponse("Failed to update Product");
//            }
//        } else {
//            return APIResponseUtil.errorResponse("Product does not exist");
//        }
//    }
    public static String getListByCompanyId(Request req, Response res) throws JsonProcessingException {
        String companyId = req.params("companyId");
        int companyIdInt = Integer.parseInt(companyId);

        // Lấy danh sách sản phẩm của công ty từ Redis
        List<ProductEnt> products = ProductCA.getListByCompanyId(companyIdInt);
        if (!products.isEmpty()) {
            // Chuyển đối tượng sản phẩm thành chuỗi JSON
            String productsJson = JSONUtil.serialize(products);
            return APIResponseUtil.successResponse(productsJson);
        } else {
            return APIResponseUtil.errorResponse("No products found for the company");
        }
    }

    public static String addProduct(Request req, Response res) throws JsonProcessingException {
        String companyId = req.queryParams("companyId");
        String productKey = req.queryParams("productKey");
        String name = req.queryParams("name");
        String description = req.queryParams("description");
        String content = req.queryParams("content");
        String productionUnitCode = req.queryParams("productionUnitCode");
        String acreage = req.queryParams("acreage");
        String rawMaterialArea = req.queryParams("rawMaterialArea");
        String photos = req.queryParams("photos");
        String recipe = req.queryParams("recipe");
        String recipePhotos = req.queryParams("recipePhotos");
        String farmingPhotos = req.queryParams("farmingPhotos");
        String video = req.queryParams("video");
        String certification = req.queryParams("certification");

        int companyIdInt = Integer.parseInt(companyId);
        float acreageFloat = Float.parseFloat(acreage);

        // Thêm sản phẩm mới
        ProductEnt newProduct = new ProductEnt();
        newProduct.companyId = companyIdInt;
        newProduct.productKey = productKey;
        newProduct.name = name;
        newProduct.description = description;
        newProduct.content = content;
        newProduct.productionUnitCode = productionUnitCode;
        newProduct.acreage = acreageFloat;
        newProduct.rawMaterialArea = rawMaterialArea;
        newProduct.photos = photos;
        newProduct.recipe = recipe;
        newProduct.recipePhotos = recipePhotos;
        newProduct.farmingPhotos = farmingPhotos;
        newProduct.video = video;
        newProduct.certification = certification;

        boolean success = ProductBC.insert(newProduct);

        if (success) {
            return APIResponseUtil.successResponse("Product added successfully", newProduct);
        } else {
            return APIResponseUtil.errorResponse("Failed to add Product");
        }
    }

}
