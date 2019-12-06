package com.lva.shop.ui.detail.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductOrder {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("quality")
    @Expose
    private Integer quality;

    public ProductOrder(String id, String name, Integer quality) {
        this.id = id;
        this.name = name;
        this.quality = quality;
    }

    public ProductOrder() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuality() {
        return quality;
    }

    public void setQuality(Integer quality) {
        this.quality = quality;
    }
}
