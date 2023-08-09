package com.frutas.update_order.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.frutas.api.ApiClient;
import com.frutas.create_requisition.datamodel.ProductHolder;
import com.frutas.create_requisition.datamodel.UnitList;
import com.frutas.create_requisition.post_data_model.PostOrder;
import com.frutas.create_requisition.post_data_model.PostOrderResponse;
import com.frutas.update_order.UpdateContract;
import com.frutas.update_order.UpdateContract.OnFinishedListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateModel implements UpdateContract.Model {
    private static final String TAG = "MY-CHECK";

    private UnitList unitList;

    @Override
    public void getUnitList(OnFinishedListener onFinishedListener) {
        if(unitList == null){
            hitUnitApi(onFinishedListener);
        } else {
            onFinishedListener.onSuccessUnitList(unitList);
        }
    }

    @Override
    public void getOrderItem(OnFinishedListener onFinishedListener) {

    }

    @Override
    public void postUpdateRequisition(OnFinishedListener onFinishedListener,
                                      PostOrder postOrder, String orderID) {

        Log.d(TAG, "postUpdateRequisition: is called");

        ApiClient.getApiService().updateRequisition(orderID, postOrder).enqueue(
                new Callback<PostOrderResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<PostOrderResponse> call, @NonNull Response<PostOrderResponse> response) {
                        if (response.isSuccessful()) {
                            onFinishedListener.onSuccessUpdateOrder(response.body());
                            Log.d(TAG, "onResponse: success update");
                        } else {
                            Log.d(TAG, "onResponse: failed success");
                            Log.d(TAG, "onResponse: response code " + response.code());
                            onFinishedListener.onFailureUpdateOrder(new Throwable("Error " + response.code()));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<PostOrderResponse> call, @NonNull Throwable t) {
                        onFinishedListener.onFailureUpdateOrder(t);
                        Log.d(TAG, "onFailure: failed success");
                    }
                });
    }

    private void hitUnitApi(OnFinishedListener onFinishedListener){
        ApiClient.getApiService().getUnit().enqueue(new Callback<UnitList>() {
            @Override
            public void onResponse(@NonNull Call<UnitList> call, @NonNull Response<UnitList> response) {
                if(response.isSuccessful()){
                    unitList = response.body();
                    onFinishedListener.onSuccessUnitList(response.body());
                } else {
                    onFinishedListener.onFailureUnitList(new Throwable("Error " + response.code()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<UnitList> call, @NonNull Throwable t) {
                onFinishedListener.onFailureUnitList(t);
            }
        });
    }




}
