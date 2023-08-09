package com.frutas.create_requisition;

import com.frutas.create_requisition.datamodel.ShopHolder;
import com.frutas.create_requisition.datamodel.UnitList;
import com.frutas.create_requisition.post_data_model.PostOrder;
import com.frutas.create_requisition.post_data_model.PostOrderResponse;
import com.frutas.db.entity.CartEntity;

import java.util.List;

public interface RequisitionContract {

    interface Presenter{
        void getProduct();
        void checkShopID(String shopID);
        void removeProduct(CartEntity cartEntity);
        void onConfirmPlaceOrder(List<CartEntity> cartEntityList);
    }

    interface View {
        void setDataToView(List<CartEntity> cartEntityList);
        void onShopValidation(boolean isValid);

        void onRemoveIconClicked(CartEntity cartEntity);

        PostOrder receiveDataToPlaceOrder(List<CartEntity> cartEntityList);

        void onRemoveConfirmed();

        void onEditIconClicked(CartEntity cartEntity, int position);

        void onEditConfirmed(CartEntity updateValue, int position);

        Integer getUnitIdByName(String name);

        void clearCart();
        void saveShopID(String shopID);


    }

    interface Model {
        void getAllCartProduct(OnFinishedListener onFinishedListener);
        void getShop(String shopID, OnFinishedListener onFinishedListener);

        void removeProductByID(int cartID, OnFinishedListener onFinishedListener);

        void placeOrder(OnFinishedListener onFinishedListener, PostOrder postOrder);
    }

    interface OnFinishedListener{
        void onSuccessGetCart(List<CartEntity> cartEntityList);
        void onFailureGetCart(Throwable t);

        void onSuccessGetShop(ShopHolder allShop);
        void onFailureGetShop(Throwable t);

        void onSuccessRemoveCart();
        void onFailureRemoveCart();

        void onSuccessPlaceOrder(PostOrderResponse response);
        void onFailurePlaceOrder(Throwable t);
    }
}
