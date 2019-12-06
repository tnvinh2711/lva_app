package com.lva.shop.ui.login.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserInfo {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("sex")
    @Expose
    private String sex;
    @SerializedName("dob_d")
    @Expose
    private Integer dobD;
    @SerializedName("dob_m")
    @Expose
    private Integer dobM;
    @SerializedName("dob_y")
    @Expose
    private Integer dobY;
    @SerializedName("avatar")
    @Expose
    private String avatar;
    @SerializedName("url_avatar")
    @Expose
    private String urlAvatar;
    @SerializedName("province")
    @Expose
    private String province;
    @SerializedName("district")
    @Expose
    private String district;
    @SerializedName("ward")
    @Expose
    private String ward;
    @SerializedName("name_delivery")
    @Expose
    private String nameDelivery;
    @SerializedName("phone_delivery")
    @Expose
    private String phoneDelivery;
    @SerializedName("uuid")
    @Expose
    private String uuid;
    @SerializedName("ref_type")
    @Expose
    private String refType;

    public Double getPoint() {
        return point;
    }

    public void setPoint(Double point) {
        this.point = point;
    }

    @SerializedName("point")
    @Expose
    private Double point;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getDobD() {
        return dobD;
    }

    public void setDobD(Integer dobD) {
        this.dobD = dobD;
    }

    public Integer getDobM() {
        return dobM;
    }

    public void setDobM(Integer dobM) {
        this.dobM = dobM;
    }

    public Integer getDobY() {
        return dobY;
    }

    public void setDobY(Integer dobY) {
        this.dobY = dobY;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUrlAvatar() {
        return urlAvatar;
    }

    public void setUrlAvatar(String urlAvatar) {
        this.urlAvatar = urlAvatar;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getNameDelivery() {
        return nameDelivery;
    }

    public void setNameDelivery(String nameDelivery) {
        this.nameDelivery = nameDelivery;
    }

    public String getPhoneDelivery() {
        return phoneDelivery;
    }

    public void setPhoneDelivery(String phoneDelivery) {
        this.phoneDelivery = phoneDelivery;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getRefType() {
        return refType;
    }

    public void setRefType(String refType) {
        this.refType = refType;
    }
}
