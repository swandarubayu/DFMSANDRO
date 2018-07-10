package com.ramadhany.vodjo.latihan1.Model;

/**
 * Created by user on 6/28/2018.
 */

public class RegisterBody {
    private String username;
    private String password;
    private String domisili;

    public RegisterBody(String username, String password, String domisili) {
        this.username = username;
        this.password = password;
        this.domisili = domisili;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDomisili() {
        return domisili;
    }

    public void setDomisili(String domisili) {
        this.domisili = domisili;
    }
}
