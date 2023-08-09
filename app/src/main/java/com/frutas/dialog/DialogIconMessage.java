package com.frutas.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.frutas.R;

import java.util.Objects;

public class DialogIconMessage extends DialogFragment {
    public static final String TAG = "DialogIconMessage";

    public static final int Error = 0;
    public static final int Success = 1;
    public static final int Warning = 3;

    private final int type;
    private final String message;

    private ImageView ivIcon;
    private TextView tvMessage;

    public DialogIconMessage(int type, String message) {
        this.type = type;
        this.message = message;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set Style of dialog
        setStyle(DialogFragment.STYLE_NORMAL, com.frutas.R.style.DialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // transparent background
        Objects.requireNonNull(getDialog()).getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // attack view layout to dialog
        return inflater.inflate(com.frutas.R.layout.dialog_icon_message, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ivIcon = view.findViewById(R.id.ivIcon);
        tvMessage = view.findViewById(R.id.tvMessage);

        switch (type) {
            case Error:
                error();
                break;
            case Success:
                success();
                break;
            case Warning:
                warning();
        }

        view.findViewById(R.id.ivCloseNoInternet).setOnClickListener(v->{
            dismiss();
        });

    }

    private void error() {
        ivIcon.setImageDrawable(ContextCompat.getDrawable(requireContext(),
                R.drawable.ic_baseline_error_outline_24));
        ivIcon.setColorFilter(Color.RED);
        tvMessage.setBackgroundColor(Color.RED);
        tvMessage.setText(message);
    }

    private void success() {
        ivIcon.setImageDrawable(ContextCompat.getDrawable(requireContext(),
                R.drawable.ic_baseline_check_circle_outline_24));
        ivIcon.setColorFilter(
                ContextCompat.getColor(
                        requireContext(),
                        R.color.primary
                )
        );
        tvMessage.setBackgroundColor(ContextCompat.getColor(
                requireContext(),
                R.color.primary
        ));
        tvMessage.setText(message);
    }

    private void warning() {
        ivIcon.setImageDrawable(ContextCompat.getDrawable(requireContext(),
                R.drawable.ic_baseline_warning_24));
        ivIcon.setColorFilter(Color.rgb(237, 135, 45));
        tvMessage.setBackgroundColor(Color.rgb(237, 135, 45));
        tvMessage.setText(message);
    }

}
