package com.frutas.previous_order.model;


import android.util.Log;

import androidx.annotation.NonNull;

import com.frutas.api.ApiClient;
import com.frutas.api.ApiService;
import com.frutas.create_requisition.datamodel.Product;
import com.frutas.create_requisition.datamodel.ProductHolder;
import com.frutas.create_requisition.datamodel.ShopHolder;
import com.frutas.create_requisition.datamodel.UnitList;
import com.frutas.previous_order.PreviousOrderContract.OnFinishedListener;
import com.frutas.previous_order.PreviousOrderContract;
import com.frutas.previous_order.adapter.AllOrderAdapter;
import com.frutas.previous_order.data_model.AllOrder;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PreviousOrderModel implements PreviousOrderContract.Model {
    private static final String TAG = "PreviousOrder";

    private static AllOrder allOrder;
    private static UnitList unitList;
    private static ProductHolder productHolder;
    private static String mShopID = "";


    private final ApiService apiClient = ApiClient.getClient().create(ApiService.class);

    @Override
    public void requestAllOrderListToServer(OnFinishedListener onFinishedListener, String shopID) {
        if(allOrder != null && mShopID.equals(shopID)){
            onFinishedListener.onSuccessAllOrder(allOrder);
        } else {
            hitAllOrderApi(onFinishedListener, shopID);
            mShopID = shopID;
        }
    }

    @Override
    public void requestAllProductList(OnFinishedListener onFinishedListener) {
        if(unitList != null && productHolder != null){
            onFinishedListener.onFinishedProductList(productHolder);
        } else {
            hitProductListAPI(onFinishedListener);
        }
    }

    private void hitAllOrderApi(OnFinishedListener onFinishedListener, String shopID){
        ApiClient.getApiService().getAllOrder(shopID).enqueue(new Callback<AllOrder>() {
            @Override
            public void onResponse(@NonNull Call<AllOrder> call, @NonNull Response<AllOrder> response) {
                if (response.isSuccessful()) {
                    allOrder = response.body();
                    onFinishedListener.onSuccessAllOrder(response.body());
                } else {
                    onFinishedListener.onFailureAllOrder(new Throwable("Error : " + response.code()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<AllOrder> call, @NonNull Throwable t) {
                onFinishedListener.onFailureAllOrder(t);
                Log.i(TAG, "onFailure: " + t.getMessage());
            }
        });
    }




    private void hitProductListAPI(OnFinishedListener onFinishedListener){
        Thread thread = new Thread(() -> {

            //********************** retrieve unit data from API *************************************\\
            Call<UnitList> unitListCall = apiClient.getUnit();
            boolean unitSuccessIndicator;
            try {
                Response<UnitList> response = unitListCall.execute();
                if (response.isSuccessful() && response.body() != null) {
                    unitSuccessIndicator = true;
                    unitList = response.body();
                } else {
                    unitSuccessIndicator = false;
                }
            } catch (IOException e) {
                unitSuccessIndicator = false;
                onFinishedListener.onFailureProductList(e.getCause());
            }


            //******************** retrieve product data from API ************************************\\
            Call<ProductHolder> call = apiClient.getProducts();

            if (unitSuccessIndicator) {
                try {
                    Response<ProductHolder> response = call.execute();
                    if (response.isSuccessful() && response.body() != null) {
                        productHolder = response.body();
                        productHolder.setUnitList(unitList);

                        onFinishedListener.onFinishedProductList(productHolder);
                    } else {
                        onFinishedListener.onFailureProductList(new Throwable("Error " + response.code()));
                    }
                } catch (IOException e) {
                    onFinishedListener.onFailureProductList(e.getCause());
                }
            }
        });

        thread.start();
    }


    @Override
    public void checkShopId(String shopID, OnFinishedListener onFinishedListener) {
        Log.d(TAG, "checkShopId: is called");
        ApiClient.getApiService().getShopData(shopID).enqueue(new Callback<ShopHolder>() {
            @Override
            public void onResponse(@NonNull Call<ShopHolder> call, @NonNull Response<ShopHolder> response) {
                if(response.isSuccessful()){
                    onFinishedListener.onShopValid(shopID);
                } else {
                    onFinishedListener.onShopInvalid();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ShopHolder> call, @NonNull Throwable t) {
                onFinishedListener.onShopInvalid();
            }
        });
    }
}
