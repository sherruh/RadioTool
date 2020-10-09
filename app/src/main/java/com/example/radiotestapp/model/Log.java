package com.example.radiotestapp.model;

import com.example.radiotestapp.enums.EEvents;
import com.example.radiotestapp.enums.ESTATE;

public class Log {
    private String logId;
    private long date;
    private double longitude;
    private double latitude;
    private String mcc;
    private String mnc;
    private String technology;
    private String cellId;
    private String rsrp;
    private String rscp;
    private String rxLevel;
    private String cqi;
    private String snr;
    private String ecNO;
    private String ber;
    private long dlThrput;
    private long ulThrput;
    private int ping;
    private EEvents eEvent;
    private String eventParam;
    private String eventDescription;
    private ESTATE logState;

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setMcc(String mcc) {
        this.mcc = mcc;
    }

    public void setMnc(String mnc) {
        this.mnc = mnc;
    }

    public void setTechnology(String technology) {
        this.technology = technology;
    }

    public void setRsrp(String rsrp) {
        this.rsrp = rsrp;
    }

    public void setRscp(String rscp) {
        this.rscp = rscp;
    }

    public void setRxLevel(String rxLevel) {
        this.rxLevel = rxLevel;
    }

    public void setCqi(String cqi) {
        this.cqi = cqi;
    }

    public void setSnr(String snr) {
        this.snr = snr;
    }

    public void setEcNO(String ecNO) {
        this.ecNO = ecNO;
    }

    public void setBer(String ber) {
        this.ber = ber;
    }

    public void setDlThrput(long dlThrput) {
        this.dlThrput = dlThrput;
    }

    public void setUlThrput(long ulThrput) {
        this.ulThrput = ulThrput;
    }

    public void setPing(int ping) {
        this.ping = ping;
    }

    public Log() {
    }
    public EEvents geteEvent() {
        return eEvent;
    }

    public void seteEvent(EEvents eEvent) {
        this.eEvent = eEvent;
    }

    public String getEventParam() {
        return eventParam;
    }

    public void setEventParam(String eventParam) {
        this.eventParam = eventParam;
    }

    public Log(String logId, long date, double longitude, double latitude, String mcc, String mnc,
               String technology, String rsrp, String rscp, String rxLevel, String cqi, String snr,
               String ecNO, String ber, long dlThrput, long ulThrput, int ping) {
        this.logId = logId;
        this.date = date;
        this.longitude = longitude;
        this.latitude = latitude;
        this.mcc = mcc;
        this.mnc = mnc;
        this.technology = technology;
        this.rsrp = rsrp;
        this.rscp = rscp;
        this.rxLevel = rxLevel;
        this.cqi = cqi;
        this.snr = snr;
        this.ecNO = ecNO;
        this.ber = ber;
        this.dlThrput = dlThrput;
        this.ulThrput = ulThrput;
        this.ping = ping;
    }

    public String getLogId() {
        return logId;
    }

    public long getDate() {
        return date;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public String getMcc() {
        return mcc;
    }

    public String getMnc() {
        return mnc;
    }

    public String getTechnology() {
        return technology;
    }

    public String getRsrp() {
        return rsrp;
    }

    public String getRscp() {
        return rscp;
    }

    public String getRxLevel() {
        return rxLevel;
    }

    public String getCqi() {
        return cqi;
    }

    public String getSnr() {
        return snr;
    }

    public String getEcNO() {
        return ecNO;
    }

    public String getBer() {
        return ber;
    }

    public long getDlThrput() {
        return dlThrput;
    }

    public long getUlThrput() {
        return ulThrput;
    }

    public int getPing() {
        return ping;
    }

    public String getCellId() {
        return cellId;
    }

    public void setCellId(String cellId) {
        this.cellId = cellId;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public ESTATE getLogState() {
        return logState;
    }

    public void setLogState(ESTATE logState) {
        this.logState = logState;
    }
}
