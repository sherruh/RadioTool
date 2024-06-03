package com.example.radiotestapp.repository.remote;

import com.example.radiotestapp.model.LogResult;

public interface IMediationApiClient {
    public void sendLogResult(LogResult logResult, ApiCallback callback);
}

