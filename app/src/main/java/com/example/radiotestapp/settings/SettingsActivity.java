package com.example.radiotestapp.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.radiotestapp.R;

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
    private Button buttonSave;
    private Button buttonCancel;

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
    }

    private void initViews() {
        checkYoutubeNeed = findViewById(R.id.check_youtube_activity_settings);
        checkYoutubeDefault = findViewById(R.id.check_use_default_youtube_activity_settings);
        checkDownloadNeed = findViewById(R.id.check_download_activity_settings);
        checkUploadNeed = findViewById(R.id.check_upload_activity_settings);
        editYoutubeVideoId = findViewById(R.id.edit_youtube_url_activity_settings);
        editDownloadUrl = findViewById(R.id.edit_download_start_url);
        editUploadUrl = findViewById(R.id.edit_upload_start_url);
        buttonSave = findViewById(R.id.button_save_activity_settings);
        buttonCancel = findViewById(R.id.button_cancel_activity_settings);
    }

}