package com.frutas.update_order;

import com.frutas.create_requisition.datamodel.Product;
import com.frutas.create_requisition.datamodel.ProductHolder;
import com.frutas.create_requisition.datamodel.UnitList;
import com.frutas.create_requisition.post_data_model.PostOrder;
import com.frutas.create_requisition.post_data_model.PostOrderResponse;

public interface UpdateContract {
    interface Model{

        void postUpdateRequisition(OnFinishedListener onFinishedListener,
                                   PostOrder postOrder, String orderID);
        void getUnitList(OnFinishedListener onFinishedListener);

        void getOrderItem(OnFinishedListener onFinishedListener);
    }

    interface View{
        void setUnitList(UnitList unitList);
        void showLoading();
        void hideLoading();

        void onEditProductClicked(Product product, int position);
        void onUpdateProductConfirmed(int position);

        void dismiss();

        Integer getUnitIdByName(int position);

        void onAddToCartClicked(Product product);
        void onProductChosen(Product product);
        void onRemoveClicked(Product product);
    }

    interface Presenter{
        void getUnitList();
        void updateRequisition(PostOrder postOrder, String orderID);
    }

    interface OnFinishedListener{
        void onSuccessUnitList(UnitList unitList);
        void onFailureUnitList(Throwable t);

        void onSuccessUpdateOrder(PostOrderResponse response);
        void onFailureUpdateOrder(Throwable t);
    }
}
