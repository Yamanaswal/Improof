package com.app.improof_project.models.skills_assessment_model.skills_assessment_response;


import java.io.Serializable;

public class SkillsAssessmentWrapper implements Serializable {

    private SkillsAssessmentData data;

    public SkillsAssessmentData getData() {
        return data;
    }

    public void setData(SkillsAssessmentData data) {
        this.data = data;
    }

}
