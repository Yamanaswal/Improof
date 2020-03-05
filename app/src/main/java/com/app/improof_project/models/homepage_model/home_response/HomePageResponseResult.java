package com.app.improof_project.models.homepage_model.home_response;

import java.io.Serializable;
import java.util.List;

public class HomePageResponseResult implements Serializable {

    private String id;
    private int c_id;
    private String name;
    private String company_name;
    private String email;
    private String designation;
    private String pic_path;
    private int evaluater;
    private int evaluated;
    private int status;
    private List<HomePageResponseSkillsData> skills;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getC_id() {
        return c_id;
    }

    public void setC_id(int c_id) {
        this.c_id = c_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getPic_path() {
        return pic_path;
    }

    public void setPic_path(String pic_path) {
        this.pic_path = pic_path;
    }

    public int getEvaluater() {
        return evaluater;
    }

    public void setEvaluater(int evaluater) {
        this.evaluater = evaluater;
    }

    public int getEvaluated() {
        return evaluated;
    }

    public void setEvaluated(int evaluated) {
        this.evaluated = evaluated;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<HomePageResponseSkillsData> getSkills() {
        return skills;
    }

    public void setSkills(List<HomePageResponseSkillsData> skills) {
        this.skills = skills;
    }
}
