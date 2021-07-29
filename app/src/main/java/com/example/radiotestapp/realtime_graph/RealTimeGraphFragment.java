package com.example.radiotestapp.realtime_graph;

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
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

public class RealTimeGraphFragment extends Fragment {

    public RealTimeGraphFragment(MainViewModel viewModel) {
        mainViewModel = viewModel;
    }

    private MainViewModel mainViewModel;
    private LineChart lineChart;

    public static RealTimeGraphFragment newInstance(MainViewModel viewModel) {
        RealTimeGraphFragment fragment = new RealTimeGraphFragment(viewModel);
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
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
        lineChart = view.findViewById(R.id.graph_fragment_real_time_graph);
        lineChart.getDescription().setEnabled(false);
        lineChart.setTouchEnabled(false);
        lineChart.setDragEnabled(false);
        lineChart.setScaleEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setPinchZoom(true);
        lineChart.setBackgroundColor(getResources().getColor( R.color.colorParametersFrame));
        LineData data = new LineData();
        data.setValueTextColor(R.color.colorWhite);


        // add empty data
        lineChart.setData(data);

        // get the legend (only possible after setting data)
        Legend l = lineChart.getLegend();
        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
        //l.setTypeface(tfLight);
        l.setTextColor(Color.WHITE);

        XAxis xl = lineChart.getXAxis();
        //xl.setTypeface(tfLight);
        xl.setTextColor(Color.WHITE);
        xl.setDrawGridLines(false);
        xl.setAvoidFirstLastClipping(true);
        xl.setEnabled(true);

        YAxis rightAxis = lineChart.getAxisRight();
        //leftAxis.setTypeface(tfLight);
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

            ILineDataSet set = data.getDataSetByIndex(0);
            // set.addEntry(...); // can be called as well

            if (set == null) {
                set = createSet();
                data.addDataSet(set);
            }
            set.setLabel(getLabel());
            data.addEntry(new Entry(set.getEntryCount(), level), 0);
            data.notifyDataChanged();
            lineChart.getAxisRight().setAxisMaximum(set.getYMax() + 10f);
            lineChart.getAxisRight().setAxisMinimum(set.getYMin() - 10f);
            // let the chart know it's data has changed
            lineChart.notifyDataSetChanged();

            // limit the number of visible entries
            lineChart.setVisibleXRangeMaximum(60);
            // chart.setVisibleYRange(30, AxisDependency.LEFT);

            // move to the latest entry
            lineChart.moveViewToX(data.getEntryCount());

            // this automatically refreshes the chart (calls invalidate())
            // chart.moveViewTo(data.getXValCount()-7, 55f,
            // AxisDependency.LEFT);
        }
    }

    private ILineDataSet createSet() {
        LineDataSet set = new LineDataSet(null, getLabel());
        set.setAxisDependency(YAxis.AxisDependency.RIGHT);
        set.setColor(getResources().getColor(R.color.colorWhite));
        set.setCircleColor(getResources().getColor(R.color.colorWhite));
        set.setLineWidth(2f);
        set.setCircleRadius(0.5f);
        set.setFillAlpha(0);
        set.setFillColor(getResources().getColor(R.color.colorWhite));
        set.setHighLightColor(getResources().getColor(R.color.colorWhite));
        set.setValueTextColor(getResources().getColor(R.color.colorWhite));
        set.setValueTextSize(9f);
        set.setDrawValues(false);
        return set;
    }

    private String getLabel() {
        String label = App.logRepository.techLiveData.getValue();
        label = (label.equals("GSM")) ? label + " Rx level, dBm"
                : (label.equals("WCDMA") )? label + " RSCP, dBm"
                : (label.equals("LTE")) ? label + " RSRP, dBm" : "";
        return label;
    }
}