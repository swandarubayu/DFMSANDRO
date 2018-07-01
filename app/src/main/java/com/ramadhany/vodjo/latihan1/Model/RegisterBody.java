package com.ramadhany.vodjo.latihan1.Model;

/**
 * Created by user on 6/28/2018.
 */

public class RegisterBody {
    private String username;
    private String password;
    private String domisili;
    private int role;

    public RegisterBody(String username, String password, String domisili, int role) {
        this.username = username;
        this.password = password;
        this.domisili = domisili;
        this.role = role;
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

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
