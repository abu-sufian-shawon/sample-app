package com.frutas.create_requisition.thread;

import android.os.Handler;
import android.os.Looper;

// Singleton
public class FrutasLooper extends Thread {
    private static FrutasLooper instance = null;

    private FrutasLooper() {
        // default constructor is private
    }

    public static synchronized FrutasLooper getInstance() {
        if (instance == null) {
            instance = new FrutasLooper();
            instance.start();
        }
        return instance;
    }
    public Handler handler;
    public Looper looper;

    @Override
    public void run() {

        if (Looper.myLooper() == null) Looper.prepare();

        looper = Looper.myLooper();

        handler = new Handler(looper);

        Looper.loop();
    }
}
