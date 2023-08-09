package com.frutas.previous_order.presenter;

import android.util.Log;

import androidx.annotation.NonNull;

import com.frutas.create_requisition.datamodel.ProductHolder;
import com.frutas.previous_order.PreviousOrderContract;
import com.frutas.previous_order.data_model.AllOrder;
import com.frutas.previous_order.model.PreviousOrderModel;

public class PreviousOrderPresenter implements PreviousOrderContract.Presenter,
        PreviousOrderContract.OnFinishedListener {
    private static final String TAG = "PreviousOrder";
    
    private final PreviousOrderContract.View view;
    private final PreviousOrderContract.Model model;

    public PreviousOrderPresenter(PreviousOrderContract.View trackOrderView) {
        this.view = trackOrderView;
        this.model = new PreviousOrderModel();
    }

    @Override
    public void requestAllOrderListToModel(@NonNull String shopID) {
        Log.d(TAG, "requestAllOrderListToModel: is called");
        view.showLoading();
        model.requestAllOrderListToServer(this, shopID);
    }

    @Override
    public void getAllProductList() {
        Log.d(TAG, "getAllProductList: is called");
        view.showLoading();
        model.requestAllProductList(this);
    }

    @Override
    public void checkShopID(String shopID) {
        Log.d(TAG, "checkShopID: ");
        model.checkShopId(shopID, this);
    }

    @Override
    public void onSuccessAllOrder(AllOrder allOrder) {
        Log.d(TAG, "onSuccessAllOrder: is called");
        view.setAllOrderToView(allOrder);
    }

    @Override
    public void onFailureAllOrder(Throwable t) {
        Log.d(TAG, "onFailureAllOrder: is called");
        view.hideLoading();
    }

    @Override
    public void onFinishedProductList(ProductHolder productHolder) {
        Log.d(TAG, "onFinishedProductList: is called");
        view.onRetrievedProductList(productHolder);
        view.hideLoading();
    }

    @Override
    public void onFailureProductList(Throwable t) {
        Log.d(TAG, "onFailureProductList: is called");
        view.hideLoading();
    }

    @Override
    public void onShopValid(String shopID) {
        Log.d(TAG, "onShopValid: is called");
        requestAllOrderListToModel(shopID);
        view.saveShopIDOnDevice(shopID);
        view.showShopIdToView(shopID);
    }

    @Override
    public void onShopInvalid() {
        Log.d(TAG, "onShopInvalid: is called");
        view.saveShopIDOnDevice("");
        view.showShopIdToView("");
    }


}
