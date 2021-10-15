package com.example.radiotestapp.test_result;

import androidx.lifecycle.ViewModel;

import com.example.radiotestapp.App;
import com.example.radiotestapp.enums.EEvents;
import com.example.radiotestapp.enums.EState;
import com.example.radiotestapp.model.Event;
import com.example.radiotestapp.model.Log;
import com.example.radiotestapp.utils.Logger;

import java.text.DecimalFormat;
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
        if (isTestedUpload) calculateUploadTest();
    }

    private void calculateUploadTest() {
        int uploadStart = 0;
        int uploadFinish = 0;
        int uploadFailed = 0;
        Double uploadSR = 0.0;
        for (Event e : eventList){
            switch (e.getEvent()){
                case US:
                    uploadStart++;
                    break;
                case UF:
                    uploadFinish++;
                    break;
                case UE:
                    uploadFailed++;
                    break;
            }
        }
        try{
            uploadSR = (double)uploadFinish / (double)uploadStart;
        } catch (Exception exception){
            uploadSR = 0.0;
        }
        uploadSR *= 100;
        Logger.d("TestResultData upload avg " + calculateUploadThruput() + " " + uploadSR);
    }

    private long calculateUploadThruput() {
        long k = 0L;
        long avgThrput = 0L;
        for (Log l : logList){
            if (l.getLogState() == EState.UPLOAD_TEST) {
                try{
                    avgThrput += l.getUlThrput();
                    k++;
                } catch (Exception exception ){ Logger.d("TestResultData upload " + exception.getMessage()); }
            }
        }
        if (k != 0L) avgThrput /= k;
        return avgThrput;
    }

    private void calculateDownloadTest() {
        int downloadStart = 0;
        int downloadFinish = 0;
        int downloadFailed = 0;
        Double downloadSR = 0.0;
        for (Event e : eventList){
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
            downloadSR = 0.0;
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

        calculateYoutubeResolution();
    }

    private void calculateYoutubeResolution() {
        int res144p = 0;
        int res240p = 0;
        int res360p = 0;
        int res480p = 0;
        int res720p = 0;
        int res1080p = 0;
        int resMore1080p = 0;
        boolean isNextRes = true;
        for (int i = logList.size() - 1 ; i >= 0; i-- ){
            Logger.d("TestResultData resolution " + i + " " + logList.get(i).getYoutubeResolution());
            if (logList.get(i).getYoutubeResolution() == null || logList.get(i).getYoutubeResolution().equals("")
                    || logList.get(i).getYoutubeResolution().equals("null")) {
                isNextRes = true;
                continue;
            };
            if (isNextRes){
                if (logList.get(i).getYoutubeResolution().equals("144p")) {
                    res144p++;
                    isNextRes = false;
                    continue;
                }
                if (logList.get(i).getYoutubeResolution().equals("240p")) {
                    res240p++;
                    isNextRes = false;
                    continue;
                }
                if (logList.get(i).getYoutubeResolution().equals("360p")) {
                    res360p++;
                    isNextRes = false;
                    continue;
                }
                if (logList.get(i).getYoutubeResolution().equals("480p")) {
                    res480p++;
                    isNextRes = false;
                    continue;
                }
                if (logList.get(i).getYoutubeResolution().equals("720p")) {
                    res720p++;
                    isNextRes = false;
                    continue;
                }
                if (logList.get(i).getYoutubeResolution().equals("1080p")) {
                    res1080p++;
                    isNextRes = false;
                    continue;
                }
                if (logList.get(i).getYoutubeResolution().equals(">1080p")) {
                    resMore1080p++;
                    isNextRes = false;
                    continue;
                }
            }
        }
        Logger.d("TestResultData resolution " + res144p + " " + res240p + " " + res360p + " " +
                    res480p + " " + res720p + " " + res1080p + " " + resMore1080p);

        double res144Rate = 0.0;
        double res240Rate = 0.0;
        double res360Rate = 0.0;
        double res480Rate = 0.0;
        double res720Rate = 0.0;
        double res1080Rate = 0.0;
        double resMore1080Rate = 0.0;

        int sum = res144p + res240p + res360p + res480p + res720p + res1080p + resMore1080p;
        if (sum == 0) sum = 1;
        res144Rate = (double)res144p / (double)sum;
        res240Rate = (double)res240p / (double)sum;
        res360Rate = (double)res360p / (double)sum;
        res480Rate = (double)res480p / (double)sum;
        res720Rate = 100.0 * (double)res720p / (double)sum;
        res1080Rate = (double)res1080p / (double)sum;
        resMore1080Rate = (double)resMore1080Rate / (double)sum;
        Logger.d("TestResultData resolution " + new DecimalFormat("##.0").format(res720Rate));
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
                } catch (Exception exception ){

                }

            }
        }
        if (k != 0) avgThrput /= k;
        return avgThrput;
    }


}
