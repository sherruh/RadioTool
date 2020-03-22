package com.example.radiotestapp.main;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.telephony.CellInfoLte;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.content.Context;
import android.telephony.gsm.GsmCellLocation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModel;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.radiotestapp.enums.ETechnology;
import com.example.radiotestapp.main.radio.CustomPhoneStateListener;
import com.example.radiotestapp.main.thread.LoggerRunnable;
import com.example.radiotestapp.model.Log;
import com.example.radiotestapp.services.GettingLocationService;
import com.example.radiotestapp.utils.Logger;
import com.example.radiotestapp.utils.SingleLiveEvent;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;

public class MainViewModel extends ViewModel implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    public SingleLiveEvent<Void> isPermissionNotGranted = new SingleLiveEvent<>();

    private Context mContext;
    private TelephonyManager telephonyManager;
    private GsmCellLocation gsmCellLocation;
    private CellInfoLte cellInfoLte;
    private CustomPhoneStateListener customPhoneStateListenerSignalStrength;
    private CustomPhoneStateListener customPhoneStateListenerCellLocation;
    private String signalLevel;
    private LoggerRunnable logger;
    private Thread threadForLog;
    private List<Log> logs = new ArrayList<>();
    private Log currentLog;
    private String plmn;
    private GoogleApiClient googleApiClient;
    private LocationRequest geoLocationRequest;
    private Location geoLocation;

    public void onViewCreated(Context context, CustomPhoneStateListener.OnSignalStrengthChangedListener onSignalStrengthChangedListener,
                              CustomPhoneStateListener.OnCellLocationChangeListener onCellLocationChangeListener) {
        mContext = context;
        telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        customPhoneStateListenerSignalStrength = CustomPhoneStateListener.start(onSignalStrengthChangedListener,null,
                onCellLocationChangeListener);
        customPhoneStateListenerCellLocation = CustomPhoneStateListener.start(onSignalStrengthChangedListener,null,
                onCellLocationChangeListener);
        telephonyManager.listen(customPhoneStateListenerSignalStrength, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
        telephonyManager.listen(customPhoneStateListenerCellLocation, PhoneStateListener.LISTEN_CELL_LOCATION);
        initService();
    }

    public void start(String mSignalStrength) {
        currentLog = new Log();
        currentLog.setRscp(mSignalStrength);
        logs.clear();
        logger = new LoggerRunnable(logs, currentLog);
        threadForLog = new Thread(logger);
        threadForLog.start();
    }


    public void stop() {
        logger.stopLog();
        try {
            threadForLog.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void stateChanged(String mSignalStrength, CellLocation mCellLocation) {
        gsmCellLocation = (GsmCellLocation) getCellLocation();
        if(gsmCellLocation != null){
            Logger.d("Cell " + gsmCellLocation.getCid());
        }else Logger.d("Cell is null");
        currentLog = new Log();
        currentLog.setRscp(getSignalStrength());
        Logger.d(currentLog.getRscp());
        if(logger != null)
            logger.setLog(currentLog);
        plmn = getPlmn();

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
                /*Logger.d(String.valueOf(gsmCellLocation.getCid()));
                Logger.d(String.valueOf(gsmCellLocation.getLac()));*/
                if (this.telephonyManager != null) {
                    /*try {
                        Logger.d(String.valueOf(telephonyManager.getPhoneType()));
                        Logger.d(String.valueOf(telephonyManager.getDeviceSoftwareVersion()));
                        Logger.d(String.valueOf(telephonyManager.getSimCountryIso().toUpperCase()));
                        Logger.d(String.valueOf(telephonyManager.getSimOperatorName()));
                        Logger.d(telephonyManager.getSimOperator());
                        Logger.d(String.valueOf(telephonyManager.getNetworkType()));
                    } catch (Exception e) {
                        Logger.d(e.getMessage());
                    }*/
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
        if (geoLocation != null) {
            Logger.d("ViewModel Start lon " + geoLocation.getLongitude() + " Start lat " + geoLocation.getLatitude());
        }

        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null)
            Logger.d("ViewModel Lon " + location.getLongitude() + " Lat " + location.getLatitude());
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

                        if (latitude != null && longitude != null) {
                            Logger.d("Service lat " + latitude + " service lon " + longitude);
                        }
                    }
                }, new IntentFilter(GettingLocationService.ACTION_LOCATION_BROADCAST));


        Intent intent = new Intent(mContext.getApplicationContext(), GettingLocationService.class);
        mContext.getApplicationContext().startService(intent);

    }
}
