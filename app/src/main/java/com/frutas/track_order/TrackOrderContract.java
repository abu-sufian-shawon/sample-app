package com.frutas.track_order;

import com.frutas.create_requisition.datamodel.ProductHolder;
import com.frutas.previous_order.data_model.AllOrder;

public interface TrackOrderContract {
    interface Model{
        void requestOrdersDataToServer(OnFinishedListener onFinishedListener, String shopId);
        void getShopById(OnFinishedListener onFinishedListener, String shopId);
        void requestAllProductList(OnFinishedListener onFinishedListener);
    }

    interface View{
        void setOrdersDataToView(AllOrder all);
        void onResponseFailure(Throwable t);
        void onResponseShopId(boolean response);
        void onSuccessProductList(ProductHolder productHolder);
        void onFailureProductList(Throwable t);

    }

    interface Presenter{
        void sendOrderDataRequestToPresenter(String shopId);
        void checkShopId(String shopId);
        void getAllProductList();
    }

    interface OnFinishedListener{
        void onFinished(AllOrder allOrder);
        void onFailure(Throwable t);
        void onResponseShopId(boolean response);
        void onFinishedProductList(ProductHolder productHolder);
        void onFailureProductList(Throwable t);
    }
}
