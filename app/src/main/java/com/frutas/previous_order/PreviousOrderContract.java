package com.frutas.previous_order;

import androidx.annotation.NonNull;

import com.frutas.create_requisition.datamodel.ProductHolder;
import com.frutas.dialog.DialogIconMessage;
import com.frutas.previous_order.data_model.AllOrder;

public interface PreviousOrderContract {
    interface Model{
        void requestAllOrderListToServer(OnFinishedListener onFinishedListener, String shopID);

        void requestAllProductList(OnFinishedListener onFinishedListener);

        void checkShopId(String shopID, OnFinishedListener onFinishedListener);
    }

    interface OnFinishedListener{
        void onSuccessAllOrder(AllOrder allOrder);
        void onFailureAllOrder(Throwable t);
        void onFinishedProductList(ProductHolder productHolder);
        void onFailureProductList(Throwable t);
        void onShopValid(String shopID);
        void onShopInvalid();

    }

    interface View{
        void setAllOrderToView(AllOrder allOrder);
        void onRetrievedProductList(ProductHolder productHolder);

        void showLoading();
        void hideLoading();

        void showShopIdToView(String shopID);
        void saveShopIDOnDevice(String shopID);

        void showMessageDialog(String Message, int type);
    }

    interface Presenter{
        void requestAllOrderListToModel(@NonNull String shopID);
        void getAllProductList();
        void checkShopID(String shopID);
    }
}
