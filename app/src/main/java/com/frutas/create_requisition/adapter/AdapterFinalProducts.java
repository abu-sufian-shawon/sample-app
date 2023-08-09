package com.frutas.create_requisition.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.frutas.R;
import com.frutas.create_requisition.RequisitionContract;
import com.frutas.db.entity.CartEntity;

import java.util.List;

public class AdapterFinalProducts extends RecyclerView.Adapter<AdapterFinalProducts.ViewHolder> {
    private final List<CartEntity> productList;
    private final List<String> units;
    private final RequisitionContract.View view;

    public AdapterFinalProducts(List<CartEntity> productList, List<String> units,
                                RequisitionContract.View view) {
        this.productList = productList;
        this.units = units;
        this.view = view;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(
                                R.layout.list_item_final_product,
                                parent,
                                false
                        )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (units.isEmpty()) return;

        String candidad = "";

        if (productList.get(position).getUnitId() == null) {
            candidad = String.valueOf(productList.get(position).getQuantity());
        } else {
            candidad = productList.get(position).getQuantity() + " "
                    + units.get(productList.get(position).getUnitId());
        }

        holder.tvSlNo.setText(String.valueOf(position + 1));
        holder.tvProductName.setText(productList.get(position).getName());
        holder.tvCandidad.setText(candidad);
        holder.ivRemoveItem.setOnClickListener(v -> view.onRemoveIconClicked(productList.get(position)));

        holder.ivEditFinal.setOnClickListener(v -> {
            view.onEditIconClicked(productList.get(position), position);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvSlNo, tvProductName, tvCandidad;
        ImageView ivRemoveItem, ivEditFinal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSlNo = itemView.findViewById(R.id.tvSlNo);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvCandidad = itemView.findViewById(R.id.tvCantidad);
            ivRemoveItem = itemView.findViewById(R.id.ivRemoveItem);
            ivEditFinal = itemView.findViewById(R.id.ivEditFinal);
        }
    }
}
