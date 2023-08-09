package com.frutas.previous_order.data_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Datum {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("requisition_number")
    @Expose
    private String requisitionNumber;
    @SerializedName("order_date")
    @Expose
    private String orderDate;
    @SerializedName("total_amount")
    @Expose
    private Double totalAmount;
    @SerializedName("return_amount")
    @Expose
    private Double returnAmount;
    @SerializedName("grand_total")
    @Expose
    private Double grandTotal;
    @SerializedName("paid_amount")
    @Expose
    private Double paidAmount;
    @SerializedName("due_amount")
    @Expose
    private Double dueAmount;
    @SerializedName("reference_requisition_number")
    @Expose
    private String referenceRequisitionNumber;
    @SerializedName("remark")
    @Expose
    private String remark;
    @SerializedName("approved_by")
    @Expose
    private Integer approvedBy;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("shop")
    @Expose
    private Shop shop;
    @SerializedName("orders")
    @Expose
    private List<Order> orders = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRequisitionNumber() {
        return requisitionNumber;
    }

    public void setRequisitionNumber(String requisitionNumber) {
        this.requisitionNumber = requisitionNumber;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public Integer getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(Integer approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Double getReturnAmount() {
        return returnAmount;
    }

    public void setReturnAmount(Double returnAmount) {
        this.returnAmount = returnAmount;
    }

    public Double getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(Double grandTotal) {
        this.grandTotal = grandTotal;
    }

    public Double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(Double paidAmount) {
        this.paidAmount = paidAmount;
    }

    public Double getDueAmount() {
        return dueAmount;
    }

    public void setDueAmount(Double dueAmount) {
        this.dueAmount = dueAmount;
    }

    public String getReferenceRequisitionNumber() {
        return referenceRequisitionNumber;
    }

    public void setReferenceRequisitionNumber(String referenceRequisitionNumber) {
        this.referenceRequisitionNumber = referenceRequisitionNumber;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
