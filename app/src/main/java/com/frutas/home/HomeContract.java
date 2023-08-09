package com.frutas.home;

import com.frutas.create_requisition.datamodel.Product;

public interface HomeContract {
    interface Model{
        interface OnFinishedListener{
            void onFinished(OnFinishedListener onFinishedListener);
            void onFailure(Throwable t);
        }
    }

    interface View{
        void setDataToView();
        void onResponseFailure(Throwable t);
    }

    interface ListProductView{
    }

    interface Presenter{
        void requestForData();
    }
}
