package com.lva.shop.ui.main.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Knowledge {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<Data> data = new ArrayList<>();

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public class Data {

        @SerializedName("news_id")
        @Expose
        private String newsId;
        @SerializedName("news_category_id")
        @Expose
        private String newsCategoryId;
        @SerializedName("news_title")
        @Expose
        private String newsTitle;
        @SerializedName("news_desc")
        @Expose
        private String newsSortdesc;
        @SerializedName("link_image")
        @Expose
        private String linkImage;
        @SerializedName("link_detail")
        @Expose
        private String linkDetail;
        @SerializedName("image_name")
        @Expose
        private String imageName;
        @SerializedName("image_path")
        @Expose
        private String imagePath;

        public String getNewsId() {
            return newsId;
        }

        public void setNewsId(String newsId) {
            this.newsId = newsId;
        }

        public String getNewsCategoryId() {
            return newsCategoryId;
        }

        public void setNewsCategoryId(String newsCategoryId) {
            this.newsCategoryId = newsCategoryId;
        }

        public String getNewsTitle() {
            return newsTitle;
        }

        public void setNewsTitle(String newsTitle) {
            this.newsTitle = newsTitle;
        }

        public String getNewsSortdesc() {
            return newsSortdesc;
        }

        public void setNewsSortdesc(String newsSortdesc) {
            this.newsSortdesc = newsSortdesc;
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

        public String getImageName() {
            return imageName;
        }

        public void setImageName(String imageName) {
            this.imageName = imageName;
        }

        public String getImagePath() {
            return imagePath;
        }

        public void setImagePath(String imagePath) {
            this.imagePath = imagePath;
        }

    }
}
