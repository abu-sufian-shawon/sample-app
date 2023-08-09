package com.frutas.contact_us.presenter;

import com.frutas.contact_us.ContactUsContract;
import com.frutas.contact_us.data_model.ContactHolder;
import com.frutas.contact_us.model.ContactModel;

public class ContactPresenter implements ContactUsContract.Presenter,
        ContactUsContract.Model.OnFinishedListener {
    private final ContactUsContract.Model contactModel;
    private final ContactUsContract.View contactView;

    public ContactPresenter(ContactUsContract.View contactView) {
        this.contactView = contactView;
        this.contactModel = new ContactModel();
    }

    @Override
    public void getContactInfo() {
        contactModel.requestContactInfoToServer(this);
    }

    @Override
    public void onFinished(ContactHolder contactHolder) {
        contactView.setContactDataToView(contactHolder);
    }

    @Override
    public void onFailure(Throwable t) {
        contactView.onResponseFailure(t);
    }
}
