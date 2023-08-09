package com.frutas.create_requisition.view;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.frutas.R;
import com.frutas.animation.Anim;
import com.frutas.create_requisition.ListProductContract;
import com.frutas.create_requisition.adapter.AdapterFinalProducts;
import com.frutas.create_requisition.adapter.SelectProductAdapter;
import com.frutas.create_requisition.datamodel.ProductHolder;
import com.frutas.create_requisition.datamodel.Unit;
import com.frutas.create_requisition.datamodel.UnitList;
import com.frutas.create_requisition.dialog.DialogChooseProduct;
import com.frutas.create_requisition.presenter.ListProductPresenter;
import com.frutas.create_requisition.thread.FrutasLooper;
import com.frutas.db.FrutasDAO;
import com.frutas.db.FrutasDatabase;
import com.frutas.db.entity.CartEntity;
import com.frutas.dialog.DialogLoading;

import java.util.ArrayList;
import java.util.List;

public class FragmentListProduct extends DialogFragment implements ListProductContract.ListProductView {
    private static final String TAG = "LIFECYCLE";

    private DialogLoading dialogLoading;
    private FragmentManager fragmentManager;

    private TextView tvCartCounter;
    private RecyclerView rvSelectProduct;
    private List<CartEntity> selectedProduct = new ArrayList<>();

    private final List<String> units = new ArrayList<>();

    private SelectProductAdapter adapter;

    private UnitList unitList;

    private final FrutasLooper looper;

    private FrutasDAO database;

    private int cartCounter;

    public FragmentListProduct() {
        looper = FrutasLooper.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView: is called");
        fragmentManager = requireActivity().getSupportFragmentManager();
        return inflater.inflate(R.layout.fragment_list_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d(TAG, "onViewCreated: is called");

        ListProductContract.Presenter presenter = new ListProductPresenter(this);

        // init view components
        rvSelectProduct = view.findViewById(R.id.rvSelectProduct);
        tvCartCounter = view.findViewById(R.id.tvCartCounter);

        database = FrutasDatabase.getInstance(requireContext()).frutasDAO();

        // set onClick in Show cart Icon
        view.findViewById(R.id.ivCartIcon).setOnClickListener(v -> showCart());
        // set onClick in Checkout Now Textview
        view.findViewById(R.id.tvCheckout).setOnClickListener(v -> showCart());

        // update cart counter. when
        notifyCartCounter();

        presenter.getProductData();

        LinearLayout llSearchProduct = view.findViewById(R.id.llSearchProduct);

        setupSearchView(llSearchProduct);

        EditText edtSearchProduct = view.findViewById(R.id.edtSearchProduct);

        edtSearchProduct.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                        adapter.getFilter().filter(s.toString());

                    Log.d(TAG, "afterTextChanged: search query text = " + s.toString());

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d(TAG, "onResume: is called");
        onRetrieveCache();
    }

    @Override
    public void onAddCartClicked(int position, CartEntity cartEntity) {
        DialogChooseProduct dialogChooseProduct = new DialogChooseProduct(this, cartEntity, units);

        for(CartEntity entity : selectedProduct){
            if(cartEntity.getProductId().equals(entity.getProductId())){
                dialogChooseProduct = new DialogChooseProduct(this, entity, units);
                break;
            }
        }

        dialogChooseProduct.show(fragmentManager, DialogChooseProduct.TAG);
    }

    @Override
    public void onProductChosen(CartEntity cartEntity) {
        looper.handler.post(() -> database.insetCart(cartEntity));

        onRetrieveCache();

        adapter.removeItem(cartEntity);
    }

    private void onRetrieveCache() {
        Log.d("ROOM", "onRetrieveCache: is called");
        if (looper.handler == null) {
            looper.start();
            Log.d("ROOM", "onRetrieveCache: looper is null");
            onRetrieveCache();
        }

        looper.handler.post(() -> {
            FrutasDAO frutasDAO = FrutasDatabase.mInstance.frutasDAO();

            selectedProduct = frutasDAO.getAllProductFromCart();

            requireActivity().runOnUiThread(() -> {
                cartCounter = selectedProduct.size();
                notifyCartCounter();
            });
            Log.d("ROOM", "onRetrieveCache: cartEntity size = " + selectedProduct.size());
        });

    }

    @Override
    public void setDataToView(ProductHolder productHolder) {
        adapter = new SelectProductAdapter(this, productHolder.getData());
        rvSelectProduct.setAdapter(adapter);

    }

    @Override
    public void setUnitToView(UnitList unit) {
        this.unitList = unit;
        this.units.clear();
        this.units.add("Select");
        for (Unit u : unit.getData()) {
            this.units.add(u.getName());
        }
    }

    @Override
    public Integer getUnitIdByName(String name) {
        for(Unit unit : unitList.getData()){
            if(unit.getName().equals(name)){
                return unit.getId();
            }
        }
        return null;
    }

    @Override
    public void showLoading() {
        dialogLoading = new DialogLoading();
        if (!dialogLoading.isVisible()) dialogLoading.show(fragmentManager, DialogLoading.TAG);
    }

    @Override
    public void hideLoading() {
        dialogLoading.dismiss();
    }

    public void notifyCartCounter() {
        cartCounter = selectedProduct.size();
        if (cartCounter > 0) {
            tvCartCounter.setVisibility(View.VISIBLE);
            tvCartCounter.setText(String.valueOf(cartCounter));
            setupCheckoutView(requireView().findViewById(R.id.checkoutNow), View.VISIBLE);
        } else {
            tvCartCounter.setText("0");
            tvCartCounter.setVisibility(View.GONE);
            setupCheckoutView(requireView().findViewById(R.id.checkoutNow), View.GONE);
        }
    }


    // it will called when cart icon is pressed
    private void showCart() {
        Log.d(TAG, "showCart: is called");
        fragmentManager.beginTransaction().replace(
                R.id.fragmentContainer, new FragmentRequisition(unitList),
                FragmentRequisition.TAG)
                .addToBackStack(null)
                .commit();
        Log.d("ROOM", "showCart: selected product = " + selectedProduct.size());

    }

    private void setupSearchView(View view) {
        view.getRootView().findViewById(R.id.ivSearchProduct).setOnClickListener(v -> {

            switch (view.getVisibility()) {
                case View.VISIBLE:
                    view.startAnimation(Anim.animationHide(view));
                    break;
                case View.GONE:
                    view.startAnimation(Anim.animationShow(view));
                    break;
                case View.INVISIBLE:
                    break;
            }

            EditText edtSearchProduct = view.findViewById(R.id.edtSearchProduct);
            edtSearchProduct.requestFocus();
            InputMethodManager imm = (InputMethodManager) requireActivity()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(edtSearchProduct, InputMethodManager.SHOW_IMPLICIT);
        });
    }

    private void setupCheckoutView(View view, int visibility) {
        switch (visibility) {
            case View.VISIBLE:
                view.startAnimation(Anim.animationShow(view));
                break;
            case View.GONE:
                view.startAnimation(Anim.animationHide(view));
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: is called");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: is called");
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.d(TAG, "onViewStateRestored: is called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: is called");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: is called");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        Log.d(TAG, "onSaveInstanceState: is called");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dialogLoading.dismiss();

        Log.d(TAG, "onDestroyView: is called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d(TAG, "onDestroy: is called");
    }
}
