package com.app.improof_project.models.login_model.login_response;

import com.google.gson.annotations.SerializedName;

public class LoginResponseWrapper {

    @SerializedName("data")
    private LoginResponseData baseData;


    public LoginResponseData getBaseData() {
        return baseData;
    }

    public void setBaseData(LoginResponseData baseData) {
        this.baseData = baseData;
    }
}
