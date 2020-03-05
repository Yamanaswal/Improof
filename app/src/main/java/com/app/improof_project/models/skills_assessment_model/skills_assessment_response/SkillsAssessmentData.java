package com.app.improof_project.models.skills_assessment_model.skills_assessment_response;

import java.io.Serializable;
import java.util.List;

public class SkillsAssessmentData implements Serializable {
    private int status;
    private int resCode;
    private String message;
    private List<SkillsAssessmentResult> result;

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

    public List<SkillsAssessmentResult> getResult() {
        return result;
    }

    public void setResult(List<SkillsAssessmentResult> result) {
        this.result = result;
    }

}
