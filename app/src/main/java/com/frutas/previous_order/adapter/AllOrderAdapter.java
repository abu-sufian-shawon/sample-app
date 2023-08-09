package com.frutas.previous_order.adapter;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.frutas.R;
import com.frutas.create_requisition.datamodel.ProductHolder;
import com.frutas.previous_order.data_model.AllOrder;
import com.frutas.previous_order.view.FragmentRequisitionDetails;
import com.frutas.update_order.view.FragmentUpdateOrder;

import static com.frutas.previous_order.Status.CANCELED;
import static com.frutas.previous_order.Status.COMPLETED;
import static com.frutas.previous_order.Status.DELIVERED;
import static com.frutas.previous_order.Status.PENDING;
import static com.frutas.previous_order.Status.PROCESSING;

public class AllOrderAdapter extends RecyclerView.Adapter<AllOrderAdapter.ViewHolder> {
    private static final String TAG = "AllOrderAdapter";
    private final FragmentActivity activity;
    private final AllOrder allOrder;
    private final ProductHolder productHolder;

    public AllOrderAdapter(FragmentActivity activity, AllOrder allOrder, ProductHolder productHolder) {
        this.activity = activity;
        this.allOrder = allOrder;
        this.productHolder = productHolder;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(
                                R.layout.list_item_order,
                                parent,
                                false
                        )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        /*
            change UI depend over order status
         */
        switch (allOrder.getData().get(position).getStatus()){
            case PENDING:
                holder.tvOrderStatus.setText(PENDING);
                holder.tvOrderStatus.setTextColor(getColor(PENDING));
                holder.tvOrderStatus.setBackground(getDrawable(PENDING));
                holder.ivEditOrder.setVisibility(View.VISIBLE);

                break;
            case PROCESSING:
                holder.tvOrderStatus.setText(PROCESSING);
                holder.tvOrderStatus.setTextColor(getColor(PROCESSING));
                holder.tvOrderStatus.setBackground(getDrawable(PROCESSING));
                break;
            case COMPLETED:
                holder.tvOrderStatus.setText(COMPLETED);
                holder.tvOrderStatus.setTextColor(getColor(COMPLETED));
                holder.tvOrderStatus.setBackground(getDrawable(COMPLETED));
                break;
            case DELIVERED:
                holder.tvOrderStatus.setText(DELIVERED);
                holder.tvOrderStatus.setTextColor(getColor(DELIVERED));
                holder.tvOrderStatus.setBackground(getDrawable(DELIVERED));
                break;
            case CANCELED:
                holder.tvOrderStatus.setText(CANCELED);
                holder.tvOrderStatus.setTextColor(getColor(CANCELED));
                holder.tvOrderStatus.setBackground(getDrawable(CANCELED));
                break;
        }

        String str = "#" + allOrder.getData().get(position).getRequisitionNumber();
        holder.tvOrderNumber.setText(str);
        holder.tvOrderDate.setText(allOrder.getData().get(position).getOrderDate());
        holder.tvQuantity.setText(
                String.valueOf(
                        allOrder.getData().get(position).getOrders().size()
                )
        );

        holder.clOrder.setOnClickListener(v->{
            Log.i(TAG, "onBindViewHolder: product is pressed...");
            FragmentRequisitionDetails fragmentRequisitionDetails = new FragmentRequisitionDetails(
                    allOrder,
                    productHolder,
                    position
            );

            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, fragmentRequisitionDetails,
                            FragmentRequisitionDetails.class.getSimpleName())
                    .addToBackStack(FragmentRequisitionDetails.class.getSimpleName())
                    .commit();


        });

        holder.ivEditOrder.setVisibility(View.GONE);

        holder.ivEditOrder.setOnClickListener(v->{
            FragmentUpdateOrder updateOrder = new FragmentUpdateOrder(
                    productHolder,
                    allOrder.getData().get(0).getOrders(),
                    allOrder.getData().get(0).getShop().getShopNumber(),
                    allOrder.getData().get(0).getId().toString()
                    );
            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, updateOrder, FragmentUpdateOrder.class.getSimpleName())
                    .addToBackStack(FragmentUpdateOrder.class.getSimpleName())
                    .commit();
        });
    }

    @Override
    public int getItemCount() {
        return allOrder.getData().size();
    }

    private int getColor(String status){
        switch (status){
            case PENDING:
                return activity.getResources().getColor(R.color.pending);
            case PROCESSING:
                return activity.getResources().getColor(R.color.processing);
            case COMPLETED:
                return activity.getResources().getColor(R.color.completed);
            case DELIVERED:
                return activity.getResources().getColor(R.color.delivered);
            case CANCELED:
                return activity.getResources().getColor(R.color.cancel);
        }
        return 0;
    }

    private Drawable getDrawable(String status){
        switch (status){
            case PENDING:
                return ContextCompat.getDrawable(activity, R.drawable.bg_status_pending);
            case PROCESSING:
                return ContextCompat.getDrawable(activity, R.drawable.bg_status_processing);
            case COMPLETED:
                return ContextCompat.getDrawable(activity, R.drawable.bg_status_completed);
            case DELIVERED:
                return ContextCompat.getDrawable(activity, R.drawable.bg_status_delivered);
            case CANCELED:
                return ContextCompat.getDrawable(activity, R.drawable.bg_status_cancel);
        }
        return null;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderNumber, tvOrderDate, tvQuantity, tvOrderStatus;
        ImageView ivEditOrder;
        ConstraintLayout clOrder;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderNumber = itemView.findViewById(R.id.tvOrderNumber);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvOrderStatus = itemView.findViewById(R.id.tvOrderStatus);

            ivEditOrder = itemView.findViewById(R.id.ivEditOrder);

            clOrder = itemView.findViewById(R.id.clOrder);
        }
    }
}
