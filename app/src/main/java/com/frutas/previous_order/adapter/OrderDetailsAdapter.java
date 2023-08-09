package com.frutas.previous_order.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.frutas.R;
import com.frutas.create_requisition.datamodel.Product;
import com.frutas.create_requisition.datamodel.ProductHolder;
import com.frutas.create_requisition.datamodel.Unit;
import com.frutas.previous_order.data_model.Order;

import java.util.List;

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder> {
    private List<Order> orderList;
    private ProductHolder productHolder;

    public OrderDetailsAdapter(List<Order> orderList, ProductHolder productHolder) {
        this.orderList = orderList;
        this.productHolder = productHolder;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(
                                R.layout.list_item_order_details,
                                parent,
                                false
                        )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String cantidad = getCantidad(orderList.get(position).getQuantity(),
                orderList.get(position).getUnitId());
        Double boxWeight = orderList.get(position).getBoxWeight();

        holder.tvProductName.setText(getProductName(orderList.get(position).getProductId()));
        holder.tvCantidad1.setText(cantidad);
        if (boxWeight != null){
            holder.tvTara.setText(String.valueOf(boxWeight.doubleValue()));
        }

        if (orderList.get(position).getNetWeight() != null){
            holder.tvNetto.setText(String.valueOf(orderList.get(position).getNetWeight()));
        }

        if (orderList.get(position).getSellingPrice() != null) {
            holder.tvPrice.setText(String.valueOf(orderList.get(position).getSellingPrice()));
        }
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    private String getProductName(int productId){
        for (Product product : productHolder.getData()){
            if (product.getId().equals(productId)){
                return product.getName();
            }
        }
        return "";
    }

    private String getCantidad(Integer quantity, Integer unitID){
        return quantity.toString() + " " + getDetalle(unitID);
    }

    private String getDetalle(Integer unitID){
        if (unitID != null){
            for (Unit unit : productHolder.getUnitList().getData()){
                if ( unit.getId().equals(unitID)){
                    return unit.getName();
                }
            }
        }
        return " ";
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvSerialNo, tvProductName, tvCantidad1, tvTotalKilo, tvTara, tvNetto, tvPrice;
        LinearLayout llTotalKilo, llTara;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvSerialNo = itemView.findViewById(R.id.tvSerialNo);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvCantidad1 = itemView.findViewById(R.id.tvCantidad);
            tvTotalKilo = itemView.findViewById(R.id.tvKilo);
            tvTara = itemView.findViewById(R.id.tvTara);
            tvNetto = itemView.findViewById(R.id.tvNetto);
            tvPrice = itemView.findViewById(R.id.tvPrice);

            llTotalKilo = itemView.findViewById(R.id.llTotalKilo);
            llTara = itemView.findViewById(R.id.llTara);
        }
    }
}
