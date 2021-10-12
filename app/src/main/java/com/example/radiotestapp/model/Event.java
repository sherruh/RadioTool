package com.example.radiotestapp.model;

import androidx.room.Entity;
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

    public String getParameter2() {
        return parameter2;
    }

    public void setParameter2(String parameter2) {
        this.parameter2 = parameter2;
    }

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

    public Event(String logId, EEvents event, long eventTime, String parameter, String parameter2, EState eState) {
        this.event = event;
        this.eventTime = eventTime;
        this.parameter = parameter;
        this.parameter2 = parameter2;
        this.logId = logId;
        this.state = eState;
    }

    public Event(String logId, EEvents event, long eventTime, String parameter, EState eState) {
        this.event = event;
        this.eventTime = eventTime;
        this.parameter = parameter;
        this.logId = logId;
        this.state = eState;
    }
}
