package com.appspace.appspacelibrary.util;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by siwaweswongcharoen on 3/28/2016 AD.
 */
public class InternetUtil {

    public static boolean isInternetAvailable(Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null
                && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
