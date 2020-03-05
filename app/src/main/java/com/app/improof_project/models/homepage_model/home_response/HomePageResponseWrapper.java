package com.app.improof_project.models.homepage_model.home_response;

import java.io.Serializable;

public class HomePageResponseWrapper implements Serializable {

    private HomePageResponseData data;

    public HomePageResponseData getData() {
        return data;
    }

    public void setData(HomePageResponseData data) {
        this.data = data;
    }
}
