/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utrace.model.Ent;

/**
 *
 * @author Admin
 */
public class ProductEnt {
    public int id;
    public int companyId;
    public String productKey;
    public String name;
    public String description;
    public String content;
    public String productionUnitCode;
    public float acreage;
    public String rawMaterialArea;
    public String photos;
    public String recipe;
    public String recipePhotos;
    public String farmingPhotos;
    public String video;
    public String certification;
    public boolean status;
    public long createdAt;
    public long updatedAt;

    public ProductEnt() {
        this.id = 0;
        this.companyId = 0;
        this.productKey = "";
        this.name = "";
        this.description = "";
        this.content = "";
        this.productionUnitCode = "";
        this.acreage = 0.0f;
        this.rawMaterialArea = "";
        this.photos = "";
        this.recipe = "";
        this.recipePhotos = "";
        this.farmingPhotos = "";
        this.video = "";
        this.certification = "";
        this.status = false;
        this.createdAt = 0;
        this.updatedAt = 0;
    }
}

