package com.utrace.model;

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
    public long createdAt;
    public long updatedAt;
    public String passwordUser;
    
    public UserEnt () {
        this.id = 0;
        this.email = "";
        this.emailVerified = false;
        this.fullname = "";
        this.phone = "";
        this.status = true;
        this.createdAt = 0;
        this.updatedAt = 0;
        this.passwordUser = "";
    }

    public void setPasswordUser(String passwordUser) {
        this.passwordUser = passwordUser;
    }

    // User Entity
    public UserEnt(int id, String email, boolean emailVerified, String fullname, String phone, boolean status, long createdAt, long updatedAt) {
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
    
}
