package com.example.radiotestapp.repository;

import com.example.radiotestapp.enums.EEvents;
import com.example.radiotestapp.model.Log;
import com.example.radiotestapp.utils.Logger;

public class LogRepository {
    private Log mLog = new Log();
    private EEvents mEvent;
    private String mEventParam;
    private String mEventDescription;
    private String mCellId;

    public void setLog(Log log){
        synchronized (this){
            mLog = log;
            mLog.seteEvent(mEvent);
            mLog.setEventParam(mEventParam);
            mLog.setEventDescription(mEventDescription);
            mLog.setCellId(mCellId);
        }
    }

    public Log getLog(){
        mLog.seteEvent(mEvent);
        mLog.setEventParam(mEventParam);
        mLog.setEventDescription(mEventDescription);
        return mLog;
    }

    public void setDate(long date){
        synchronized (this){
            mLog.setDate(date);
        }
    }

    public void setEvent(EEvents event, String eventParam, String eventDescription){
        synchronized (this){
            Logger.d("Youtube + " + event);
            mEvent = event;
            mEventParam = eventParam;
            mEventDescription = eventDescription;
        }
    }

    public void clearLastEvent() {
        synchronized (this){
            mLog.seteEvent(null);
            mLog.setEventParam(null);
            mLog.setEventDescription(null);
            mEvent = null;
            mEventParam = null;
            mEventDescription = null;
        }
    }

    public void setCellId(String cellId){
        synchronized (this){
            mCellId = cellId;
        }
    }

    public String getCellId(){
        return mCellId;
    }
}
