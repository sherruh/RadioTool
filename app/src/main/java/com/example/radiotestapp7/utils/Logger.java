package com.example.radiotestapp7.utils;

import android.util.Log;

public class Logger {

  private static boolean isShow = true;
  private static final String TAG = "ololo";
  public static void d(String message){
    if (Logger.isShow) Log.d(Logger.TAG,message);
  }
}
