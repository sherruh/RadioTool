package com.example.radiotestapp.test_result;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.radiotestapp.R;
import com.example.radiotestapp.utils.Logger;

public class TestResultActivity extends AppCompatActivity {

    private static final String IS_TESTED_YOUTUBE_EXTRA = "IS_TESTED_YOUTUBE_EXTRA";
    private static final String IS_TESTED_DOWNLOAD_EXTRA = "IS_TESTED_DOWNLOAD_EXTRA";
    private static final String IS_TESTED_UPLOAD_EXTRA = "IS_TESTED_UPLOAD_EXTRA";
    private static final String LOG_ID = "LOG_ID";

    public static void startActivity(boolean isTestedYoutube, boolean isTestedDownload,
                                     boolean isTestedUpload, String logId, Context context){
        Intent intent = new Intent(context, TestResultActivity.class);
        intent.putExtra(IS_TESTED_YOUTUBE_EXTRA, isTestedYoutube);
        intent.putExtra(IS_TESTED_DOWNLOAD_EXTRA, isTestedDownload);
        intent.putExtra(IS_TESTED_UPLOAD_EXTRA, isTestedUpload);
        intent.putExtra(LOG_ID, logId);
        context.startActivity(intent);
    }

    private TestResultViewModel viewModel;
    private boolean isTestedYoutube;
    private boolean isTestedDownload;
    private boolean isTestedUpload;
    private String logId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_result);
        getExtras();
        initViews();
        initViewModel();
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(TestResultViewModel.class);
        viewModel.getLogsAndEventsByLogId(logId, isTestedYoutube, isTestedDownload, isTestedUpload);
    }

    private void initViews() {
    }

    private void getExtras() {
        isTestedYoutube = getIntent().getBooleanExtra(IS_TESTED_YOUTUBE_EXTRA, false);
        isTestedDownload = getIntent().getBooleanExtra(IS_TESTED_DOWNLOAD_EXTRA, false);
        isTestedUpload = getIntent().getBooleanExtra(IS_TESTED_UPLOAD_EXTRA, false);
        logId = getIntent().getStringExtra(LOG_ID);
        Logger.d("TestResultData " +logId + " " + isTestedYoutube + " " + isTestedDownload + " " + isTestedUpload);
    }
}