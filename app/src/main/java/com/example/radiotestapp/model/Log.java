package com.example.radiotestapp.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.radiotestapp.enums.EEvents;
import com.example.radiotestapp.enums.EState;
import com.example.radiotestapp.enums.EYoutubeState;
import com.example.radiotestapp.utils.DateConverter;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "log")
public class Log implements Cloneable {
    @SerializedName("id")
    @Expose
    @PrimaryKey
    private long id;

    @SerializedName("logId")
    @Expose
    private String logId;
    @SerializedName("date")
    @Expose
    private long date;
    @SerializedName("longitude")
    @Expose
    private double longitude;
    private double latitude;
    private int altitude;
    private String mcc;
    private String mnc;
    private String technology;
    private String tacLac;
    private String eNodeB;
    private String cellId;
    private String bsic;
    private String psc;
    private String pci;
    private String rsrp;
    private String rsrq;
    private String rscp;
    private String rxLevel;
    private String cqi;
    private String snr;
    private String ecNO;
    private String ber;
    private String channel;
    private long dlThrput;
    private long ulThrput;
    private int ping;
    private String youtubeResolution;
    private EEvents eEvent;
    private String eventParam;
    private String eventDescription;
    private EState logState;
    private EYoutubeState youtubeState;

    private boolean isUploaded;

    public boolean isUploaded() {
        return isUploaded;
    }

    public void setUploaded(boolean uploaded) {
        isUploaded = uploaded;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public Log(long id, String logId, long date, double longitude, double latitude, int altitude, String mcc, String mnc, String technology, String tacLac, String eNodeB, String cellId, String bsic, String psc, String pci, String rsrp, String rsrq, String rscp, String rxLevel, String cqi, String snr, String ecNO, String ber, String channel, long dlThrput, long ulThrput, int ping, String youtubeResolution, EEvents eEvent, String eventParam, String eventDescription, EState logState, EYoutubeState youtubeState) {
        this.id = id;
        this.logId = logId;
        this.date = date;
        this.longitude = longitude;
        this.latitude = latitude;
        this.altitude = altitude;
        this.mcc = mcc;
        this.mnc = mnc;
        this.technology = technology;
        this.tacLac = tacLac;
        this.eNodeB = eNodeB;
        this.cellId = cellId;
        this.bsic = bsic;
        this.psc = psc;
        this.pci = pci;
        this.rsrp = rsrp;
        this.rsrq = rsrq;
        this.rscp = rscp;
        this.rxLevel = rxLevel;
        this.cqi = cqi;
        this.snr = snr;
        this.ecNO = ecNO;
        this.ber = ber;
        this.channel = channel;
        this.dlThrput = dlThrput;
        this.ulThrput = ulThrput;
        this.ping = ping;
        this.youtubeResolution = youtubeResolution;
        this.eEvent = eEvent;
        this.eventParam = eventParam;
        this.eventDescription = eventDescription;
        this.logState = logState;
        this.youtubeState = youtubeState;
    }

    public EYoutubeState getYoutubeState() {
        return youtubeState;
    }

    public void setYoutubeState(EYoutubeState youtubeState) {
        this.youtubeState = youtubeState;
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

    public String getTacLac() {
        return tacLac;
    }

    public void setTacLac(String tacLac) {
        this.tacLac = tacLac;
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

    public String getPsc() {
        return psc;
    }

    public void setPsc(String psc) {
        this.psc = psc;
    }

    public String getPci() {
        return pci;
    }

    public void setPci(String pci) {
        this.pci = pci;
    }

    @Override
    public String toString() {
        return logId + '\t' + DateConverter.dateWithMillis(date) + '\t' + longitude + '\t' + latitude + '\t' + altitude + '\t'
                + mcc + '\t' + mnc + '\t' + technology + '\t' + tacLac + '\t' + eNodeB + '\t'
                + cellId + '\t' + bsic + '\t' + psc + '\t' + pci + '\t' + rsrp + '\t'
                + rsrq + '\t' + rscp + '\t' + rxLevel + '\t' + cqi + '\t' + snr + '\t'
                + ecNO + '\t' + ber + '\t' + channel + '\t' + dlThrput + '\t' +  ulThrput + '\t'
                + ping +'\t' + youtubeResolution + '\t' + eEvent + '\t' + eventParam + '\t'
                + eventDescription + '\t' +  logState + '\t' + youtubeState;
    }

    public String getYoutubeResolution() {
        return youtubeResolution;
    }

    public void setYoutubeResolution(String youtubeResolution) {
        this.youtubeResolution = youtubeResolution;
    }

    public String getENodeB() {
        return eNodeB;
    }

    public void setENodeB(String eNodeB) {
        this.eNodeB = eNodeB;
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

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public Log() {
    }
    public EEvents getEEvent() {
        return eEvent;
    }

    public void setEEvent(EEvents eEvent) {
        this.eEvent = eEvent;
    }

    public String getEventParam() {
        return eventParam;
    }

    public void setEventParam(String eventParam) {
        this.eventParam = eventParam;
    }

    public String getRsrq() {
        return rsrq;
    }

    public void setRsrq(String rsrq) {
        this.rsrq = rsrq;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Log(int id, String logId, long date, double longitude, double latitude, int altitude,
               String mcc, String mnc, String technology, String tacLac, String eNodeB,
               String cellId, String bsic, String psc, String pci, String rsrp, String rsrq,
               String rscp, String rxLevel, String cqi, String snr, String ecNO, String ber,
               String channel, long dlThrput, long ulThrput, int ping, String youtubeResolution,
               EEvents eEvent, String eventParam, String eventDescription, EState logState) {
        this.id = id;
        this.logId = logId;
        this.date = date;
        this.longitude = longitude;
        this.latitude = latitude;
        this.altitude = altitude;
        this.mcc = mcc;
        this.mnc = mnc;
        this.technology = technology;
        this.tacLac = tacLac;
        this.eNodeB = eNodeB;
        this.cellId = cellId;
        this.bsic = bsic;
        this.psc = psc;
        this.pci = pci;
        this.rsrp = rsrp;
        this.rsrq = rsrq;
        this.rscp = rscp;
        this.rxLevel = rxLevel;
        this.cqi = cqi;
        this.snr = snr;
        this.ecNO = ecNO;
        this.ber = ber;
        this.channel = channel;
        this.dlThrput = dlThrput;
        this.ulThrput = ulThrput;
        this.ping = ping;
        this.youtubeResolution = youtubeResolution;
        this.eEvent = eEvent;
        this.eventParam = eventParam;
        this.eventDescription = eventDescription;
        this.logState = logState;
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

    public EState getLogState() {
        return logState;
    }

    public void setLogState(EState logState) {
        this.logState = logState;
    }

    public int getAltitude() {
        return altitude;
    }

    public void setAltitude(int altitude) {
        this.altitude = altitude;
    }

    public String getBsic() {
        return bsic;
    }

    public void setBsic(String bsic) {
        this.bsic = bsic;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}
