package com.example.radiotestapp.download_test;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;

import com.example.radiotestapp.utils.Logger;

import java.util.Timer;
import java.util.TimerTask;

public class Downloader {

    private final int DOWNLOAD_TIMEOUT = 10000;
    private final int DOWNLOAD_DURATION_TIMEOUT = 30000;
    private Context mContext;
    private DownloadListener downloadListener;
    private Timer timerForDuration;
    private DownloadManager manager;
    private String url;
    private BroadcastReceiver downloadReceiver;
    private boolean isNeedStart = true;

    public void download(String url, Context context, DownloadListener downloadListener ) {
        if (downloadReceiver != null) mContext.unregisterReceiver(downloadReceiver);
        mContext = context;
        this.downloadListener = downloadListener;
        manager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        this.url = url;
        if (!url.contains("http://") && !url.contains("https://")) {
            downloadListener.onFailure("Error url");
            isNeedStart = false;
            return;
        }
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
        long downloadId = manager.enqueue(request);
        Cursor cursor = manager.query(new DownloadManager.Query().setFilterById(downloadId));

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                int status = -1;

                DownloadManager.Query q = new DownloadManager.Query();
                q.setFilterById(downloadId);
                Cursor c = manager.query(q);
                int downloadedBytes = 0;
                if (c.moveToFirst()) {
                   downloadedBytes = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                   Logger.d("DownloadedBytes " + downloadedBytes);
                }
                if (downloadedBytes == 0) {
                    if (timerForDuration != null) timerForDuration.cancel();
                    downloadListener.onFailure("Time out");
                    Logger.d("DownloadManager " + "Download failed " + downloadId + status);
                    isNeedStart = false;
                    timer.cancel();
                } else {
                    startTimerForDownloading();
                    registrOnCompleteReceiver(downloadId);
                }
                timer.cancel();
            }
        }, DOWNLOAD_TIMEOUT);
    }

    private void startTimerForDownloading() {
        if (timerForDuration != null) return;
        timerForDuration = new Timer();
        timerForDuration.schedule(new TimerTask() {
            @Override
            public void run() {
                downloadListener.onComplete();
                isNeedStart = false;
                timerForDuration.cancel();
            }
        }, DOWNLOAD_DURATION_TIMEOUT);
    }

    private void registrOnCompleteReceiver(long downloadId ){
        Logger.d("DownloadManager " + "Downloaded test started " + downloadId);
        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        downloadReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (downloadId == -1)
                    return;

                Cursor cursor = manager.query(new DownloadManager.Query().setFilterById(downloadId));
                if (cursor.moveToFirst()) {
                    int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                    if(status == DownloadManager.STATUS_SUCCESSFUL){
                        Logger.d("DownloadManager " + "Downloaded test file " + downloadId);
                        if (isNeedStart)download(url,mContext,downloadListener);
                    }
                    else {
                        // download is cancelled
                    }
                }
                else {
                    // download is cancelled
                }
            }
        };

        mContext.registerReceiver(downloadReceiver, filter);
    }

    public void stopDownload(){
        if (timerForDuration != null) timerForDuration.cancel();
        isNeedStart = false;
    }


    public interface DownloadListener{
        void onFailure(String message);
        void onComplete();
    }

}
