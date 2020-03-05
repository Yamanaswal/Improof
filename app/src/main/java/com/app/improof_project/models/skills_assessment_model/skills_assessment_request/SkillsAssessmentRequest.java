package com.app.improof_project.models.skills_assessment_model.skills_assessment_request;

public class SkillsAssessmentRequest {

    private String c_id;
    private String given_by;
    private String given_to;

    public String getApp_key() {
        return app_key;
    }

    public void setApp_key(String app_key) {
        this.app_key = app_key;
    }

    private String app_key;

    public String getC_id() {
        return c_id;
    }

    public void setC_id(String c_id) {
        this.c_id = c_id;
    }

    public String getGiven_by() {
        return given_by;
    }

    public void setGiven_by(String given_by) {
        this.given_by = given_by;
    }

    public String getGiven_to() {
        return given_to;
    }

    public void setGiven_to(String given_to) {
        this.given_to = given_to;
    }
}
