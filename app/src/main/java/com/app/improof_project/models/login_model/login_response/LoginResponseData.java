package com.app.improof_project.models.login_model.login_response;

public class LoginResponseData {

    private int status;
    private int resCode;
    private String message;
    private LoginResponseResult result;

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

    public LoginResponseResult getResult() {
        return result;
    }

    public void setResult(LoginResponseResult result) {
        this.result = result;
    }
}
