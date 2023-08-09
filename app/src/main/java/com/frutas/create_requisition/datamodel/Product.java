package com.frutas.create_requisition.datamodel;

import androidx.annotation.NonNull;

import com.frutas.db.entity.CartEntity;
import com.frutas.previous_order.data_model.Order;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Here, This model class store all info about Product.
 *
 * @Field Integer id : it holds products id which retrieve from server
 * @Field String name: it holds products name which retrieve from server
 * @Field Boolean Select: As default it will false, when a user add a product to their order list
 * then it will be true.
 * @Field int unitId: When user added a product to order list, it will contain a unit id value, else
 * it will store 0 as value
 * @Field int quantity: after adding product to cart, it contains the quantity of product
 */

public class Product {
    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("name")
    @Expose
    private String name;

    private boolean select = false;

    private Integer unitId = 0;

    private int quantity = 0;

    private String orderId = null;

    public Product() {

    }

    public Product(@NonNull Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.select = product.isSelect();
        this.unitId = product.getUnitId();
        this.quantity = product.getQuantity();
        this.orderId = product.getOrderId();
    }

    public Product(@NonNull CartEntity cartEntity) {
        this.id = cartEntity.getProductId();
        this.name = cartEntity.getName();
        this.select = cartEntity.isSelect();
        this.unitId = cartEntity.getUnitId();
        this.quantity = cartEntity.getQuantity();
        this.orderId = cartEntity.getOrderId();
    }

    public Product setOrder(@NonNull Order order) {
        this.quantity = order.getQuantity();
        this.unitId = order.getUnitId();
        this.orderId = String.valueOf(order.getId());
        return this;
    }

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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
