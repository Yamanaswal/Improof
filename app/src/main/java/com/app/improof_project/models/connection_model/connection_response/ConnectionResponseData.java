package com.app.improof_project.models.connection_model.connection_response;

import java.util.List;

public class ConnectionResponseData {

    private int status;
    private int resCode;
    private String message;
    private List<ConnectionResponseResult> result;

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

    public List<ConnectionResponseResult> getResult() {
        return result;
    }

    public void setResult(List<ConnectionResponseResult> result) {
        this.result = result;
    }


}
