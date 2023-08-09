package com.frutas.create_requisition.presenter;

import android.util.Log;

import com.frutas.create_requisition.ListProductContract;
import com.frutas.create_requisition.datamodel.ProductHolder;
import com.frutas.create_requisition.datamodel.UnitList;
import com.frutas.create_requisition.model.ListProductModel;

public class ListProductPresenter implements ListProductContract.Presenter, ListProductContract.OnFinishedListener {
    private final ListProductContract.Model requisitionModel;
    private final ListProductContract.ListProductView listProductView;

    public ListProductPresenter(ListProductContract.ListProductView listProductView) {
        this.listProductView = listProductView;
        this.requisitionModel = ListProductModel.getInstance();
        requisitionModel.prepareData();
    }

    @Override
    public void getProductData() {
        Log.d("ROOM", "getProductData: is called");
        listProductView.showLoading();
        requisitionModel.getAllProduct(this);
        requisitionModel.getAllUnits(this);
    }


    @Override
    public void onSuccessGetProduct(ProductHolder productHolder) {
        Log.d("ROOM", "onSuccessGetProduct: is called.");
        listProductView.setDataToView(productHolder);
        listProductView.hideLoading();
    }

    @Override
    public void onFailureGetProduct(Throwable t) {
        Log.d("ROOM", "onFailureGetProduct: " + t.getMessage());
        listProductView.hideLoading();
    }

    @Override
    public void onSuccessGetUnits(UnitList unitList) {
        Log.d("ROOM", "onSuccessGetUnits: is called");
        listProductView.setUnitToView(unitList);
        listProductView.hideLoading();
    }

    @Override
    public void onFailureGetUnits(Throwable t) {

    }

    //------------------------------------------END----------------------------------------------\\
}
