package com.example.radiotestapp.repository.remote;

import com.example.radiotestapp.model.Event;
import com.example.radiotestapp.model.Log;
import com.example.radiotestapp.model.LogResult;
import com.example.radiotestapp.repository.Callback;

public interface IApiClient {
    public void sendLog(Log log, Callback<String> callback);
    public void sendEvent(Event event, Callback<String> callback);
    public void sendLogResult(LogResult logResult, Callback<String> callback);
}
