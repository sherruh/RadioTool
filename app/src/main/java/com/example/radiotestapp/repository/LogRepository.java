package com.example.radiotestapp.repository;

import com.example.radiotestapp.enums.EEvents;
import com.example.radiotestapp.model.Log;
import com.example.radiotestapp.utils.Logger;

public class LogRepository {
    private Log mLog = new Log();
    private String mCellId;

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

    public void setCellId(String cellId){
        synchronized (this){
            mCellId = cellId;
        }
    }

    public String getCellId(){
        return mCellId;
    }
}
