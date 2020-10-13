package com.example.radiotestapp.main.framnet_youtube_player;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.radiotestapp.R;
import com.example.radiotestapp.main.MainViewModel;
import com.example.radiotestapp.utils.Logger;
import com.example.radiotestapp.utils.Toaster;
import com.example.radiotestapp.utils.YoutubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;


public class YoutubePlayerFragment extends Fragment {

    private MainViewModel mViewModel;
    private YoutubePlayerListener youtubePlayerListener;
    private YouTubePlayerView youTubePlayerView;
    private YouTubePlayer mYouTubePlayer;

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
        initYoutubeListener();
        youTubePlayerView = getActivity().findViewById(R.id.youtube_player_advanced_youtube_player_fragment);
        youTubePlayerView.initialize(youtubePlayerListener);
        mViewModel.youTubePlayerInitializing();

        youTubePlayerView.getYouTubePlayerWhenReady(new YouTubePlayerCallback() {
            @Override
            public void onYouTubePlayer(YouTubePlayer youTubePlayer) {
                mYouTubePlayer = youTubePlayer;
                youTubePlayer.cueVideo("QuV9iPaZTBU",0);
                youTubePlayer.play();
            }

        });
    }

    private void initYoutubeListener() {
        youtubePlayerListener = new YoutubePlayerListener(){
            @Override
            public void onError(YouTubePlayer youTubePlayer, PlayerConstants.PlayerError playerError) {
                Toaster.showShort(getContext(),playerError.toString());
            }

            @Override
            public void onCurrentSecond(YouTubePlayer youTubePlayer, float v) {
                if (v > 0.0) setInitialBuffering(false);
            }

            @Override
            public void onPlaybackQualityChange(YouTubePlayer youTubePlayer, PlayerConstants.PlaybackQuality playbackQuality) {
                Toaster.showShort(getContext(), playbackQuality.name());
            }

            @Override
            public void onStateChange(YouTubePlayer youTubePlayer, PlayerConstants.PlayerState playerState) {
                switch (playerState){
                    case ENDED:{
                        mViewModel.youtubePlaybackEnded();
                        onDestroy();
                        break;
                    }
                    case BUFFERING:{
                        mViewModel.startBuffering(isInitialBuffering());
                        break;
                    }
                    case PLAYING:{
                        mViewModel.startPlayingVideo(isInitialBuffering());
                        break;
                    }
                    case VIDEO_CUED:{
                        mViewModel.videoCued();
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

    @Override
    public void onDestroy() {
        youTubePlayerView.release();
        super.onDestroy();
    }


}