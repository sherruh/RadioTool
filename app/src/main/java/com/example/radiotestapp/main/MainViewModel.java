package com.example.radiotestapp.main;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
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
import android.content.Context;
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
import com.example.radiotestapp.enums.EEvents;
import com.example.radiotestapp.enums.EState;
import com.example.radiotestapp.enums.EYoutubeState;
import com.example.radiotestapp.main.radio.CustomPhoneStateListener;
import com.example.radiotestapp.main.thread.LoggerRunnable;
import com.example.radiotestapp.main.thread.RadioParamsUpdateRunnable;
import com.example.radiotestapp.model.Event;
import com.example.radiotestapp.model.Log;
import com.example.radiotestapp.services.GettingLocationService;
import com.example.radiotestapp.utils.DateConverter;
import com.example.radiotestapp.utils.Logger;
import com.example.radiotestapp.utils.SingleLiveEvent;
import com.example.radiotestapp.utils.Toaster;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;

public class MainViewModel extends ViewModel implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    public SingleLiveEvent<Void> isPermissionNotGranted = new SingleLiveEvent<>();
    public SingleLiveEvent<Void> youtubeErrorEvent = new SingleLiveEvent<>();
    public SingleLiveEvent<Void> onStartYoutubeClickedEvent = new SingleLiveEvent<>();
    public SingleLiveEvent<Void> loggingStoppedEvent = new SingleLiveEvent<>();
    public SingleLiveEvent<Void> exitClickEvent = new SingleLiveEvent<>();
    public SingleLiveEvent<Void> updateLevelListEvent = App.logRepository.updateLevelListEvent;
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
    private long finishBufferingTime;
    private final long TIMEOUT_DELAY = 60000L;
    private int countOfRepeats = 1;
    private EYoutubeState youtubeState;

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
    }

    private void startThreadForRadioParamsUpdating() {
        radioParamsUpdateRunnable = new RadioParamsUpdateRunnable();
        threadForRadioParamsUpdate = new Thread(radioParamsUpdateRunnable);
        threadForRadioParamsUpdate.start();
    }

    public void start(String mSignalStrength, int value) {
        countOfRepeats = value;
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
        logger.stopLog();
        try {
            threadForLog.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        isLogging.setValue(false);
        App.logRepository.setYoutubeResolution("");
        App.logRepository.closeLogFile();
        loggingStoppedEvent.call();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void radioStateChanged(String mSignalStrength, CellLocation mCellLocation) {
        gsmCellLocation = (GsmCellLocation) getCellLocation();
        updateSignalData(getSignalStrength());
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
        int rxIndex = signalData.indexOf("ss=");
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

    Timer timer = new Timer();

    public void youTubePlayerInitializing() {
        App.logRepository.setLogState(EState.YOUTUBE_TEST);
        startInitYoutubeTime = System.currentTimeMillis();
        eventLogs.add(new Event( logId,EEvents.YSI,startInitYoutubeTime,"",EState.YOUTUBE_TEST));
        App.logRepository.saveEvent(eventLogs.get(eventLogs.size() - 1));
        timer = new Timer();
        final long start = System.currentTimeMillis();
        timer.schedule(new TimerTask() {
            @Override
            public void run () {
                eventLogs.add(new Event(logId,EEvents.YEI,System.currentTimeMillis(),"",EState.YOUTUBE_TEST));
                App.logRepository.saveEvent(eventLogs.get(eventLogs.size() - 1));
                youtubeErrorEvent.call();
                timer.cancel();
            }
        }, TIMEOUT_DELAY);
    }

    public void startBuffering(boolean initialBuffering) {
        timer = new Timer();
        final long start = System.currentTimeMillis();
        timer.schedule(new TimerTask() {
            @Override
            public void run () {
                eventLogs.add(new Event(logId,EEvents.YEB,System.currentTimeMillis(),"",EState.YOUTUBE_TEST));
                App.logRepository.saveEvent(eventLogs.get(eventLogs.size() - 1));
                youtubeErrorEvent.call();
                timer.cancel();
            }
        }, TIMEOUT_DELAY);
        if (initialBuffering) {
            startBufferingTime = System.currentTimeMillis();
            eventLogs.add(new Event( logId,EEvents.YSB,startBufferingTime,"",EState.YOUTUBE_TEST));
            App.logRepository.saveEvent(eventLogs.get(eventLogs.size() - 1));
        }
    }

    public void videoCued() {
        finishInitYoutubeTime = System.currentTimeMillis();
        eventLogs.add(new Event( logId,EEvents.YFI,finishInitYoutubeTime,
                String.valueOf(finishInitYoutubeTime - startInitYoutubeTime),EState.YOUTUBE_TEST));
        App.logRepository.saveEvent(eventLogs.get(eventLogs.size() - 1));
        initTimeLiveData.setValue(finishInitYoutubeTime - startInitYoutubeTime);
        timer.cancel();
    }

    public void startPlayingVideo(boolean initialBuffering) {
        if (initialBuffering) {
            finishBufferingTime = System.currentTimeMillis();
            eventLogs.add(new Event( logId,EEvents.YFB,finishBufferingTime,
                    String.valueOf(finishBufferingTime - startBufferingTime),EState.YOUTUBE_TEST));
            App.logRepository.saveEvent(eventLogs.get(eventLogs.size() - 1));
            bufferingTimeLiveData.setValue(finishBufferingTime - startBufferingTime);
        }
        youtubeState = EYoutubeState.PLAYING;
        timer.cancel();
    }

    public void youtubePlaybackEnded(){
        App.logRepository.setLogState(EState.IDLE);
        App.logRepository.setYoutubeResolution("");
        youtubeState = EYoutubeState.FINISHED;
        checkWhetherToStartYoutubePlayback();
    }

    private void checkWhetherToStartYoutubePlayback() {
        if (countOfRepeats > 0 && isLogging.getValue()){
            countOfRepeats--;
            onStartYoutubeClickedEvent.call();
            return;
        }
        stop();
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
}
