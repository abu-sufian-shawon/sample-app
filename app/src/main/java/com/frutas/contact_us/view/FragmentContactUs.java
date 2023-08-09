package com.frutas.contact_us.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.frutas.R;
import com.frutas.contact_us.ContactUsContract;
import com.frutas.contact_us.data_model.Contact;
import com.frutas.contact_us.data_model.ContactHolder;
import com.frutas.contact_us.presenter.ContactPresenter;
import com.frutas.dialog.DialogIconMessage;
import com.frutas.dialog.DialogLoading;
import com.frutas.dialog.DialogNoInternet;
import com.frutas.network.ConnectionDetector;

import java.util.Objects;

public class FragmentContactUs extends Fragment implements ContactUsContract.View {
    private DialogLoading dialogLoading;
    private FragmentManager fragmentManager;
    private TextView tvEmail, tvMobile, tvAddress;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact_us, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvEmail = view.findViewById(R.id.tvEmail);
        tvMobile = view.findViewById(R.id.tvMobile);
        tvAddress = view.findViewById(R.id.tvAddress);

        fragmentManager = getFragmentManager();

        ContactUsContract.Presenter contactPresenter = new ContactPresenter(this);

        ConnectionDetector connectionDetector = new ConnectionDetector();
        dialogLoading = new DialogLoading();
        DialogNoInternet dialogNoInternet = new DialogNoInternet();

        if (connectionDetector.isNetAvailable(Objects.requireNonNull(getContext()))) {
            dialogLoading.show(fragmentManager, DialogLoading.class.getSimpleName());
            contactPresenter.getContactInfo();
        } else {
            dialogNoInternet.show(fragmentManager, DialogNoInternet.class.getSimpleName());
        }
    }

    @Override
    public void setContactDataToView(ContactHolder contactHolder) {
        if (dialogLoading.isVisible()) dialogLoading.dismiss();

        if (contactHolder != null) {
            Contact contact = contactHolder.getContact();
            tvEmail.setText(contact.getEmail());
            tvMobile.setText(contact.getMobile());
            tvAddress.setText(contact.getAddress());
        } else {
            DialogIconMessage dialogIconMessage = new DialogIconMessage(DialogIconMessage.Error, "Error 1200");
            dialogIconMessage.show(fragmentManager, DialogIconMessage.class.getSimpleName());
        }
    }

    @Override
    public void onResponseFailure(Throwable t) {
        DialogIconMessage dialogIconMessage = new DialogIconMessage(DialogIconMessage.Error, t.getMessage());
        dialogIconMessage.show(fragmentManager, DialogIconMessage.class.getSimpleName());
    }
}