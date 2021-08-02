package com.example.radiotestapp.download_test;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.radiotestapp.App;
import com.example.radiotestapp.R;
import com.example.radiotestapp.main.MainViewModel;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DownloadTestFragment extends Fragment {

    private LineChart lineChart;
    private MainViewModel mainViewModel;

    public DownloadTestFragment(MainViewModel viewModel) {
        mainViewModel = viewModel;
    }

    public static DownloadTestFragment newInstance(MainViewModel viewModel) {
        DownloadTestFragment fragment = new DownloadTestFragment(viewModel);
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_download_test, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        initViewModel();
    }

    private void initViewModel() {
        mainViewModel.updateLevelListEvent.observe(getViewLifecycleOwner(),o->{
            float level;
            try {
                level = Float.parseFloat(App.logRepository.levelList.getLast());
                addEntry(level);
            }catch (Exception e) {}
        });
    }

    private void initViews(View view) {
        lineChart = view.findViewById(R.id.graph_dl_fragment_download_test);
        lineChart.getDescription().setEnabled(false);
        lineChart.setTouchEnabled(false);
        lineChart.setDragEnabled(false);
        lineChart.setScaleEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setPinchZoom(true);
        lineChart.setBackgroundColor(getResources().getColor( R.color.colorParametersFrame));
        LineData data = new LineData();
        data.setValueTextColor(R.color.colorWhite);

        lineChart.setData(data);
        Legend l = lineChart.getLegend();
        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.WHITE);

        XAxis xl = lineChart.getXAxis();
        xl.setTextColor(Color.WHITE);
        xl.setDrawGridLines(false);
        xl.setAvoidFirstLastClipping(true);
        xl.setEnabled(true);

        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setTextColor(Color.WHITE);
        rightAxis.setAxisMaximum(-140f);
        rightAxis.setAxisMinimum(-140f);
        rightAxis.setDrawGridLines(false);
        rightAxis.setGridColor(getResources().getColor(R.color.colorWhite));

        YAxis lefAxis = lineChart.getAxisLeft();
        lefAxis.setEnabled(false);
        addInitailEntries();
    }

    private void addInitailEntries() {
        if (App.logRepository.levelList.size() > 0){

            for(String s : new ArrayList<String>( App.logRepository.levelList) ){
                try {
                    addEntry(Float.parseFloat(s));
                }catch (Exception e) {}
            }
        }
    }

    private void addEntry(float level) {

        LineData data = lineChart.getData();
        if (data != null) {

            LineDataSet set = (LineDataSet) data.getDataSetByIndex(0);
            if (set == null) {
                set = (LineDataSet) createSet();
                data.addDataSet(set);
            }
            set.setLabel(getLabel());
            data.addEntry(new Entry(set.getEntryCount(), level), 0);
            data.notifyDataChanged();
            lineChart.getAxisRight().setAxisMaximum(set.getYMax() + 10f);
            lineChart.getAxisRight().setAxisMinimum(set.getYMin() - 10f);
            lineChart.notifyDataSetChanged();
            lineChart.setVisibleXRangeMaximum(60);
            lineChart.moveViewToX(data.getEntryCount());
        }
    }

    private ILineDataSet createSet() {
        LineDataSet set1 = new LineDataSet(null,"dd");

        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set1.setCubicIntensity(0.2f);
        set1.setDrawFilled(true);
        set1.setDrawCircles(false);
        set1.setLineWidth(1.8f);
        set1.setCircleRadius(4f);
        set1.setCircleColor(Color.WHITE);
        set1.setHighLightColor(Color.rgb(244, 117, 117));
        set1.setHighlightEnabled(false);
        set1.setColor(Color.WHITE);
        set1.setFillColor(Color.WHITE);
        set1.setFillAlpha(100);
        set1.setDrawValues(false);
        set1.setDrawHorizontalHighlightIndicator(false);
        set1.setFillFormatter(new IFillFormatter() {
            @Override
            public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                return lineChart.getAxisLeft().getAxisMinimum();
            }
        });
        return set1;
    }

    private String getLabel() {
        String label = "Download Throughput, Mb/s";
        return label;
    }
}