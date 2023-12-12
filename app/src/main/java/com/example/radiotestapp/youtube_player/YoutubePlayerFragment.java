package com.example.radiotestapp.youtube_player;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.radiotestapp.App;
import com.example.radiotestapp.R;
import com.example.radiotestapp.core.Constants;
import com.example.radiotestapp.main.MainViewModel;
import com.example.radiotestapp.utils.Logger;
import com.example.radiotestapp.utils.Toaster;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;


public class YoutubePlayerFragment extends Fragment {

    private MainViewModel mViewModel;
    private YoutubePlayerListener youtubePlayerListener;
    private YouTubePlayerView youTubePlayerView;
    private YouTubePlayer mYouTubePlayer;
    private boolean isLoadedYet;
    private boolean isQualitySet;
    private int xSettings;
    private int ySettings;
    private int xQuality;
    private int yQuality;
    private int x1080;
    private int y1080;

    public YoutubePlayerFragment(MainViewModel mainViewModel) {
        mViewModel = mainViewModel;
    }

    public static YoutubePlayerFragment newInstance(MainViewModel mainViewModel) {
        YoutubePlayerFragment fragment = new YoutubePlayerFragment(mainViewModel);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_youtube_player, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        playYoutube();
    }

    private void playYoutube(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;


        initYoutubeListener();
        youTubePlayerView = getActivity().findViewById(R.id.youtube_player_advanced_youtube_player_fragment);
        //youTubePlayerView.initialize(youtubePlayerListener);
        int[] location = new int[2];
        youTubePlayerView.getLocationOnScreen(location);
        Logger.d("Touched window " + width + " " + height);
        Logger.d("Touched window " + location[0] + " " + location[1]);
        youTubePlayerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                youTubePlayerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                Logger.d("Touched width " + youTubePlayerView.getWidth());  ; //height is ready
                Logger.d("Touched height " + youTubePlayerView.getHeight());
                xSettings = (int)((double)location[0] + (double)youTubePlayerView.getWidth() * 0.8);
                xQuality = (int)((double)location[0] + (double)youTubePlayerView.getWidth() * 0.78);
                x1080 = (int)((double)location[0] + (double)youTubePlayerView.getWidth() * 0.78);
                ySettings = (int)((double)location[1] + (double)youTubePlayerView.getHeight() * 0.9);
                yQuality = (int)((double)location[1] + (double)youTubePlayerView.getHeight() * 0.7);
                y1080 = (int)((double)location[1] + (double)youTubePlayerView.getHeight() * 0.43);

            }
        });


        youTubePlayerView.addYouTubePlayerListener(youtubePlayerListener);
        //youTubePlayerView.initializeWithWebUi(youtubePlayerListener,true);
        mViewModel.youTubePlayerInitializing();

        youTubePlayerView.getYouTubePlayerWhenReady(new YouTubePlayerCallback() {
            @Override
            public void onYouTubePlayer(YouTubePlayer youTubePlayer) {
                mYouTubePlayer = youTubePlayer;
                String videoId = getVideoId();
                youTubePlayer.cueVideo(videoId,0);//VBKNoLcj8jA
                /*Logger.d("YoutubePlayerPlay");
                youTubePlayer.play();*/
            }
        });

    }

    private String getVideoId() {
        /*if (App.localStorage.getSettingsParameter(Constants.IS_YOUTUBE_DEFAULT) == null ||
                App.localStorage.getSettingsParameter(Constants.IS_YOUTUBE_DEFAULT).getValue().equals(Constants.YES))
            return "fimmQNc6_uI";*/
        String videoId = "fimmQNc6_uI";
        /*SettingsParameter settingsParameter = App.localStorage.getSettingsParameter(Constants.YOUTUBE_URL);
        if (settingsParameter != null) videoId = settingsParameter.getValue();
        else videoId = "fimmQNc6_uI";*/
        if (App.localStorage.getSettingsParameter(Constants.YOUTUBE_QUALITY) == null ||
                App.localStorage.getSettingsParameter(Constants.YOUTUBE_QUALITY).getValue() == null)
            return videoId;
        switch (App.localStorage.getSettingsParameter(Constants.YOUTUBE_QUALITY).getValue()){
            case Constants.YOUTUBE_QUALITY_AUTO:
                videoId = Constants.YOUTUBE_VIDEOID_AUTO;
                break;
            case Constants.YOUTUBE_QUALITY_144p:
                videoId = Constants.YOUTUBE_VIDEOID_144p;
                break;
            case Constants.YOUTUBE_QUALITY_240p:
                videoId = Constants.YOUTUBE_VIDEOID_240p;
                break;
            case Constants.YOUTUBE_QUALITY_360p:
                videoId = Constants.YOUTUBE_VIDEOID_360p;
                break;
            case Constants.YOUTUBE_QUALITY_480p:
                videoId = Constants.YOUTUBE_VIDEOID_480p;
                break;
            case Constants.YOUTUBE_QUALITY_720p:
                videoId = Constants.YOUTUBE_VIDEOID_720p;
                break;
            case Constants.YOUTUBE_QUALITY_1080p:
                videoId = Constants.YOUTUBE_VIDEOID_1080p;
                break;
        }
        return videoId;
    }

    private void initYoutubeListener() {
        youtubePlayerListener = new YoutubePlayerListener(){
            @Override
            public void onError(YouTubePlayer youTubePlayer, PlayerConstants.PlayerError playerError) {
                Toaster.showShort(getContext(),playerError.toString());
                onStateChange(youTubePlayer, PlayerConstants.PlayerState.ENDED);
            }

            @Override
            public void onCurrentSecond(YouTubePlayer youTubePlayer, float v) {
                if (isQualitySet && v > 0.0) setInitialBuffering(false);
            }

            @Override
            public void onPlaybackQualityChange(YouTubePlayer youTubePlayer, PlayerConstants.PlaybackQuality playbackQuality) {
                if (isQualitySet) mViewModel.youtubeQualityChanged(playbackQuality);
            }

            @Override
            public void onVideoLoadedFraction(YouTubePlayer youTubePlayer, float v) {
                Logger.d("Loaded percent" + v);
                if (v == 1.0 && !isLoadedYet){
                    if (isQualitySet){
                        mViewModel.youtubeVideoLoaded();
                        isLoadedYet = true;
                    }

                }
            }

            @Override
            public void onStateChange(YouTubePlayer youTubePlayer, PlayerConstants.PlayerState playerState) {
                switch (playerState){
                    case ENDED:{
                        if (isQualitySet){
                            mViewModel.youtubePlaybackEnded();
                            onDestroy();
                        }
                        break;
                    }
                    case BUFFERING:{
                        Logger.d("Buffering now system " + System.currentTimeMillis());
                        if (isQualitySet){
                            //mViewModel.startBuffering(isInitialBuffering());
                            break;
                        }
                    }
                    case PLAYING:{
                        if (isQualitySet){
                            mViewModel.startPlayingVideo(isInitialBuffering());
                        }else {
                            youTubePlayer.pause();
                            youTubePlayer.seekTo(0L);
                            isQualitySet = true;
                            touchSettings();
                        }

                        break;
                    }
                    case VIDEO_CUED:{
                        mViewModel.videoCued();
                        youTubePlayer.play();
                        break;

                    }
                    case UNKNOWN:{
                        Toaster.showShort(getContext(), "UNKNOWN!");
                        break;
                    }
                }
            }

        };
    }

    private void touchSettings() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    Runtime.getRuntime().exec("/system/bin/input tap" + " " + xSettings + " " + ySettings);
                    touchQuality();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        },3000L);
    }

    private void touchQuality() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    Runtime.getRuntime().exec("/system/bin/input tap " + xQuality + " " + yQuality);
                    touch1080();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        },3000L);
    }

    private void touch1080() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    Runtime.getRuntime().exec("/system/bin/input tap " + x1080 +" " + y1080);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                startPlayWithSetQuality();
            }
        },3000L);

    }

    private void startPlayWithSetQuality() {
        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                Logger.d("Buffering now " + System.currentTimeMillis());
                mViewModel.startBuffering(true);
                mYouTubePlayer.play();
            }
        },100L);
    }


    @Override
    public void onDestroy() {
        youTubePlayerView.release();
        super.onDestroy();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        youTubePlayerView.release();
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        youTubePlayerView.release();
        super.onDetach();
    }
}