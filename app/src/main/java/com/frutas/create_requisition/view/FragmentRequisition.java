package com.frutas.create_requisition.view;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.frutas.R;
import com.frutas.create_requisition.RequisitionContract;
import com.frutas.create_requisition.adapter.AdapterFinalProducts;
import com.frutas.create_requisition.datamodel.Unit;
import com.frutas.create_requisition.datamodel.UnitList;
import com.frutas.create_requisition.dialog.DialogConfirmation;
import com.frutas.create_requisition.dialog.DialogEditProduct;
import com.frutas.create_requisition.post_data_model.PostOrder;
import com.frutas.create_requisition.presenter.RequisitionPresenter;
import com.frutas.create_requisition.thread.FrutasLooper;
import com.frutas.db.FrutasDAO;
import com.frutas.db.FrutasDatabase;
import com.frutas.db.entity.CartEntity;
import com.frutas.dialog.DialogIconMessage;
import com.frutas.dialog.DialogNoInternet;
import com.frutas.network.ConnectionDetector;
import com.frutas.shared_preference.ShopPreference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FragmentRequisition extends Fragment implements RequisitionContract.View {
    public static final String TAG = "ROOM";

    private boolean isShopIdValid = false;

    private DialogNoInternet dialogNoInternet;
    private AdapterFinalProducts adapter;
    private DatePickerDialog.OnDateSetListener setListener;
    private final UnitList unitList;
    private final List<String> units;
    private List<CartEntity> cartEntityList;

    private TextView tvDate;
    private EditText edtShopNo;
    private RecyclerView rvItemForPlace;

    private RequisitionContract.Presenter presenter;

    private DialogConfirmation dialogConfirmation = null;
    private DialogConfirmation confirmPlaceOrder;

    private final FrutasLooper looper;

    public FragmentRequisition(UnitList unitList) {
        looper = FrutasLooper.getInstance();

        this.unitList = unitList;
        this.units = new ArrayList<>();

        this.units.add("Select");
        for (Unit u : unitList.getData()) {
            this.units.add(u.getName());
        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: is called");

        dialogNoInternet = new DialogNoInternet();
        presenter = new RequisitionPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_requisition, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d(TAG, "onViewCreated: is called");

        // init view component
        tvDate = view.findViewById(R.id.tvDate);
        edtShopNo = view.findViewById(R.id.edtShopId);
        rvItemForPlace = view.findViewById(R.id.rvConfirmItemForPlace);

        ShopPreference shopPreference = new ShopPreference(requireContext());
        rvItemForPlace.setAdapter(adapter);
        rvItemForPlace.setVisibility(View.VISIBLE);

        setDate(); // set date to the view

        if (ConnectionDetector.isNetAvailable(requireContext())) {
            presenter.getProduct();
        } else {
            dialogNoInternet.show(requireActivity().getSupportFragmentManager(),
                    DialogNoInternet.TAG);
        }

        // *************** ONCLICK LISTENER *****************\\

        /*
            As Fist View this Fragment, I check the previous shop id in shared preference.
            if preference has an shop id, then set it to shop id EditText and check it via
            server.
         */
        String shopId = shopPreference.readData(ShopPreference.SHOP_ID);
        if (shopId != null) {
            edtShopNo.setText(shopId);
            if (ConnectionDetector.isNetAvailable(requireContext())) {
                presenter.checkShopID(shopId);
            } else {
                dialogNoInternet.show(requireActivity().getSupportFragmentManager(),
                        DialogNoInternet.TAG);
            }
        }

        view.findViewById(R.id.btnAddMoreProduct).setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStackImmediate());


        view.findViewById(R.id.btnPlaceOrder).setOnClickListener(v -> {

            if (cartEntityList.isEmpty()) {
                new DialogIconMessage(DialogIconMessage.Error, "Cart is Empty")
                        .show(requireActivity().getSupportFragmentManager(), DialogIconMessage.TAG);
                return;
            }

            if (edtShopNo.getText().length() < 6 && !isShopIdValid) {
                new DialogIconMessage(DialogIconMessage.Error, "Invalid Shop ID")
                        .show(requireActivity().getSupportFragmentManager(), DialogIconMessage.TAG);
                return;
            }

            if (tvDate.getText().length() < 8) {
                new DialogIconMessage(DialogIconMessage.Error, "Invalid Date")
                        .show(requireActivity().getSupportFragmentManager(), DialogIconMessage.TAG);
                return;
            }


            confirmPlaceOrder = new DialogConfirmation(
                    v1 -> presenter.onConfirmPlaceOrder(cartEntityList),
                    getString(R.string.text_place_order_message)
            );
            confirmPlaceOrder.show(requireActivity().getSupportFragmentManager(),
                    DialogConfirmation.TAG);
        });

        edtShopNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 6) {
                    if (ConnectionDetector.isNetAvailable(requireContext())) {
                        presenter.checkShopID(s.toString());
                    } else {
                        dialogNoInternet.show(requireActivity().getSupportFragmentManager(),
                                DialogNoInternet.TAG);
                        isShopIdValid = false;
                    }
                } else {
                    edtShopNo.setError("Invalid Id");
                    isShopIdValid = false;
                }
            }
        });

    } // onViewCreated()

    @Override
    public void setDataToView(List<CartEntity> cartEntityList) {
        this.cartEntityList = cartEntityList;
        requireActivity().runOnUiThread(() -> {
            adapter = new AdapterFinalProducts(cartEntityList, units, this);
            rvItemForPlace.setAdapter(adapter);

            if (cartEntityList.isEmpty()) {
                rvItemForPlace.setVisibility(View.GONE);
                requireView().findViewById(R.id.tvNoProductSelected).setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onShopValidation(boolean isValid) {
        isShopIdValid = isValid;
        if (isValid) {
            Toast.makeText(requireContext(), "Shop ID Verified", Toast.LENGTH_SHORT).show();
            edtShopNo.setCompoundDrawablesWithIntrinsicBounds(0,
                    0, R.drawable.ic_baseline_check_green, 0);
            saveShopID(edtShopNo.getText().toString());
        } else {
            edtShopNo.setError("Invalid ID");
        }
    }


    @Override
    public void onRemoveIconClicked(CartEntity cartEntity) {
        dialogConfirmation = new DialogConfirmation(
                v -> presenter.removeProduct(cartEntity),
                getString(R.string.text_remove_message)
        );
        dialogConfirmation.show(requireActivity().getSupportFragmentManager(), DialogConfirmation.TAG);
    }

    @Override
    public void onRemoveConfirmed() {
        if (dialogConfirmation != null) dialogConfirmation.dismiss();
    }

    @Override
    public void onEditIconClicked(CartEntity cartEntity, int position) {
        DialogEditProduct dialogEditProduct = new DialogEditProduct(
                cartEntity, units, position, this
        );
        dialogEditProduct.show(requireActivity().getSupportFragmentManager(), DialogEditProduct.TAG);
    }

    @Override
    public void onEditConfirmed(CartEntity updateValue, int position) {
        Log.d(TAG, "onEditConfirmed: is clicked");
        adapter.notifyItemChanged(position);

        if (looper.handler != null) {
            looper.handler.post(() -> {
                FrutasDAO database = FrutasDatabase.mInstance.frutasDAO();
                database.insetCart(updateValue);
            });
        } else {
            Log.d(TAG, "onEditConfirmed: looper is null");
        }

    }

    @Override
    public Integer getUnitIdByName(String name) {
        for (Unit unit : unitList.getData()) {
            if (unit.getName().equals(name)) {
                return unit.getId();
            }
        }
        return null;
    }

    @Override
    public PostOrder receiveDataToPlaceOrder(List<CartEntity> cartEntityList) {
        return preparePostOrder(
                edtShopNo.getText().toString(),
                tvDate.getText().toString(),
                cartEntityList
        );
    }

    @Override
    public void clearCart() {
        DialogIconMessage dialogIconMessage = new DialogIconMessage(
                DialogIconMessage.Success,
                "Success!"
        );
        dialogIconMessage.show(requireActivity().getSupportFragmentManager(), DialogIconMessage.TAG);

        looper.handler.post(() -> {
            FrutasDAO database = FrutasDatabase.mInstance.frutasDAO();
            database.clearCart();

            requireActivity().runOnUiThread(() -> {
                confirmPlaceOrder.dismiss();

                requireActivity().getSupportFragmentManager().popBackStackImmediate();
            });
        });

    }

    @Override
    public void saveShopID(String shopID) {
        ShopPreference shopPreference = new ShopPreference(requireContext());
        shopPreference.writeData(ShopPreference.SHOP_ID, shopID);
    }

    private PostOrder preparePostOrder(String shopNo, String date, List<CartEntity> entities) {
        List<String> quantityList = new ArrayList<>();
        List<String> productIdList = new ArrayList<>();
        List<String> unitIdList = new ArrayList<>();
        List<String> orderIdList = new ArrayList<>();

        for (CartEntity entity : entities) {
            quantityList.add(String.valueOf(entity.getQuantity()));

            productIdList.add(String.valueOf(entity.getProductId()));

            if (entity.getUnitId() == null) {
                unitIdList.add(null);
            } else {
                unitIdList.add(String.valueOf(entity.getUnitId()));
            }
            orderIdList.add(null);
        }

        return new PostOrder(shopNo, date, quantityList, productIdList, unitIdList, orderIdList);
    }


    /**
     * this method is responsible to get date from system and set it to requisition order
     */
    private void setDate() {
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Set Current Date
        String currentDate = year + "-" + (month + 1) + "-" + day;
        tvDate.setText(currentDate);


        setListener = (view1, year1, month1, dayOfMonth) -> {
            month1 = month1 + 1;
            String date = year + "-" + month1 + "-" + dayOfMonth;
            tvDate.setText(date);
        };

        tvDate.setOnClickListener(v -> {
            DatePickerDialog dialog = new DatePickerDialog(getContext(),
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    setListener,
                    year,
                    month,
                    day);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        });
    } // setDate()


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}