package com.frutas.previous_order.view;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.frutas.R;
import com.frutas.create_requisition.datamodel.Product;
import com.frutas.create_requisition.datamodel.ProductHolder;
import com.frutas.create_requisition.datamodel.Unit;
import com.frutas.previous_order.data_model.AllOrder;
import com.frutas.previous_order.data_model.Datum;
import com.frutas.previous_order.data_model.Order;
import com.frutas.previous_order.data_model.Shop;

import java.text.DecimalFormat;
import java.util.List;

import static com.frutas.previous_order.Status.CANCELED;
import static com.frutas.previous_order.Status.COMPLETED;
import static com.frutas.previous_order.Status.DELIVERED;
import static com.frutas.previous_order.Status.PENDING;
import static com.frutas.previous_order.Status.PROCESSING;

public class FragmentRequisitionDetails extends Fragment {
    private static final String TAG = "DialogRequisitionDetail";
    private final String euro = "\u20ac ";
    private Context context;
    private DecimalFormat decimalFormat;
    private FragmentManager fragmentManager;
    private final AllOrder allOrder;
    private final ProductHolder productHolder;
    private final Integer orderPosition;

    private TextView tvRequisitionNumber, tvDate, tvShopName, tvShopContact, tvOrderStatus,
            tvDirection, tvTotalPriceValue, tvExcludedPriceValue, tvGrandPriceValue,
            tvPaindAmountValue, tvDueAmountValue, tvReferenceRequisitionNumber;

    private LinearLayout rvProductDetails;


    public FragmentRequisitionDetails(AllOrder allOrder, ProductHolder productHolder, Integer orderPosition) {
        this.allOrder = allOrder;
        this.productHolder = productHolder;
        this.orderPosition = orderPosition;
        this.decimalFormat = new DecimalFormat("#.##");
        Log.i(TAG, "DialogRequisitionDetails: is viewing");
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_requisition_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.context = getContext();
        fragmentManager = getFragmentManager();

        initView(view); // init view component

        setShopToView(view);

        showOrderList(allOrder.getData().get(orderPosition).getOrders());
    }

    private void setShopToView(View v) {
        Shop shop = allOrder.getData().get(orderPosition).getShop();
        Datum datum = allOrder.getData().get(orderPosition);

        // set shop data
        if (datum.getRequisitionNumber() != null) {
            tvRequisitionNumber.setText(datum.getRequisitionNumber());
        }

        if (datum.getOrderDate() != null) {
            tvDate.setText(datum.getOrderDate());
        }

        if (shop.getName() != null) {
            tvShopName.setText(shop.getName());
        }

        if (shop.getMobile() != null) {
            tvShopContact.setText(shop.getMobile());
        }

        if (shop.getAddress() != null)
            tvDirection.setText(shop.getAddress());

        if (datum.getStatus() != null) {
            setOrderStatus(datum.getStatus());
        }

        if (datum.getTotalAmount() != null) {
            tvTotalPriceValue.setText(String.valueOf(datum.getTotalAmount()));
        }

        if (datum.getReferenceRequisitionNumber() != null) {
            tvReferenceRequisitionNumber.setVisibility(View.VISIBLE);
            String str = "Reference #" + datum.getReferenceRequisitionNumber();
            if (datum.getRemark() != null){
                str = "Reference #" + datum.getReferenceRequisitionNumber() + " (" +
                        datum.getRemark() + ")";
            }

            tvReferenceRequisitionNumber.setText(str);
            String returnAmount = "-" + datum.getReturnAmount();
            tvExcludedPriceValue.setText(returnAmount);
        } else {
            v.findViewById(R.id.clReference).setVisibility(View.GONE);
        }

        if (datum.getTotalAmount() != null) {
            String temp = euro + datum.getTotalAmount();

            tvTotalPriceValue.setText(temp);
        }

        if (datum.getPaidAmount() != null) {
            String temp = euro + decimalFormat.format(datum.getPaidAmount());
            tvPaindAmountValue.setText(temp);
        }

        if (datum.getDueAmount() != null) {
            String temp = euro + decimalFormat.format(datum.getDueAmount());
            tvDueAmountValue.setText(temp);
        }

        if (datum.getGrandTotal() != null) {
            String temp = euro + decimalFormat.format(datum.getGrandTotal());
            tvGrandPriceValue.setText(temp);
        }
    }

    private void setOrderStatus(String status) {
        switch (status) {
            case PENDING:
                tvOrderStatus.setText(status);
                tvOrderStatus.setTextColor(ContextCompat.getColor(context, R.color.pending));
                break;
            case PROCESSING:
                tvOrderStatus.setText(status);
                tvOrderStatus.setTextColor(ContextCompat.getColor(context, R.color.processing));
                break;
            case COMPLETED:
                tvOrderStatus.setText(status);
                tvOrderStatus.setTextColor(ContextCompat.getColor(context, R.color.completed));
                break;
            case DELIVERED:
                tvOrderStatus.setText(status);
                tvOrderStatus.setTextColor(ContextCompat.getColor(context, R.color.delivered));
                break;
            case CANCELED:
                tvOrderStatus.setText(status);
                tvOrderStatus.setTextColor(ContextCompat.getColor(context, R.color.cancel));
                break;
        }
    }

