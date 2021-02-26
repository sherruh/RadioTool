package com.example.radiotestapp.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter {
    public static String shortDate(Date date){
        String pattern = "ddMMyyyy_HHmmss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String dateStr = simpleDateFormat.format(date);
        return dateStr;
    }

    public static String dateWithMillis(long date){
        String pattern = "dd-MM-yyyy_HH:mm:ss.SSS";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String dateStr = simpleDateFormat.format(new Date(date));
        return dateStr;
    }

}
