package com.lva.shop.ui.login.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelFacebook {
    @SerializedName("display_name")
    @Expose
    private String display_name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("photo_url")
    @Expose
    private String photo_url;


    public ModelFacebook(String display_name, String email, String photo_url) {
        this.display_name = display_name;
        this.email = email;
        this.photo_url = photo_url;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }
}
