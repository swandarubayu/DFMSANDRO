package com.ramadhany.vodjo.latihan1.Model;

/**
 * Created by user on 7/8/2018.
 */

public class UserMovBody {

    private int user_id;
    private String location;
    private String updatedat;

    public UserMovBody(int user_id, String location, String updatedat) {
        this.user_id = user_id;
        this.location = location;
        this.updatedat = updatedat;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUpdatedat() {
        return updatedat;
    }

    public void setUpdatedat(String updatedat) {
        this.updatedat = updatedat;
    }
}

