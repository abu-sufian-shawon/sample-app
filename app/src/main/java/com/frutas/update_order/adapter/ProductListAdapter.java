package com.frutas.update_order.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.frutas.R;
import com.frutas.create_requisition.datamodel.Product;
import com.frutas.update_order.view.FragmentUpdateOrder;

import java.util.ArrayList;
import java.util.List;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder>
        implements Filterable {
    private static final String TAG = "DEBUG";

    private final FragmentUpdateOrder fragmentUpdateOrder;
    private List<Product> filteredProductList;
    private final List<Product> productList = new ArrayList<>();

    public ProductListAdapter(
            FragmentUpdateOrder fragmentUpdateOrder,
            List<Product> productList) {
        this.fragmentUpdateOrder = fragmentUpdateOrder;
        this.productList.addAll(productList);
        filteredProductList = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(
                                R.layout.list_item_select_product,
                                parent,
                                false
                        )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvProductName.setText(filteredProductList.get(position).getName());

        holder.ivAddToCart.setOnClickListener(v ->
                fragmentUpdateOrder.onAddToCartClicked(filteredProductList.get(position)));
    }

    @Override
    public int getItemCount() {
        return filteredProductList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvProductName;
        ImageView ivAddToCart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            ivAddToCart = itemView.findViewById(R.id.ivAddCart);
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String queryStr = constraint.toString();

                List<Product> filteredProduct = new ArrayList<>();

                if (queryStr.isEmpty()) {
                    filteredProductList = productList;
                } else {
                    for (Product product : productList) {
                        if (product.getName().toLowerCase().contains(queryStr)) {
                            filteredProduct.add(product);
                        }
                    }
                    filteredProductList = filteredProduct;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredProductList;

                return filterResults;
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                notifyDataSetChanged();
            }
        };
    }
}
