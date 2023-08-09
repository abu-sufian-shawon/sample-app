package com.frutas.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;

import androidx.annotation.NonNull;

public class ConnectionDetector {

    /**
     * connectivityManager.getAllNetworkInfo() method is deprecated
     * from API Level 23, because This method does not support multiple
     * connected networks of the same type.
     *
     * Alongside getActiveNetwork() is available from API Level 23
     *
     * @param context required for get ConnectivityManager instance
     *                from system.
     * @return boolean value. if any kind pf network is active, it
     * return true otherwise return false
     */
    public static synchronized boolean isNetAvailable(@NonNull Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Network activeNetwork = connectivityManager.getActiveNetwork();
                return activeNetwork != null;
            } else {
                NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
                for (NetworkInfo info : networkInfos) {
                    if (info.isConnected()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
