package com.example.radiotestapp.main;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.CellInfoLte;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.content.Context;
import android.telephony.gsm.GsmCellLocation;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModel;

import com.example.radiotestapp.main.radio.CustomPhoneStateListener;
import com.example.radiotestapp.main.thread.LoggerRunnable;
import com.example.radiotestapp.model.Log;
import com.example.radiotestapp.utils.Logger;
import com.example.radiotestapp.utils.SingleLiveEvent;

import java.util.ArrayList;
import java.util.List;

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
    private List<Log> logs = new ArrayList<>();
    private Log currentLog;

    public void onViewCreated(Context context, CustomPhoneStateListener.OnSignalStrengthChangedListener onSignalStrengthChangedListener,
                              CustomPhoneStateListener.OnCellLocationChangeListener onCellLocationChangeListener) {
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
                        Logger.d(telephonyManager.getSimOperator());
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

            customPhoneStateListener = CustomPhoneStateListener.start(onSignalStrengthChangedListener,null,
                    onCellLocationChangeListener);
            telephonyManager.listen(customPhoneStateListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
            telephonyManager.listen(customPhoneStateListener, PhoneStateListener.LISTEN_CELL_LOCATION);
        }


    }

    public void start(String mSignalStrength) {
        currentLog = new Log();
        currentLog.setRscp(mSignalStrength);
        logs.clear();
        logger = new LoggerRunnable(logs, currentLog);
        threadForLog = new Thread(logger);
        threadForLog.start();
    }

    public void signalStrengthChanged(String signalStrengthData, CellLocation mCellLocation) {
        currentLog = new Log();
        currentLog.setRscp(signalStrengthData);
        Logger.d(currentLog.getRscp());
        gsmCellLocation = (GsmCellLocation)mCellLocation;
        if(gsmCellLocation != null){
            Logger.d("Cell " + gsmCellLocation.getCid());
        }
        if(logger != null)
            logger.setLog(currentLog);
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
