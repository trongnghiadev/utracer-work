/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utrace.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.urvega.framework.util.JSONUtil;
import com.utrace.model.BC.MemberBC;
import com.utrace.model.CA.MemberCA;
import com.utrace.model.Ent.MemberEnt;
import com.utrace.util.APIResponseUtil;
import java.util.List;
import spark.Request;
import spark.Response;

/**
 *
 * @author Admin
 */
public class MemberController {

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
        MemberEnt existingMember = MemberCA.get(companyIdInt, memberIdInt);
        if (existingMember != null) {
            // Cập nhật thông tin thành viên
            existingMember.name = name;
            existingMember.acreage = acreageFloat;
            existingMember.location = location;
            boolean success = MemberBC.update(existingMember);

            if (success) {
                return APIResponseUtil.successResponse("Member updated successfully", existingMember);
            } else {
                return APIResponseUtil.errorResponse("Failed to update Member");
            }
        } else {
            return APIResponseUtil.errorResponse("Member does not exist");
        }
    }

    public static String getListByCompanyId(Request req, Response res) throws JsonProcessingException {
        String idCom = req.params("idCom");
        int idComInt = Integer.parseInt(idCom);

        // Lấy danh sách thành viên của công ty từ Redis
        List<MemberEnt> members = MemberCA.getListByCompanyId(idComInt);
        if (!members.isEmpty()) {
            // Chuyển đối tượng thành viên thành chuỗi JSON
            String membersJson = JSONUtil.serialize(members);
            return APIResponseUtil.successResponse(membersJson);
        } else {
            return APIResponseUtil.errorResponse("No members found for the company");
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
            return APIResponseUtil.successResponse("Member added successfully", newMember);
        } else {
            return APIResponseUtil.errorResponse("Failed to add Member");
        }
    }
}
