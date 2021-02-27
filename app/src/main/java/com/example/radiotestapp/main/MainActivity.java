package com.example.radiotestapp.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.core.os.EnvironmentCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.CellLocation;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.radiotestapp.main.framnet_youtube_player.YoutubePlayerFragment;

import com.example.radiotestapp.R;
import com.example.radiotestapp.main.radio.CustomPhoneStateListener;
import com.example.radiotestapp.utils.Logger;
import com.example.radiotestapp.utils.Toaster;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CustomPhoneStateListener.OnSignalStrengthChangedListener,
        CustomPhoneStateListener.OnCellLocationChangeListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private MainViewModel viewModel;
    private String mSignalStrength;
    private CellLocation mCellLocation;
    private YoutubePlayerFragment youtubePlayerFragment;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private Location geoLocation;
    private GoogleApiClient googleApiClient;
    private LocationRequest geoLocationRequest;
    private static final long UPDATE_INTERVAL = 1000, FASTEST_INTERVAL = 500;

    private Button buttonStart;
    private Button buttonStop;
    private ImageView imageViewYoutube;
    private TextView textYoutubeThroughput;
    private TextView textMcc;
    private TextView textMnc;
    private TextView textTech;
    private TextView textTacLac;
    private TextView textEnodeB;
    private TextView textCid;
    private TextView textChannel;
    private TextView textPciPscBsic;
    private TextView textLevel;
    private TextView textLevelTitle;
    private TextView textRsrqEcNo;
    private TextView textRsrqEcNoTitle;
    private TextView textSnr;
    private TextView textCqi;
    private TextView textInitializationTime;
    private TextView textBufferingTime;
    private TextView textYoutubeResolution;
    private NumberPicker numberPickerCountOfRepeats;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        mSignalStrength = "";
        mCellLocation = null;
        checkPlayServices();
        checkPermissions();
    }

    private void initViews() {
        buttonStart = findViewById(R.id.button_start_main_activity);
        buttonStop = findViewById(R.id.button_stop_main_activity);
        imageViewYoutube = findViewById(R.id.image_youtube_main_activity);
        Glide.with(imageViewYoutube).load(R.drawable.youtube_logo).centerCrop().into(imageViewYoutube);
        textYoutubeThroughput = findViewById(R.id.text_youtube_throughput_value_main_activity);
        textMcc = findViewById(R.id.text_mcc_value_main_activity);
        textMnc = findViewById(R.id.text_mnc_value_main_activity);
        textTech = findViewById(R.id.text_tech_value_main_activity);
        textTacLac = findViewById(R.id.text_tac_value_main_activity);
        textEnodeB = findViewById(R.id.text_enodeb_value_main_activity);
        textCid = findViewById(R.id.text_cid_value_main_activity);
        textChannel = findViewById(R.id.text_channel_value_main_activity);
        textPciPscBsic = findViewById(R.id.text_psc_value_main_activity);
        textLevel = findViewById(R.id.text_level_value_main_activity);
        textLevelTitle = findViewById(R.id.text_level_main_activity);
        textRsrqEcNo = findViewById(R.id.text_rsrq_value_main_activity);
        textRsrqEcNoTitle = findViewById(R.id.text_rsrq_main_activity);
        textSnr = findViewById(R.id.text_snr_value_main_activity);
        textCqi = findViewById(R.id.text_cqi_value_main_activity);
        textInitializationTime = findViewById(R.id.text_youtube_init_value_main_activity);
        textBufferingTime = findViewById(R.id.text_youtube_buffering_value_main_activity);
        textYoutubeResolution = findViewById(R.id.text_youtube_resolution_value_value_main_activity);
        numberPickerCountOfRepeats = findViewById(R.id.number_picker_count_of_repeats_main_activity);
        numberPickerCountOfRepeats.setMaxValue(9999);
        numberPickerCountOfRepeats.setMinValue(1);
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
        viewModel.onStartYoutubeClickedEvent.observe(this, aVoid -> {
            initYoutubePlayerFragment();
            imageViewYoutube.setVisibility(View.GONE);
            textBufferingTime.setText("");
            textInitializationTime.setText("");
        });
        viewModel.youtubeThroughputLiveData.observe(this, along ->{
            textYoutubeThroughput.setText(String.valueOf( along / 1000.0));
        });
        viewModel.mccLiveData.observe(this, s ->{ textMcc.setText(s);});
        viewModel.mncLiveData.observe(this, s ->{ textMnc.setText(s);});
        viewModel.techLiveData.observe(this, s ->{
            textTech.setText(s);
            if (s.equals("GSM")) {
                textLevelTitle.setText("RxLvl(dBm):");
                textRsrqEcNoTitle.setText("C/I (dB):");
            }
            if (s.equals("WCDMA")) {
                textLevelTitle.setText("RSCP(dBm):");
                textRsrqEcNoTitle.setText("Ec/N0 (dB):");
            }
            if (s.equals("LTE")) {
                textLevelTitle.setText("RSRP(dBm):");
                textRsrqEcNoTitle.setText("RSRQ (dB):");
            }
        });
        viewModel.tacLiveData.observe(this, s ->{ textTacLac.setText(s);});
        viewModel.eNodeBLiveData.observe(this, s ->{ textEnodeB.setText(s);});
        viewModel.cidLiveData.observe(this, s ->{ textCid.setText(s);});
        viewModel.channelLiveData.observe(this, s ->{ textChannel.setText(s);});
        viewModel.pciPscBsicLiveData.observe(this, s ->{ textPciPscBsic.setText(s);});
        viewModel.levelLiveData.observe(this, s ->{ textLevel.setText(s);});
        viewModel.rsrqEcNoLiveData.observe(this, s ->{ textRsrqEcNo.setText(s);});
        viewModel.snrLiveData.observe(this, s ->{ textSnr.setText(s);});
        viewModel.cqiLiveData.observe(this, s ->{ textCqi.setText(s);});
        viewModel.initTimeLiveData.observe(this, s ->{ textInitializationTime
                .setText(String.valueOf(s / 1000.0));});
        viewModel.bufferingTimeLiveData.observe(this, s ->{textBufferingTime
                .setText(String.valueOf(s / 1000.0));});
        viewModel.youtubeResolutionLiveData.observe(this, s -> {textYoutubeResolution.setText(s);});
        viewModel.youtubeErrorEvent.observe(this, v ->{
            viewModel.youtubePlaybackEnded();
            Toaster.showShort(getBaseContext(),"Youtube timeout");
        });
        viewModel.loggingStoppedEvent.observe(this,aVoid ->{
            if (youtubePlayerFragment != null){
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.remove(youtubePlayerFragment).commit();
                textBufferingTime.setText("");
                textInitializationTime.setText("");
                imageViewYoutube.setVisibility(View.VISIBLE);
                textYoutubeThroughput.setText("");
                buttonStop.setVisibility(View.GONE);
                buttonStart.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onBackPressed() {

    }

    private void initYoutubePlayerFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (youtubePlayerFragment != null) {
            transaction.remove(youtubePlayerFragment).commit();
            transaction = getSupportFragmentManager().beginTransaction();
        }
        youtubePlayerFragment = YoutubePlayerFragment.newInstance(viewModel);
        transaction.replace(R.id.frame_main_activity, youtubePlayerFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    //region Permissions and Google Play
    private void checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST);
            } else {
                Toaster.showLong(MainActivity.this,"Необходимо подключить Google Play");
                finish();
            }
            Toaster.showLong(MainActivity.this,"Необходимо подключить Google Play");
            finish();
        }
    }

    private void checkPermissions() {
            Dexter.withActivity(this)
                    .withPermissions(
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.WAKE_LOCK,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.INTERNET,
                            Manifest.permission.ACCESS_NETWORK_STATE,
                            Manifest.permission.ACCESS_WIFI_STATE,
                            Manifest.permission.CALL_PHONE,
                            Manifest.permission.RECEIVE_BOOT_COMPLETED,
                            Manifest.permission.READ_SMS
                            )
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {
                                initViewModel();
                                startGettingLocation();
                            }
                            else {
                                if(report.getDeniedPermissionResponses().size() == 1
                                    && report.getDeniedPermissionResponses().get(0).getPermissionName()
                                        .equals("android.permission.ACCESS_BACKGROUND_LOCATION"))
                                {
                                    Toaster.showLong(MainActivity.this,"Необходимо разрешение на постоянный доступ к местоположению," +
                                            " иначе возможно неккоретное отображение сектора.");
                                    initViewModel();
                                    startGettingLocation();
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

    }


    //endregion

    private void startGettingLocation() {
        viewModel.startGettingLocation();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                googleApiClient = new GoogleApiClient.Builder(getBaseContext()).
                        addApi(LocationServices.API).
                        addConnectionCallbacks(MainActivity.this).
                        addOnConnectionFailedListener(MainActivity.this)
                        .build();
                googleApiClient.connect();
            }
        });
        t.start();

    }

    @Override
    public void onSignalStrengthChanged(String signalStrengthData) {
        mSignalStrength = signalStrengthData;
        viewModel.radioStateChanged(signalStrengthData, mCellLocation);
    }

    public void onButtonStartClick(View view) {
        viewModel.start(mSignalStrength,numberPickerCountOfRepeats.getValue());
        buttonStop.setVisibility(View.VISIBLE);
        buttonStart.setVisibility(View.GONE);
    }

    public void onButtonStopClick(View view) {
        viewModel.stop();
        buttonStop.setVisibility(View.GONE);
        buttonStart.setVisibility(View.VISIBLE);
    }


    @Override
    public void onCellLocationChanged(CellLocation cellLocation) {
        mCellLocation = cellLocation;
        viewModel.radioStateChanged(mSignalStrength, mCellLocation);
    }

    //region GPS Location
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        geoLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (geoLocation != null) {
            Logger.d("Start lon " + geoLocation.getLongitude() + " Start lat " + geoLocation.getLatitude());
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
        }

    private void startLocationUpdates() {
        geoLocationRequest = new LocationRequest();
        geoLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        geoLocationRequest.setInterval(UPDATE_INTERVAL);
        geoLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, geoLocationRequest, this);
    }

    //endregion


}
