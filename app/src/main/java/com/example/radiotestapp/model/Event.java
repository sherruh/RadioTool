package com.example.radiotestapp.model;

import com.example.radiotestapp.enums.EEvents;
import com.example.radiotestapp.enums.EState;

public class Event {
    private EEvents event;
    private long eventTime;
    private String parameter;
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

    public Event(String logId, EEvents event, long eventTime, String parameter, EState eState) {
        this.event = event;
        this.eventTime = eventTime;
        this.parameter = parameter;
        this.logId = logId;
        this.state = eState;
    }
}
