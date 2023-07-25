/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utrace.model.Ent;

/**
 *
 * @author Admin
 */
public class MemberEnt {
    public int id;
    public String name;
    public int companyId;
    public float acreage;
    public String location;
    public boolean status;
    public long createdAt;
    public long updatedAt;

    public MemberEnt() {
        this.id = 0;
        this.name = "";
        this.companyId = 0;
        this.acreage = 0.0f;
        this.location = "";
        this.status = false;
        this.createdAt = 0;
        this.updatedAt = 0;
    }

    public String[] toString(String[] string) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}