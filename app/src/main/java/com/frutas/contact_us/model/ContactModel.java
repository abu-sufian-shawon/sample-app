package com.frutas.contact_us.model;

import androidx.annotation.NonNull;

import com.frutas.api.ApiClient;
import com.frutas.api.ApiService;
import com.frutas.contact_us.ContactUsContract;
import com.frutas.contact_us.data_model.ContactHolder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactModel implements ContactUsContract.Model {
    @Override
    public void requestContactInfoToServer(OnFinishedListener onFinishedListener) {
        ApiService apiClient = ApiClient.getClient().create(ApiService.class);

        Call<ContactHolder> call = apiClient.getContactData();

        call.enqueue(new Callback<ContactHolder>() {
            @Override
            public void onResponse(@NonNull Call<ContactHolder> call, @NonNull Response<ContactHolder> response) {
                if (response.isSuccessful()){
                    onFinishedListener.onFinished(response.body());
                } else {
                    onFinishedListener.onFailure(new Throwable("Error " + response.code()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ContactHolder> call, @NonNull Throwable t) {
                onFinishedListener.onFailure(t);
            }
        });

    }
}
