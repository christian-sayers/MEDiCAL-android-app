package com.example.fydp.ui;

import android.app.Application;

public class AccessToken extends Application {
    private String access_token;

    public String getGlobalVariable() {
        return access_token;
    }

    public void setGlobalVariable(String value) {
        this.access_token = value;
    }
}

