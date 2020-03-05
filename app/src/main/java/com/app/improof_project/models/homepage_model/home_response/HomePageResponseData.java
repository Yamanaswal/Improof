package com.app.improof_project.models.homepage_model.home_response;

import com.app.improof_project.models.login_model.login_response.LoginResponseResult;

import java.io.Serializable;

public class HomePageResponseData implements Serializable {
    private int status;
    private int resCode;
    private String message;
    private HomePageResponseResult result;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getResCode() {
        return resCode;
    }

    public void setResCode(int resCode) {
        this.resCode = resCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HomePageResponseResult getResult() {
        return result;
    }

    public void setResult(HomePageResponseResult result) {
        this.result = result;
    }


}
