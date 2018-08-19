package com.example.keshl.nytreader;

import android.content.Context;
import android.net.ConnectivityManager;

public class NetworkUtil {

    public static boolean isNetworkAvailable(Context context) {
        if(context!=null){
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            return (cm.getActiveNetworkInfo() != null
                    && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected());
        }
        return false;
    }
}
