package com.example.radiotestapp.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "log_result")
public class LogResult {
    @PrimaryKey
    @NonNull
    private String id;

    private boolean isYoutubeTested;
    private boolean isDownloadTested;

    public LogResult() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isYoutubeTested() {
        return isYoutubeTested;
    }

    public void setYoutubeTested(boolean youtubeTested) {
        isYoutubeTested = youtubeTested;
    }

    public boolean isDownloadTested() {
        return isDownloadTested;
    }

    public void setDownloadTested(boolean downloadTested) {
        isDownloadTested = downloadTested;
    }

    public boolean isUploadTested() {
        return isUploadTested;
    }

    public void setUploadTested(boolean uploadTested) {
        isUploadTested = uploadTested;
    }

    public String getRsrp() {
        return rsrp;
    }

    public void setRsrp(String rsrp) {
        this.rsrp = rsrp;
    }

    public String getRscp() {
        return rscp;
    }

    public void setRscp(String rscp) {
        this.rscp = rscp;
    }

    public String getRxLvl() {
        return rxLvl;
    }

    public void setRxLvl(String rxLvl) {
        this.rxLvl = rxLvl;
    }

    public String getSnr() {
        return snr;
    }

    public void setSnr(String snr) {
        this.snr = snr;
    }

    public String getEcN0() {
        return ecN0;
    }

    public void setEcN0(String ecN0) {
        this.ecN0 = ecN0;
    }

    public String getCI() {
        return cI;
    }

    public void setCI(String cI) {
        this.cI = cI;
    }

    public String getLteCqi() {
        return lteCqi;
    }

    public void setLteCqi(String lteCqi) {
        this.lteCqi = lteCqi;
    }

    public String getRsrq() {
        return rsrq;
    }

    public void setRsrq(String rsrq) {
        this.rsrq = rsrq;
    }

    public String getUmtsCqi() {
        return umtsCqi;
    }

    public void setUmtsCqi(String umtsCqi) {
        this.umtsCqi = umtsCqi;
    }

    public String getFirstRatio() {
        return firstRatio;
    }

    public void setFirstRatio(String firstRatio) {
        this.firstRatio = firstRatio;
    }

    public String getFirstTech() {
        return firstTech;
    }

    public void setFirstTech(String firstTech) {
        this.firstTech = firstTech;
    }

    public String getFirstTacLac() {
        return firstTacLac;
    }

    public void setFirstTacLac(String firstTacLac) {
        this.firstTacLac = firstTacLac;
    }

    public String getFirstENodeB() {
        return firstENodeB;
    }

    public void setFirstENodeB(String firstENodeB) {
        this.firstENodeB = firstENodeB;
    }

    public String getFirstCid() {
        return firstCid;
    }

    public void setFirstCid(String firstCid) {
        this.firstCid = firstCid;
    }

    public String getSecondRatio() {
        return secondRatio;
    }

    public void setSecondRatio(String secondRatio) {
        this.secondRatio = secondRatio;
    }

    public String getSecondTech() {
        return secondTech;
    }

    public void setSecondTech(String secondTech) {
        this.secondTech = secondTech;
    }

    public String getSecondTacLac() {
        return secondTacLac;
    }

    public void setSecondTacLac(String secondTacLac) {
        this.secondTacLac = secondTacLac;
    }

    public String getSecondENodeB() {
        return secondENodeB;
    }

    public void setSecondENodeB(String secondENodeB) {
        this.secondENodeB = secondENodeB;
    }

    public String getSecondCid() {
        return secondCid;
    }

    public void setSecondCid(String secondCid) {
        this.secondCid = secondCid;
    }

    public String getThirdRatio() {
        return thirdRatio;
    }

    public void setThirdRatio(String thirdRatio) {
        this.thirdRatio = thirdRatio;
    }

    public String getThirdTech() {
        return thirdTech;
    }

    public void setThirdTech(String thirdTech) {
        this.thirdTech = thirdTech;
    }

    public String getThirdTacLac() {
        return thirdTacLac;
    }

    public void setThirdTacLac(String thirdTacLac) {
        this.thirdTacLac = thirdTacLac;
    }

    public String getThirdENodeB() {
        return thirdENodeB;
    }

    public void setThirdENodeB(String thirdENodeB) {
        this.thirdENodeB = thirdENodeB;
    }

    public String getThirdCid() {
        return thirdCid;
    }

    public void setThirdCid(String thirdCid) {
        this.thirdCid = thirdCid;
    }

    public String getBufferTime() {
        return bufferTime;
    }

    public void setBufferTime(String bufferTime) {
        this.bufferTime = bufferTime;
    }

    public String getBufferThroughput() {
        return bufferThroughput;
    }

    public void setBufferThroughput(String bufferThroughput) {
        this.bufferThroughput = bufferThroughput;
    }

    public String getBufferSR() {
        return bufferSR;
    }

    public void setBufferSR(String bufferSR) {
        this.bufferSR = bufferSR;
    }

    public String getInitTime() {
        return initTime;
    }

    public void setInitTime(String initTime) {
        this.initTime = initTime;
    }

    public String getInitSR() {
        return initSR;
    }

    public void setInitSR(String initSR) {
        this.initSR = initSR;
    }

