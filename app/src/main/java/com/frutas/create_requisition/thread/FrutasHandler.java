package com.frutas.create_requisition.thread;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

public class FrutasHandler extends Handler {

    public FrutasHandler(Looper looper) {
        super(looper);

        Log.d("ROOM", "FrutasHandler: is called");
    }

    @Override
    public void handleMessage(@NonNull Message msg) {

    }
}
