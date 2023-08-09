package com.frutas.create_requisition.thread;

import com.frutas.create_requisition.datamodel.Product;

import java.util.List;

public interface FilterProductCallBack {
    void onFilteredProduct(List<Product> filteredProduct);
}
