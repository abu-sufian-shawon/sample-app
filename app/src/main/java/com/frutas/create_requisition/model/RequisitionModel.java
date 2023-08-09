package com.frutas.create_requisition.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.frutas.api.ApiClient;
import com.frutas.api.ApiService;
import com.frutas.create_requisition.RequisitionContract;
import com.frutas.create_requisition.datamodel.ShopHolder;
import com.frutas.create_requisition.post_data_model.PostOrder;
import com.frutas.create_requisition.post_data_model.PostOrderResponse;
import com.frutas.create_requisition.thread.FrutasLooper;
import com.frutas.db.FrutasDAO;
import com.frutas.db.FrutasDatabase;
import com.frutas.db.entity.CartEntity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequisitionModel implements RequisitionContract.Model {
    private static final String TAG = "ROOM";

    private final FrutasLooper looper = FrutasLooper.getInstance();
    private final ApiService apiService;

    public RequisitionModel() {
        if(!looper.isAlive()) looper.start();
        apiService = ApiClient.getApiService();
    }


    @Override
    public void getAllCartProduct(RequisitionContract.OnFinishedListener onFinishedListener) {
        Log.d(TAG, "getAllCartProduct: is called");
        looper.handler.post(() -> {
            FrutasDAO database = FrutasDatabase.mInstance.frutasDAO();

            List<CartEntity> cartEntityList = database.getAllProductFromCart();
            Log.d(TAG, "getAllCartProduct: inside looper thread");
            onFinishedListener.onSuccessGetCart(cartEntityList);
            Log.d(TAG, "getAllCartProduct: new cart size = " + cartEntityList.size());

        });
    }

    @Override
    public void getShop(String shopId,
                        RequisitionContract.OnFinishedListener onFinishedListener) {
        apiService.getShopData(shopId).enqueue(new Callback<ShopHolder>() {
            @Override
            public void onResponse(@NonNull Call<ShopHolder> call, @NonNull Response<ShopHolder> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse: is success");
                    onFinishedListener.onSuccessGetShop(response.body());
                } else {
                    onFinishedListener.onFailureGetShop(new Throwable(response.message()));
                    Log.d(TAG, "onResponse: is failed with code = " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ShopHolder> call, @NonNull Throwable t) {
                onFinishedListener.onFailureGetShop(t);
                Log.d(TAG, "onFailure: is failed with message = " + t.getMessage());
            }
        });
    }

    @Override
    public void removeProductByID(int cartID, RequisitionContract.OnFinishedListener onFinishedListener) {

        looper.handler.post(() -> {
            FrutasDAO database = FrutasDatabase.mInstance.frutasDAO();
            int result = database.deleteProductByID(cartID);

            if (result > 0) {
                Log.d(TAG, "removeProductByID: is success");
                onFinishedListener.onSuccessRemoveCart();
            } else {
                Log.d(TAG, "removeProductByID: is failed");
                onFinishedListener.onFailureRemoveCart();
            }

            Log.d(TAG, "removeProductByID: result = " + result);
        });
    }


    @Override
    public void placeOrder(RequisitionContract.OnFinishedListener onFinishedListener, PostOrder postOrder) {
        Log.d(TAG, "placeOrder: is called");
        
        apiService.sendRequisitionOrder(postOrder).enqueue(new Callback<PostOrderResponse>() {
            @Override
            public void onResponse(@NonNull Call<PostOrderResponse> call, @NonNull Response<PostOrderResponse> response) {
                if(response.isSuccessful()){
                    onFinishedListener.onSuccessPlaceOrder(response.body());
                } else {
                    onFinishedListener.onFailurePlaceOrder(new Throwable("Error code "+ response.code()));
                    Log.d(TAG, "onResponse: failed");
                }
            }

            @Override
            public void onFailure(@NonNull Call<PostOrderResponse> call, @NonNull Throwable t) {
                onFinishedListener.onFailurePlaceOrder(t);
                Log.d(TAG, "onFailure: is failed");
            }
        });
    }
}
