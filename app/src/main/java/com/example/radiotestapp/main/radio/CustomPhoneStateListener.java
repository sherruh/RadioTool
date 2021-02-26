package com.example.radiotestapp.main.radio;

import android.telephony.CellLocation;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;


public class CustomPhoneStateListener extends PhoneStateListener {

    public static CustomPhoneStateListener start(OnSignalStrengthChangedListener onSignalStrengthChangedListener,
                                                 CustomPhoneStateListener customPhoneStateListener,
                                                 OnCellLocationChangeListener onCellLocationChangeListener){
        return new CustomPhoneStateListener(onSignalStrengthChangedListener, customPhoneStateListener,
                onCellLocationChangeListener);
    }

    private OnSignalStrengthChangedListener mOnSignalStrengthChangedListener;
    private OnCellLocationChangeListener mOnCellLocationChangeListener;

    private CustomPhoneStateListener() {
    }

    /* synthetic */ CustomPhoneStateListener(OnSignalStrengthChangedListener onSignalStrengthChangedListener,
                                             CustomPhoneStateListener customPhoneStateListener,
                                             OnCellLocationChangeListener onCellLocationChangeListener) {
        this();
        mOnSignalStrengthChangedListener = onSignalStrengthChangedListener;
        mOnCellLocationChangeListener = onCellLocationChangeListener;
    }


    @Override
    public void onSignalStrengthsChanged(SignalStrength signalStrength) {
        super.onSignalStrengthsChanged(signalStrength);
        mOnSignalStrengthChangedListener.onSignalStrengthChanged(signalStrength.toString());
    }

    @Override
    public void onCellLocationChanged(CellLocation location) {
        super.onCellLocationChanged(location);
        mOnCellLocationChangeListener.onCellLocationChanged(location);
    }

    public interface OnSignalStrengthChangedListener{
        void onSignalStrengthChanged(String signalStrengthData);
    }

    public interface OnCellLocationChangeListener{
        void onCellLocationChanged(CellLocation cellLocation);
    }
}
