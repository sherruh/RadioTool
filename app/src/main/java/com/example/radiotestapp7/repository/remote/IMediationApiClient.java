package com.example.radiotestapp7.repository.remote;

import com.example.radiotestapp7.model.LogResult;

public interface IMediationApiClient {
    public void sendLogResult(LogResult logResult, ApiCallback callback);
}

