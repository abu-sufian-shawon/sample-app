package com.frutas.update_order.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.frutas.R;
import com.frutas.create_requisition.datamodel.Product;
import com.frutas.update_order.UpdateContract;

import java.util.List;
import java.util.Objects;

public class DialogEditProduct extends DialogFragment {
    public static final String TAG = "DialogEditProduct";

    private final UpdateContract.View mainView;

    private Spinner spnEditChooseUnit;
    private EditText edtEditQuantity;

    private final int position;

    private final Product product;
    private final List<String> unit;

    public DialogEditProduct(Product product, List<String> unit, UpdateContract.View view,
                             int position) {
        this.product = product;
        this.unit = unit;
        this.mainView = view;
        this.position = position;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogTheme);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Objects.requireNonNull(getDialog()).getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return inflater.inflate(R.layout.dialog_edit_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // init view components
        TextView tvEditProductTitle = view.findViewById(R.id.tvEditProductTitle);
        Button btnSave = view.findViewById(R.id.btnSave);
        spnEditChooseUnit = view.findViewById(R.id.spnEditChooseUnit);
        edtEditQuantity = view.findViewById(R.id.edtEditQuantity);

        tvEditProductTitle.setText(product.getName());

        if (product.getQuantity() == 0) { // default value of quantity will be 0
            edtEditQuantity.setText("1");
        } else {
            edtEditQuantity.setText(String.valueOf(product.getQuantity()));
        }


        // Spinner Adapter Creating
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, unit);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnEditChooseUnit.setAdapter(adapter);

        if (product.getUnitId() == null) {
            spnEditChooseUnit.setSelection(0);
        } else {
            spnEditChooseUnit.setSelection(product.getUnitId());
        }


        // onclick method implementations
        // Increment Quantity
        view.findViewById(R.id.ivEditIncrement).setOnClickListener(v -> {
            if (!edtEditQuantity.getText().toString().isEmpty()) {
                int quantity = Integer.parseInt(edtEditQuantity.getText().toString());
                quantity++;
                edtEditQuantity.setText(String.valueOf(quantity));
                product.setQuantity(quantity);
            } else {
                int quantity = 0;
                quantity++;
                edtEditQuantity.setText(String.valueOf(quantity));
                product.setQuantity(quantity);
            }
        });

        // Decrement Quantity
        view.findViewById(R.id.ivEditDecrement).setOnClickListener(v -> {
            if (!edtEditQuantity.getText().toString().isEmpty()) {
                int quantity = Integer.parseInt(edtEditQuantity.getText().toString());
                if (quantity > 1) {
                    quantity--;
                }
                edtEditQuantity.setText(String.valueOf(quantity));
                product.setQuantity(quantity);
            }
        });

        view.findViewById(R.id.ivEditCloseDialog).setOnClickListener(v -> dismiss());

        btnSave.setOnClickListener(v -> {
            if (Integer.parseInt(edtEditQuantity.getText().toString()) <= 0) {
                edtEditQuantity.setError("");
            } else {
                product.setQuantity(Integer.parseInt(edtEditQuantity.getText().toString()));
                Integer unitId =
                        mainView.getUnitIdByName(spnEditChooseUnit.getSelectedItemPosition());

                product.setUnitId(unitId);
                mainView.onUpdateProductConfirmed(position);
                dismiss();
            }

        });

    }

}
