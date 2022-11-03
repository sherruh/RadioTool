package com.example.radiotestapp.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.radiotestapp.enums.EEvents;
import com.example.radiotestapp.enums.EState;

@Entity(tableName = "event")
public class Event implements Cloneable {
    @PrimaryKey
    private long id;

    public long getId() {
        return id;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public void setId(long id) {
        this.id = id;
    }



    private EEvents event;
    private long eventTime;
    private String parameter;
    private String parameter2;
    private boolean isUploaded = false;

    public boolean isUploaded() {
        return isUploaded;
    }

    public Event(long id, EEvents event, long eventTime, String parameter, String parameter2, boolean isUploaded, String logId, EState state) {
        this.id = id;
        this.event = event;
        this.eventTime = eventTime;
        this.parameter = parameter;
        this.parameter2 = parameter2;
        this.isUploaded = isUploaded;
        this.logId = logId;
        this.state = state;
    }

    public void setUploaded(boolean uploaded) {
        isUploaded = uploaded;
    }

    public String getParameter2() {
        return parameter2;
    }

    public void setParameter2(String parameter2) {
        this.parameter2 = parameter2;
    }

    @Ignore
    public Event(long id, EEvents event, long eventTime, String parameter, String parameter2, String logId, EState state) {
        this.id = id;
        this.event = event;
        this.eventTime = eventTime;
        this.parameter = parameter;
        this.parameter2 = parameter2;
        this.logId = logId;
        this.state = state;
    }

    private String logId;
    private EState state;

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public EEvents getEvent() {
        return event;
    }

    public void setEvent(EEvents event) {
        this.event = event;
    }

    public long getEventTime() {
        return eventTime;
    }

    public void setEventTime(long eventTime) {
        this.eventTime = eventTime;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public EState getState() {
        return state;
    }

    public void setState(EState state) {
        this.state = state;
    }

    @Ignore
    public Event(String logId, EEvents event, long eventTime, String parameter, String parameter2, EState eState) {
        this.event = event;
        this.eventTime = eventTime;
        this.parameter = parameter;
        this.parameter2 = parameter2;
        this.logId = logId;
        this.state = eState;
    }

    @Ignore
    public Event(String logId, EEvents event, long eventTime, String parameter, EState eState) {
        this.event = event;
        this.eventTime = eventTime;
        this.parameter = parameter;
        this.logId = logId;
        this.state = eState;
    }
}
