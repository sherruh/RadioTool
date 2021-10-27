package com.example.radiotestapp.repository.local;

import android.os.Environment;

import com.example.radiotestapp.App;
import com.example.radiotestapp.core.Constants;
import com.example.radiotestapp.model.Event;
import com.example.radiotestapp.model.Log;
import com.example.radiotestapp.model.SettingsParameter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class LogFileWriter implements ILocalLogRepository {

    private final String HEADER = "LogId\tDate\tlongitude\tlatitude\taltitude\tMCC\tMNC\tTechnology" +
            "\tTACLAC\tENodeB\tCID\tbsic\tPSC\tPCI\tRSRP\tRSRQ\tRSCP\tRxLevel\tCQI\tSNR\tEcN0\tBER" +
            "\tChannel\tDlThrput\tUlThrput\tping\tYoutubeQuality\tEvent\tEventParameter\tEventDescription" +
            "\tLogState\tYoutubeState";
    private File logFile;
    private String logName = "";
    private File folder = new File(Environment.getExternalStorageDirectory(), Constants.LOG_FOLDER);
    private FileOutputStream fileOutputStream;

    @Override
    public void saveEvent(Event event) {
        synchronized (this){
            if (logFile == null || fileOutputStream == null) return;
            Log log = new Log();
            log.setEEvent(event.getEvent());
            log.setEventParam(event.getParameter());
            log.setLogId(event.getLogId());
            log.setDate(event.getEventTime());
            log.setLogState(event.getState());
            log.setEventDescription(event.getParameter2());
            PrintStream printstream = new PrintStream(fileOutputStream);
            printstream.println(log);
        }
    }

    @Override
    public void saveLog(Log log) {
        synchronized (this){
            if (logFile == null || fileOutputStream == null) return;
            PrintStream printstream = new PrintStream(fileOutputStream);
            printstream.println (log);
        }
    }

    @Override
    public void createLogFile(String logId){
        SettingsParameter settingsParameter = App.localStorage.getSettingsParameter(Constants.IS_ALLOWED_SAVING_LOGS);
        if (settingsParameter == null || settingsParameter.getValue().equals(Constants.NO)) return;
        logName = logId;
        if (folder.exists());
        else {
            String filepath = Environment.getExternalStorageDirectory().getPath();
            folder = new File(filepath,Constants.LOG_FOLDER);
            if (!folder.mkdir()) return;
        }
        logFile = new File(folder.getPath() + "/" + logId + ".txt");
        try {
            logFile.createNewFile();
            fileOutputStream = new FileOutputStream(logFile,true);
            PrintStream printstream = new PrintStream(fileOutputStream);
            printstream.println(HEADER);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void closeLogFile() {
        if (fileOutputStream == null) return;
        try {
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
