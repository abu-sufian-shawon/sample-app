package com.frutas.track_order.view;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.frutas.R;
import com.frutas.create_requisition.datamodel.ProductHolder;
import com.frutas.dialog.DialogIconMessage;
import com.frutas.dialog.DialogLoading;
import com.frutas.dialog.DialogNoInternet;
import com.frutas.network.ConnectionDetector;
import com.frutas.previous_order.Status;
import com.frutas.previous_order.data_model.AllOrder;
import com.frutas.previous_order.data_model.Datum;
import com.frutas.shared_preference.ShopPreference;
import com.frutas.track_order.TrackOrderContract;
import com.frutas.track_order.adapter.TrackOrderAdapter;
import com.frutas.track_order.presenter.TrackOrderPresenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class FragmentTrackOrder extends Fragment implements TrackOrderContract.View {
    private static final String TAG = "FragmentTrackOrder";

    private Context context;
    private FragmentManager fragmentManager;
    private TrackOrderPresenter trackOrderPresenter;
    private DialogLoading dialogLoading;
    private AllOrder allOrder;
    private final List<Datum> pendingOrderList = new ArrayList<>();
    private TrackOrderAdapter trackOrderAdapter;

    private RecyclerView rvTrackOrder;
    private EditText edtSearch;

    private boolean shopIdValidity = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_track_order, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // init view component
        rvTrackOrder = view.findViewById(R.id.rvTrackOrder);
        edtSearch = view.findViewById(R.id.edtSearch);
        TextView tvSearch = view.findViewById(R.id.tvSearch);

        // init other component
        context = getContext();
        fragmentManager = requireActivity().getSupportFragmentManager();
        trackOrderPresenter = new TrackOrderPresenter(this);
        dialogLoading = new DialogLoading();
        DialogNoInternet dialogNoInternet = new DialogNoInternet();

        if (ConnectionDetector.isNetAvailable(context)) {
            ShopPreference shopPreference = new ShopPreference(context);
            String shopId = shopPreference.readData(ShopPreference.SHOP_ID);
            if (pendingOrderList.size() > 0) {
                rvTrackOrder.setLayoutManager(new LinearLayoutManager(context));
                rvTrackOrder.setAdapter(trackOrderAdapter);
            } else if (shopId != null) {
                dialogLoading.show(fragmentManager, DialogLoading.TAG); // show loading dialog
                edtSearch.setText(shopId); // set shop id to searchBox
                trackOrderPresenter.sendOrderDataRequestToPresenter(shopId); // send request to get order data
            }


        } else {
            dialogNoInternet.show(fragmentManager,
                    DialogNoInternet.class.getSimpleName());
        }


        // IMPLEMENTS ONCLICK LISTENERS
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 6) {
                    trackOrderPresenter.checkShopId(s.toString());
                } else {
                    edtSearch.setError("Invalid ID");
                    shopIdValidity = false;
                }
            }
        });

        tvSearch.setOnClickListener(v -> {
            if (shopIdValidity) {
                String shopId = edtSearch.getText().toString();
                if (!dialogLoading.isVisible())
                    dialogLoading.show(fragmentManager, DialogLoading.TAG);
                trackOrderPresenter.sendOrderDataRequestToPresenter(shopId);
            } else {
                edtSearch.setError("Invalid Id");
            }
        });
    }

    @Override
    public void setOrdersDataToView(AllOrder allOrder) {
        this.allOrder = allOrder;
        trackOrderPresenter.getAllProductList();

    }

    @Override
    public void onResponseFailure(Throwable t) {
        // SHOW ERROR DIALOG
        DialogIconMessage iconMessage = new DialogIconMessage(DialogIconMessage.Error, t.getMessage());
        iconMessage.onDismiss(new DialogInterface() {
            @Override
            public void cancel() {
                fragmentManager.popBackStack();
            }
            @Override
            public void dismiss() {
                fragmentManager.popBackStack();
            }
        });
        iconMessage.show(fragmentManager, DialogIconMessage.class.getSimpleName());
    }

    @Override
    public void onResponseShopId(boolean response) {
        shopIdValidity = response;
    }

    @Override
    public void onSuccessProductList(ProductHolder productHolder) {
        if (dialogLoading.isVisible())
            dialogLoading.dismiss();

        pendingOrderList.clear();
        for (Datum datum : allOrder.getData()) {
            if (datum.getStatus().equals(Status.PENDING)) {
                pendingOrderList.add(datum);
            }
        }

        trackOrderAdapter = new TrackOrderAdapter(getActivity(), allOrder, productHolder,  pendingOrderList,
                allOrder.getData().get(0).getShop().getShopNumber());

       requireActivity().runOnUiThread(() -> {
            rvTrackOrder.setLayoutManager(new LinearLayoutManager(context));
            rvTrackOrder.setAdapter(trackOrderAdapter);

            if (pendingOrderList.size() == 0){
                rvTrackOrder.setVisibility(View.GONE);
            }
        });


    }

    @Override
    public void onFailureProductList(Throwable t) {

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: is called");
        Log.i(TAG, "onResume: list size = " + pendingOrderList.size());

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}