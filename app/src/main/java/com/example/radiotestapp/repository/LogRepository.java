package com.example.radiotestapp.repository;

import android.os.Build;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityWcdma;

import androidx.lifecycle.MutableLiveData;

import com.example.radiotestapp.App;
import com.example.radiotestapp.enums.EState;
import com.example.radiotestapp.enums.EYoutubeState;
import com.example.radiotestapp.model.Event;
import com.example.radiotestapp.model.Log;
import com.example.radiotestapp.repository.local.ILocalLogRepository;
import com.example.radiotestapp.repository.local.LogFileWriter;
import com.example.radiotestapp.utils.Logger;
import com.example.radiotestapp.utils.SingleLiveEvent;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class LogRepository {

    private ILocalLogRepository localLogRepository = new LogFileWriter();

    private List<Log> logList = new ArrayList();
    private List<Event> eventList = new ArrayList();
    public SingleLiveEvent<Void> updateLevelListEvent = new SingleLiveEvent<>();
    public SingleLiveEvent<Void> updateUploadThroughputListEvent = new SingleLiveEvent<>();
    public SingleLiveEvent<Void> updateDownloadThroughputListEvent = new SingleLiveEvent<>();
    public MutableLiveData<Long> youtubeThroughputLiveData = new MutableLiveData<>();
    public MutableLiveData<Long> uploadThroughputLiveData = new MutableLiveData<>();
    public MutableLiveData<Long> downloadThroughputLiveData = new MutableLiveData<>();
    public MutableLiveData<String> mccLiveData = new MutableLiveData<>("--");
    public MutableLiveData<String> mncLiveData = new MutableLiveData<>("--");
    public MutableLiveData<String> techLiveData = new MutableLiveData<>("--");
    public MutableLiveData<String> tacLiveData = new MutableLiveData<>("--");
    public MutableLiveData<String> eNodeBLiveData = new MutableLiveData<>("--");
    public MutableLiveData<String> cidLiveData = new MutableLiveData<>("--");
    public MutableLiveData<String> channelLiveData = new MutableLiveData<>("--");
    public MutableLiveData<String> pciPscBsicLiveData = new MutableLiveData<>("--");
    public MutableLiveData<String> levelLiveData = new MutableLiveData<>("--");
    public LinkedList<String> levelList = new LinkedList<>();
    public LinkedList<Long> uploadThroughputList = new LinkedList<>();
    public LinkedList<Long> downloadThroughputList = new LinkedList<>();
    public MutableLiveData<String> rsrqEcNoLiveData = new MutableLiveData<>("--");
    public MutableLiveData<String> snrLiveData = new MutableLiveData<>("--");
    public MutableLiveData<String> cqiLiveData = new MutableLiveData<>("--");
    public MutableLiveData<String> youtubeResolutionLiveData = new MutableLiveData<>();

    private Log mLog = new Log();
    private String mCellId;

    public EState getLogState() {
        return logState;
    }

    public void setLogState(EState logState) {
        this.logState = logState;
    }

    private EState logState;
    private EYoutubeState youtubeState;

    public void setLog(Log log){
        synchronized (this){
            mLog = log;
            mLog.setCellId(mCellId);
        }
    }

    public Log getLog(){
        return mLog;
    }

    public void setDate(long date){
        synchronized (this){
            mLog.setDate(date);
        }
    }

    public void setYoutubeThroughput(long dlThroughput){
        synchronized (this){
            mLog.setDlThrput(dlThroughput);
            mLog.setLogState(logState);
            youtubeThroughputLiveData.postValue(dlThroughput);
        }
    }

    public EYoutubeState getYoutubeState() {
        return youtubeState;
    }

    public void setYoutubeState(EYoutubeState youtubeState) {
        synchronized (this) {
            Logger.d("TestResultData youtubestate" + youtubeState + " resolution " + mLog.getYoutubeResolution());
            this.youtubeState = youtubeState;
            mLog.setYoutubeState(youtubeState);
        }
    }

    public void setDownloadTestThroughput(long dlThroughput){
        synchronized (this){
            mLog.setDlThrput(dlThroughput);
            mLog.setLogState(logState);
            addToDownloadThroughputList(dlThroughput);
            downloadThroughputLiveData.postValue(dlThroughput);
        }
    }

    public void setCellId(String cellId){
        synchronized (this){
            mCellId = cellId;
            mLog.setCellId(cellId);
            cidLiveData.setValue(cellId);
        }
    }

    public void setLocation(Double latitude, Double longitude, int altitude){
        synchronized (this){
            mLog.setLatitude(latitude);
            mLog.setLongitude(longitude);
            mLog.setAltitude(altitude);
        }
    }

    public void clearSignalData() {
        synchronized (this) {
            mLog.setTechnology("");
            mLog.setRscp("");
            mLog.setBer("");
            mLog.setCqi("");
            mLog.setEcNO("");
            mLog.setRsrp("");
            mLog.setRxLevel("");
            mLog.setSnr("");
            mLog.setRsrq("");
            mLog.setTacLac("");
            mLog.setBsic("");
            mLog.setChannel("");
            mLog.setPsc("");
            mLog.setPci("");
            mLog.setENodeB("");
        }
    }

    public void setRsrp(String rsrp) {
        synchronized (this){
            mLog.setRsrp(rsrp);
            levelLiveData.setValue(rsrp);
        }
    }

    public void setRsrq(String rsrq) {
        synchronized (this) {
            mLog.setRsrq(rsrq);
            rsrqEcNoLiveData.setValue(rsrq);
        }
    }


    public void setRsSnr(String rsSnr) {
        synchronized (this){
            mLog.setSnr(rsSnr);
            snrLiveData.setValue(rsSnr);
        }
    }

    public void setCqi(String cqi) {
        synchronized (this){
            if (cqi.length() > 2){
                mLog.setCqi("null");
                cqiLiveData.setValue("--");
            }else {
                mLog.setCqi(cqi);
                cqiLiveData.setValue(cqi);
            }
        }
    }

    public void setTech(String tech) {
        synchronized (this){
            mLog.setTechnology(tech);
            if (tech.equals("null")) techLiveData.setValue("--");
            else techLiveData.setValue(tech);
        }
    }

    public void setBer(String ber) {
        synchronized (this){
            mLog.setBer(ber);
        }
    }

    public void setRscp(String rscp) {
        synchronized (this){
            mLog.setRscp(rscp);
            levelLiveData.setValue(rscp);
        }
    }

    public void setEcno(String ecno) {
        synchronized (this){

            if (ecno.equals("0")) {
                rsrqEcNoLiveData.setValue("--");
                mLog.setEcNO("null");
            }
            else {
                rsrqEcNoLiveData.setValue(ecno);
                mLog.setEcNO(ecno);
            }
        }
    }

    public void setRxLevel(String rxLevel) {
        synchronized (this){
            mLog.setRxLevel(rxLevel);
            levelLiveData.setValue(rxLevel);
        }
    }

    public void setPlmn(String plmn) {
        synchronized (this){
            if (plmn.length() == 5){
                mLog.setMcc(plmn.substring(0,3));
                mLog.setMnc(plmn.substring(3,5));
            } else {
                mLog.setMcc("--");
                mLog.setMnc("--");
            }
            mccLiveData.setValue(mLog.getMcc());
            mncLiveData.setValue(mLog.getMnc());
        }
    }

    public void setGsmCellInfo(CellIdentityGsm cellIdentityGsm) {
        synchronized (this){
            mLog.setTacLac(String.valueOf(cellIdentityGsm.getLac()));
            tacLiveData.setValue(mLog.getTacLac());
            eNodeBLiveData.setValue("--");
            rsrqEcNoLiveData.setValue("--");
            snrLiveData.setValue("--");
            cqiLiveData.setValue("--");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mLog.setBsic(String.valueOf(cellIdentityGsm.getBsic()));
                pciPscBsicLiveData.setValue(mLog.getBsic());
                mLog.setChannel(String.valueOf(cellIdentityGsm.getArfcn()));
                channelLiveData.setValue(mLog.getChannel());
            }
        }
    }

    public void setWcdmaCellInfo(CellIdentityWcdma cellIdentityWcdma) {
        synchronized (this){
            mLog.setTacLac(String.valueOf(cellIdentityWcdma.getLac()));
            tacLiveData.setValue(mLog.getTacLac());
            mLog.setPsc(String.valueOf(cellIdentityWcdma.getPsc()));
            pciPscBsicLiveData.setValue(mLog.getPsc());
            eNodeBLiveData.setValue("--");
            snrLiveData.setValue("--");
            cqiLiveData.setValue("--");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mLog.setChannel(String.valueOf(cellIdentityWcdma.getUarfcn()));
                channelLiveData.setValue(mLog.getChannel());
            }
        }
    }

    public void setLteCellInfo(CellIdentityLte cellIdentityLte) {
        synchronized (this){
            mLog.setTacLac(String.valueOf(cellIdentityLte.getTac()));
            tacLiveData.setValue(mLog.getTacLac());
            mLog.setPci(String.valueOf(cellIdentityLte.getPci()));
            pciPscBsicLiveData.setValue(mLog.getPci());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mLog.setChannel(String.valueOf(cellIdentityLte.getEarfcn()));
                channelLiveData.setValue(mLog.getChannel());
            }
        }
    }

    public void setEnodeB(String eNodeB) {
        synchronized (this){
            mLog.setENodeB(eNodeB);
            eNodeBLiveData.setValue(eNodeB);
        }
    }

    public void setYoutubeResolution(String youtubeResolution) {
        synchronized (this){
            mLog.setYoutubeResolution(youtubeResolution);
            youtubeResolutionLiveData.postValue(youtubeResolution);
        }
    }

    public void saveEvent(Event event){
        synchronized (this){
            localLogRepository.saveEvent(event);
            Event currentEvent = null;
            try {
                currentEvent = (Event) event.clone();
                currentEvent.setId(System.currentTimeMillis());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            eventList.add(currentEvent);
        }
    }

    public void saveLog(Log log){
        synchronized (this){
            localLogRepository.saveLog(log);
            Log currentLog = null;
            try {
                currentLog = (Log)log.clone();
                currentLog.setId(System.currentTimeMillis());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            logList.add(currentLog);
        }
    }

    public void createLogFile(String logId){
        localLogRepository.createLogFile(logId);
        logList.clear();
        eventList.clear();
    }

    public void closeLogFile(Callback<List<Long>> callback){
        localLogRepository.closeLogFile();
        App.localStorage.saveEvents(eventList, new Callback<List<Long>>() {
            @Override
            public void onSuccess(List<Long> longs) {
                Logger.d("LocalStorage1 events " + longs.size());
            }

            @Override
            public void onFailure(String s) {

            }
        });
        App.localStorage.saveLogs(logList,callback);
    }

    public void addToLevelList(String level) {
        levelList.add(level);
        if (levelList.size() > 60){
            levelList.removeFirst();
        }
        updateLevelListEvent.call();
    }

    private void addToUploadThroughputList(long ulThroughput){
        uploadThroughputList.add(ulThroughput);
        if (uploadThroughputList.size() > 60){
            uploadThroughputList.removeFirst();
        }
        updateUploadThroughputListEvent.call();
    }

    private void addToDownloadThroughputList(long dlThroughput){
        downloadThroughputList.add(dlThroughput);
        if (downloadThroughputList.size() > 60){
            downloadThroughputList.removeFirst();
        }
        updateDownloadThroughputListEvent.call();
    }

    public void setUlThroughput(long ulThroughput) {
        synchronized (this){
            mLog.setUlThrput(ulThroughput);
            mLog.setLogState(logState);
            uploadThroughputLiveData.postValue(ulThroughput);
            addToUploadThroughputList(ulThroughput);
        }
    }
}
