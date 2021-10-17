package com.example.radiotestapp.test_result;

import androidx.core.util.Pair;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.radiotestapp.App;
import com.example.radiotestapp.enums.EEvents;
import com.example.radiotestapp.enums.EState;
import com.example.radiotestapp.model.Event;
import com.example.radiotestapp.model.Log;
import com.example.radiotestapp.utils.Logger;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class TestResultViewModel extends ViewModel {

    private List<Log> logList = new ArrayList();
    private List<Event> eventList = new ArrayList();

    public MutableLiveData<Double> rsrpLiveData = new MutableLiveData<>();
    public MutableLiveData<Double> rscpLiveData = new MutableLiveData<>();
    public MutableLiveData<Double> rxLevelLiveData = new MutableLiveData<>();
    public MutableLiveData<Double> snrLiveData = new MutableLiveData<>();
    public MutableLiveData<Double> ecN0LiveData = new MutableLiveData<>();
    public MutableLiveData<Double> ciLiveData = new MutableLiveData<>();
    public MutableLiveData<Double> cqiLteLiveData = new MutableLiveData<>();
    public MutableLiveData<Double> rsrqLiveData = new MutableLiveData<>();
    public MutableLiveData<Double> cqiUmtsLiveData = new MutableLiveData<>();

    public MutableLiveData<String> firstTech = new MutableLiveData<>();
    public MutableLiveData<String> firstTacLac = new MutableLiveData<>();
    public MutableLiveData<String> firstENodeB = new MutableLiveData<>();
    public MutableLiveData<String> firstCID = new MutableLiveData<>();
    public MutableLiveData<String> secondTech = new MutableLiveData<>();
    public MutableLiveData<String> secondTacLac = new MutableLiveData<>();
    public MutableLiveData<String> secondENodeB = new MutableLiveData<>();
    public MutableLiveData<String> secondCID = new MutableLiveData<>();
    public MutableLiveData<String> thirdTech = new MutableLiveData<>();
    public MutableLiveData<String> thirdTacLac = new MutableLiveData<>();
    public MutableLiveData<String> thirdENodeB = new MutableLiveData<>();
    public MutableLiveData<String> thirdCID = new MutableLiveData<>();
    public MutableLiveData<Double> firstRateLiveData = new MutableLiveData<>();
    public MutableLiveData<Double> secondRateLiveData = new MutableLiveData<>();
    public MutableLiveData<Double> thirdRateLiveData = new MutableLiveData<>();

    public MutableLiveData<Double> bufferingTimeLiveData = new MutableLiveData<>();
    public MutableLiveData<Double> bufferingThroughputLiveData = new MutableLiveData<>();
    public MutableLiveData<Double> bufferingSuccessRateLiveData = new MutableLiveData<>();
    public MutableLiveData<Double> initTimeLiveData = new MutableLiveData<>();
    public MutableLiveData<Double> initSuccessRateLiveData = new MutableLiveData<>();
    public MutableLiveData<Double> youtubeSuccessRateLiveData = new MutableLiveData<>();
    public MutableLiveData<List<Double>> youtubeResolutionList = new MutableLiveData<>();

    public MutableLiveData<Long> downThrputLiveData = new MutableLiveData<>();
    public MutableLiveData<Double> downSRLiveData = new MutableLiveData<>();
    public MutableLiveData<Long> uploadThrputLiveData = new MutableLiveData<>();
    public MutableLiveData<Double> uploadSRLiveData = new MutableLiveData<>();

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
        getTopCells();
        calculateLevels();
        calculateQualities();
    }

    private void calculateQualities() {
        calculateLteQualities();
        calculateUmtsQualities();
        calculateGsmQualities();

    }

    private void calculateGsmQualities() {
        double ci = 0.0;
        double ciSum = 0.0;
        double ciCount = 0.0;

        for (Log log : logList){
            if (log.getTechnology() != null && log.getTechnology().equals("GSM")){
                try {
                    ciSum += Double.parseDouble(log.getSnr());
                    ciCount ++;
                }catch (Exception e) {};
            }
        }

        if (ciCount > 0.0) ci = ciSum / ciCount;

        ciLiveData.setValue(ci);
        Logger.d("TestResultData level ci " + ci);
    }

    private void calculateUmtsQualities() {
        double ecN0 = 0.0;
        double ecN0Sum = 0.0;
        double ecN0Count = 0.0;
        double cqiUmts = 0.0;
        double cqiUmtsSum = 0.0;
        double cqiUmtsCount = 0.0;

        for (Log log : logList){
            if (log.getTechnology() != null && log.getTechnology().equals("WCDMA")){
                try {
                    ecN0Sum += Double.parseDouble(log.getEcNO());
                    ecN0Count ++;
                }catch (Exception e) {};
                try {
                    cqiUmtsSum += Double.parseDouble(log.getCqi());
                    cqiUmtsCount ++;
                }catch (Exception e) {};
            }
        }

        if (ecN0Count > 0.0) ecN0 = ecN0Sum / ecN0Count;
        if (cqiUmtsCount > 0.0) cqiUmts = cqiUmtsSum / cqiUmtsCount;

        ecN0LiveData.setValue(ecN0);
        cqiUmtsLiveData.setValue(cqiUmts);

        Logger.d("TestResultData level ecN0 " + ecN0);
        Logger.d("TestResultData level cqi " + cqiUmts);
    }

    private void calculateLteQualities() {
        double rsrq = 0.0;
        double rsrqSum = 0.0;
        double rsrqCount = 0.0;
        double snr = 0.0;
        double snrSum = 0.0;
        double snrCount = 0.0;
        double cqiLte = 0.0;
        double cqiLteSum = 0.0;
        double cqiLteCount = 0.0;
        for (Log log: logList){
            if (log.getTechnology() != null && log.getTechnology().equals("LTE")){
                try {
                    rsrqSum += Double.parseDouble(log.getRsrq());
                    rsrqCount ++;
                }catch (Exception e) {};
                try{
                    snrSum += Double.parseDouble(log.getSnr());
                    snrCount ++;
                }catch (Exception e) {};
                try{
                    cqiLteSum += Double.parseDouble(log.getCqi());
                    cqiLteCount ++;
                }
                catch (Exception e) {};
            }
        }

        if(rsrqCount > 0.0) rsrq = rsrqSum / rsrqCount;
        if (snrCount > 0.0) snr = snrSum / snrCount;
        if (cqiLteCount > 0.0) cqiLte = cqiLteSum / cqiLteCount;

        snrLiveData.setValue(snr);
        cqiLteLiveData.setValue(cqiLte);
        rsrqLiveData.setValue(rsrq);

        Logger.d("TestResultData level rsrq " + rsrq);
        Logger.d("TestResultData level snr " + snr);
        Logger.d("TestResultData level cqi " + cqiLte);
    }

    private void calculateLevels() {
        double lteLevel = 0.0;
        double lteSumLevel = 0.0;
        double lteCount = 0.0;
        double umtsLevel = 0.0;
        double umtsSumLevel = 0.0;
        double umtsCount = 0.0;
        double gsmLevel = 0.0;
        double gsmSumLevel = 0.0;
        double gsmCount = 0.0;
        for (Log log: logList){
            if (log.getTechnology() != null && log.getTechnology().equals("LTE")){
                try {
                    lteSumLevel += Double.parseDouble(log.getRsrp());
                    lteCount ++;
                }catch (Exception e) {};
            }
            if (log.getTechnology() != null && log.getTechnology().equals("WCDMA")){
                try {
                    umtsSumLevel += Double.parseDouble(log.getRscp());
                    umtsCount ++;
                }catch (Exception e) {};
            }
            if (log.getTechnology() != null && log.getTechnology().equals("GSM")){
                try {
                    gsmSumLevel += Double.parseDouble(log.getRxLevel());
                    gsmCount ++;
                }catch (Exception e) {};
            }
        }
        if (lteCount > 0.0) lteLevel = lteSumLevel / lteCount;
        if (umtsCount > 0.0) umtsLevel = umtsSumLevel / umtsCount;
        if (gsmCount > 0.0) gsmLevel = gsmSumLevel / gsmCount;
        rsrpLiveData.setValue(lteLevel);
        rscpLiveData.setValue(umtsLevel);
        rxLevelLiveData.setValue(gsmLevel);
        Logger.d("TestResultData level " + lteLevel + " counts " + lteCount);
        Logger.d("TestResultData level " + umtsLevel + " counts " + umtsCount);
        Logger.d("TestResultData level " + gsmLevel + " counts " + gsmCount);
    }

    private void getTopCells() {
        HashSet<String> cellsSet = new HashSet<>();
        List<String> cellsList = new ArrayList<>();
        for (Log l : logList){
            if (l.getTechnology() == null || l.getTacLac() == null || l.getTacLac().equals("")) continue;
            if (l.getTechnology().equals("LTE")){
                cellsSet.add("LTE" + l.getENodeB() + "_" + l.getCellId() + "-" + l.getTacLac());
                cellsList.add("LTE" + l.getENodeB() + "_" + l.getCellId() + "-" + l.getTacLac());
                continue;
            }
            Logger.d("TestResultData cells " + l.getTechnology());
            if (l.getTechnology().equals("WCDMA") || l.getTechnology().equals("GSM"))
            {
                Logger.d("TestResultData cells " + l.getTechnology());
                if (l.getTacLac() == null || l.getTacLac().equals("")) continue;
                cellsSet.add(l.getTechnology() + l.getTacLac() + "_" + l.getCellId());
                cellsList.add(l.getTechnology() + l.getTacLac() + "_" + l.getCellId());
            }
        }


        List<Pair<String,Integer>> cellsPairList= new ArrayList<>();
        Iterator<String> iterator = cellsSet.iterator();
        while (iterator.hasNext()){
            String cell = iterator.next();
            cellsPairList.add(new Pair<>(cell, Collections.frequency(cellsList, cell)));
        }

        if (cellsPairList.size() > 1){
            Collections.sort(cellsPairList, (t1, t2) -> {
                if (t1.second > t2.second) return -1;
                if (t1.second.equals(t2.second)) return 0;
                return 1;
            });
        }
        List<Pair<String, Double>> cellsRatePairList = new ArrayList<>();
        int sum = 0;
        for (Pair<String, Integer> pair : cellsPairList ){
            sum += pair.second;
            Logger.d("TestResultData cells " + pair.first + " number " + pair.second );
        }
        for (Pair<String, Integer> pair : cellsPairList){
            double rate = 100.0 * (double)pair.second / (double) sum;
            cellsRatePairList.add(new Pair(pair.first, rate));
            Logger.d("TestResultData cells " + pair.first + " rate " + rate );
        }

        if (cellsRatePairList.size() == 1){
            setDataForFirst(cellsRatePairList.get(0));
            firstRateLiveData.setValue(cellsRatePairList.get(0).second);
        }
        if (cellsRatePairList.size() == 2){
            setDataForFirst(cellsRatePairList.get(0));
            setDataForSecond(cellsRatePairList.get(1));
            firstRateLiveData.setValue(cellsRatePairList.get(0).second);
            secondRateLiveData.setValue(cellsRatePairList.get(1).second);
        }
        if (cellsRatePairList.size() >= 3){
            setDataForFirst(cellsRatePairList.get(0));
            setDataForSecond(cellsRatePairList.get(1));
            setDataForThird(cellsRatePairList.get(2));
            firstRateLiveData.setValue(cellsRatePairList.get(0).second);
            secondRateLiveData.setValue(cellsRatePairList.get(1).second);
            thirdRateLiveData.setValue(cellsRatePairList.get(2).second);
        }

    }

    private void setDataForThird(Pair<String, Double> stringDoublePair) {
        setDataForCell(thirdTech, thirdTacLac, thirdENodeB, thirdCID, stringDoublePair);
    }

    private void setDataForSecond(Pair<String, Double> stringDoublePair) {
        setDataForCell(secondTech, secondTacLac, secondENodeB, secondCID, stringDoublePair);
    }

    private void setDataForFirst(Pair<String, Double> stringDoublePair) {
        setDataForCell(firstTech, firstTacLac, firstENodeB, firstCID, stringDoublePair);
    }

    private void setDataForCell(MutableLiveData<String> techLiveData, MutableLiveData<String> tacLacLiveData,
                                MutableLiveData<String> eNodeBLiveData, MutableLiveData<String> cidLiveData,
                                Pair<String, Double> pair) {
        String s = pair.first;
        if (s.contains("LTE")){
            techLiveData.setValue("LTE");
            int idx = s.indexOf("_");
            String eNodeB = "--";
            try{
                eNodeB = s.substring(3,idx);
            }catch (Exception e){}
            eNodeBLiveData.setValue(eNodeB);
            int idxSecond = s.indexOf("-");
            String cid = "--";
            try{
                cid = s.substring(idx + 1, idxSecond);
            }catch (Exception e){}
            cidLiveData.setValue(cid);
            String lacTac = "--";
            try{
                lacTac = s.substring(idxSecond + 1);
            }catch (Exception e){}
            tacLacLiveData.setValue(lacTac);
        }

        if (s.contains("WCDMA") || s.contains("GSM")){
            int startIdx = 0;
            int idx = s.indexOf("_");
            if (s.contains("WCDMA")) startIdx = 5;
            else startIdx = 4;
            String lacTac = "--";
            try {
                lacTac = s.substring(startIdx,idx);
            }catch (Exception e){}
            String cid = "--";
            try{
                cid = s.substring(idx + 1);
            }catch (Exception e){}
            tacLacLiveData.setValue(lacTac);
            cidLiveData.setValue(cid);
        }

        if (s.contains("WCDMA")) techLiveData.setValue("WCDMA");
        if (s.contains("GSM")) techLiveData.setValue("GSM");
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
        uploadSRLiveData.setValue(uploadSR);
        uploadThrputLiveData.setValue(calculateUploadThruput());
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
        downThrputLiveData.setValue(calculateDownloadThruput());
        downSRLiveData.setValue(downloadSR);
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
        Double bufferSR = 0.0;
        Double initSR = 0.0;
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
        try{
            bufferSR = (double) youBufFinish / (double) youBufStart * 100.0;
        }catch (Exception e){}
        try{
            initSR = (double) youInitFinish / (double) youInitStart * 100.0;
        }catch (Exception e){}

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

        bufferingTimeLiveData.setValue(avgBufferTime);
        bufferingThroughputLiveData.setValue((double)calculateYoutubeThruput());
        bufferingSuccessRateLiveData.setValue(bufferSR);
        initSuccessRateLiveData.setValue(initSR);
        initTimeLiveData.setValue(avgInitTime);
        youtubeSuccessRateLiveData.setValue(youtubeSR);
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
        res144Rate = 100.0 * (double)res144p / (double)sum;
        res240Rate = 100.0 * (double)res240p / (double)sum;
        res360Rate = 100.0 * (double)res360p / (double)sum;
        res480Rate = 100.0 * (double)res480p / (double)sum;
        res720Rate = 100.0 * (double)res720p / (double)sum;
        res1080Rate = 100.0 * (double)res1080p / (double)sum;
        resMore1080Rate = 100.0 * (double)resMore1080Rate / (double)sum;
        Logger.d("TestResultData resolution " + new DecimalFormat("##.0").format(res720Rate));

        List<Double> resolutionList = new ArrayList<>();
        resolutionList.add(res144Rate);
        resolutionList.add(res240Rate);
        resolutionList.add(res360Rate);
        resolutionList.add(res480Rate);
        resolutionList.add(res720Rate);
        resolutionList.add(res1080Rate + resMore1080Rate);
        youtubeResolutionList.setValue(resolutionList);
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
