package com.example.radiotestapp.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.radiotestapp.R;
import com.example.radiotestapp.core.Constants;
import com.example.radiotestapp.test_result.TestResultActivity;
import com.example.radiotestapp.utils.Toaster;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {

    public static void startActivity(Context context){
        context.startActivity(new Intent(context,SettingsActivity.class));
    }

    private CheckBox checkYoutubeNeed;
    private CheckBox checkYoutubeDefault;
    private CheckBox checkDownloadNeed;
    private CheckBox checkUploadNeed;
    private EditText editYoutubeVideoId;
    private EditText editDownloadUrl;
    private EditText editUploadUrl;
    private EditText editLogId;
    private EditText editInitTimeout;
    private EditText editBufferTimeout;
    private EditText editDownDuration;
    private EditText editUploadDuration;
    private Button buttonSave;
    private Button buttonCancel;
    private Button buttonShowLog;
    private Spinner spinnerYoutubeQuality;

    private SettingsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initViews();
        initViewModel();
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        viewModel.start();
        viewModel.isYouTubeNeedLiveData.observe(this, b -> {
            if (b) {
                checkYoutubeNeed.setChecked(true);
                checkYoutubeDefault.setEnabled(true);
                editYoutubeVideoId.setEnabled(true);
            }
            else {
                checkYoutubeNeed.setChecked(false);
                checkYoutubeDefault.setEnabled(false);
                editYoutubeVideoId.setEnabled(false);
            }
        });
        viewModel.isYouTubeDefaultUrlLiveData.observe(this,b ->{
            if (b){
                checkYoutubeDefault.setChecked(true);
                editYoutubeVideoId.setEnabled(false);
            } else {
                checkYoutubeDefault.setChecked(false);
                editYoutubeVideoId.setEnabled(true);
            }
        });
        viewModel.youtubeUrlLiveData.observe(this, s -> {
            if (s .length() > 1) editYoutubeVideoId.setText(s);

        });
        viewModel.isDownloadNeedLiveData.observe(this, b ->{
            checkDownloadNeed.setChecked(b);
        });
        viewModel.downloadUrlLiveData.observe(this, s -> {
            if (s.length() > 1) editDownloadUrl.setText(s);
        });
        viewModel.isUploadNeedLiveData.observe(this, b -> {
            checkUploadNeed.setChecked(b);
        });
        viewModel.uploadUrlLiveData.observe(this, s-> {
            if (s.length() > 1) editUploadUrl.setText(s);
        });
        viewModel.youtubeQualityLiveData.observe(this,s -> {
            switch (s){
                case Constants.YOUTUBE_QUALITY_AUTO:
                    spinnerYoutubeQuality.setSelection(0);
                    break;
                case Constants.YOUTUBE_QUALITY_144p:
                    spinnerYoutubeQuality.setSelection(1);
                    break;
                case Constants.YOUTUBE_QUALITY_240p:
                    spinnerYoutubeQuality.setSelection(2);
                    break;
                case Constants.YOUTUBE_QUALITY_360p:
                    spinnerYoutubeQuality.setSelection(3);
                    break;
                case Constants.YOUTUBE_QUALITY_480p:
                    spinnerYoutubeQuality.setSelection(4);
                    break;
                case Constants.YOUTUBE_QUALITY_720p:
                    spinnerYoutubeQuality.setSelection(5);
                    break;
                case Constants.YOUTUBE_QUALITY_1080p:
                    spinnerYoutubeQuality.setSelection(6);
                    break;
            }
        });
        viewModel.initTImeOutLiveDataLiveData.observe(this, i -> editInitTimeout.setText(i.toString()));
        viewModel.bufferTImeOutLiveData.observe(this, i -> editBufferTimeout.setText(i.toString()));
        viewModel.downloadDurationLiveData.observe(this, i -> editDownDuration.setText(i.toString()));
        viewModel.uploadDurationLiveData.observe(this, i -> editUploadDuration.setText(i.toString()));
        viewModel.settingsSavedEvent.observe(this, v ->{finish();});
    }

    private void initViews() {
        checkYoutubeNeed = findViewById(R.id.check_youtube_activity_settings);
        checkYoutubeNeed.setOnClickListener(l -> {
            checkYoutubeDefault.setEnabled(checkYoutubeNeed.isChecked());
            editYoutubeVideoId.setEnabled(checkYoutubeNeed.isChecked());
        });
        checkYoutubeDefault = findViewById(R.id.check_use_default_youtube_activity_settings);
        checkYoutubeDefault.setOnClickListener(l -> {
            editYoutubeVideoId.setEnabled(!checkYoutubeDefault.isChecked());
        });
        checkDownloadNeed = findViewById(R.id.check_download_activity_settings);
        checkUploadNeed = findViewById(R.id.check_upload_activity_settings);
        editYoutubeVideoId = findViewById(R.id.edit_youtube_url_activity_settings);
        editDownloadUrl = findViewById(R.id.edit_download_start_url);
        editUploadUrl = findViewById(R.id.edit_upload_start_url);
        buttonSave = findViewById(R.id.button_save_activity_settings);
        buttonSave.setOnClickListener( l -> { saveSettings(); });
        buttonCancel = findViewById(R.id.button_cancel_activity_settings);
        buttonCancel.setOnClickListener( l -> { finish(); });
        editLogId = findViewById(R.id.edit_log_activity_settings);
        editInitTimeout = findViewById(R.id.edit_init_time_out_activity_settings);
        editBufferTimeout = findViewById(R.id.edit_buffer_time_out_activity_settings);
        editDownDuration = findViewById(R.id.edit_download_duration_time_activity_settings);
        editUploadDuration = findViewById(R.id.edit_upload_duration_time_activity_settings);
        buttonShowLog = findViewById(R.id.button_show_activity_settings);
        buttonShowLog.setOnClickListener( l -> {
            TestResultActivity.startActivity(true,true,true,
                    editLogId.getText().toString(),this);
        });
        initSpinner();
    }

    private void initSpinner() {
        spinnerYoutubeQuality = findViewById(R.id.spinner_youtube_quality_activity_settings);
        ArrayList<String> spinnerItems =  new ArrayList<>();
        spinnerItems.add(Constants.YOUTUBE_QUALITY_AUTO);
        spinnerItems.add(Constants.YOUTUBE_QUALITY_144p);
        spinnerItems.add(Constants.YOUTUBE_QUALITY_240p);
        spinnerItems.add(Constants.YOUTUBE_QUALITY_360p);
        spinnerItems.add(Constants.YOUTUBE_QUALITY_480p);
        spinnerItems.add(Constants.YOUTUBE_QUALITY_720p);
        spinnerItems.add(Constants.YOUTUBE_QUALITY_1080p);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item,spinnerItems);
        spinnerYoutubeQuality.setAdapter(arrayAdapter);
    }

    private void saveSettings() {
        if ( checkYoutubeNeed.isChecked() && !checkYoutubeDefault.isChecked()
                && editYoutubeVideoId.getText().toString().isEmpty()){
            Toaster.showLong(this,"Fill Youtube url");
            return;
        }
        if ( checkDownloadNeed.isChecked() && editDownloadUrl.getText().toString().isEmpty()){
            Toaster.showLong(this,"Fill Download url");
            return;
        }
        if ( checkUploadNeed.isChecked() && editUploadUrl.getText().toString().isEmpty()){
            Toaster.showLong(this,"Fill Upload url");
            return;
        }
        if (editInitTimeout.getText().toString() == null || editInitTimeout.getText().toString().equals("")
                || editInitTimeout.getText().toString().equals("0")){
            Toaster.showLong(this,"Fill init timeout");
            return;
        }
        if (editBufferTimeout.getText().toString() == null || editBufferTimeout.getText().toString().equals("")
                || editBufferTimeout.getText().toString().equals("0")){
            Toaster.showLong(this,"Fill buffer timeout");
            return;
        }
        if (editDownDuration.getText().toString() == null || editDownDuration.getText().toString().equals("")
                || editDownDuration.getText().toString().equals("0")){
            Toaster.showLong(this,"Fill download duration time");
            return;
        }
        if (editUploadDuration.getText().toString() == null || editUploadDuration.getText().toString().equals("")
                || editUploadDuration.getText().toString().equals("0")){
            Toaster.showLong(this,"Fill upload duration time");
            return;
        }

        try {
            int i = Integer.parseInt(editInitTimeout.getText().toString());
            if ( i == 0 ){
                Toaster.showLong(this,"Fill init timeout");
                return;
            }
        } catch (Exception e){
            Toaster.showLong(this,"Fill init timeout");
            return;
        }

        try {
            int i = Integer.parseInt(editBufferTimeout.getText().toString());
            if ( i == 0 ){
                Toaster.showLong(this,"Fill buffer timeout");
                return;
            }
        } catch (Exception e){
            Toaster.showLong(this,"Fill buffer timeout");
            return;
        }

        try {
            int i = Integer.parseInt(editDownDuration.getText().toString());
            if ( i == 0 ){
                Toaster.showLong(this,"Fill download duration time");
                return;
            }
        } catch (Exception e){
            Toaster.showLong(this,"Fill download duration time");
            return;
        }

        try {
            int i = Integer.parseInt(editUploadDuration.getText().toString());
            if ( i == 0 ){
                Toaster.showLong(this,"Fill upload duration time");
                return;
            }
        } catch (Exception e){
            Toaster.showLong(this,"Fill upload duration time");
            return;
        }

        viewModel.saveSettings(true,editYoutubeVideoId.getText().toString()
                ,checkYoutubeDefault.isChecked(),checkDownloadNeed.isChecked()
                ,editDownloadUrl.getText().toString(),checkUploadNeed.isChecked()
                ,editUploadUrl.getText().toString(), spinnerYoutubeQuality.getSelectedItem().toString());

        viewModel.saveTimeSettings(editInitTimeout.getText().toString(), editBufferTimeout.getText().toString(),
                editDownDuration.getText().toString(),editUploadDuration.getText().toString());
    }

}