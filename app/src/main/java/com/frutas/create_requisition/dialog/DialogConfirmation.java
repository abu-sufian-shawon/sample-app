package com.frutas.create_requisition.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.frutas.R;

public class DialogConfirmation extends DialogFragment {
    public static final String TAG = "DialogConfirmation";

    private final OnClickListener onConfirmListener;
    private final String message;

    public DialogConfirmation(View.OnClickListener onConfirmListener, String message){
        this.onConfirmListener = onConfirmListener;
        this.message = message;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        requireDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.dialog_confirmation, container, false);

        view.findViewById(R.id.tvConfirmRemove).setOnClickListener(onConfirmListener);

        view.findViewById(R.id.tvCancelRemove).setOnClickListener(v-> this.dismiss());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvMessage = view.findViewById(R.id.tvMessage);
        tvMessage.setText(message);
    }
}
