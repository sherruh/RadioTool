package com.example.radiotestapp.test_result;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.radiotestapp.R;
import com.example.radiotestapp.utils.Logger;

import java.text.DecimalFormat;

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

    private TextView textRsrp;
    private TextView textRscp;
    private TextView textRxLvl;
    private TextView textSnr;
    private TextView textEcN0;
    private TextView textCi;
    private TextView textLteCqi;
    private TextView textRsrq;
    private TextView textUmtsCqi;

    private TextView textFirstCell;
    private TextView textSecondCell;
    private TextView textThirdCell;
    private TextView textFirstTech;
    private TextView textSecondTech;
    private TextView textThirdTech;
    private TextView textFirstTacLAc;
    private TextView textSecondTacLAc;
    private TextView textThirdTacLAc;
    private TextView textFirstENodeB;
    private TextView textSecondENodeB;
    private TextView textThirdENodeB;
    private TextView textFirstCid;
    private TextView textSecondCid;
    private TextView textThirdCid;

    private TextView textBufferTime;
    private TextView textBufferThroughput;
    private TextView textBufferSR;
    private TextView textInitTime;
    private TextView textInitSR;
    private TextView textYoutubeSR;
    private TextView textYoutube144;
    private TextView textYoutube240;
    private TextView textYoutube360;
    private TextView textYoutube480;
    private TextView textYoutube720;
    private TextView textYoutube1080;

    private TextView textDownThrput;
    private TextView textDownSR;
    private TextView textUploadSR;
    private TextView textUploadThrput;

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
        viewModel.rsrpLiveData.observe(this, d ->  setDoubleValue(textRsrp, d));
        viewModel.rscpLiveData.observe(this, d ->  setDoubleValue(textRscp, d));
        viewModel.rxLevelLiveData.observe(this, d ->  setDoubleValue(textRxLvl, d));
        viewModel.snrLiveData.observe(this, d ->  setDoubleValue(textSnr, d));
        viewModel.ecN0LiveData.observe(this, d ->  setDoubleValue(textEcN0, d));
        viewModel.ciLiveData.observe(this, d ->  setDoubleValue(textCi, d));
        viewModel.cqiLteLiveData.observe(this, d ->  setDoubleValue(textLteCqi, d));
        viewModel.rsrqLiveData.observe(this, d ->  setDoubleValue(textRsrq, d));
        viewModel.cqiUmtsLiveData.observe(this, d ->  setDoubleValue(textUmtsCqi, d));
        viewModel.firstRateLiveData.observe(this, d ->  {
            if (d > 0.0) textFirstCell.setText("1st cell " + d.toString() + "%:");
        });
        viewModel.secondRateLiveData.observe(this, d ->  {
            if (d > 0.0) textSecondCell.setText("1st cell " + d.toString() + "%:");
        });
        viewModel.thirdRateLiveData.observe(this, d ->  {
            if (d > 0.0) textThirdCell.setText("1st cell " + d.toString() + "%:");
        });
        viewModel.firstTech.observe(this, s -> textFirstTech.setText(s));
        viewModel.secondTech.observe(this, s -> textSecondTech.setText(s));
        viewModel.thirdTech.observe(this, s -> textThirdTech.setText(s));
        viewModel.firstTacLac.observe(this, s -> textFirstTacLAc.setText(s));
        viewModel.secondTacLac.observe(this, s -> textSecondTacLAc.setText(s));
        viewModel.thirdTacLac.observe(this, s -> textThirdTacLAc.setText(s));
        viewModel.firstENodeB.observe(this, s -> textFirstENodeB.setText(s));
        viewModel.secondENodeB.observe(this, s -> textSecondENodeB.setText(s));
        viewModel.thirdENodeB.observe(this, s -> textThirdENodeB.setText(s));
        viewModel.firstCID.observe(this, s -> textFirstCid.setText(s));
        viewModel.secondCID.observe(this, s -> textSecondCid.setText(s));
        viewModel.thirdCID.observe(this, s -> textThirdCid.setText(s));

        viewModel.bufferingTimeLiveData.observe(this, d -> setDoubleValue(textBufferTime,d));
        viewModel.bufferingThroughputLiveData.observe(this, d -> setDoubleValue(textBufferThroughput,d));
        viewModel.bufferingSuccessRateLiveData.observe(this, d -> setDoubleValue(textBufferSR,d));
        viewModel.initTimeLiveData.observe(this, d -> setDoubleValue(textInitTime,d));
        viewModel.initSuccessRateLiveData.observe(this, d -> setDoubleValue(textInitSR,d));
        viewModel.youtubeSuccessRateLiveData.observe(this, d -> setDoubleValue(textYoutubeSR,d));

        viewModel.youtubeResolutionList.observe(this, l -> {
            setDoubleValue(textYoutube144,l.get(0));
            setDoubleValue(textYoutube240,l.get(1));
            setDoubleValue(textYoutube360,l.get(2));
            setDoubleValue(textYoutube480,l.get(3));
            setDoubleValue(textYoutube720,l.get(4));
            setDoubleValue(textYoutube1080,l.get(5));
        });

        viewModel.downThrputLiveData.observe(this, l -> setDoubleValue(textDownThrput, (double) l / 1024));
        viewModel.uploadThrputLiveData.observe(this, l -> setDoubleValue(textUploadThrput, (double) l / 1024));
        viewModel.downSRLiveData.observe(this, l -> setDoubleValue(textDownSR, (double) l / 1024));
        viewModel.uploadSRLiveData.observe(this, l -> setDoubleValue(textUploadSR, (double) l / 1024));

    }

    private void setDoubleValue(TextView tv, Double d){
        if (d != 0.0){
            tv.setText(new DecimalFormat("##.0").format(d));
        }
    }

    private void initViews() {
        textRsrp = findViewById(R.id.text_rsrp_value_result_activity);
        textRscp = findViewById(R.id.text_rscp_value_result_activity);
        textRxLvl = findViewById(R.id.text_rx_level_value_result_activity);
        textSnr = findViewById(R.id.text_snr_value_result_activity);
        textEcN0 = findViewById(R.id.text_ecn0_value_result_activity);
        textCi = findViewById(R.id.text_ci_value_result_activity);
        textLteCqi = findViewById(R.id.text_cqi_lte_value_result_activity);
        textRsrq = findViewById(R.id.text_rsrq_value_result_activity);
        textUmtsCqi = findViewById(R.id.text_cqi_umts_value_result_activity);

        textFirstCell = findViewById(R.id.text_first_cell_activity_result);
        textSecondCell = findViewById(R.id.text_second_cell_activity_result);
        textThirdCell = findViewById(R.id.text_third_cell_activity_result);
        textFirstTech = findViewById(R.id.text_first_cell_tech_value_result_activity);
        textSecondTech = findViewById(R.id.text_second_cell_tech_value_result_activity);
        textThirdTech = findViewById(R.id.text_third_cell_tech_value_result_activity);
        textFirstTacLAc = findViewById(R.id.text_first_cell_tac_lac_value_result_activity);
        textSecondTacLAc = findViewById(R.id.text_second_cell_tac_lac_value_result_activity);
        textThirdTacLAc = findViewById(R.id.text_third_cell_tac_lac_value_result_activity);
        textFirstENodeB = findViewById(R.id.text_first_cell_enodeb_value_result_activity);
        textSecondENodeB = findViewById(R.id.text_second_cell_enodeb_value_result_activity);
        textThirdENodeB = findViewById(R.id.text_third_cell_enodeb_value_result_activity);
        textFirstCid = findViewById(R.id.text_first_cell_cid_value_activity_result);
        textSecondCid = findViewById(R.id.text_second_cell_cid_value_activity_result);
        textThirdCid = findViewById(R.id.text_third_cell_cid_value_activity_result);

        textBufferTime = findViewById(R.id.text_buffering_time_value_result_activity);
        textBufferThroughput = findViewById(R.id.text_bufferin_throughput_value_result_activity);
        textBufferSR = findViewById(R.id.text_buffering_sr_value_result_activity);
        textInitTime = findViewById(R.id.text_init_time_value_result_activity);
        textInitSR = findViewById(R.id.text_init_sr_value_result_activity);
        textYoutubeSR = findViewById(R.id.text_youtube_sr_value_result_activity);
        textYoutube144 = findViewById(R.id.text_144p_value_result_activity);
        textYoutube240 = findViewById(R.id.text_240p_value_result_activity);
        textYoutube360 = findViewById(R.id.text_360p_value_result_activity);
        textYoutube480 = findViewById(R.id.text_480p_value_result_activity);
        textYoutube720 = findViewById(R.id.text_720p_value_result_activity);
        textYoutube1080 = findViewById(R.id.text_1080p_value_result_activity);

        textDownSR = findViewById(R.id.text_download_sr_value_result_activity);
        textUploadSR = findViewById(R.id.text_upload_sr_value_result_activity);
        textDownThrput = findViewById(R.id.text_download_thr_value_result_activity);
        textUploadThrput = findViewById(R.id.text_upload_thr_value_result_activity);
    }

    private void getExtras() {
        isTestedYoutube = getIntent().getBooleanExtra(IS_TESTED_YOUTUBE_EXTRA, false);
        isTestedDownload = getIntent().getBooleanExtra(IS_TESTED_DOWNLOAD_EXTRA, false);
        isTestedUpload = getIntent().getBooleanExtra(IS_TESTED_UPLOAD_EXTRA, false);
        logId = getIntent().getStringExtra(LOG_ID);
        Logger.d("TestResultData " +logId + " " + isTestedYoutube + " " + isTestedDownload + " " + isTestedUpload);
    }
}