package com.example.radiotestapp.main.radio;

import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;


public class CustomPhoneStateListener extends PhoneStateListener {

    public static CustomPhoneStateListener start(OnSignalStrengthChangedListener onSignalStrengthChangedListener,
                                                 CustomPhoneStateListener customPhoneStateListener){
        return new CustomPhoneStateListener(onSignalStrengthChangedListener, customPhoneStateListener);
    }

    private OnSignalStrengthChangedListener mOnSignalStrengthChangedListener;

    private CustomPhoneStateListener() {
    }

    /* synthetic */ CustomPhoneStateListener(OnSignalStrengthChangedListener onSignalStrengthChangedListener,
                                             CustomPhoneStateListener customPhoneStateListener) {
        this();
        mOnSignalStrengthChangedListener = onSignalStrengthChangedListener;
    }


    @Override
    public void onSignalStrengthsChanged(SignalStrength signalStrength) {
        super.onSignalStrengthsChanged(signalStrength);
        mOnSignalStrengthChangedListener.onChange(signalStrength.toString());
    }

    public interface OnSignalStrengthChangedListener{
        void onChange(String signalStrengthData);
    }
}
