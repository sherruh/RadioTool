package com.example.radiotestapp.utils;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;

import static android.content.Context.DOWNLOAD_SERVICE;

public class DownloadManagerDisabler {

    public static void disableAllDownloadings(Context context){
        DownloadManager.Query query = new DownloadManager.Query();
        DownloadManager dm = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
        Cursor c = dm.query(query);
        while(c.moveToNext()) {
            dm.remove(c.getLong(c.getColumnIndex(DownloadManager.COLUMN_ID)));

        }
    }
}
