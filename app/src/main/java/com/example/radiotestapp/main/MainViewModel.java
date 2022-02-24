package com.example.radiotestapp.main;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.TrafficStats;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfo;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.radiotestapp.App;
import com.example.radiotestapp.core.Constants;
import com.example.radiotestapp.download_test.Downloader;
import com.example.radiotestapp.enums.EEvents;
import com.example.radiotestapp.enums.EState;
import com.example.radiotestapp.enums.EYoutubeState;
import com.example.radiotestapp.main.radio.CustomPhoneStateListener;
import com.example.radiotestapp.main.thread.LoggerRunnable;
import com.example.radiotestapp.main.thread.RadioParamsUpdateRunnable;
import com.example.radiotestapp.model.Event;
import com.example.radiotestapp.model.Log;
import com.example.radiotestapp.model.SettingsParameter;
import com.example.radiotestapp.repository.Callback;
import com.example.radiotestapp.services.GettingLocationService;
import com.example.radiotestapp.test_result.TestResultActivity;
import com.example.radiotestapp.upload_test.Uploader;
import com.example.radiotestapp.utils.DateConverter;
import com.example.radiotestapp.utils.DownloadManagerDisabler;
import com.example.radiotestapp.utils.Logger;
import com.example.radiotestapp.utils.SingleLiveEvent;
import com.example.radiotestapp.utils.Toaster;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;

