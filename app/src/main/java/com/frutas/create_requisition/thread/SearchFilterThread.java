package com.frutas.create_requisition.thread;

import com.frutas.create_requisition.datamodel.Product;

import java.util.List;

public class SearchFilterThread extends Thread {
    private final FilterProductCallBack filterProductCallBack;
    private final List<Product> productList;
    private final List<Product> filteredList;
    private String searchKey;

    public SearchFilterThread(List<Product> productList, List<Product> filteredList,
                              FilterProductCallBack filterProductCallBack) {
        this.productList = productList;
        this.filteredList = filteredList;
        this.filterProductCallBack = filterProductCallBack;
    }

    public SearchFilterThread setSearchKey(String searchKey) {
        this.searchKey = searchKey;
        return this;
    }

    @Override
    public void run() {
        super.run();
        if (productList != null) {
            filteredList.clear();
            for (Product product : productList) {
                if (product.getName().toLowerCase().contains(searchKey.toLowerCase()))
                    filteredList.add(product);
            }
            filterProductCallBack.onFilteredProduct(filteredList);
        }
    }
}
