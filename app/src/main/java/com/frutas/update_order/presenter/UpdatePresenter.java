package com.frutas.update_order.presenter;

import android.util.Log;

import com.frutas.create_requisition.datamodel.ProductHolder;
import com.frutas.create_requisition.datamodel.UnitList;
import com.frutas.create_requisition.post_data_model.PostOrder;
import com.frutas.create_requisition.post_data_model.PostOrderResponse;
import com.frutas.dialog.DialogIconMessage;
import com.frutas.dialog.DialogLoading;
import com.frutas.update_order.UpdateContract;
import com.frutas.update_order.model.UpdateModel;

public class UpdatePresenter implements UpdateContract.Presenter, UpdateContract.OnFinishedListener {
    private static final String TAG = "MY-CHECK";

    private final UpdateContract.View view;
    private final UpdateContract.Model updateModel;

    public UpdatePresenter(UpdateContract.View view) {
        this.view = view;
        this.updateModel = new UpdateModel();
    }


    @Override
    public void getUnitList() {
        updateModel.getUnitList(this);
    }

    @Override
    public void updateRequisition(PostOrder postOrder, String orderID) {
        Log.d(TAG, "updateRequisition: called");
        view.showLoading();
        updateModel.postUpdateRequisition(
                this,
                postOrder,
                orderID
        );
    }


    @Override
    public void onSuccessUnitList(UnitList unitList) {
        view.setUnitList(unitList);
        view.hideLoading();

    }

    @Override
    public void onFailureUnitList(Throwable t) {
        view.hideLoading();
    }

    @Override
    public void onSuccessUpdateOrder(PostOrderResponse response) {
        Log.d(TAG, "onSuccessUpdateOrder: is called");

        view.dismiss();
        view.hideLoading();
    }

    @Override
    public void onFailureUpdateOrder(Throwable t) {
        Log.d(TAG, "onFailureUpdateOrder: is called");
        view.hideLoading();
    }


}
