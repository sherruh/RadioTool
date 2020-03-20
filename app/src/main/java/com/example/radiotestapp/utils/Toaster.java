package com.example.radiotestapp.utils;
import android.content.Context;
import android.widget.Toast;

public class Toaster {

    public static void showLong(Context context, String message){
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }

    public static void showShort(Context context, String message){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }
}
