package com.example.radiotestapp.youtube_params;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.radiotestapp.R;
import com.example.radiotestapp.main.MainViewModel;
import com.example.radiotestapp.utils.Toaster;

public class YoutubeParamsFragment extends Fragment {

    private MainViewModel mViewModel;
    private TextView textInitializationTime;
    private TextView textBufferingTime;
    private TextView textThroughput;
    private TextView textResolution;

    public YoutubeParamsFragment(MainViewModel mainViewModel) {
        mViewModel = mainViewModel;
    }

    public static YoutubeParamsFragment newInstance(MainViewModel mainViewModel) {
        YoutubeParamsFragment fragment = new YoutubeParamsFragment(mainViewModel);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_youtube_params, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        initViewModel();
    }

    private void initViewModel() {
        mViewModel.initTimeLiveData.observe(getViewLifecycleOwner(), s ->{ textInitializationTime
                .setText(String.valueOf(s / 1000.0));});
        mViewModel.bufferingTimeLiveData.observe(getViewLifecycleOwner(), s ->{textBufferingTime
                .setText(String.valueOf(s / 1000.0));});
        mViewModel.youtubeThroughputLiveData.observe(getViewLifecycleOwner(), along ->{
            textThroughput.setText(String.valueOf( along / 1000.0));
        });
        mViewModel.youtubeResolutionLiveData.observe(getViewLifecycleOwner(), s -> {textResolution.setText(s);});
        mViewModel.onStartYoutubeClickedEvent.observe(getViewLifecycleOwner(), aVoid -> {
            textInitializationTime.setText("--");
            textBufferingTime.setText("--");
        });
        mViewModel.loggingStoppedEvent.observe(getViewLifecycleOwner(),aVoid -> {
            textInitializationTime.setText("--");
            textBufferingTime.setText("--");
        });
    }

    private void initViews(View view) {
        textInitializationTime = view.findViewById(R.id.text_youtube_init_value_youtube_params_fragment);
        textBufferingTime = view.findViewById(R.id.text_youtube_buffering_value_youtube_params_fragment);
        textThroughput = view.findViewById(R.id.text_youtube_throughput_value_youtube_params_fragment);
        textResolution = view.findViewById(R.id.text_youtube_resolution_value_youtube_params_fragment);
        textInitializationTime.setText("--");
        textBufferingTime.setText("--");
        textThroughput.setText("--");
        textResolution.setText("--");
    }
}