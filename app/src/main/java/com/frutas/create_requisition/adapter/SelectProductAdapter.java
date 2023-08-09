package com.frutas.create_requisition.adapter;

import android.annotation.SuppressLint;
import android.util.Log;
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
import com.frutas.create_requisition.ListProductContract;
import com.frutas.create_requisition.datamodel.Product;
import com.frutas.db.entity.CartEntity;

import java.util.ArrayList;
import java.util.List;

public class SelectProductAdapter extends RecyclerView.Adapter<SelectProductAdapter.ViewHolder>
        implements Filterable {

    private static final String TAG = "ROOM";
    private final List<Product> productList;
    private List<Product> filteredProductList;
    private final ListProductContract.ListProductView view;


    public SelectProductAdapter(
            ListProductContract.ListProductView view,
            List<Product> filteredProduct) {
        this.view = view;
        this.productList = filteredProduct;
        this.filteredProductList = filteredProduct;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.list_item_select_product,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = filteredProductList.get(position);

        holder.tvProductName.setText(product.getName());
        holder.ivAddCart.setTag(position);

        holder.ivAddCart.setOnClickListener(v -> view.onAddCartClicked(position,
                new CartEntity(product)));
    }

    @Override
    public int getItemCount() {
        return filteredProductList.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName;
        ImageView ivAddCart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            ivAddCart = itemView.findViewById(R.id.ivAddCart);
        }

    }

    public void removeItem(CartEntity cartEntity){
        int pos = -1;
        for(int i = 0; i < filteredProductList.size(); i++){
            if(filteredProductList.get(i).getId().equals(cartEntity.getProductId())){
                pos = i;
                break;
            }
        }

        if(pos >= 0){
            filteredProductList.remove(pos);
            notifyItemRemoved(pos);
        }

    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String queryString = constraint.toString();
                Log.d(TAG, "performFiltering: is called. query text = " + queryString);

                List<Product> filterProduct = new ArrayList<>();

                if (queryString.isEmpty()) {
                    filteredProductList = productList;
                } else {
                    Log.d(TAG, "performFiltering: product size = " + productList.size());
                    for (Product product : productList) {
                        if (product.getName().toLowerCase().contains(queryString.toLowerCase())) {
                            filterProduct.add(product);
                        }
                    }
                    filteredProductList = filterProduct;
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
