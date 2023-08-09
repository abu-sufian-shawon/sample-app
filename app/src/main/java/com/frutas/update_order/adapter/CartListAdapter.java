package com.frutas.update_order.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.frutas.R;
import com.frutas.create_requisition.datamodel.Product;
import com.frutas.update_order.view.FragmentUpdateOrder;

import java.util.List;

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.ViewHolder> {
    private final FragmentUpdateOrder fragmentUpdateOrder;
    private final List<Product> productList;
    private final List<String> units;

    public CartListAdapter(@NonNull FragmentUpdateOrder fragmentUpdateOrder,
                           List<Product> productList, List<String> units) {
        this.fragmentUpdateOrder = fragmentUpdateOrder;
        this.productList = productList;
        this.units = units;
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
        holder.tvSlNo.setText(String.valueOf((position + 1)));
        holder.tvProductName.setText(productList.get(position).getName());

        String cantidad;

        Integer unitId = productList.get(position).getUnitId();

        if(unitId == null) cantidad = String.valueOf(productList.get(position).getQuantity());
        else cantidad = productList.get(position).getQuantity() + " " + units.get(unitId);



        holder.tvCantidad.setText(cantidad);

        if(!productList.get(position).isSelect()) {
            holder.ivRemoveItem.setVisibility(View.GONE);
        }

        holder.ivRemoveItem.setOnClickListener((v)->
                fragmentUpdateOrder.onRemoveClicked(productList.get(position)));

        holder.ivEditFinal.setOnClickListener(v->
                fragmentUpdateOrder.onEditProductClicked(productList.get(position), position));

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvSlNo, tvProductName, tvCantidad;
        ImageView ivRemoveItem, ivEditFinal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvSlNo = itemView.findViewById(R.id.tvSlNo);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvCantidad = itemView.findViewById(R.id.tvCantidad);
            ivRemoveItem = itemView.findViewById(R.id.ivRemoveItem);
            ivEditFinal = itemView.findViewById(R.id.ivEditFinal);
        }
    }

}
