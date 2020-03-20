package com.example.radiotestapp.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.os.Bundle;
import android.view.View;

import com.example.radiotestapp.R;
import com.example.radiotestapp.main.radio.CustomPhoneStateListener;
import com.example.radiotestapp.utils.Toaster;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class MainActivity extends AppCompatActivity implements CustomPhoneStateListener.OnSignalStrengthChangedListener {

    private MainViewModel viewModel;
    private String mSignalStrength;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSignalStrength = "";
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

        viewModel.onViewCreated(this, this);
    }

    private void checkPermissions() {
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

    @Override
    public void onChange(String signalStrengthData) {
        mSignalStrength = signalStrengthData;
        viewModel.signalStrengthChanged(signalStrengthData);
    }

    public void onButtonClick(View view) {
        viewModel.start(mSignalStrength);
    }

    public void onButton2Click(View view) { viewModel.stop();
    }
}
