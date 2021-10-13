package com.example.radiotestapp.test_result;

import androidx.lifecycle.ViewModel;

import com.example.radiotestapp.App;
import com.example.radiotestapp.enums.EEvents;
import com.example.radiotestapp.enums.EState;
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
        if (isTestedDownload) calculateDownloadTest();
    }

    private void calculateDownloadTest() {
        int downloadStart = 0;
        int downloadFinish = 0;
        int downloadFailed = 0;
        Double downloadSR = 0.0;
        for(Event e : eventList){
            switch (e.getEvent()){
                case DS:
                    downloadStart++;
                    break;
                case DF:
                    downloadFinish++;
                    break;
                case DE:
                    downloadFailed++;
                    break;
            }
        }
        try{
            downloadSR = (double)downloadFinish / (double) downloadStart;
        } catch (Exception exception){

        }
        downloadSR *= 100;
        Logger.d("TestResultData download avg " + calculateDownloadThruput() + " " + downloadSR);
    }

    private long calculateDownloadThruput() {
        long k = 0L;
        long avgThrput = 0L;
        for (Log l : logList){
            if (l.getLogState() == EState.DOWNLOAD_TEST) {
                try{
                    avgThrput += l.getDlThrput();
                    k++;
                } catch (Exception exception ){ Logger.d("TestResultData download " + exception.getMessage()); }
            }
        }
        if (k != 0L) avgThrput /= k;
        return avgThrput;
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

        Logger.d("TestResultData " + youtubeSR + " " + avgInitTime + " " + avgBufferTime +
                "AvgYoutubeThrpu " + calculateYoutubeThruput());
    }

    private int calculateYoutubeThruput() {
        int k = 0;
        int avgThrput = 0;
        for (Event e : eventList){
            if (e.getEvent() == EEvents.YFB) {
                Logger.d("TestResultData youtubethrput in event " +e.getParameter2());

                try{
                    avgThrput+= Integer.parseInt(e.getParameter2());
                    k++;
                } catch (Exception exception ){}

            }
        }
        if (k != 0) avgThrput /= k;
        return avgThrput;
    }


}