    public String getYoutubeSR() {
        return youtubeSR;
    }

    public void setYoutubeSR(String youtubeSR) {
        this.youtubeSR = youtubeSR;
    }

    public String getResolution144() {
        return resolution144;
    }

    public void setResolution144(String resolution144) {
        this.resolution144 = resolution144;
    }

    public String getResolution240() {
        return resolution240;
    }

    public void setResolution240(String resolution240) {
        this.resolution240 = resolution240;
    }

    public String getResolution360() {
        return resolution360;
    }

    public void setResolution360(String resolution360) {
        this.resolution360 = resolution360;
    }

    public String getResolution480() {
        return resolution480;
    }

    public void setResolution480(String resolution480) {
        this.resolution480 = resolution480;
    }

    public String getResolution720() {
        return resolution720;
    }

    public void setResolution720(String resolution720) {
        this.resolution720 = resolution720;
    }

    public String getResolution1080() {
        return resolution1080;
    }

    public void setResolution1080(String resolution1080) {
        this.resolution1080 = resolution1080;
    }

    public String getDownThrput() {
        return downThrput;
    }

    public void setDownThrput(String downThrput) {
        this.downThrput = downThrput;
    }

    public String getDownSR() {
        return downSR;
    }

    public void setDownSR(String downSR) {
        this.downSR = downSR;
    }

    public String getUploadThrput() {
        return uploadThrput;
    }

    public void setUploadThrput(String uploadThrput) {
        this.uploadThrput = uploadThrput;
    }

    public String getUploadSR() {
        return uploadSR;
    }

    public void setUploadSR(String uploadSR) {
        this.uploadSR = uploadSR;
    }

    public LogResult(String id, boolean isYoutubeTested, boolean isDownloadTested, boolean isUploadTested, String rsrp, String rscp, String rxLvl, String snr, String ecN0, String cI, String lteCqi, String rsrq, String umtsCqi, String firstRatio, String firstTech, String firstTacLac, String firstENodeB, String firstCid, String secondRatio, String secondTech, String secondTacLac, String secondENodeB, String secondCid, String thirdRatio, String thirdTech, String thirdTacLac, String thirdENodeB, String thirdCid, String bufferTime, String bufferThroughput, String bufferSR, String initTime, String initSR, String youtubeSR, String resolution144, String resolution240, String resolution360, String resolution480, String resolution720, String resolution1080, String downThrput, String downSR, String uploadThrput, String uploadSR) {
        this.id = id;
        this.isYoutubeTested = isYoutubeTested;
        this.isDownloadTested = isDownloadTested;
        this.isUploadTested = isUploadTested;
        this.rsrp = rsrp;
        this.rscp = rscp;
        this.rxLvl = rxLvl;
        this.snr = snr;
        this.ecN0 = ecN0;
        this.cI = cI;
        this.lteCqi = lteCqi;
        this.rsrq = rsrq;
        this.umtsCqi = umtsCqi;
        this.firstRatio = firstRatio;
        this.firstTech = firstTech;
        this.firstTacLac = firstTacLac;
        this.firstENodeB = firstENodeB;
        this.firstCid = firstCid;
        this.secondRatio = secondRatio;
        this.secondTech = secondTech;
        this.secondTacLac = secondTacLac;
        this.secondENodeB = secondENodeB;
        this.secondCid = secondCid;
        this.thirdRatio = thirdRatio;
        this.thirdTech = thirdTech;
        this.thirdTacLac = thirdTacLac;
        this.thirdENodeB = thirdENodeB;
        this.thirdCid = thirdCid;
        this.bufferTime = bufferTime;
        this.bufferThroughput = bufferThroughput;
        this.bufferSR = bufferSR;
        this.initTime = initTime;
        this.initSR = initSR;
        this.youtubeSR = youtubeSR;
        this.resolution144 = resolution144;
        this.resolution240 = resolution240;
        this.resolution360 = resolution360;
        this.resolution480 = resolution480;
        this.resolution720 = resolution720;
        this.resolution1080 = resolution1080;
        this.downThrput = downThrput;
        this.downSR = downSR;
        this.uploadThrput = uploadThrput;
        this.uploadSR = uploadSR;
    }

    private boolean isUploadTested;

    private String rsrp;
    private String rscp;
    private String rxLvl;
    private String snr;
    private String ecN0;
    private String cI;
    private String lteCqi;
    private String rsrq;
    private String umtsCqi;
    private String firstRatio;
    private String firstTech;
    private String firstTacLac;
    private String firstENodeB;
    private String firstCid;
    private String secondRatio;
    private String secondTech;
    private String secondTacLac;
    private String secondENodeB;
    private String secondCid;
    private String thirdRatio;
    private String thirdTech;
    private String thirdTacLac;
    private String thirdENodeB;
    private String thirdCid;
    private String bufferTime;
    private String bufferThroughput;
    private String bufferSR;
    private String initTime;
    private String initSR;
    private String youtubeSR;
    private String resolution144;
    private String resolution240;
    private String resolution360;
    private String resolution480;
    private String resolution720;
    private String resolution1080;
    private String downThrput;
    private String downSR;
    private String uploadThrput;
    private String uploadSR;

}
