package com.utrace.example.model;

import java.sql.Timestamp;

/**
 *
 * @author superman
 */
public class UserEnt {

    public int id;
    public String email;
    public boolean emailVerified;
    public String fullname;
    public String phone;
    public boolean status;
    public Timestamp createdAt;
    public Timestamp updatedAt;

    // User Entity
    public UserEnt(int id, String email, boolean emailVerified, String fullname, String phone, boolean status, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.email = email;
        this.emailVerified = emailVerified;
        this.fullname = fullname;
        this.phone = phone;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UserEnt(String email, boolean emailVerified, String fullname, String phone, boolean status) {
        this.email = email;
        this.emailVerified = emailVerified;
        this.fullname = fullname;
        this.phone = phone;
        this.status = status;
    }

    public UserEnt() {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
}
