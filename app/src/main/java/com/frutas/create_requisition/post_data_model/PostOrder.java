 package com.frutas.create_requisition.post_data_model;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostOrder {
    @SerializedName("shop_number")
    @Expose
    private String shopNumber;

    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("quantity")
    @Expose
    private List<String> quantity = new ArrayList<>();

    @SerializedName("product")
    @Expose
    private List<String> product = new ArrayList<>();

    @SerializedName("unit")
    @Expose
    private List<String> unit = new ArrayList<>();

    @SerializedName("order_id")
    @Expose
    private List<String> orderId = new ArrayList<>();

    public PostOrder() {

    }

    public PostOrder(String shopNumber, String date, List<String> quantity, List<String> product, List<String> unit, List<String> orderId) {
        this.shopNumber = shopNumber;
        this.date = date;
        this.quantity = quantity;
        this.product = product;
        this.unit = unit;
        this.orderId = orderId;
    }

    public String getShopNumber() {
        return shopNumber;
    }

    public void setShopNumber(String shopNumber) {
        this.shopNumber = shopNumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<String> getQuantity() {
        return quantity;
    }

    public void setQuantity(List<String> quantity) {
        this.quantity = quantity;
    }

    public List<String> getProduct() {
        return product;
    }

    public void setProduct(List<String> product) {
        this.product = product;
    }

    public List<String> getUnit() {
        return unit;
    }

    public void setUnit(List<String> unit) {
        this.unit = unit;
    }

    public List<String> getOrderId() {
        return orderId;
    }

    public void setOrderId(List<String> orderId) {
        this.orderId = orderId;
    }
}