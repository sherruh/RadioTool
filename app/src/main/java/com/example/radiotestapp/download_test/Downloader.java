package com.example.radiotestapp.download_test;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;

public class Downloader {

    private Context mContext;

    public void download(String url, Context context ) {
        mContext = context;
        DownloadManager manager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        long reference = manager.enqueue(request);
    }

    public interface DownloadListener{
        void onFailure(String message);
    }
}