    /**
     * Init all view components
     *
     * @param v view object of dialog to find all components inside it.
     */
    private void initView(View v) {
        tvRequisitionNumber = v.findViewById(R.id.tvRequisitionNumber);
        tvDate = v.findViewById(R.id.tvDate);
        tvShopName = v.findViewById(R.id.tvShopName);
        tvShopContact = v.findViewById(R.id.tvShopContact);
        tvOrderStatus = v.findViewById(R.id.tvOrderStatus);
        tvTotalPriceValue = v.findViewById(R.id.tvTotalPriceValue);
        tvExcludedPriceValue = v.findViewById(R.id.tvExcludedPriceValue);
        tvGrandPriceValue = v.findViewById(R.id.tvGrandPriceValue);
        tvPaindAmountValue = v.findViewById(R.id.tvPaindAmountValue);
        tvDueAmountValue = v.findViewById(R.id.tvDueAmountValue);
        rvProductDetails = v.findViewById(R.id.rvProductDetails);
        tvDirection = v.findViewById(R.id.tvDirection);
        tvReferenceRequisitionNumber = v.findViewById(R.id.tvReferenceRequisitionNumber);
    }


    private void showOrderList(List<Order> orderList) {

        TextView tvSerialNo, tvProductName, tvCantidad1, tvKilo, tvSinglePrice, tvTara, tvNetto, tvPrice;
        LinearLayout llTotalKilo, llTara;


        for (int position = 0; position < orderList.size(); position++) {

            View itemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item_order_details,
                    rvProductDetails,
                    false
            );
            tvSerialNo = itemView.findViewById(R.id.tvSerialNo);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvCantidad1 = itemView.findViewById(R.id.tvCantidad);
            tvKilo = itemView.findViewById(R.id.tvKilo);
            tvTara = itemView.findViewById(R.id.tvTara);
            tvNetto = itemView.findViewById(R.id.tvNetto);
            tvPrice = itemView.findViewById(R.id.tvPriceValue);

            tvSinglePrice = itemView.findViewById(R.id.tvSinglePrice);

            llTotalKilo = itemView.findViewById(R.id.llTotalKilo);
            llTara = itemView.findViewById(R.id.llTara);


            Double boxWeight = orderList.get(position).getBoxWeight();

            tvSerialNo.setText(String.valueOf((position + 1)));

            if (orderList.get(position).getProductId() != null) {
                tvProductName.setText(getProductName(orderList.get(position).getProductId()));
            }

            if (orderList.get(position).getQuantity() != null) {
                String cantidad = getCantidad(orderList.get(position).getQuantity(),
                        orderList.get(position).getUnitId());
                tvCantidad1.setText(cantidad);
            }

            if (orderList.get(position).getKilo() != null) {
                tvKilo.setText(String.valueOf(orderList.get(position).getKilo()));
            }

            if (orderList.get(position).getBoxWeight() != null) {
                tvTara.setText(String.valueOf(orderList.get(position).getBoxWeight()));
            }

            if (orderList.get(position).getNetWeight() != null) {
                tvNetto.setText(String.valueOf(orderList.get(position).getNetWeight()));
            }

            if (orderList.get(position).getSellingPrice() != null
                    && orderList.get(position).getNetWeight() != null) {
                double price = orderList.get(position).getSellingPrice() * orderList.get(position).getNetWeight();
                String temp = euro + decimalFormat.format(price);
                tvPrice.setText(temp);

                temp = euro + orderList.get(position).getSellingPrice();
                tvSinglePrice.setText(temp);
            }


            if (boxWeight != null) {
                tvTara.setText(String.valueOf(decimalFormat.format(boxWeight)));
            }

            if (orderList.get(position).getNetWeight() != null) {
                tvNetto.setText(String.valueOf(decimalFormat.format(orderList.get(position).getNetWeight())));
            }

            rvProductDetails.addView(itemView);
        }
    }

    private String getProductName(int productId) {
        for (Product product : productHolder.getData()) {
            if (product.getId().equals(productId)) {
                return product.getName();
            }
        }
        return "";
    }

    private String getCantidad(Integer quantity, Integer unitID) {
        return quantity.toString() + " " + getDetalle(unitID);
    }

    private String getDetalle(Integer unitID) {
        if (unitID != null) {
            for (Unit unit : productHolder.getUnitList().getData()) {
                if (unit.getId().equals(unitID)) {
                    return unit.getName();
                }
            }
        }
        return " ";
    }


}
