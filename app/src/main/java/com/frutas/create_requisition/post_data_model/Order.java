package com.frutas.create_requisition.post_data_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Order {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("product_id")
    @Expose
    private Integer productId;
    @SerializedName("requisition_id")
    @Expose
    private Integer requisitionId;
    @SerializedName("unit_id")
    @Expose
    private String unitId;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("quantity")
    @Expose
    private Integer quantity;
    @SerializedName("kilo")
    @Expose
    private Double kilo;
    @SerializedName("total_kilo")
    @Expose
    private Double totalKilo;
    @SerializedName("box_weight")
    @Expose
    private Double boxWeight;
    @SerializedName("net_weight")
    @Expose
    private Double netWeight;
    @SerializedName("buying_price")
    @Expose
    private Double buyingPrice;
    @SerializedName("selling_price")
    @Expose
    private Double sellingPrice;
    @SerializedName("total")
    @Expose
    private Double total;
    @SerializedName("remark")
    @Expose
    private String remark;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getRequisitionId() {
        return requisitionId;
    }

    public void setRequisitionId(Integer requisitionId) {
        this.requisitionId = requisitionId;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getKilo() {
        return kilo;
    }

    public void setKilo(Double kilo) {
        this.kilo = kilo;
    }

    public Double getTotalKilo() {
        return totalKilo;
    }

    public void setTotalKilo(Double totalKilo) {
        this.totalKilo = totalKilo;
    }

    public Double getBoxWeight() {
        return boxWeight;
    }

    public void setBoxWeight(Double boxWeight) {
        this.boxWeight = boxWeight;
    }

    public Double getNetWeight() {
        return netWeight;
    }

    public void setNetWeight(Double netWeight) {
        this.netWeight = netWeight;
    }

    public Double getBuyingPrice() {
        return buyingPrice;
    }

    public void setBuyingPrice(Double buyingPrice) {
        this.buyingPrice = buyingPrice;
    }

    public Double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(Double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
