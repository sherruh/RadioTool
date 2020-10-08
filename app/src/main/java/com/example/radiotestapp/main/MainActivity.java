package com.example.radiotestapp.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.telephony.CellLocation;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;

import com.example.radiotestapp.App;
import com.example.radiotestapp.R;
import com.example.radiotestapp.main.radio.CustomPhoneStateListener;
import com.example.radiotestapp.services.GettingLocationService;
import com.example.radiotestapp.utils.Logger;
import com.example.radiotestapp.utils.Toaster;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;

import java.util.List;

import kr.co.prnd.YouTubePlayerView;

public class MainActivity extends AppCompatActivity implements CustomPhoneStateListener.OnSignalStrengthChangedListener,
        CustomPhoneStateListener.OnCellLocationChangeListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private MainViewModel viewModel;
    private String mSignalStrength;
    private CellLocation mCellLocation;
    private YouTubePlayerView youTubePlayerView;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private Location geoLocation;
    private GoogleApiClient googleApiClient;
    private LocationRequest geoLocationRequest;
    private static final long UPDATE_INTERVAL = 1000, FASTEST_INTERVAL = 500;

    private Button buttonStart;
    private Button buttonStop;

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
        youTubePlayerView = findViewById(R.id.youtube_player_main_activity);
        initYoutube();
    }

    private void initYoutube() {
        com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView youTubePlayerView1 = findViewById(R.id.youtube_player_advanced_main_activity);
        getLifecycle().addObserver(youTubePlayerView1);

        youTubePlayerView1.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer) {
                String videoId = "S0Q4gqBUs7c";
                youTubePlayer.loadVideo(videoId, 0);

            }
        });

        youTubePlayerView1.addYouTubePlayerListener(new YouTubePlayerListener() {
            @Override
            public void onReady(com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer) {

            }

            @Override
            public void onStateChange(com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer, PlayerConstants.PlayerState playerState) {

            }

            @Override
            public void onPlaybackQualityChange(com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer, PlayerConstants.PlaybackQuality playbackQuality) {
                Toaster.showLong(MainActivity.this, String.valueOf(playbackQuality.name()));
            }

            @Override
            public void onPlaybackRateChange(com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer, PlayerConstants.PlaybackRate playbackRate) {

            }

            @Override
            public void onError(com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer, PlayerConstants.PlayerError playerError) {

            }

            @Override
            public void onCurrentSecond(com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer, float v) {

            }

            @Override
            public void onVideoDuration(com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer, float v) {

            }

            @Override
            public void onVideoLoadedFraction(com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer, float v) {

            }

            @Override
            public void onVideoId(com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer, String s) {

            }

            @Override
            public void onApiChange(com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer) {

            }
        });
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
                                startGettingLocation();
                            }
                            else {
                                if(report.getDeniedPermissionResponses().size() == 1
                                    && report.getDeniedPermissionResponses().get(0).getPermissionName()
                                        .equals("android.permission.ACCESS_BACKGROUND_LOCATION"))
                                {
                                    Toaster.showLong(MainActivity.this,"Необходимо разрешение на постоянный доступ к местоположению," +
                                            " иначе вохможно неккоретное отображение сектора.");
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
                                startGettingLocation();
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
        viewModel.stateChanged(signalStrengthData, mCellLocation);
    }

    public void onButtonStartClick(View view) {
        viewModel.start(mSignalStrength);
        buttonStop.setVisibility(View.VISIBLE);
        buttonStart.setVisibility(View.GONE);
        playYoutube();
    }

    public void onButtonStopClick(View view) {
        viewModel.stop();
        buttonStop.setVisibility(View.GONE);
        buttonStart.setVisibility(View.VISIBLE);
    }


    @Override
    public void onCellLocationChanged(CellLocation cellLocation) {
        mCellLocation = cellLocation;
        viewModel.stateChanged(mSignalStrength, mCellLocation);
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
        if (location != null)
            Logger.d("Lon " + location.getLongitude() + " Lat " + location.getLatitude());

    }

    private void startLocationUpdates() {
        geoLocationRequest = new LocationRequest();
        geoLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        geoLocationRequest.setInterval(UPDATE_INTERVAL);
        geoLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, geoLocationRequest, this);
    }

    //endregion

    private void playYoutube(){
        Logger.d("Youtube initializing");
        youTubePlayerView.play("EzKImzjwGyM", new YouTubePlayerView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.setShowFullscreenButton(false);
                youTubePlayer.setPlaybackEventListener(new YouTubePlayer.PlaybackEventListener() {
                    @Override
                    public void onPlaying() {
                        Logger.d("Youtube Playing");
                        MainActivity.this.viewModel.startPlayingVideo();
                    }

                    @Override
                    public void onPaused() {
                    }

                    @Override
                    public void onStopped() {

                    }

                    @Override
                    public void onBuffering(boolean b) {
                        if (b){
                            Logger.d("Youtube Buffering");
                            MainActivity.this.viewModel.startBuffering();
                        }
                        else{
                            Logger.d("Youtube NOT Buffering");
                            MainActivity.this.viewModel.finishBuffering();
                        }
                    }

                    @Override
                    public void onSeekTo(int i) {

                    }
                });
                MainActivity.this.viewModel.youTubePlayerInitialized();
                youTubePlayer.loadVideo("EzKImzjwGyM");
                youTubePlayer.play();
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                MainActivity.this.viewModel.youTubePlayerFailedInitialization(youTubeInitializationResult.toString());
            }
        });
    }

}
