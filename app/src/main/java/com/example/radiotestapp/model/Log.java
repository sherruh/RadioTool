package com.example.radiotestapp.model;

public class Log {
    private String logId;
    private long date;
    private double longitude;
    private double latitude;
    private String mcc;
    private String mnc;
    private String technology;
    private String rsrp;
    private String rscp;
    private String rxLevel;
    private String cqi;
    private String snr;
    private String ecNO;

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

    public void setDlThrput(String dlThrput) {
        this.dlThrput = dlThrput;
    }

    public void setUlThrput(String ulThrput) {
        this.ulThrput = ulThrput;
    }

    public void setPing(String ping) {
        this.ping = ping;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    private String ber;
    private String dlThrput;
    private String ulThrput;
    private String ping;
    private String event;

    public Log() {
    }

    public Log(String logId, long date, double longitude, double latitude, String mcc, String mnc, String technology, String rsrp, String rscp, String rxLevel, String cqi, String snr, String ecNO, String ber, String dlThrput, String ulThrput, String ping, String event) {
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
        this.event = event;
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

    public String getDlThrput() {
        return dlThrput;
    }

    public String getUlThrput() {
        return ulThrput;
    }

    public String getPing() {
        return ping;
    }

    public String getEvent() {
        return event;
    }
}
