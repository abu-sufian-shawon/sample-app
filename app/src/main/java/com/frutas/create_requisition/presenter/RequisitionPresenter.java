package com.frutas.create_requisition.presenter;

import android.util.Log;

import com.frutas.create_requisition.RequisitionContract;
import com.frutas.create_requisition.datamodel.ShopHolder;
import com.frutas.create_requisition.model.RequisitionModel;
import com.frutas.create_requisition.post_data_model.PostOrder;
import com.frutas.create_requisition.post_data_model.PostOrderResponse;
import com.frutas.db.entity.CartEntity;

import java.util.List;

public class RequisitionPresenter implements RequisitionContract.Presenter,
                                   RequisitionContract.OnFinishedListener {

    private static final String TAG = "ROOM";

    private final RequisitionContract.View view;
    private final RequisitionContract.Model model;

    public RequisitionPresenter(RequisitionContract.View view) {
        this.view = view;
        model = new RequisitionModel();
    }

    @Override
    public void getProduct() {
        model.getAllCartProduct(this);
    }

    @Override
    public void checkShopID(String shopID) {
        Log.d("ROOM", "checkShopID: is called");
        model.getShop(shopID, this);
    }

    @Override
    public void removeProduct(CartEntity cartEntity) {
        model.removeProductByID(cartEntity.getProductId(), this);
    }

    @Override
    public void onConfirmPlaceOrder(List<CartEntity> cartEntityList) {
        PostOrder postOrder = view.receiveDataToPlaceOrder(cartEntityList);
        model.placeOrder(this, postOrder);
    }

    @Override
    public void onSuccessGetCart(List<CartEntity> cartEntityList) {
        view.setDataToView(cartEntityList);
    }

    @Override
    public void onFailureGetCart(Throwable t) {

    }

    @Override
    public void onSuccessGetShop(ShopHolder allShop) {
        view.onShopValidation(true);
    }

    @Override
    public void onFailureGetShop(Throwable t) {
        view.onShopValidation(false);
    }

    @Override
    public void onSuccessRemoveCart() {
        Log.d(TAG, "onSuccessRemoveCart: Done!");
        model.getAllCartProduct(this);
        view.onRemoveConfirmed();
    }

    @Override
    public void onFailureRemoveCart() {
        // Notify to view
        view.onRemoveConfirmed();
    }

    @Override
    public void onSuccessPlaceOrder(PostOrderResponse response) {
        Log.d(TAG, "onSuccessPlaceOrder: success");
        view.clearCart();
    }

    @Override
    public void onFailurePlaceOrder(Throwable t) {

    }
}
