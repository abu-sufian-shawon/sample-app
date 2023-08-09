package com.frutas.contact_us;

import com.frutas.contact_us.data_model.ContactHolder;

public interface ContactUsContract {
    interface Model {
        interface OnFinishedListener {
            void onFinished(ContactHolder contactHolder);

            void onFailure(Throwable t);
        }

        void requestContactInfoToServer(OnFinishedListener onFinishedListener);
    }

    interface View {
        void setContactDataToView(ContactHolder contactHolder);
        void onResponseFailure(Throwable t);
    }

    interface Presenter{
        void getContactInfo();
    }
}
