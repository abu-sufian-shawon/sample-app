package com.frutas.update_order.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.DialogFragment;

import com.frutas.R;
import com.frutas.create_requisition.ListProductContract;
import com.frutas.create_requisition.datamodel.Product;
import com.frutas.db.entity.CartEntity;
import com.frutas.update_order.UpdateContract;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.List;
import java.util.Objects;

public class DialogChooseProduct extends DialogFragment {
    public static final String TAG = "DialogFragment";
    
    private EditText edtQuantity;
    private AppCompatSpinner spnChooseUnit;

    private final UpdateContract.View updateProductView;
    private final Product product;
    private final List<String> units;

    private int quantity = 1;

    public DialogChooseProduct(UpdateContract.View updateProductView,
                               Product product,
                               List<String> units) {
        this.updateProductView = updateProductView;
        this.product = product;
        this.units = units;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Objects.requireNonNull(getDialog()).getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return inflater.inflate(R.layout.dialog_choose_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // init view component
        ImageView ivIncrement = view.findViewById(R.id.ivIncrement);
        ImageView ivDecrement = view.findViewById(R.id.ivDecrement);
        ImageView ivCloseDialog = view.findViewById(R.id.ivCloseDialog);

        TextView tvProductTitle = view.findViewById(R.id.tvProductTitle);

        edtQuantity = view.findViewById(R.id.edtQuantity);

        spnChooseUnit = view.findViewById(R.id.spnChooseUnit);

        tvProductTitle.setText(product.getName());
        Log.d(TAG, "onViewCreated: quantity = " + product.getQuantity());

        if(product.getQuantity() != 0){
            edtQuantity.setText(String.valueOf(product.getQuantity()));
        }

        ivIncrement.setOnClickListener(v -> {
            if (edtQuantity.getText().toString().isEmpty()) {
                quantity = 1;
                edtQuantity.setText(String.valueOf(quantity));
            } else {
                quantity = Integer.parseInt(edtQuantity.getText().toString());
                quantity++;
            }
            edtQuantity.setText(String.valueOf(quantity));
            edtQuantity.clearFocus();
        });

        ivDecrement.setOnClickListener(v -> {
            if (edtQuantity.getText().toString().isEmpty()) {
                quantity = 1;
                edtQuantity.setText(String.valueOf(quantity));
            } else {
                quantity = Integer.parseInt(edtQuantity.getText().toString());
                if ((quantity == 1)) {
                    FancyToast.makeText(getContext(), "La cantidad no puede ser 0",
                            FancyToast.ERROR, FancyToast.LENGTH_LONG, false).show();
                } else {
                    quantity--;
                    edtQuantity.setText(String.valueOf(quantity));
                }
            }
            edtQuantity.clearFocus();
        });

        view.findViewById(R.id.btnCancel).setOnClickListener(v -> dismiss());

        view.findViewById(R.id.btnConfirm).setOnClickListener(v->{
            product.setQuantity(Integer.parseInt(edtQuantity.getText().toString()));

            Integer unitId =
                    updateProductView.getUnitIdByName(spnChooseUnit.getSelectedItemPosition());

            Log.d(TAG, "onViewCreated: unit id = " + unitId);
            product.setUnitId(unitId);
            product.setSelect(true);

            updateProductView.onProductChosen(this.product);
            dismiss();
        });

        ivCloseDialog.setOnClickListener(v -> dismiss());

        // Spinner Adapter Creating
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, units);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnChooseUnit.setAdapter(adapter);
        Integer unitID  = product.getUnitId();
        if(unitID != null){
            spnChooseUnit.setSelection(unitID);
        }

    }
}
