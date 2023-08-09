package com.frutas.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.frutas.create_requisition.datamodel.Product;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "cart")
public class CartEntity {
    @PrimaryKey(autoGenerate = true)
    private Integer productId;
    private String name;
    private boolean select;
    private Integer unitId;
    private Integer quantity;
    private String orderId;

    public CartEntity(){}

    @Ignore
    public CartEntity(Product product){
        this.productId = product.getId();
        this.name = product.getName();
        this.select = product.isSelect();
        this.unitId = product.getUnitId();
        this.quantity = product.getQuantity();
        this.orderId = product.getOrderId();
    }

    @Ignore
    public CartEntity(Integer productId, String name, boolean select, Integer unitId, int quantity, String orderId) {
        this.productId = productId;
        this.name = name;
        this.select = select;
        this.unitId = unitId;
        this.quantity = quantity;
        this.orderId = orderId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
