package com.example.radiotestapp7.repository.remote;


import com.example.radiotestapp7.model.LogResult;

public class RemoteRepository {
    private IMediationApiClient mediationApiClient;

    public RemoteRepository() {
        mediationApiClient = new MediationApiClient();
    }

    public void sendLogResult(LogResult logResult, ApiCallback callback){
        mediationApiClient.sendLogResult(logResult,callback);
    }
}
