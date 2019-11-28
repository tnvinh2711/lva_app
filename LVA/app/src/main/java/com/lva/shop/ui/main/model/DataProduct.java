package com.lva.shop.ui.main.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.lva.shop.utils.CommonUtils;

public class DataProduct {
    private int quality;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("product_category_id")
    @Expose
    private String productCategoryId;
    @SerializedName("cat_name")
    @Expose
    private String catName;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("price_discount")
    @Expose
    private String priceDiscount;
    @SerializedName("product_sortdesc")
    @Expose
    private String productSortdesc;
    @SerializedName("link_image")
    @Expose
    private String linkImage;
    @SerializedName("link_detail")
    @Expose
    private String linkDetail;
    @SerializedName("image_name")
    @Expose
    private Object imageName;
    @SerializedName("image_path")
    @Expose
    private Object imagePath;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductCategoryId() {
        return productCategoryId;
    }

    public void setProductCategoryId(String productCategoryId) {
        this.productCategoryId = productCategoryId;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPriceDiscount() {
        return priceDiscount;
    }

    public void setPriceDiscount(String priceDiscount) {
        this.priceDiscount = priceDiscount;
    }

    public String getProductSortdesc() {
        return CommonUtils.convertHtmlToString(productSortdesc);
    }

    public void setProductSortdesc(String productSortdesc) {
        this.productSortdesc = productSortdesc;
    }

    public String getLinkImage() {
        return linkImage;
    }

    public void setLinkImage(String linkImage) {
        this.linkImage = linkImage;
    }

    public String getLinkDetail() {
        return linkDetail;
    }

    public void setLinkDetail(String linkDetail) {
        this.linkDetail = linkDetail;
    }

    public Object getImageName() {
        return imageName;
    }

    public void setImageName(Object imageName) {
        this.imageName = imageName;
    }

    public Object getImagePath() {
        return imagePath;
    }

    public void setImagePath(Object imagePath) {
        this.imagePath = imagePath;
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }
}
