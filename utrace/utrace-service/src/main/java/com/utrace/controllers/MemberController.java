/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utrace.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.urvega.framework.util.JSONUtil;
import com.utrace.model.BC.MemberBC;
import com.utrace.model.Ent.MemberEnt;
import com.utrace.util.APIResponseUtil;
import spark.Request;
import spark.Response;

/**
 *
 * @author Admin
 */
public class MemberController {

    public static String getMember(Request req, Response res) throws JsonProcessingException {
        String idCom = req.queryParams("idCom");
        String idMem = req.queryParams("idMem");

        int idComInt = Integer.parseInt(idCom);
        int idMemInt = Integer.parseInt(idMem);

        // Lấy thông tin thành viên
        MemberEnt member = MemberBC.get(idComInt, idMemInt);
        if (member != null) {
            // Chuyển đối tượng thành viên thành chuỗi JSON
            String memberJson = JSONUtil.serialize(member);
            return APIResponseUtil.successResponse(memberJson);
        } else {
            return APIResponseUtil.errorResponse("Member not found");
        }
    }

   public static String updateMember(Request req, Response res) throws JsonProcessingException {
    String memberId = req.queryParams("memberId");
    String name = req.queryParams("name");
    String companyId = req.queryParams("companyId");
    String acreage = req.queryParams("acreage");
    String location = req.queryParams("location");

    int memberIdInt = Integer.parseInt(memberId);
    int companyIdInt = Integer.parseInt(companyId);
    float acreageFloat = Float.parseFloat(acreage);

    // Kiểm tra xem thành viên đã tồn tại hay chưa
    MemberEnt existingMember = MemberBC.get(companyIdInt, memberIdInt);
    if (existingMember != null) {
        // Cập nhật thông tin thành viên
        existingMember.name = name;
        existingMember.acreage = acreageFloat;
        existingMember.location = location;
        boolean success = MemberBC.update(existingMember);

        if (success) {
            return APIResponseUtil.successResponse("Member updated successfully");
        } else {
            return APIResponseUtil.errorResponse("Failed to update Member");
        }
    } else {
        return APIResponseUtil.errorResponse("Member does not exist");
    }
}

public static String addMember(Request req, Response res) throws JsonProcessingException {
    String name = req.queryParams("name");
    String companyId = req.queryParams("companyId");
    String acreage = req.queryParams("acreage");
    String location = req.queryParams("location");

    int companyIdInt = Integer.parseInt(companyId);
    float acreageFloat = Float.parseFloat(acreage);

    // Thêm thành viên mới
    MemberEnt newMember = new MemberEnt();
    newMember.name = name;
    newMember.companyId = companyIdInt;
    newMember.acreage = acreageFloat;
    newMember.location = location;
    boolean success = MemberBC.insert(newMember);

    if (success) {
        return APIResponseUtil.successResponse("Member added successfully");
    } else {
        return APIResponseUtil.errorResponse("Failed to add Member");
    }
}


}
