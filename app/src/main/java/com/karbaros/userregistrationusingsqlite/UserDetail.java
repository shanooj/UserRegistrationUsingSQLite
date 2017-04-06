package com.karbaros.userregistrationusingsqlite;

import java.io.Serializable;

/**
 * Created by shanu on 04-Apr-17.
 */

public class UserDetail implements Serializable{

    private int id;
    private String name;
    private String email;
    private long number;
    private String password;
    private String status;


    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public long getNumber() {
        return number;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getStatus() {
        return status;
    }
}
