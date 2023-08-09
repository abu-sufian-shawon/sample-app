package com.frutas.track_order.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.frutas.R;
import com.frutas.create_requisition.datamodel.ProductHolder;
import com.frutas.network.ConnectionDetector;
import com.frutas.previous_order.data_model.AllOrder;
import com.frutas.previous_order.data_model.Datum;
import com.frutas.previous_order.data_model.Order;
import com.frutas.previous_order.view.FragmentRequisitionDetails;
import com.frutas.update_order.view.FragmentUpdateOrder;

import java.util.List;

public class TrackOrderAdapter extends RecyclerView.Adapter<TrackOrderAdapter.ViewHolder> {
    private final FragmentActivity activity;
    private final AllOrder allOrder;
    private final ProductHolder productHolder;
    private final String shopID;
    private final List<Datum> pendingOrders;


    public TrackOrderAdapter(FragmentActivity activity, AllOrder allOrder, ProductHolder productHolder,
                             List<Datum> pendingOrders, String shopID) {
        this.shopID = shopID;
        this.allOrder = allOrder;
        this.productHolder = productHolder;
        this.activity = activity;
        this.pendingOrders = pendingOrders;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.list_item_order,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvOderNumber.setText(pendingOrders.get(position).getRequisitionNumber());
        holder.tvOrderDate.setText(pendingOrders.get(position).getOrderDate());
        holder.tvQuantity.setText(String.valueOf(pendingOrders.get(position).getOrders().size()));
        holder.tvOrderStatus.setText(pendingOrders.get(position).getStatus());

        holder.editOrder.setVisibility(View.VISIBLE);
        holder.editOrder.setOnClickListener(v -> {
            if (ConnectionDetector.isNetAvailable(activity)) {
                List<Order> orders = pendingOrders.get(position).getOrders();
                String orderId = pendingOrders.get(position).getId().toString();
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(
                                R.id.fragmentContainer,
                                new FragmentUpdateOrder(productHolder ,orders, shopID, orderId),
                                FragmentUpdateOrder.class.getSimpleName()
                        ).addToBackStack(FragmentUpdateOrder.class.getSimpleName()).commit();
            }
        });

        holder.clOrder.setOnClickListener(v -> shopOrderDetails(
                allOrder.getData().indexOf(pendingOrders.get(position))
        ));
    }

    @Override
    public int getItemCount() {
        return pendingOrders.size();
    }

    private void shopOrderDetails(int position) {
        FragmentRequisitionDetails requisitionDetails = new FragmentRequisitionDetails(
                allOrder,
                productHolder,
                position
        );
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, requisitionDetails)
                .addToBackStack(FragmentRequisitionDetails.class.getSimpleName())
                .commit();

    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvOderNumber, tvOrderDate, tvQuantity, tvOrderStatus;
        LinearLayout editOrder;
        ConstraintLayout clOrder;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOderNumber = itemView.findViewById(R.id.tvOrderNumber);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            editOrder = itemView.findViewById(R.id.editOrder);
            clOrder = itemView.findViewById(R.id.clOrder);
            tvOrderStatus = itemView.findViewById(R.id.tvOrderStatus);
        }
    }
}
