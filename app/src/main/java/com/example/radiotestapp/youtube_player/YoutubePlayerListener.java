package com.example.radiotestapp.youtube_player;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;

public class YoutubePlayerListener implements YouTubePlayerListener {


    private boolean isInitialBuffering;

    public boolean isInitialBuffering() {
        return isInitialBuffering;
    }

    public void setInitialBuffering(boolean initialBuffering) {
        isInitialBuffering = initialBuffering;
    }

    public YoutubePlayerListener() {
        isInitialBuffering = true;
    }

    @Override
    public void onApiChange(YouTubePlayer youTubePlayer) {

    }

    @Override
    public void onCurrentSecond(YouTubePlayer youTubePlayer, float v) {
    }

    @Override
    public void onError(YouTubePlayer youTubePlayer, PlayerConstants.PlayerError playerError) {
    }

    @Override
    public void onPlaybackQualityChange(YouTubePlayer youTubePlayer, PlayerConstants.PlaybackQuality playbackQuality) {

    }

    @Override
    public void onPlaybackRateChange(YouTubePlayer youTubePlayer, PlayerConstants.PlaybackRate playbackRate) {

    }

    @Override
    public void onReady(YouTubePlayer youTubePlayer) {

    }

    @Override
    public void onStateChange(YouTubePlayer youTubePlayer, PlayerConstants.PlayerState playerState) {

    }

    @Override
    public void onVideoDuration(YouTubePlayer youTubePlayer, float v) {

    }

    @Override
    public void onVideoId(YouTubePlayer youTubePlayer, String s) {
    }

    @Override
    public void onVideoLoadedFraction(YouTubePlayer youTubePlayer, float v) {

    }
}
