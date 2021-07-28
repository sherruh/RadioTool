package com.example.radiotestapp.realtime_graph;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.radiotestapp.R;

public class RealTimeGraphFragment extends Fragment {

    public RealTimeGraphFragment() {
    }

    public static RealTimeGraphFragment newInstance() {
        RealTimeGraphFragment fragment = new RealTimeGraphFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_real_time_graph, container, false);
    }
}