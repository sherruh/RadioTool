package com.example.radiotestapp.test_result;

import androidx.lifecycle.ViewModel;

import com.example.radiotestapp.App;
import com.example.radiotestapp.enums.EState;
import com.example.radiotestapp.enums.EYoutubeState;
import com.example.radiotestapp.model.Event;
import com.example.radiotestapp.model.Log;
import com.example.radiotestapp.utils.Logger;

import java.util.ArrayList;
import java.util.List;

public class TestResultViewModel extends ViewModel {

    private List<Log> logList = new ArrayList();
    private List<Event> eventList = new ArrayList();

    public void getLogsAndEventsByLogId(String logId, boolean isTestedYoutube,
                                        boolean isTestedDownload, boolean isTestedUpload){
        logList.addAll(App.localStorage.getLogsByLogId(logId));
        eventList.addAll(App.localStorage.getEventsByLogId(logId));
        startCalculations(isTestedYoutube, isTestedDownload, isTestedUpload);
    }

    private void startCalculations(boolean isTestedYoutube, boolean isTestedDownload, boolean isTestedUpload) {
        if (isTestedYoutube) calculateYoutube();
    }

    private void calculateYoutube() {
        int youInitStart = 0;
        int youInitFinish = 0;
        int youInitFailed = 0;
        List<Double> youInitTimeList = new ArrayList<>();
        double avgInitTime = 0.0;
        int youBufStart = 0;
        int youBufFinish = 0;
        int youBufFailed = 0;
        List<Double> youBufferTimeList = new ArrayList<>();
        double avgBufferTime = 0.0;
        int youStartPlay = 0;
        Double youtubeSR = 0.0;
        for (Event e : eventList){
            switch (e.getEvent()){
                case YSI:
                    youInitStart++;
                    break;
                case YFI:
                    youInitFinish++;
                    try{
                        youInitTimeList.add(Double.parseDouble(e.getParameter()));
                    }catch (Exception exception) {}
                    break;
                case YEI:
                    youInitFailed++;
                    break;
                case YSB:
                    youBufStart++;
                    break;
                case YFB:
                    youBufFinish++;
                    try{
                        youBufferTimeList.add(Double.parseDouble(e.getParameter()));
                    } catch (Exception exception){}
                    break;
                case YEB:
                    youBufFailed++;
                    break;
                case YSP:
                    youStartPlay++;
                    break;
            }
        }
        try {
            youtubeSR = ((double)youInitFinish / (double)youInitStart) *
                    ((double)youBufFinish / (double)youBufStart);
        } catch (Exception exception){ }
        youtubeSR *= 100;

        for (double d : youInitTimeList){
            avgInitTime += d;
        }
        if (youInitTimeList.size() > 0) avgInitTime /= (double)youInitTimeList.size();

        for (double d : youBufferTimeList){
            avgBufferTime += d;
        }
        if (youBufferTimeList.size() > 0) avgBufferTime /= (double) youBufferTimeList.size();

        Logger.d("TestResultData " + youtubeSR + " " + avgInitTime + " " + avgBufferTime);
        calculateYoutubeThruput();
    }

    private void calculateYoutubeThruput() {
        for (Log l : logList){
            Logger.d("TestResultData youtubethrput " + l.getLogState() + " " +l .getYoutubeState());
            if (l.getLogState() == EState.YOUTUBE_TEST && l.getYoutubeState() == EYoutubeState.BUFFERING){
                Logger.d("TestResultData youtubethrput " + l);
            }
        }
    }


}
