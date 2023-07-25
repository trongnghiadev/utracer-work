/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utrace.model.Ent;

/**
 *
 * @author Admin
 */
public class CompanyEnt {
    public int id;
    public int userId;
    public String name;
    public String phone;
    public String address;
    public String logo;
    public String website;
    public boolean status;
    public long createdAt;
    public long updatedAt;
    
    public CompanyEnt() {
        this.id = 0;
        this.userId = 0;
        this.name = "";
        this.phone = "";
        this.address = "";
        this.logo = "";
        this.website = "";
        this.status = false;
        this.createdAt = 0;
        this.updatedAt = 0;
    }
    
}
