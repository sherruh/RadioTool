package com.example.radiotestapp.main;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.CellInfoLte;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.content.Context;
import android.telephony.gsm.GsmCellLocation;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModel;

import com.example.radiotestapp.main.radio.CustomPhoneStateListener;
import com.example.radiotestapp.main.thread.LoggerRunnable;
import com.example.radiotestapp.utils.Logger;
import com.example.radiotestapp.utils.SingleLiveEvent;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;

public class MainViewModel extends ViewModel {

    public SingleLiveEvent<Void> isPermissionNotGranted = new SingleLiveEvent<>();

    private TelephonyManager telephonyManager;
    private GsmCellLocation gsmCellLocation;
    private CellInfoLte cellInfoLte;
    private CustomPhoneStateListener customPhoneStateListener;
    private String signalLevel;
    private LoggerRunnable logger;
    private Thread threadForLog;

    public void onViewCreated(Context context, CustomPhoneStateListener.OnSignalStrengthChangedListener onSignalStrengthChangedListener) {
        telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        if (ContextCompat.checkSelfPermission(context,
                ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
            isPermissionNotGranted.call();
        else {
            gsmCellLocation = (GsmCellLocation) telephonyManager.getCellLocation();
            if (gsmCellLocation != null) {
                Logger.d(String.valueOf(gsmCellLocation.getCid()));
                Logger.d(String.valueOf(gsmCellLocation.getLac()));

                if (this.telephonyManager != null) {
                    try {
                        Logger.d(String.valueOf(telephonyManager.getPhoneType()));
                        Logger.d(String.valueOf(telephonyManager.getDeviceSoftwareVersion()));
                        Logger.d(String.valueOf(telephonyManager.getSimCountryIso().toUpperCase()));
                        Logger.d(String.valueOf(telephonyManager.getSimOperatorName()));
                        Logger.d(String.valueOf(telephonyManager.getNetworkType()));
                    } catch (Exception e) {
                        Logger.d(e.getMessage());
                    }
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (telephonyManager.getPhoneCount() == 1) {
                        //TODO
                    }
                }
            }

            customPhoneStateListener = CustomPhoneStateListener.start(onSignalStrengthChangedListener,null);
            telephonyManager.listen(customPhoneStateListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
        }


    }

    public void start() {
        logger = new LoggerRunnable(null);
        threadForLog = new Thread(logger);
        threadForLog.start();
    }

    public void signalStrengthChanged(String signalStrengthData) {
        Logger.d(signalStrengthData);
    }

    public void stop() {
        logger.stopLog();
        try {
            threadForLog.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
