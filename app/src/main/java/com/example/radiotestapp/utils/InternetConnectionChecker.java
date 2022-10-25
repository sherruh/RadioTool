package com.example.radiotestapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;

public class InternetConnectionChecker {
     public static boolean isNetworkConnected(Context context) {
         ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
         return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
     }
}
