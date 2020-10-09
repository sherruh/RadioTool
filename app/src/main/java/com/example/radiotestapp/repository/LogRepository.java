package com.example.radiotestapp.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.radiotestapp.enums.EEvents;
import com.example.radiotestapp.enums.ESTATE;
import com.example.radiotestapp.model.Log;
import com.example.radiotestapp.utils.Logger;

public class LogRepository {
    public MutableLiveData<Long> thrp = new MutableLiveData<>();
    private Log mLog = new Log();
    private String mCellId;

    public ESTATE getLogState() {
        return logState;
    }

    public void setLogState(ESTATE logState) {
        this.logState = logState;
    }

    private ESTATE logState;

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

    public void setDlThroughput(long dlThroughput){
        synchronized (this){
            mLog.setDlThrput(dlThroughput);
            mLog.setLogState(logState);
            thrp.postValue(dlThroughput);
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
