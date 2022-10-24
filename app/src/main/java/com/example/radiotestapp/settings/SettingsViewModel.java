package com.example.radiotestapp.settings;

import static com.example.radiotestapp.core.Constants.DEFAULT_DOWNLOAD_URL;
import static com.example.radiotestapp.core.Constants.DEFAULT_UPLOAD_URL;
import static com.example.radiotestapp.core.Constants.DOWNLOAD_URL;
import static com.example.radiotestapp.core.Constants.IS_NURTEL;
import static com.example.radiotestapp.core.Constants.IS_YOUTUBE_NEED;
import static com.example.radiotestapp.core.Constants.NO;
import static com.example.radiotestapp.core.Constants.UPLOAD_URL;
import static com.example.radiotestapp.core.Constants.YES;
import static com.example.radiotestapp.core.Constants.YOUTUBE_DEFAULT_URL;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.radiotestapp.App;
import com.example.radiotestapp.core.Constants;
import com.example.radiotestapp.model.SettingsParameter;
import com.example.radiotestapp.utils.SingleLiveEvent;

public class SettingsViewModel extends ViewModel {

    public MutableLiveData<Boolean> isYouTubeNeedLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> isYouTubeDefaultUrlLiveData = new MutableLiveData<>();
    public MutableLiveData<String> youtubeUrlLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> isDownloadNeedLiveData = new MutableLiveData<>();
    public MutableLiveData<String> downloadUrlLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> isUploadNeedLiveData = new MutableLiveData<>();
    public MutableLiveData<String> uploadUrlLiveData = new MutableLiveData<>();
    public SingleLiveEvent<Void> settingsSavedEvent = new SingleLiveEvent<>();
    public MutableLiveData<Integer> initTImeOutLiveDataLiveData = new MutableLiveData<>();
    public MutableLiveData<Integer> bufferTImeOutLiveData = new MutableLiveData<>();
    public MutableLiveData<Integer> downloadDurationLiveData = new MutableLiveData<>();
    public MutableLiveData<Integer> uploadDurationLiveData = new MutableLiveData<>();

    public void start() {
        SettingsParameter settingsIsYoutubeNeed = App.localStorage.getSettingsParameter(Constants.IS_YOUTUBE_NEED);
        if (settingsIsYoutubeNeed == null) {
            isYouTubeNeedLiveData.setValue(false);
            isYouTubeDefaultUrlLiveData.setValue(false);
            App.localStorage.saveSettingsParameter(new SettingsParameter(IS_YOUTUBE_NEED,NO));
        } else {
            if (settingsIsYoutubeNeed.getValue().equals(Constants.YES)) isYouTubeNeedLiveData.setValue(true);
            if (settingsIsYoutubeNeed.getValue().equals(Constants.NO)) isYouTubeNeedLiveData.setValue(false);
        }

        SettingsParameter settingsIsYoutubeDefaultUrl = App.localStorage
                .getSettingsParameter(Constants.IS_YOUTUBE_DEFAULT);
        if (settingsIsYoutubeDefaultUrl == null){
            isYouTubeDefaultUrlLiveData.setValue(true);
        } else {
            if (settingsIsYoutubeDefaultUrl.getValue().equals(Constants.YES)){
                isYouTubeDefaultUrlLiveData.setValue(true);
            }
            if (settingsIsYoutubeDefaultUrl.getValue().equals(Constants.NO)){
                isYouTubeDefaultUrlLiveData.setValue(false);
            }
        }

        SettingsParameter settingsYoutubeUrl = App.localStorage.getSettingsParameter(Constants.YOUTUBE_URL);
        if (settingsYoutubeUrl != null) youtubeUrlLiveData.setValue(settingsYoutubeUrl.getValue());

        SettingsParameter settingsIsDownloadNeed = App.localStorage.getSettingsParameter(Constants.IS_DOWNLOAD_NEED);
        if (settingsIsDownloadNeed != null){
            if (settingsIsDownloadNeed.getValue().equals( Constants.YES)) isDownloadNeedLiveData.setValue(true);
            else isDownloadNeedLiveData.setValue(false);
        } else isDownloadNeedLiveData.setValue(false);

        SettingsParameter settingsDownloadUrl = App.localStorage.getSettingsParameter(Constants.DOWNLOAD_URL);
        if (settingsDownloadUrl != null) downloadUrlLiveData.setValue(settingsDownloadUrl.getValue());
        else {
            downloadUrlLiveData.setValue(DEFAULT_DOWNLOAD_URL);
            App.localStorage.saveSettingsParameter(new SettingsParameter(Constants.DOWNLOAD_URL,DEFAULT_DOWNLOAD_URL));
        }

        SettingsParameter settingsIsUploadNeed = App.localStorage.getSettingsParameter(Constants.IS_UPLOAD_NEED);
        if (settingsIsUploadNeed != null){
            if (settingsIsUploadNeed.getValue().equals(Constants.YES)) isUploadNeedLiveData.setValue(true);
            else isUploadNeedLiveData.setValue(false);
        } else isUploadNeedLiveData.setValue(false);

        SettingsParameter settingsUploadUrl = App.localStorage.getSettingsParameter(Constants.UPLOAD_URL);
        if (settingsUploadUrl != null) uploadUrlLiveData.setValue(settingsUploadUrl.getValue());
        else {
            uploadUrlLiveData.setValue(DEFAULT_UPLOAD_URL);
            App.localStorage.saveSettingsParameter(new SettingsParameter(Constants.UPLOAD_URL,DEFAULT_UPLOAD_URL));
        }

        SettingsParameter settingsInitTimeOut = App.localStorage.getSettingsParameter(Constants.INIT_TIMEOUT);
        if (settingsInitTimeOut == null) initTImeOutLiveDataLiveData.setValue(90);
        else {
            int i = Integer.parseInt(settingsInitTimeOut.getValue());
            initTImeOutLiveDataLiveData.setValue(i);
        }

        SettingsParameter settingsBufferTimeOut = App.localStorage.getSettingsParameter(Constants.BUFFER_TIMEOUT);
        if (settingsBufferTimeOut == null) bufferTImeOutLiveData.setValue(90);
        else {
            int i = Integer.parseInt(settingsBufferTimeOut.getValue());
            bufferTImeOutLiveData.setValue(i);
        }

        SettingsParameter settingDownloadDuration = App.localStorage.getSettingsParameter(Constants.DOWNLOAD_DURATION);
        if (settingDownloadDuration == null) downloadDurationLiveData.setValue(60);
        else {
            int i = Integer.parseInt(settingDownloadDuration.getValue());
            downloadDurationLiveData.setValue(i);
        }

        SettingsParameter settingUploadDuration = App.localStorage.getSettingsParameter(Constants.UPLOAD_DURATION);
        if (settingUploadDuration == null) uploadDurationLiveData.setValue(60);
        else {
            int i = Integer.parseInt(settingUploadDuration.getValue());
            uploadDurationLiveData.setValue(i);
        }
    }