public class MainViewModel extends ViewModel implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    public SingleLiveEvent<Void> isPermissionNotGranted = new SingleLiveEvent<>();
    public SingleLiveEvent<Void> youtubeErrorEvent = new SingleLiveEvent<>();
    public SingleLiveEvent<String> uploadErrorEvent = new SingleLiveEvent<>();
    public SingleLiveEvent<Void> onStartYoutubeClickedEvent = new SingleLiveEvent<>();
    public SingleLiveEvent<Void> loggingStoppedEvent = new SingleLiveEvent<>();
    public SingleLiveEvent<Void> exitClickEvent = new SingleLiveEvent<>();
    public SingleLiveEvent<Void> updateLevelListEvent = App.logRepository.updateLevelListEvent;
    public SingleLiveEvent<Void> updateUploadThroughputListEvent = App.logRepository.updateUploadThroughputListEvent;
    public SingleLiveEvent<Void> updateDownloadThroughputListEvent = App.logRepository.updateDownloadThroughputListEvent;
    public SingleLiveEvent<Void> downloadTestStartEvent = new SingleLiveEvent<>();
    public SingleLiveEvent<String> downloadTestFailedEvent = new SingleLiveEvent<>();
    public SingleLiveEvent<Void> downloadTestStopEvent = new SingleLiveEvent<>();
    public SingleLiveEvent<Void> uploadTestStartEvent = new SingleLiveEvent<>();
    public SingleLiveEvent<Void> uploadTestStopEvent = new SingleLiveEvent<>();
    public SingleLiveEvent<String> logSavedEvent = new SingleLiveEvent<>();
    public MutableLiveData<Boolean> isLogging = new MutableLiveData<>();
    public MutableLiveData<Long> youtubeThroughputLiveData = App.logRepository.youtubeThroughputLiveData;
    public MutableLiveData<String> mccLiveData = App.logRepository.mccLiveData;
    public MutableLiveData<String> mncLiveData = App.logRepository.mncLiveData;
    public MutableLiveData<String> techLiveData = App.logRepository.techLiveData;
    public MutableLiveData<String> tacLiveData = App.logRepository.tacLiveData;
    public MutableLiveData<String> eNodeBLiveData = App.logRepository.eNodeBLiveData;
    public MutableLiveData<String> cidLiveData = App.logRepository.cidLiveData;
    public MutableLiveData<String> channelLiveData = App.logRepository.channelLiveData;
    public MutableLiveData<String> pciPscBsicLiveData = App.logRepository.pciPscBsicLiveData;
    public MutableLiveData<String> levelLiveData = App.logRepository.levelLiveData;
    public MutableLiveData<String> rsrqEcNoLiveData = App.logRepository.rsrqEcNoLiveData;
    public MutableLiveData<String> snrLiveData = App.logRepository.snrLiveData;
    public MutableLiveData<String> cqiLiveData = App.logRepository.cqiLiveData;
    public MutableLiveData<Boolean> isProgressStartBarShowLiveData = new MutableLiveData<>();
    public MutableLiveData<Long> initTimeLiveData = new MutableLiveData<>();
    public MutableLiveData<Long> bufferingTimeLiveData = new MutableLiveData<>();
    public MutableLiveData<String> youtubeResolutionLiveData = App.logRepository.youtubeResolutionLiveData;

    private Context mContext;
    private TelephonyManager telephonyManager;
    private GsmCellLocation gsmCellLocation;
    private CellInfoLte cellInfoLte;
    private CustomPhoneStateListener customPhoneStateListenerSignalStrength;
    private CustomPhoneStateListener customPhoneStateListenerCellLocation;
    private String signalLevel;
    private String logId;
    private Uploader uploader;
    private Downloader downloader;
    private LoggerRunnable logger;
    private RadioParamsUpdateRunnable radioParamsUpdateRunnable;
    private Thread threadForLog;
    private Thread threadForRadioParamsUpdate;
    private List<Log> logs = new ArrayList<>();
    private List<Event> eventLogs = new ArrayList<>();
    private Log currentLog;
    private String plmn;
    private GoogleApiClient googleApiClient;
    private LocationRequest geoLocationRequest;
    private Location geoLocation;
    private long startInitYoutubeTime;
    private long finishInitYoutubeTime;
    private long startBufferingTime;
    private long startBufferingKbits;
    private long finishBufferingTime;
    private long finishBufferingKbits;
    private long finishYoutubeVideoLoadingTime;
    private long youtubeInitTimeOut = 90000L;
    private long youtubeBufferTimeOut = 90000L;
    private long uploadDuration = 30000L;
    private int countOfRepeats = 1;
    private int timerDelay = 1;
    private boolean isNeverEnding = false;
    public MutableLiveData<Integer> currentNumberOfRepeatsLiveData = new MutableLiveData<>();
    public MutableLiveData<Integer> initialNumberOfRepeatsLiveData = new MutableLiveData<>();
    private int initialNumberOfRepeats = 1;
    private EYoutubeState youtubeState;
    private boolean isNeedYoutubeTest = true;
    private boolean isNeedDownloadTest = false;
    private boolean isNeedUploadTest = false;
    private boolean isNeedPingTest = true;
    Timer timerYouTubeBuffering = new Timer();
    Timer timerYouTubeInitial = new Timer();
    Timer timerUpload = new Timer();
    Timer timerDelayBeforeRestart = new Timer();


    public void onViewCreated(Context context, CustomPhoneStateListener.OnSignalStrengthChangedListener onSignalStrengthChangedListener,
                              CustomPhoneStateListener.OnCellLocationChangeListener onCellLocationChangeListener) {
        mContext = context;
        telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        customPhoneStateListenerSignalStrength = CustomPhoneStateListener.start(onSignalStrengthChangedListener, null,
                onCellLocationChangeListener);
        customPhoneStateListenerCellLocation = CustomPhoneStateListener.start(onSignalStrengthChangedListener, null,
                onCellLocationChangeListener);
        telephonyManager.listen(customPhoneStateListenerSignalStrength, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
        telephonyManager.listen(customPhoneStateListenerCellLocation, PhoneStateListener.LISTEN_CELL_LOCATION);
        initService();
        startThreadForRadioParamsUpdating();
        createFileForUpload();
        App.localStorage.saveSettingsParameter(new SettingsParameter(Constants.IS_ALLOWED_SAVING_LOGS,Constants.YES));
    }


    private void createFileForUpload() {
        File folder = new File(Environment.getExternalStorageDirectory(), Constants.LOG_FOLDER);
        if (!folder.exists()){
            folder.mkdir();
        }
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(folder.getPath() + "/" + "1.bin","rw");
            randomAccessFile.setLength(1024*1024*50);
            randomAccessFile.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startThreadForRadioParamsUpdating() {
        radioParamsUpdateRunnable = new RadioParamsUpdateRunnable();
        threadForRadioParamsUpdate = new Thread(radioParamsUpdateRunnable);
        threadForRadioParamsUpdate.start();
    }

    public void start(String mSignalStrength, int value, int numberPickerDelayValue, boolean checkedNeverEnding) {
        countOfRepeats = value;
        initialNumberOfRepeats = value;
        timerDelay = numberPickerDelayValue;
        isNeverEnding = checkedNeverEnding;
        currentNumberOfRepeatsLiveData.setValue(countOfRepeats);
        currentLog = new Log();
        Date date = new Date(System.currentTimeMillis());
        logId = DateConverter.shortDate(date);
        App.logRepository.createLogFile(logId);
        currentLog.setLogId(logId);
        eventLogs.clear();
        logs.clear();
        logger = new LoggerRunnable(logs, currentLog, mContext);
        threadForLog = new Thread(logger);
        threadForLog.start();
        isLogging.setValue(true);
        checkWhetherToStartYoutubePlayback();
    }

    public void stop() {
        isProgressStartBarShowLiveData.postValue(true);
        initialNumberOfRepeatsLiveData.postValue(initialNumberOfRepeats);
        if(timerUpload != null) timerUpload.cancel();
        if(timerYouTubeBuffering != null) timerYouTubeBuffering.cancel();
        if(timerYouTubeInitial != null) timerYouTubeInitial.cancel();
        if(timerDelayBeforeRestart != null) timerDelayBeforeRestart.cancel();
        if (uploader != null) {
            uploader.setUploadAgain(false);
            uploader.cancelUpload();
        }
        if (downloader != null) downloader.stopDownload();
        logger.stopLog();
        try {
            threadForLog.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        isLogging.postValue(false);
        App.logRepository.setYoutubeResolution("");
        App.logRepository.setLogState(EState.IDLE);
        App.logRepository.closeLogFile(new Callback<List<Long>>() {
            @Override
            public void onSuccess(List<Long> longs) {
                isProgressStartBarShowLiveData.postValue(false);
                Logger.d("LocalStorage1 logs " + longs);
                Logger.d("TestResultData " + App.localStorage.getLogById((long) (longs.get(longs.size() - 1))).getLogId());
                String logId = App.localStorage.getLogById((long) (longs.get(longs.size() - 1))).getLogId();
                TestResultActivity.startActivity(isNeedYoutubeTest,isNeedDownloadTest,isNeedUploadTest,logId,mContext);
            }

            @Override
            public void onFailure(String s) {
                isProgressStartBarShowLiveData.postValue(false);
            }
        });
        SettingsParameter settingsParameter = App.localStorage.getSettingsParameter(Constants.IS_ALLOWED_SAVING_LOGS);
        if (settingsParameter != null && settingsParameter.getValue().equals(Constants.YES)){
            logSavedEvent.postValue("Log saved: " + Constants.LOG_FOLDER + "/" + logId + ".txt");
        }
        loggingStoppedEvent.call();
        DownloadManagerDisabler.disableAllDownloadings(mContext);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void radioStateChanged(String mSignalStrength, CellLocation mCellLocation) {
        gsmCellLocation = (GsmCellLocation) getCellLocation();
        updateSignalData(getSignalStrength());
        Logger.d("TestResultData cells " + getSignalStrength());
        plmn = getPlmn();
        App.logRepository.setPlmn(plmn);
        if (ActivityCompat.checkSelfPermission(mContext, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (telephonyManager.getAllCellInfo().size() > 0) updateCellIdentityParams();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void updateCellIdentityParams() {
        if (ActivityCompat.checkSelfPermission(mContext, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        CellInfo ci = telephonyManager.getAllCellInfo().get(0);
        if (ci instanceof CellInfoGsm)
        {
            CellIdentityGsm cellIdentityGsm = ((CellInfoGsm)ci).getCellIdentity();
            App.logRepository.setCellId(String.valueOf(cellIdentityGsm.getCid()));
            App.logRepository.setGsmCellInfo(cellIdentityGsm);
        }
        else if (ci instanceof CellInfoWcdma)
        {
            CellIdentityWcdma cellIdentityWcdma =( (CellInfoWcdma)ci).getCellIdentity();
            App.logRepository.setCellId(String.valueOf((cellIdentityWcdma.getCid() - 786432)));
            App.logRepository.setWcdmaCellInfo(cellIdentityWcdma);
        }
        else if (ci instanceof CellInfoLte)
        {
            CellIdentityLte cellIdentityLte = ((CellInfoLte)ci).getCellIdentity();
            String eNodeB = "";
            String cellId = "";
            try{
                int longCid = cellIdentityLte.getCi();
                String cellidHex = Integer.toHexString(longCid);
                String eNBHex = cellidHex.substring(0, cellidHex.length() - 2);
                String cellHex = cellidHex.substring(cellidHex.length() - 2);
                eNodeB = String.valueOf(Integer.parseInt(eNBHex, 16));
                cellId = String.valueOf(Integer.parseInt(cellHex, 16));
            }catch (Exception ignored){}
            App.logRepository.setEnodeB(eNodeB);
            App.logRepository.setCellId(cellId);
            App.logRepository.setLteCellInfo(cellIdentityLte);
        }
    }


    private void updateSignalData(String signalStrength) {
        Logger.d(signalStrength);
        App.logRepository.clearSignalData();
        if (signalStrength.equals("")) return;
        int networkType = telephonyManager.getNetworkType();
        Logger.d("TestResultData cells " + networkType);
        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:{
                updateGsmSignalData(signalStrength);
                break;
            }
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:{
                updateWcdmaSignalData(signalStrength);
                break;
            }
            case TelephonyManager.NETWORK_TYPE_LTE:{
                updateLteSignalData(signalStrength);
                break;
            }
            /*case TelephonyManager.NETWORK_TYPE_NR:
                break;*/
            default:
                break;
        }
    }

    private void updateGsmSignalData(String signalData) {
        int rxIndex = signalData.indexOf("ssi=");
        App.logRepository.setTech("GSM");
        App.logRepository.setRxLevel(getValueOfParameter(signalData,rxIndex));
    }

    private void updateWcdmaSignalData(String signalData) {
        int berIndex = signalData.indexOf("ber=");
        int rscpIndex = signalData.indexOf("rscp=");
        int ecnoIndex = signalData.indexOf("ecno=");
        App.logRepository.setTech("WCDMA");
        App.logRepository.setBer(getValueOfParameter(signalData,berIndex));
        App.logRepository.setRscp(getValueOfParameter(signalData,rscpIndex));
        App.logRepository.setEcno(getValueOfParameter(signalData,ecnoIndex));
    }

    private void updateLteSignalData(String signalData) {
        int rsrpIndex = signalData.indexOf("rsrp=");
        int rsrqIndex = signalData.indexOf("rsrq=");
        int rssnrIndex = signalData.indexOf("rssnr=");
        int cqiIndex = signalData.indexOf("cqi=");
        App.logRepository.setTech("LTE");
        App.logRepository.setRsrp(getValueOfParameter(signalData,rsrpIndex));
        App.logRepository.setRsrq(getValueOfParameter(signalData,rsrqIndex));
        String snr = "";
        try{
           Double snrDouble = Double.parseDouble(getValueOfParameter(signalData,rssnrIndex)) / 10.0;
           snr = String.valueOf(snrDouble);
        }catch (Exception ignored){}
        App.logRepository.setRsSnr(snr);
        App.logRepository.setCqi(getValueOfParameter(signalData,cqiIndex));
    }

    private String getValueOfParameter(String s, int startIndex){
        try{
            String s1 = s.substring(startIndex);
            startIndex = s1.indexOf("=") + 1;
            int endIndex = s1.indexOf(" ");
            return s1.substring(startIndex,endIndex);
        } catch (Exception e){
            return "";
        }
    }

    private String getSignalStrength(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            return telephonyManager.getSignalStrength().toString();
        }else return "";
    }

    @SuppressLint({"NewApi"})
    private CellLocation getCellLocation(){
        CellLocation cellLocation;
        if (ContextCompat.checkSelfPermission(mContext,
                ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
            isPermissionNotGranted.call();
        else {
            cellLocation = telephonyManager.getCellLocation();
            if (cellLocation != null) {
                if (this.telephonyManager != null) {
                }
                return cellLocation;
            }
        }return null;
    }

    private String getPlmn(){
        if (telephonyManager == null) return "";
        return telephonyManager.getSimOperator();
    }

    public void startGettingLocation() {
        googleApiClient = new GoogleApiClient.Builder(mContext).
                addApi(LocationServices.API).
                addConnectionCallbacks(this).
                addOnConnectionFailedListener(this)
                .build();
        googleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        geoLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void startLocationUpdates() {
        geoLocationRequest = new LocationRequest();
        geoLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        geoLocationRequest.setInterval(1000);
        geoLocationRequest.setFastestInterval(500);
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, geoLocationRequest, this);
    }

    private void initService() {
        LocalBroadcastManager.getInstance(mContext.getApplicationContext()).registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        String latitude = intent.getStringExtra(GettingLocationService.EXTRA_LATITUDE);
                        String longitude = intent.getStringExtra(GettingLocationService.EXTRA_LONGITUDE);
                        String altitude = intent.getStringExtra(GettingLocationService.EXTRA_ALTITUDE);
                        if (altitude == null || altitude.equals("")) altitude = "0";
                        if (latitude != null && longitude != null) {
                            App.logRepository.setLocation(Double.parseDouble(latitude),Double.parseDouble(longitude),(int)Double.parseDouble(altitude));
                        }
                    }
                }, new IntentFilter(GettingLocationService.ACTION_LOCATION_BROADCAST));


        Intent intent = new Intent(mContext.getApplicationContext(), GettingLocationService.class);
        mContext.getApplicationContext().startService(intent);

    }

    public void youTubePlayerInitializing() {
        App.logRepository.setLogState(EState.YOUTUBE_TEST);
        App.logRepository.setYoutubeState(EYoutubeState.INITIALIZING);
        startInitYoutubeTime = System.currentTimeMillis();
        eventLogs.add(new Event( logId,EEvents.YSI,startInitYoutubeTime,"",EState.YOUTUBE_TEST));
        App.logRepository.saveEvent(eventLogs.get(eventLogs.size() - 1));
        timerYouTubeInitial = new Timer();
        youtubeInitTimeOut = getYoutubeInitTimeOut();
        timerYouTubeInitial.schedule(new TimerTask() {
            @Override
            public void run () {
                eventLogs.add(new Event(logId,EEvents.YEI,System.currentTimeMillis(),"",EState.YOUTUBE_TEST));
                App.logRepository.saveEvent(eventLogs.get(eventLogs.size() - 1));
                App.logRepository.setYoutubeState(EYoutubeState.FINISHED);
                youtubeErrorEvent.call();
                timerYouTubeInitial.cancel();
            }
        }, youtubeInitTimeOut);
    }

    private long getYoutubeInitTimeOut() {
        Long l = 90000L;
        SettingsParameter initTimeOutSettings = App.localStorage.getSettingsParameter(Constants.INIT_TIMEOUT);
        if (initTimeOutSettings != null) {
            try{
                l = Long.parseLong(initTimeOutSettings.getValue()) * 1000L;
            }catch (Exception e){}
        }
        return l;
    }

    public void startBuffering(boolean initialBuffering) {
        youtubeBufferTimeOut = getYoutubeBufferTimeOut();
        timerYouTubeBuffering = new Timer();
        App.logRepository.setYoutubeState(EYoutubeState.BUFFERING);
        timerYouTubeBuffering.schedule(new TimerTask() {
            @Override
            public void run () {
                App.logRepository.setYoutubeState(EYoutubeState.FINISHED);
                eventLogs.add(new Event(logId,EEvents.YEB,System.currentTimeMillis(),"",EState.YOUTUBE_TEST));
                App.logRepository.saveEvent(eventLogs.get(eventLogs.size() - 1));
                youtubeErrorEvent.call();
                timerYouTubeBuffering.cancel();
            }
        }, youtubeBufferTimeOut);
        if (initialBuffering) {
            startBufferingTime = System.currentTimeMillis();
            startBufferingKbits = TrafficStats.getTotalRxBytes() / 1024 * 8;
            eventLogs.add(new Event( logId,EEvents.YSB,startBufferingTime,"",EState.YOUTUBE_TEST));
            App.logRepository.saveEvent(eventLogs.get(eventLogs.size() - 1));
        }
    }

    private long getYoutubeBufferTimeOut() {
        long l = 90000L;
        SettingsParameter bufferTimeOutSettings = App.localStorage.getSettingsParameter(Constants.BUFFER_TIMEOUT);
        if (bufferTimeOutSettings != null) {
            try{
                l = Long.parseLong(bufferTimeOutSettings.getValue()) * 1000L;
            }catch (Exception e){}
        }
        return l;
    }

    public void videoCued() {
        finishInitYoutubeTime = System.currentTimeMillis();
        eventLogs.add(new Event( logId,EEvents.YFI,finishInitYoutubeTime,
                String.valueOf(finishInitYoutubeTime - startInitYoutubeTime),EState.YOUTUBE_TEST));
        App.logRepository.saveEvent(eventLogs.get(eventLogs.size() - 1));
        initTimeLiveData.setValue(finishInitYoutubeTime - startInitYoutubeTime);
        //timerYouTubeBuffering.cancel();
        timerYouTubeInitial.cancel();
    }

    public void startPlayingVideo(boolean initialBuffering) {
        if (initialBuffering) {
            finishBufferingTime = System.currentTimeMillis();
            finishBufferingKbits = TrafficStats.getTotalRxBytes() / 1024 * 8;
            Logger.d("Loaded bits " + String.valueOf(finishBufferingKbits - startBufferingKbits));
            eventLogs.add(new Event( logId,EEvents.YFB,finishBufferingTime,
                    String.valueOf(finishBufferingTime - startBufferingTime),
                    String.valueOf(1000 * (finishBufferingKbits - startBufferingKbits)/((finishBufferingTime - startBufferingTime))),
                    EState.YOUTUBE_TEST));
            App.logRepository.saveEvent(eventLogs.get(eventLogs.size() - 1));
            bufferingTimeLiveData.setValue(finishBufferingTime - startBufferingTime);
        }
        youtubeState = EYoutubeState.PLAYING;
        App.logRepository.setYoutubeState(youtubeState);
        timerYouTubeInitial.cancel();
        timerYouTubeBuffering.cancel();
    }

    public void youtubePlaybackEnded(){
        App.logRepository.setLogState(EState.IDLE);
        App.logRepository.setYoutubeResolution("");
        youtubeState = EYoutubeState.FINISHED;
        App.logRepository.setYoutubeState(youtubeState);
        Logger.d("checkWhetherToStartYoutubePlayback " + "youtubePlaybackEnded");
        isProgressStartBarShowLiveData.postValue(true);
        checkWhetherToStartDownloadTest();
    }

    private void checkWhetherToStartDownloadTest(){
        isNeedDownloadTest = App.localStorage.getSettingsParameter(Constants.IS_DOWNLOAD_NEED) != null &&
                (App.localStorage.getSettingsParameter(Constants.IS_DOWNLOAD_NEED)
                .getValue().equals(Constants.YES));
        isProgressStartBarShowLiveData.postValue(false);
        if (isNeedDownloadTest && isLogging.getValue()){
            downloadTestStart();
        } else {
            isProgressStartBarShowLiveData.postValue(true);
            checkWhetherToStartUploadTest();
        }
    }

    private void checkWhetherToStartUploadTest(){
        isNeedUploadTest = App.localStorage.getSettingsParameter(Constants.IS_UPLOAD_NEED) != null
                && App.localStorage.getSettingsParameter(Constants.IS_UPLOAD_NEED)
                .getValue().equals(Constants.YES);
        isProgressStartBarShowLiveData.postValue(false);
        if (isNeedUploadTest && isLogging.getValue()){
            uploadTestStart();
        } else {
            isProgressStartBarShowLiveData.postValue(true);
            checkWhetherToStartYoutubePlayback();
        }
    }

    private void downloadTestStart() {
        downloadTestStartEvent.call();
        App.logRepository.setLogState(EState.DOWNLOAD_TEST);
        downloader = new Downloader();
        eventLogs.add(new Event( logId,EEvents.DS,System.currentTimeMillis(),
                "",EState.DOWNLOAD_TEST));
        App.logRepository.saveEvent(eventLogs.get(eventLogs.size() - 1));
        downloader.download(getDownloadUrl(), mContext, new Downloader.DownloadListener() {
            @Override
            public void onFailure(String message) {
                if (App.logRepository.getLogState() != EState.DOWNLOAD_TEST) return;
                downloadTestFailedEvent.postValue(message);
                eventLogs.add(new Event( logId,EEvents.DE,System.currentTimeMillis(),
                        "",EState.DOWNLOAD_TEST));
                App.logRepository.saveEvent(eventLogs.get(eventLogs.size() - 1));
                downloadTestEnded();
            }

            @Override
            public void onComplete() {
                eventLogs.add(new Event( logId,EEvents.DF,System.currentTimeMillis(),
                        "",EState.DOWNLOAD_TEST));
                App.logRepository.saveEvent(eventLogs.get(eventLogs.size() - 1));
                downloadTestEnded();
            }
        });

        /*DownloadManagerBroadcastReceiver downloadManagerBroadcastReceiver = new DownloadManagerBroadcastReceiver();
        downloadManagerBroadcastReceiver.registerOnCompleteReceiver(downloadID);
        downloadManagerBroadcastReceiver.registerOnFailedReceiver(downloadID);
        downloadManagerBroadcastReceiver.registerOnFinishedReceiver(downloadID);
        mContext.registerReceiver(downloadManagerBroadcastReceiver.onCompleteReceiver,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
*/

        /*downloadTestEnded();
        uploadTestStart();*/

    }

    private String getDownloadUrl() {
        SettingsParameter settingsParameter = App.localStorage.getSettingsParameter(Constants.DOWNLOAD_URL);
        if (settingsParameter != null) return settingsParameter.getValue();
        return "";
    }

    private void uploadTestStart() {
        uploadTestStartEvent.call();
        uploader = new Uploader();
        App.logRepository.setLogState(EState.UPLOAD_TEST);
        eventLogs.add(new Event( logId,EEvents.US,System.currentTimeMillis(),
                "",EState.UPLOAD_TEST));
        App.logRepository.saveEvent(eventLogs.get(eventLogs.size() - 1));
        uploader.uploadFile(getUploadUrl(), new Uploader.UploadListener() {
            @Override
            public void onFailure(String message) {
                uploader.setUploadAgain(false);
                uploader.cancelUpload();
                if (!message.equals("Socket closed")){
                    uploadErrorEvent.postValue(message);
                    eventLogs.add(new Event( logId,EEvents.UE,System.currentTimeMillis(),
                            "",EState.UPLOAD_TEST));
                    App.logRepository.saveEvent(eventLogs.get(eventLogs.size() - 1));
                    uploadTestEnded();
                }

                return;
            }
        });
        timerUpload = new Timer();

        uploadDuration = getUploadDuration();

        timerUpload.schedule(new TimerTask() {
            @Override
            public void run () {
                uploader.setUploadAgain(false);
                eventLogs.add(new Event( logId,EEvents.UF,System.currentTimeMillis(),
                        "",EState.UPLOAD_TEST));
                App.logRepository.saveEvent(eventLogs.get(eventLogs.size() - 1));
                uploadTestEnded();
                timerUpload.cancel();
            }
        }, uploadDuration);
    }

    private long getUploadDuration() {
        long l = 30000L;
        SettingsParameter uploadDurationSettings = App.localStorage.getSettingsParameter(Constants.UPLOAD_DURATION);
        if (uploadDurationSettings != null) {
            try{
                l = Long.parseLong(uploadDurationSettings.getValue()) * 1000L;
                Logger.d("uploadDuration: " + uploadDuration);
            }catch (Exception e){            }
        }
        return l;
    }

    private String getUploadUrl() {
        SettingsParameter settingsParameter = App.localStorage.getSettingsParameter(Constants.UPLOAD_URL);
        if (settingsParameter != null) return settingsParameter.getValue();
        return "";
    }

    private void downloadTestEnded() {
        App.logRepository.setLogState(EState.IDLE);
        DownloadManagerDisabler.disableAllDownloadings(mContext);
        checkWhetherToStartUploadTest();
    }

    private void uploadTestEnded(){
        if (uploader != null) uploader.cancelUpload();
        timerUpload.cancel();
        uploadTestStopEvent.call();
        App.logRepository.setLogState(EState.IDLE);
        isProgressStartBarShowLiveData.postValue(true);
        checkWhetherToStartYoutubePlayback();
    }


    private void checkWhetherToStartYoutubePlayback() {
        timerDelayBeforeRestart = new Timer();
        timerDelayBeforeRestart.schedule(new TimerTask() {
            @Override
            public void run() {
                timerDelayBeforeRestart = null;
                Logger.d("checkWhetherToStartYoutubePlayback here " + countOfRepeats);
                if (countOfRepeats > 0 && isLogging.getValue()){
                    if (!isNeverEnding){
                        countOfRepeats--;
                        currentNumberOfRepeatsLiveData.postValue(countOfRepeats);
                    }
                    if (App.localStorage.getSettingsParameter(Constants.IS_YOUTUBE_NEED) != null &&
                            App.localStorage.getSettingsParameter(Constants.IS_YOUTUBE_NEED).getValue().equals(Constants.NO)){
                        isNeedYoutubeTest = false;
                        checkWhetherToStartDownloadTest();
                    }else{
                        isNeedYoutubeTest = true;
                        isProgressStartBarShowLiveData.postValue(false);
                        onStartYoutubeClickedEvent.call();
                    }
                }else{
                    isProgressStartBarShowLiveData.postValue(false);
                    stop();
                }
            }
        }, timerDelay * 1000);
        App.logRepository.saveLogListsForCurrentSession();
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    public void youtubeQualityChanged(PlayerConstants.PlaybackQuality playbackQuality) {
        String s = "";
        switch (playbackQuality){
            case UNKNOWN:
                s = "144p";
                break;
            case SMALL:
                s = "240p";
                break;
            case MEDIUM:
                s = "360p";
                break;
            case LARGE:
                s = "480p";
                break;
            case HD720:
                s = "720p";
                break;
            case HD1080:
                s = "1080p";
                break;
            case HIGH_RES:
                s = ">1080p";
                break;
            default:
                Toaster.showShort(mContext,"QUALITY NONE");
        }
        if (youtubeState != null){
            App.logRepository.setYoutubeResolution(s);
        }
    }

    public void onExitClick() {
        if (uploader != null) uploader.setUploadAgain(false);
        radioParamsUpdateRunnable.stop();
        try {
            threadForRadioParamsUpdate.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        exitClickEvent.call();
        int pid = android.os.Process.myPid();
        android.os.Process.killProcess(pid);
    }

    public void youtubeVideoLoaded() {
        finishYoutubeVideoLoadingTime = System.currentTimeMillis();
        long loadedKbits = TrafficStats.getTotalRxBytes() / 1024 * 8;
        eventLogs.add(new Event( logId,EEvents.YFL,finishYoutubeVideoLoadingTime,
                String.valueOf(finishYoutubeVideoLoadingTime - startBufferingTime)
                ,String.valueOf(loadedKbits - startBufferingKbits),EState.YOUTUBE_TEST));
        App.logRepository.saveEvent(eventLogs.get(eventLogs.size() - 1));
        Logger.d("Loaded percent " + String.valueOf(finishYoutubeVideoLoadingTime - startBufferingTime));
        Logger.d("Loaded percent " + String.valueOf(loadedKbits - startBufferingKbits));
    }
}
