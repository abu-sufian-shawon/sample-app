package com.frutas.create_requisition.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.frutas.R;
import com.frutas.create_requisition.RequisitionContract;
import com.frutas.db.entity.CartEntity;

import java.util.List;

public class DialogEditProduct extends DialogFragment {
    public static final String TAG = "DialogEditProduct";
    private final CartEntity cartEntity;
    private final List<String> unitList;
    private final RequisitionContract.View requisitionView;

    private final int position;

    public DialogEditProduct(CartEntity cartEntity, List<String> unitList, int position,
                             RequisitionContract.View requisitionView) {
        this.cartEntity = cartEntity;
        this.unitList = unitList;
        this.position = position;
        this.requisitionView = requisitionView;
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
        requireDialog().getWindow()
                .setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return inflater.inflate(R.layout.dialog_edit_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvEditProductTitle = view.findViewById(R.id.tvEditProductTitle);
        EditText edtEditQuantity = view.findViewById(R.id.edtEditQuantity);
        Spinner spnEditChooseUnit = view.findViewById(R.id.spnEditChooseUnit);

        tvEditProductTitle.setText(cartEntity.getName());
        edtEditQuantity.setText(String.valueOf(cartEntity.getQuantity()));

        // Spinner Adapter Creating
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, unitList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnEditChooseUnit.setAdapter(adapter);

        // set selected quantity
        Integer unitID  = cartEntity.getUnitId();
        if(unitID != null){
            spnEditChooseUnit.setSelection(unitID);
        }


        // onclick method implementations
        // Increment Quantity
        view.findViewById(R.id.ivEditIncrement).setOnClickListener(v-> {
            if (!edtEditQuantity.getText().toString().isEmpty()) {
                int quantity = Integer.parseInt(edtEditQuantity.getText().toString());
                quantity++;
                edtEditQuantity.setText(String.valueOf(quantity));
                cartEntity.setQuantity(quantity);
            } else {
                int quantity = 0;
                quantity++;
                edtEditQuantity.setText(String.valueOf(quantity));
                cartEntity.setQuantity(quantity);
            }
        });

        // Decrement Quantity
        view.findViewById(R.id.ivEditDecrement).setOnClickListener(v-> {
            if (!edtEditQuantity.getText().toString().isEmpty()) {
                int quantity = Integer.parseInt(edtEditQuantity.getText().toString());
                if(quantity > 1){
                    quantity--;
                }
                edtEditQuantity.setText(String.valueOf(quantity));
                cartEntity.setQuantity(quantity);
            }
        });

        view.findViewById(R.id.ivEditCloseDialog).setOnClickListener(v -> dismiss());

        view.findViewById(R.id.btnSave).setOnClickListener(v -> {
            if (!edtEditQuantity.getText().toString().isEmpty()) {
                int quantity = Integer.parseInt(edtEditQuantity.getText().toString());
                if (quantity > 0) {
                    cartEntity.setQuantity(quantity);
                } else {
                    // Quantity not fulfill requirements
                    Toast.makeText(getContext(), "Quantity must be bigger than 0",
                            Toast.LENGTH_LONG).show();
                    return;
                }
            } else {
                // Empty EditText
                return;
            }

            // update product
            int unitPosition = spnEditChooseUnit.getSelectedItemPosition();

            Integer unitId = requisitionView.getUnitIdByName(unitList.get(unitPosition));

            cartEntity.setUnitId(unitId);
            requisitionView.onEditConfirmed(cartEntity, position);
            dismiss();
        });
    }
}
