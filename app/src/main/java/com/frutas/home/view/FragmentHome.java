package com.frutas.home.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.frutas.R;
import com.frutas.contact_us.view.FragmentContactUs;
import com.frutas.create_requisition.view.FragmentListProduct;
import com.frutas.create_requisition.view.FragmentRequisition;
import com.frutas.dialog.DialogNoInternet;
import com.frutas.network.ConnectionDetector;
import com.frutas.previous_order.view.FragmentPreviousOrder;
import com.frutas.track_order.view.FragmentTrackOrder;

import java.util.Objects;

public class FragmentHome extends Fragment {
    private DialogNoInternet dialogNoInternet;

    private FragmentListProduct fragmentListProduct;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        // init view components
        CardView cvCreateRequisition = view.findViewById(R.id.cvCreateRequisition);
        CardView cvTrackOrder = view.findViewById(R.id.cvTrackOrder);
        CardView cvPreviousOrder = view.findViewById(R.id.cvPreviousOrder);

        dialogNoInternet = new DialogNoInternet();

        fragmentListProduct = new FragmentListProduct();


        // implement onclick listener
        cvCreateRequisition.setOnClickListener(v -> {
            if (ConnectionDetector.isNetAvailable(requireContext())) {
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, fragmentListProduct,
                                FragmentRequisition.class.getSimpleName())
                        .addToBackStack(null)
                        .commit();
            } else {
                dialogNoInternet.show(requireActivity().getSupportFragmentManager(), DialogNoInternet.class.getSimpleName());
            }

        });

        cvTrackOrder.setOnClickListener(v -> {
            if (ConnectionDetector.isNetAvailable(requireContext())) {
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, new FragmentTrackOrder(),
                                FragmentTrackOrder.class.getSimpleName())
                        .addToBackStack(null)
                        .commit();
            } else {
                dialogNoInternet.show(requireActivity().getSupportFragmentManager(),
                        DialogNoInternet.class.getSimpleName());
            }

        });

        cvPreviousOrder.setOnClickListener(v -> {
            if (ConnectionDetector.isNetAvailable(requireContext())) {
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.dialog_fragment_in, R.anim.dialog_fragment_out)
                        .replace(R.id.fragmentContainer, new FragmentPreviousOrder(),
                                FragmentPreviousOrder.class.getSimpleName())
                        .addToBackStack(null)
                        .commit();
            } else {
                dialogNoInternet.show(requireActivity().getSupportFragmentManager(), DialogNoInternet.class.getSimpleName());
            }
        });

        view.findViewById(R.id.llContactUs).setOnClickListener(v -> {
            if (ConnectionDetector.isNetAvailable(requireContext())) {
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, new FragmentContactUs(),
                                FragmentContactUs.class.getSimpleName())
                        .addToBackStack(null)
                        .commit();
            } else {
                dialogNoInternet.show(requireActivity().getSupportFragmentManager(),
                        DialogNoInternet.class.getSimpleName());
            }
        });

    }

}