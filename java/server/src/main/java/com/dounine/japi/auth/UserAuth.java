package com.dounine.japi.auth;

import java.time.LocalDateTime;

/**
 * Created by huanghuanlai on 2017/2/27.
 */
public class UserAuth {
    private String username;
    private String password;
    private boolean isAdmin = false;
    private LocalDateTime liveTime;

    public UserAuth(){}

    public UserAuth(String username,String password){
        this.username = username;
        this.password = password;
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

    public LocalDateTime getLiveTime() {
        return liveTime;
    }

    public void setLiveTime(LocalDateTime liveTime) {
        this.liveTime = liveTime;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