    public void saveSettings(boolean checkedIsYoutubeNeed, String youtubeUrl,
                             boolean checkedIsYoutubeDefault, boolean checkedIsDownloadNeed,
                             String downloadUrl, boolean checkedIsUploadNeed, String uploadUrl) {
        String s;
        if (checkedIsYoutubeNeed) s = Constants.YES;
        else s = Constants.NO;
        App.localStorage.saveSettingsParameter(new SettingsParameter(Constants.IS_YOUTUBE_NEED,s));
        App.localStorage.saveSettingsParameter(new SettingsParameter(Constants.YOUTUBE_URL,youtubeUrl));
        if (checkedIsYoutubeDefault) s = Constants.YES;
        else s = Constants.NO;
        App.localStorage.saveSettingsParameter(new SettingsParameter(Constants.IS_YOUTUBE_DEFAULT,s));
        if (checkedIsDownloadNeed) s = Constants.YES;
        else s = Constants.NO;
        App.localStorage.saveSettingsParameter(new SettingsParameter(Constants.IS_DOWNLOAD_NEED,s));
        App.localStorage.saveSettingsParameter(new SettingsParameter(Constants.DOWNLOAD_URL,downloadUrl));
        if (checkedIsUploadNeed) s = Constants.YES;
        else s = Constants.NO;
        App.localStorage.saveSettingsParameter(new SettingsParameter(Constants.IS_UPLOAD_NEED,s));
        App.localStorage.saveSettingsParameter(new SettingsParameter(Constants.UPLOAD_URL,uploadUrl));
    }

    public void saveTimeSettings(String initTimeOut, String bufferTimeOut, String downloadDuration,
                                 String uploadDuration) {
        App.localStorage.saveSettingsParameter(new SettingsParameter(Constants.INIT_TIMEOUT, initTimeOut));
        App.localStorage.saveSettingsParameter(new SettingsParameter(Constants.BUFFER_TIMEOUT, bufferTimeOut));
        App.localStorage.saveSettingsParameter(new SettingsParameter(Constants.DOWNLOAD_DURATION, downloadDuration));
        App.localStorage.saveSettingsParameter(new SettingsParameter(Constants.UPLOAD_DURATION, uploadDuration));
        settingsSavedEvent.call();
    }

    public void saveSettingsNurtel() {
        App.localStorage.saveSettingsParameter(new SettingsParameter(IS_NURTEL, YES));
        saveSettings(true,YOUTUBE_DEFAULT_URL,true,
                true,DOWNLOAD_URL,true,UPLOAD_URL);
        saveTimeSettings("30","30","30","30");
    }
}
