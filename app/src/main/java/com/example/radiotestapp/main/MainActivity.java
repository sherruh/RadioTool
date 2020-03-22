package com.example.radiotestapp.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.telephony.CellLocation;
import android.view.View;

import com.example.radiotestapp.R;
import com.example.radiotestapp.main.radio.CustomPhoneStateListener;
import com.example.radiotestapp.utils.Logger;
import com.example.radiotestapp.utils.Toaster;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class MainActivity extends AppCompatActivity implements CustomPhoneStateListener.OnSignalStrengthChangedListener,
        CustomPhoneStateListener.OnCellLocationChangeListener {

    private MainViewModel viewModel;
    private String mSignalStrength;
    private CellLocation mCellLocation;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSignalStrength = "";
        mCellLocation = null;
        checkPlayServices();
        checkPermissions();
    }

    private void initViewModel() {
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.isPermissionNotGranted.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                checkPermissions();
            }
        });

        viewModel.onViewCreated(this, this, this);
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST);
            } else {
                Toaster.showLong(MainActivity.this,"Необходимо подключить Google Play");
                finish();
            }

            return false;
        }

        return true;
    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Dexter.withActivity(this)
                    .withPermissions(
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.WAKE_LOCK,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.INTERNET,
                            Manifest.permission.ACCESS_NETWORK_STATE,
                            Manifest.permission.ACCESS_WIFI_STATE,
                            Manifest.permission.CALL_PHONE,
                            Manifest.permission.RECEIVE_BOOT_COMPLETED,
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION
                            )
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {
                                initViewModel();
                            }
                            else {
                                if(report.getDeniedPermissionResponses().size() == 1
                                    && report.getDeniedPermissionResponses().get(0).getPermissionName()
                                        .equals("android.permission.ACCESS_BACKGROUND_LOCATION"))
                                {
                                    Toaster.showLong(MainActivity.this,"Необходимо разрешение на постоянный доступ к местоположению," +
                                            " иначе вохможно неккоретное отображение сектора.");
                                    initViewModel();
                                } else {
                                    Logger.d(String.valueOf(report.getDeniedPermissionResponses().get(0).getPermissionName()));
                                    Toaster.showLong(MainActivity.this,"Необходимы разрешения приложению");
                                    finish();
                                }

                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    })
                    .onSameThread()
                    .check();
        } else{
            Dexter.withActivity(this)
                    .withPermissions(
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.WAKE_LOCK,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.INTERNET,
                            Manifest.permission.ACCESS_NETWORK_STATE,
                            Manifest.permission.ACCESS_WIFI_STATE,
                            Manifest.permission.CALL_PHONE,
                            Manifest.permission.RECEIVE_BOOT_COMPLETED
                    )
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {
                                initViewModel();
                            }
                            else {
                                Toaster.showLong(MainActivity.this,"Необходимы разрешения приложению");
                                finish();
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    })
                    .onSameThread()
                    .check();
        }

    }

    @Override
    public void onSignalStrengthChanged(String signalStrengthData) {
        mSignalStrength = signalStrengthData;
        viewModel.stateChanged(signalStrengthData, mCellLocation);
    }

    public void onButtonClick(View view) {
        viewModel.start(mSignalStrength);
    }

    public void onButton2Click(View view) { viewModel.stop();
    }


    @Override
    public void onCellLocationChanged(CellLocation cellLocation) {
        mCellLocation = cellLocation;
        viewModel.stateChanged(mSignalStrength, mCellLocation);
    }
}
