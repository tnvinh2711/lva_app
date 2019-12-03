package com.lva.shop.ui.detail.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class History {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<Data> data = null;

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

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("invoice_total")
        @Expose
        private Integer invoiceTotal;
        @SerializedName("invoice_discount")
        @Expose
        private Integer invoiceDiscount;
        @SerializedName("invoice_price")
        @Expose
        private Integer invoicePrice;
        @SerializedName("status")
        @Expose
        private Integer status;
        @SerializedName("dilivery")
        @Expose
        private String dilivery;
        @SerializedName("date_created")
        @Expose
        private String dateCreated;
        @SerializedName("product_name")
        @Expose
        private List<String> productName = null;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getInvoiceTotal() {
            return invoiceTotal;
        }

        public void setInvoiceTotal(Integer invoiceTotal) {
            this.invoiceTotal = invoiceTotal;
        }

        public Integer getInvoiceDiscount() {
            return invoiceDiscount;
        }

        public void setInvoiceDiscount(Integer invoiceDiscount) {
            this.invoiceDiscount = invoiceDiscount;
        }

        public Integer getInvoicePrice() {
            return invoicePrice;
        }

        public void setInvoicePrice(Integer invoicePrice) {
            this.invoicePrice = invoicePrice;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public String getDilivery() {
            return dilivery;
        }

        public void setDilivery(String dilivery) {
            this.dilivery = dilivery;
        }

        public String getDateCreated() {
            return dateCreated;
        }

        public void setDateCreated(String dateCreated) {
            this.dateCreated = dateCreated;
        }

        public List<String> getProductName() {
            return productName;
        }

        public void setProductName(List<String> productName) {
            this.productName = productName;
        }

    }
}
