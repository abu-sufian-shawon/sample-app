package com.frutas.track_order.model;

import androidx.annotation.NonNull;

import com.frutas.api.ApiClient;
import com.frutas.api.ApiService;
import com.frutas.create_requisition.datamodel.ProductHolder;
import com.frutas.create_requisition.datamodel.ShopHolder;
import com.frutas.create_requisition.datamodel.UnitList;
import com.frutas.previous_order.data_model.AllOrder;
import com.frutas.track_order.TrackOrderContract.OnFinishedListener;
import com.frutas.track_order.TrackOrderContract.Model;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrackOrderModel implements Model {
    ApiService apiClient = ApiClient.getClient().create(ApiService.class);
    @Override
    public void requestOrdersDataToServer(OnFinishedListener onFinishedListener, String shopId) {
        Call<AllOrder> callAllOrder = apiClient.getAllOrder(shopId);

        callAllOrder.enqueue(new Callback<AllOrder>() {
            @Override
            public void onResponse(@NonNull Call<AllOrder> call, @NonNull Response<AllOrder> response) {
                if (response.isSuccessful())
                    onFinishedListener.onFinished(response.body());
                else
                    onFinishedListener.onFailure(new Throwable("Error Code " + response.code()));
            }

            @Override
            public void onFailure(@NonNull Call<AllOrder> call, @NonNull Throwable t) {
                onFinishedListener.onFailure(t);
            }
        });
    }

    @Override
    public void getShopById(OnFinishedListener onFinishedListener, String shopId) {
        Call<ShopHolder> call = apiClient.getShopData(shopId);

        call.enqueue(new Callback<ShopHolder>() {
            @Override
            public void onResponse(@NonNull Call<ShopHolder> call, @NonNull Response<ShopHolder> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    onFinishedListener.onResponseShopId(response.body().getSuccess());
                } else {
                    onFinishedListener.onResponseShopId(false);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ShopHolder> call, @NonNull Throwable t) {
                onFinishedListener.onResponseShopId(false);
            }
        });

    }

    @Override
    public void requestAllProductList(OnFinishedListener onFinishedListener) {
        new Thread(() -> {
            //********************** retrieve unit data from API *************************************\\
            UnitList unitList = null;
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
                onFinishedListener.onFailure(e.getCause());
            }


            //******************** retrieve product data from API ************************************\\
            Call<ProductHolder> call = apiClient.getProducts();

            if (unitSuccessIndicator) {
                try {
                    Response<ProductHolder> response = call.execute();
                    if (response.isSuccessful() && response.body() != null) {
                        ProductHolder productHolder = response.body();
                        productHolder.setUnitList(unitList);
                        onFinishedListener.onFinishedProductList(productHolder);
                    } else {
                        onFinishedListener.onFailureProductList(new Throwable("Unable to collect data from server"));
                    }
                } catch (IOException e) {
                    onFinishedListener.onFailure(e.getCause());
                }
            }
        }).start();
    }
}
