package com.frutas.create_requisition;

import com.frutas.create_requisition.datamodel.ProductHolder;
import com.frutas.create_requisition.datamodel.UnitList;
import com.frutas.db.entity.CartEntity;

public interface ListProductContract {
    interface Model {
        void prepareData();

        void getAllProduct(OnFinishedListener onFinishedListener);
        void getAllUnits(OnFinishedListener onFinishedListener);

    }

    interface Presenter {
        void getProductData();
    }

    interface ListProductView {
        void setDataToView(ProductHolder productHolder);
        void setUnitToView(UnitList unit);
        void onAddCartClicked(int position, CartEntity cartEntity);
        void onProductChosen(CartEntity cartEntity);

        void showLoading();
        void hideLoading();

        Integer getUnitIdByName(String name);
    }

    interface OnFinishedListener {
        void onSuccessGetProduct(ProductHolder productHolder);
        void onFailureGetProduct(Throwable t);

        void onSuccessGetUnits(UnitList unitList);
        void onFailureGetUnits(Throwable t);
    }
}
