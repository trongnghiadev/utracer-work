/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utrace.model.Ent;

/**
 *
 * @author Admin
 */
public class LogbookEnt {
    public int id;
    public int productId;
    public String content;
    public boolean status;
    public long createdAt;
    public long updatedAt;

    public LogbookEnt() {
        this.id = 0;
        this.productId = 0;
        this.content = "";
        this.status = false;
        this.createdAt = 0;
        this.updatedAt = 0;
    }
}
