package com.frutas.create_requisition.model;

import androidx.annotation.NonNull;

import com.frutas.api.ApiClient;
import com.frutas.api.ApiService;
import com.frutas.create_requisition.ListProductContract;
import com.frutas.create_requisition.datamodel.ProductHolder;
import com.frutas.create_requisition.datamodel.UnitList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListProductModel implements ListProductContract.Model {
    private static ListProductModel instance = null;

    private final ApiService apiService = ApiClient.getApiService();

    private ProductHolder productHolder = null;

    private UnitList unitList = null;

    private ListProductModel() {
    }

    public static ListProductModel getInstance() {
        if (instance == null) {
            instance = new ListProductModel();
        }
        return instance;
    }


    @Override
    public void prepareData() {
        if (productHolder == null) hitProductApi();
        if (productHolder == null) hitUnitApi();
    }

    @Override
    public void getAllProduct(ListProductContract.OnFinishedListener onFinishedListener) {
        if (productHolder != null) {
            onFinishedListener.onSuccessGetProduct(productHolder);
        } else {
            apiService.getProducts().enqueue(new Callback<ProductHolder>() {
                @Override
                public void onResponse(@NonNull Call<ProductHolder> call,
                                       @NonNull Response<ProductHolder> response) {
                    if (response.isSuccessful()) {
                        onFinishedListener.onSuccessGetProduct(response.body());
                        if (productHolder == null) {
                            productHolder = response.body();
                        }
                    } else {
                        onFinishedListener.onFailureGetProduct(new Throwable(response.message()));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ProductHolder> call, @NonNull Throwable t) {
                    onFinishedListener.onFailureGetProduct(t);
                }
            });
        }
    }

    @Override
    public void getAllUnits(ListProductContract.OnFinishedListener onFinishedListener) {
        if (unitList != null) onFinishedListener.onSuccessGetUnits(unitList);
        else {
            apiService.getUnit().enqueue(new Callback<UnitList>() {
                @Override
                public void onResponse(@NonNull Call<UnitList> call,
                                       @NonNull Response<UnitList> response) {
                    if (response.isSuccessful()) {
                        onFinishedListener.onSuccessGetUnits(response.body());
                        if (unitList == null) unitList = response.body();
                    } else {
                        onFinishedListener.onFailureGetUnits(new Throwable(response.message()));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<UnitList> call, @NonNull Throwable t) {
                    onFinishedListener.onFailureGetUnits(t);
                }
            });
        }
    }

    private void hitProductApi() {
        apiService.getProducts().enqueue(new Callback<ProductHolder>() {
            @Override
            public void onResponse(@NonNull Call<ProductHolder> call,
                                   @NonNull Response<ProductHolder> response) {
                if (response.isSuccessful()) {
                    productHolder = response.body();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ProductHolder> call, @NonNull Throwable t) {

            }
        });
    }

    private void hitUnitApi() {
        apiService.getUnit().enqueue(new Callback<UnitList>() {
            @Override
            public void onResponse(@NonNull Call<UnitList> call,
                                   @NonNull Response<UnitList> response) {
                if (response.isSuccessful()) unitList = response.body();
            }

            @Override
            public void onFailure(@NonNull Call<UnitList> call,
                                  @NonNull Throwable t) {

            }
        });
    }
}
