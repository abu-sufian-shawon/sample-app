package com.frutas.previous_order.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.frutas.R;
import com.frutas.create_requisition.datamodel.ProductHolder;
import com.frutas.dialog.DialogIconMessage;
import com.frutas.dialog.DialogNoInternet;
import com.frutas.network.ConnectionDetector;
import com.frutas.previous_order.PreviousOrderContract;
import com.frutas.previous_order.adapter.AllOrderAdapter;
import com.frutas.previous_order.data_model.AllOrder;
import com.frutas.previous_order.presenter.PreviousOrderPresenter;
import com.frutas.shared_preference.ShopPreference;

public class FragmentPreviousOrder extends Fragment implements PreviousOrderContract.View {
    private Context context;
    private FragmentManager fManager;
    private PreviousOrderContract.Presenter presenter;
    private DialogNoInternet dialogNoInternet;

    private AllOrder allOrder;

    private RecyclerView rvAllOrder;
    private EditText edtSearch;

    private View loadingProgress;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_previous_order, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // init view components
        rvAllOrder = view.findViewById(R.id.rvAllOrder);
        edtSearch = view.findViewById(R.id.edtSearch);
        TextView tvSearch = view.findViewById(R.id.tvSearch);
        loadingProgress = requireView().findViewById(R.id.loadingProgress);

        context = getContext();
        fManager = requireActivity().getSupportFragmentManager();

        presenter = new PreviousOrderPresenter(this);
        ShopPreference shopPreference = new ShopPreference(context);

        dialogNoInternet = new DialogNoInternet();

        if (ConnectionDetector.isNetAvailable(context)) {
            String shopId = shopPreference.readData(ShopPreference.SHOP_ID);
            if (shopId != null){
                presenter.checkShopID(shopId);
            }

        }else {
            dialogNoInternet.show(fManager, DialogNoInternet.class.getSimpleName());
        }

        // implement OnClickListener in View components
        tvSearch.setOnClickListener(v->{
            if (edtSearch.getText().length() < 6) {
                edtSearch.setError("Invalid ID");
            } else {
                String shopId = edtSearch.getText().toString();
                if (ConnectionDetector.isNetAvailable(context)) {
                    presenter.checkShopID(shopId);
                } else {
                    dialogNoInternet.show(fManager, DialogNoInternet.TAG);
                }
            }
        });
    }

    @Override
    public void showShopIdToView(String shopID) {
        edtSearch.setText(shopID);
        if(shopID.isEmpty()) edtSearch.setError("Invalid ID");
    }

    @Override
    public void setAllOrderToView(AllOrder allOrder) {
            this.allOrder = allOrder;
            presenter.getAllProductList();
    }


    @Override
    public void onRetrievedProductList(ProductHolder productHolder) {
        requireActivity().runOnUiThread(() -> {
            populateRecyclerview(allOrder, productHolder);

            if(!allOrder.getData().isEmpty()){
                requireView().findViewById(R.id.tvNoOrderMessage).setVisibility(View.GONE);
            } else {
                requireView().findViewById(R.id.tvNoOrderMessage).setVisibility(View.VISIBLE);
            }
        });

    }

    private void populateRecyclerview(AllOrder allOrder, ProductHolder productHolder){
        AllOrderAdapter allOrderAdapter = new AllOrderAdapter(getActivity(), allOrder, productHolder);
        rvAllOrder.setLayoutManager(new LinearLayoutManager(context));
        rvAllOrder.setItemAnimator(new DefaultItemAnimator());
        rvAllOrder.setAdapter(allOrderAdapter);
     }

    @Override
    public void saveShopIDOnDevice(String shopID) {
        ShopPreference shopPreference = new ShopPreference(requireContext());
        shopPreference.writeData(ShopPreference.SHOP_ID, shopID);
    }

    @Override
    public void showLoading() {
        loadingProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        requireActivity().runOnUiThread(()->{
            loadingProgress.setVisibility(View.GONE);
        });

    }

    @Override
    public void showMessageDialog(String message, int type) {
        new DialogIconMessage(type, message).show(fManager, DialogIconMessage.TAG);
    }
}