package com.example.radiotestapp.settings;

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

    public void start() {
        SettingsParameter settingsIsYoutubeNeed = App.localStorage.getSettingsParameter(Constants.IS_YOUTUBE_NEED);
        if (settingsIsYoutubeNeed == null) {
            isYouTubeNeedLiveData.setValue(true);
            isYouTubeDefaultUrlLiveData.setValue(true);
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

        SettingsParameter settingsIsUploadNeed = App.localStorage.getSettingsParameter(Constants.IS_UPLOAD_NEED);
        if (settingsIsUploadNeed != null){
            if (settingsIsUploadNeed.getValue().equals(Constants.YES)) isUploadNeedLiveData.setValue(true);
            else isUploadNeedLiveData.setValue(false);
        } else isUploadNeedLiveData.setValue(false);

        SettingsParameter settingsUploadUrl = App.localStorage.getSettingsParameter(Constants.UPLOAD_URL);
        if (settingsUploadUrl != null) uploadUrlLiveData.setValue(settingsUploadUrl.getValue());
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
        settingsSavedEvent.call();
    }
}
