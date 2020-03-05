package com.app.improof_project.models.connection_model.connection_response;

import java.io.Serializable;

public class ConnectionResponseResult implements Serializable {

    private int id;
    private int c_id;
    private String name;
    private String email;
    private String designation;
    private String password;
    private String pic_path;
    private int evaluater;
    private int evaluated;
    private int status;
    private String created_at;
    private int updated_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(int updated_at) {
        this.updated_at = updated_at;
    }
}
