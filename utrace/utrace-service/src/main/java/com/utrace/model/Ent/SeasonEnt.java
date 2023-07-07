/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utrace.model.Ent;

/**
 *
 * @author Admin
 */
public class SeasonEnt {
    public int id;
    public String lotNumber;
    public String name;
    public int memberId;
    public int productId;
    public String logbook;
    public long harvest;
    public long pack;
    public String logistic;
    public boolean status;
    public long createdAt;
    public long updatedAt;
    
    public SeasonEnt() {
        this.id = 0;
        this.lotNumber = "";
        this.name = "";
        this.memberId = 0;
        this.productId = 0;
        this.logbook = null;
        this.harvest = 0;
        this.pack = 0;
        this.logistic = null;
        this.status = false;
        this.createdAt = 0;
        this.updatedAt = 0;
    }
}

