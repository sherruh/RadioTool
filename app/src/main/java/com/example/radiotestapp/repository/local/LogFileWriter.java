package com.example.radiotestapp.repository.local;

import android.content.Context;
import android.os.Environment;

import com.example.radiotestapp.App;
import com.example.radiotestapp.model.Event;
import com.example.radiotestapp.model.Log;
import com.example.radiotestapp.utils.Logger;
import com.example.radiotestapp.utils.Toaster;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;

public class LogFileWriter implements ILocalLogRepository {

    private final String LOG_FOLDER = "RadioTestAppLogs";
    private final String HEADER = "LogId\tDate\tlongitude\tlatitude\taltitude\tMCC\tMNC\tTechnology" +
            "\tTACLAC\tENodeB\tCID\tbsic\tPSC\tPCI\tRSRP\tRSRQ\tRSCP\tRxLevel\tCQI\tSNR\tEcN0\tBER" +
            "\tChannel\tDlThrput\tUlThrput\tping\tYoutubeQuality\tEvent\tEventParameter\tEventDescription" +
            "\tLogState";
    private File logFile;
    private String logName = "";
    private File folder = new File(Environment.getExternalStorageDirectory(), LOG_FOLDER);
    private FileOutputStream fileOutputStream;

    @Override
    public void saveEvent(Event event) {
        synchronized (this){
            if (logFile == null || fileOutputStream == null) return;
            Log log = new Log();
            log.seteEvent(event.getEvent());
            log.setEventParam(event.getParameter());
            log.setLogId(event.getLogId());
            log.setDate(event.getEventTime());
            log.setLogState(event.getState());
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
        logName = logId;
        if (folder.exists());
        else {
            String filepath = Environment.getExternalStorageDirectory().getPath();
            folder = new File(filepath,LOG_FOLDER);
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
            Toaster.showLong(App.context,"Log saved: " + LOG_FOLDER + "/" + logName + ".txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
