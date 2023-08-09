package com.frutas.track_order.presenter;

import com.frutas.create_requisition.datamodel.ProductHolder;
import com.frutas.previous_order.data_model.AllOrder;
import com.frutas.track_order.TrackOrderContract;
import com.frutas.track_order.model.TrackOrderModel;

public class TrackOrderPresenter implements TrackOrderContract.Presenter,
        TrackOrderContract.OnFinishedListener {
    private final TrackOrderContract.View trackOrderView;
    private final TrackOrderContract.Model trackOrderModel;

    public TrackOrderPresenter(TrackOrderContract.View trackOrderView) {
        this.trackOrderView = trackOrderView;
        this.trackOrderModel = new TrackOrderModel();
    }

    @Override
    public void onFinished(AllOrder allOrder) {
        trackOrderView.setOrdersDataToView(allOrder);
    }

    @Override
    public void onFailure(Throwable t) {
        trackOrderView.onResponseFailure(t);
    }

    @Override
    public void onResponseShopId(boolean response) {
        trackOrderView.onResponseShopId(response);
    }

    @Override
    public void onFinishedProductList(ProductHolder productHolder) {
        trackOrderView.onSuccessProductList(productHolder);
    }

    @Override
    public void onFailureProductList(Throwable t) {
        trackOrderView.onFailureProductList(t);
    }

    @Override
    public void sendOrderDataRequestToPresenter(String shopId) {
        trackOrderModel.requestOrdersDataToServer(this, shopId);
    }

    @Override
    public void checkShopId(String shopId) {
        trackOrderModel.getShopById(this, shopId);
    }

    @Override
    public void getAllProductList() {
        trackOrderModel.requestAllProductList(this);
    }
}
