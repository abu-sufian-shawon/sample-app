package com.frutas.update_order.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.frutas.R;
import com.frutas.create_requisition.datamodel.Product;
import com.frutas.create_requisition.datamodel.ProductHolder;
import com.frutas.create_requisition.datamodel.Unit;
import com.frutas.create_requisition.datamodel.UnitList;
import com.frutas.create_requisition.dialog.DialogConfirmation;
import com.frutas.create_requisition.post_data_model.PostOrder;
import com.frutas.dialog.DialogIconMessage;
import com.frutas.dialog.DialogNoInternet;
import com.frutas.network.ConnectionDetector;
import com.frutas.previous_order.data_model.Order;
import com.frutas.update_order.UpdateContract;
import com.frutas.update_order.adapter.CartListAdapter;
import com.frutas.update_order.adapter.ProductListAdapter;
import com.frutas.update_order.dialog.DialogChooseProduct;
import com.frutas.update_order.dialog.DialogEditProduct;
import com.frutas.update_order.presenter.UpdatePresenter;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class FragmentUpdateOrder extends Fragment implements UpdateContract.View {

    private static final String TAG = "CHECK";

    public final String shopId;
    public final String orderID;
    private BottomSheetBehavior<LinearLayout> bottomSheetBehavior;
    private Context context;
    private FragmentManager fragmentManager;
    private UpdateContract.Presenter presenter;
    private DialogNoInternet dialogNoInternet;
    private ProductHolder productHolder;
    private final List<Product> productList;
    private final List<Product> productCart;
    private final List<Order> orders;
    private List<String> unitList;
    private List<Integer> unitID;

    private CartListAdapter cartListAdapter;
    private ProductListAdapter productListAdapter;

    private DialogConfirmation dialogConfirmation;


    private RecyclerView rvUpdateOrder, rvSelectedProduct;
    private TextView tvSelectedProduct;

    public FragmentUpdateOrder(ProductHolder productHolder, List<Order> orders, String shopId,
                               String orderID) {
        this.productHolder = productHolder;
        this.orders = orders;
        this.shopId = shopId;
        this.orderID = orderID;
        productList = new ArrayList<>();
        productCart = new ArrayList<>();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update_order, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getContext();

        fragmentManager = requireActivity().getSupportFragmentManager();
        // init view
        rvSelectedProduct = view.findViewById(R.id.rvSelectedProduct);
        rvUpdateOrder = view.findViewById(R.id.rvUpdateOrder);
        EditText edtSearchProduct = view.findViewById(R.id.edtSearchProduct);
        tvSelectedProduct = view.findViewById(R.id.tvSelectedProduct);

        LinearLayout bottomSheetView = view.findViewById(R.id.bottomSheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetView);

        BottomSheetBehavior.from(bottomSheetView).setPeekHeight(convertDpToPx(context, 30));

        presenter = new UpdatePresenter(this);
        dialogNoInternet = new DialogNoInternet();

        if (ConnectionDetector.isNetAvailable(context)) {
            presenter.getUnitList();
        } else {
            dialogNoInternet.show(fragmentManager, DialogNoInternet.TAG);
        }


        view.findViewById(R.id.ivAddMoreProduct).setOnClickListener(v -> {
            if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            } else {
                BottomSheetBehavior.from(bottomSheetView).setState(BottomSheetBehavior.STATE_EXPANDED);
            }

        });

        view.findViewById(R.id.btnAddProduct).setOnClickListener(v ->
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED));

        view.findViewById(R.id.btnUpdateOrder).setOnClickListener(v -> takePermission());


        edtSearchProduct.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                    productListAdapter.getFilter().filter(s.toString());
            }
        });
    }

    private void setProductListToView(ProductHolder productHolder, List<String> unitList) {
        this.productHolder = productHolder;

        if(!productList.isEmpty()) productList.clear();

        productList.addAll(productHolder.getData());
        productCart.addAll(convertOrderToProduct(orders));

        cartListAdapter = new CartListAdapter(this, productCart, unitList);
        productListAdapter = new ProductListAdapter(
                this,
                productList
        );
        tvSelectedProduct.setText(String.valueOf(productCart.size()));
        rvSelectedProduct.setAdapter(cartListAdapter);
        rvUpdateOrder.setAdapter(productListAdapter);

        Log.d(TAG, "setProductListToView: product size = " + productList.size());

    }

    @Override
    public void setUnitList(UnitList unitList) {
        List<String> list = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();
        list.add("Select");
        list2.add(null);
        for (Unit unit : unitList.getData()) {
            list.add(unit.getName());
            list2.add(unit.getId());
        }
        this.unitList = list;
        this.unitID = list2;

        setProductListToView(productHolder, list);
    }

    @Override
    public void onEditProductClicked(Product product, int position) {
        DialogEditProduct dialogEditProduct = new DialogEditProduct(
                product, unitList, this, position
        );

        dialogEditProduct.show(fragmentManager, DialogEditProduct.TAG);
    }

    @Override
    public void onUpdateProductConfirmed(int position) {
        cartListAdapter.notifyItemChanged(position);
    }

    @Override
    public Integer getUnitIdByName(int position) {
        return unitID.get(position);
    }

    @Override
    public void onAddToCartClicked(Product product) {
        DialogChooseProduct dialogChooseProduct =
                new DialogChooseProduct(this, product, unitList);
        dialogChooseProduct.show(fragmentManager, DialogChooseProduct.TAG);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onProductChosen(@NonNull Product product) {
        Log.d(TAG, "onProductChosen: product name = " + product.getName());

        int pos;
        product.setSelect(true);

        if(productCart.contains(product)){
            Log.d(TAG, "onProductChosen: product already added");
            pos = productCart.indexOf(product);
            cartListAdapter.notifyItemChanged(pos);
        } else {
            productCart.add(product);
            cartListAdapter.notifyDataSetChanged();
            productList.remove(product);
            productListAdapter.notifyDataSetChanged();
        }

        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onRemoveClicked(Product product) {
        Log.d(TAG, "onRemoveClicked: is called");
        productList.add(product);
        Log.d(TAG, "onRemoveClicked: item inserted at " + productList.indexOf(product));
        productListAdapter.notifyItemInserted(productList.indexOf(product));
        productCart.remove(product);
        cartListAdapter.notifyDataSetChanged();
        productListAdapter.notifyDataSetChanged();
    }

    @Override
    public void showLoading() {
        View loadingProgress = requireView().findViewById(R.id.loadingProgress);

        if (loadingProgress.getVisibility() == View.GONE) {
            loadingProgress.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideLoading() {
        View loadingProgress = requireView().findViewById(R.id.loadingProgress);

        if (loadingProgress.getVisibility() == View.VISIBLE) {
            loadingProgress.setVisibility(View.GONE);
        }
    }


    @NonNull
    private List<Product> convertOrderToProduct(List<Order> orderList) {
        List<Product> list = new ArrayList<>();

        Toast.makeText(requireContext(), "" + productList.size(), Toast.LENGTH_SHORT).show();

        for (Order order : orderList) {
            for (Product product : productHolder.getData()) {
                if (order.getProductId().equals(product.getId())) {
                    list.add(product.setOrder(order));
                    productList.remove(product);
                    break;
                }
            }
        }

        return list;
    }

    @NonNull
    private String getDate() {
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        return year + "-" + (month + 1) + "-" + day;
    }

    @Override
    public void dismiss() {
        dialogConfirmation.dismiss();
        DialogIconMessage iconMessage = new DialogIconMessage(DialogIconMessage.Success,
                "Order Updated!");
        iconMessage.show(fragmentManager, DialogIconMessage.TAG);
        fragmentManager.popBackStack();
        Log.d(TAG, "dismiss: is called");
    }

    private void updateRequisition() {

        String date = getDate();
        List<String> quantityList = new ArrayList<>();
        List<String> productList = new ArrayList<>();
        List<String> orderIDList = new ArrayList<>();
        List<String> unitIdList = new ArrayList<>();

        for(Product product : productCart){
            quantityList.add(String.valueOf(product.getQuantity()));
            productList.add(product.getId().toString());
            orderIDList.add(product.getOrderId());
            if (product.getUnitId() == null) {
                unitIdList.add(null);
            } else {
                unitIdList.add(product.getUnitId().toString());
            }

        }
        
        PostOrder postOrder = new PostOrder(
                shopId,
                date,
                quantityList,
                productList,
                unitIdList,
                orderIDList
        );
        
        presenter.updateRequisition(postOrder, orderID);

    }

    void takePermission(){
        dialogConfirmation = new DialogConfirmation(
                (v -> updateRequisition()),
                getString(R.string.text_update_order_message)
        );

        dialogConfirmation.show(fragmentManager, "Update");
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp      A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public int convertDpToPx(Context context, int dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }


}