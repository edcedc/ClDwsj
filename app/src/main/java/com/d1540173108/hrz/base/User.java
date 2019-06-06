package com.d1540173108.hrz.base;

import org.json.JSONObject;

public class User {

    private static class LazyHolder {
        private static final User INSTANCE = new User();
    }
    private User() {
    }
    public static final User getInstance() {
        return User.LazyHolder.INSTANCE;
    }


    private boolean login = false;
    private JSONObject userObj;
    private String userId;
    private String sessionId;
    private JSONObject userInfoObj;

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public JSONObject getUserInfoObj() {
        return userInfoObj;
    }

    public void setUserInfoObj(JSONObject userInfoObj) {
        this.userInfoObj = userInfoObj;
    }

    public void setUserObj(JSONObject userObj) {
        this.userObj = userObj;
    }

    public JSONObject getUserObj() {
        return userObj;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }

    public boolean isLogin() {
        return login;
    }

    public void setUserId(String userId) {
        if (userObj != null) {
            this.userId = userObj.optString("id");
        }
    }

    public String getUserId() {
        if (userInfoObj == null) {
           return "";
        }
        userId = userInfoObj.optString("userId");
        return userId;
    }

    public void setSessionId(String sessionId) {
        if (userObj != null) {
            this.sessionId = userObj.optString("sessionId");
        }
    }

    public String getSessionId() {
        if (userObj == null) {
            return "";
        }
        sessionId = userObj.optString("sessionId");
        return sessionId;
    }
}
