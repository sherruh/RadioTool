package com.example.radiotestapp.repository.remote;

import com.example.radiotestapp.model.Event;
import com.example.radiotestapp.model.Log;
import com.example.radiotestapp.model.LogResult;
import com.example.radiotestapp.repository.Callback;
import com.example.radiotestapp.utils.Logger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public class ApiClient implements IApiClient {

    private final String BASE_URL = "https://r-mediation.o.kg/";

    private OkHttpClient okHttpClient = new OkHttpClient()
            .newBuilder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();

    private HttpLoggingInterceptor provideLoggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        return httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build();

    RadioTestOnlineClient client = retrofit.create(RadioTestOnlineClient.class);

    @Override
    public void sendLog(Log log, Callback<String> callback) {
        HashMap<String,String> logMap = new HashMap<>();
        logMap.put("altitude", String.valueOf(log.getAltitude()));//5
        logMap.put("BER", String.valueOf(log.getBer()));//20
        logMap.put("bsic", String.valueOf(log.getBsic()));//11
        logMap.put("CID", String.valueOf(log.getCellId()));//10
        logMap.put("Channel", String.valueOf(log.getChannel()));//21
        logMap.put("CQI", String.valueOf(log.getCqi()));//18
        logMap.put("date", String.valueOf(log.getDate()));//2
        logMap.put("DlThrput", String.valueOf(log.getDlThrput()));//22
        logMap.put("ENodeB", String.valueOf(log.getENodeB()));//9
        logMap.put("EcNO", String.valueOf(log.getEcNO()));//19
        logMap.put("latitude", String.valueOf(log.getLatitude()));//4
        logMap.put("LogId", String.valueOf(log.getLogId()));//1
        logMap.put("logState", String.valueOf(log.getLogState()));//30
        logMap.put("longitude", String.valueOf(log.getLongitude()));//3
        logMap.put("MCC", String.valueOf(log.getMcc()));//6
        logMap.put("MNC", String.valueOf(log.getMnc()));//7
        logMap.put("PCI", String.valueOf(log.getPci()));//13
        logMap.put("ping", String.valueOf(log.getPing()));//24
        logMap.put("PSC", String.valueOf(log.getPsc()));//12
        logMap.put("RSCP", String.valueOf(log.getRscp()));//16
        logMap.put("RSRP", String.valueOf(log.getRsrp()));//14
        logMap.put("RSRQ", String.valueOf(log.getRsrq()));//15
        logMap.put("RxLevel", String.valueOf(log.getRxLevel()));//16
        logMap.put("SNR", String.valueOf(log.getSnr()));//17
        logMap.put("TACLAC", String.valueOf(log.getTacLac()));//8
        logMap.put("Technology", String.valueOf(log.getTechnology()));//7
        logMap.put("UlThrput", String.valueOf(log.getUlThrput()));//23
        logMap.put("youtubeState", String.valueOf(log.getYoutubeState()));//31
        logMap.put("YoutubeQuality", String.valueOf(log.getYoutubeResolution()));//25
        logMap.put("Event", " ");//26
        logMap.put("EventParameter", " ");//27
        logMap.put("EventDescription", " ");//28

        Logger.d("ResponseServer sending " + logMap.toString());

        Call<String> call = client.sendLog("UE_LOG_CD_POST",logMap);
        call.enqueue(new retrofit2.Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    if (response.body() !=null){
                        Logger.d("ResponseServer not null" + response.body());
                        callback.onSuccess("");
                    }else {
                        Logger.d("ResponseServer is null" + response.message());
                    }
                }else {
                    Logger.d("ResponseServer error " + response.code());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Logger.d("ResponseServer Failure" + t.getMessage());
                callback.onFailure(t.getMessage());
            }
        });
    }

    @Override
    public void sendEvent(Event event, Callback<String> callback) {
        HashMap<String,String> eventMap = new HashMap<>();

        eventMap.put("altitude", "");//5
        eventMap.put("BER", "");//20
        eventMap.put("bsic", "");//11
        eventMap.put("CID", "");//10
        eventMap.put("Channel", "");//21
        eventMap.put("CQI", "");//18
        eventMap.put("Date", String.valueOf(event.getEventTime()));//2
        eventMap.put("DlThrput", "");//22
        eventMap.put("ENodeB", "");//9
        eventMap.put("EcNO", "");//19
        eventMap.put("latitude", "");//4
        eventMap.put("LogId", String.valueOf(event.getLogId()));//1
        eventMap.put("logState", String.valueOf(event.getState()));//30
        eventMap.put("longitude", "");//3
        eventMap.put("MCC", "");//6
        eventMap.put("MNC", "");//7
        eventMap.put("PCI", "");//13
        eventMap.put("ping", "");//24
        eventMap.put("PSC", "");//12
        eventMap.put("RSCP", "");//16
        eventMap.put("RSRP", "");//14
        eventMap.put("RSRQ", "");//15
        eventMap.put("RxLevel", "");//16
        eventMap.put("SNR", "");//17
        eventMap.put("TACLAC", "");//8
        eventMap.put("Technology",  "");//7
        eventMap.put("UlThrput", "");//23
        eventMap.put("youtubeState", "");//31
        eventMap.put("YoutubeQuality", "");//25
        eventMap.put("Event", event.getEvent().toString());//26
        eventMap.put("EventParameter", event.getParameter());//27
        eventMap.put("EventDescription", event.getParameter2());//28

        Call<String> call = client.sendLog("UE_LOG_CD_POST",eventMap);
        call.enqueue(new retrofit2.Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    if (response.body() !=null){
                        Logger.d("ResponseServer Event not null" + response.body());
                        callback.onSuccess("");
                    }else {
                        Logger.d("ResponseServer Event is null" + response.message());
                    }
                }else {
                    Logger.d("ResponseServer Event error " + response.code());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Logger.d("ResponseServer Event Failure" + t.getMessage());
                callback.onFailure(t.getMessage());
            }
        });
    }

    @Override
    public void sendLogResult(LogResult logResult, Callback<String> callback) {
        HashMap<String,String> logResultMap = new HashMap<>();

        logResultMap.put("id", logResult.getId());//1
        logResultMap.put("isYoutubeTested", String.valueOf(logResult.isYoutubeTested()));
        logResultMap.put("isDownloadTested", String.valueOf(logResult.isDownloadTested()));
        logResultMap.put("isUploadTested", String.valueOf(logResult.isUploadTested()));
        logResultMap.put("rsrp", String.valueOf(logResult.getRsrp()));
        logResultMap.put("rscp", String.valueOf(logResult.getRscp()));
        logResultMap.put("rxLvl", String.valueOf(logResult.getRxLvl()));
        logResultMap.put("snr", String.valueOf(logResult.getSnr()));
        logResultMap.put("ecN0", String.valueOf(logResult.getEcN0()));
        logResultMap.put("cI", String.valueOf(logResult.getCI()));
        logResultMap.put("lteCqi", String.valueOf(logResult.getLteCqi()));
        logResultMap.put("rsrq", String.valueOf(logResult.getRsrq()));
        logResultMap.put("umtsCqi", String.valueOf(logResult.getUmtsCqi()));
        logResultMap.put("firstRatio", String.valueOf(logResult.getFirstRatio()));
        logResultMap.put("firstTech", String.valueOf(logResult.getFirstTech()));
        logResultMap.put("firstTacLac", String.valueOf(logResult.getFirstTacLac()));
        logResultMap.put("firstENodeB", String.valueOf(logResult.getFirstENodeB()));
        logResultMap.put("firstCid", String.valueOf(logResult.getFirstCid()));
        logResultMap.put("secondRatio", String.valueOf(logResult.getSecondRatio()));
        logResultMap.put("secondTech", String.valueOf(logResult.getSecondTech()));
        logResultMap.put("secondTacLac", String.valueOf(logResult.getSecondTacLac()));
        logResultMap.put("secondENodeB", String.valueOf(logResult.getSecondENodeB()));
        logResultMap.put("secondCid", String.valueOf(logResult.getSecondCid()));
        logResultMap.put("thirdRatio", String.valueOf(logResult.getThirdRatio()));
        logResultMap.put("thirdTech", String.valueOf(logResult.getThirdTech()));
        logResultMap.put("thirdTacLac", String.valueOf(logResult.getThirdTacLac()));
        logResultMap.put("thirdENodeB", String.valueOf(logResult.getThirdENodeB()));
        logResultMap.put("thirdCid", String.valueOf(logResult.getThirdCid()));
        logResultMap.put("bufferTime", String.valueOf(logResult.getBufferTime()));
        logResultMap.put("bufferThroughput", String.valueOf(logResult.getBufferThroughput()));
        logResultMap.put("bufferSR", String.valueOf(logResult.getBufferSR()));
        logResultMap.put("initTime", String.valueOf(logResult.getInitTime()));
        logResultMap.put("initSR", String.valueOf(logResult.getInitSR()));
        logResultMap.put("youtubeSR", String.valueOf(logResult.getYoutubeSR()));
        logResultMap.put("resolution144", String.valueOf(logResult.getResolution144()));
        logResultMap.put("resolution240", String.valueOf(logResult.getResolution240()));
        logResultMap.put("resolution360", String.valueOf(logResult.getResolution360()));
        logResultMap.put("resolution480", String.valueOf(logResult.getResolution480()));
        logResultMap.put("resolution720", String.valueOf(logResult.getResolution720()));
        logResultMap.put("resolution1080", String.valueOf(logResult.getResolution1080()));
        logResultMap.put("downThrput", String.valueOf(logResult.getDownThrput()));
        logResultMap.put("downSR", String.valueOf(logResult.getDownSR()));
        logResultMap.put("uploadThrput", String.valueOf(logResult.getUploadThrput()));
        logResultMap.put("uploadSR", String.valueOf(logResult.getUploadSR()));

        Call<String> call = client.sendLog("UE_LOG_RESULT_CD_POST",logResultMap);
        call.enqueue(new retrofit2.Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    if (response.body() !=null){
                        Logger.d("ResponseServer Event not null" + response.body());
                        callback.onSuccess("");
                    }else {
                        Logger.d("ResponseServer Event is null" + response.message());
                    }
                }else {
                    Logger.d("ResponseServer Event error " + response.code());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Logger.d("ResponseServer Event Failure" + t.getMessage());
                callback.onFailure(t.getMessage());
            }
        });
    }

    private interface RadioTestOnlineClient {

        @POST("save")
        @Headers({ "Content-Type: application/json;charset=UTF-8"})
        Call<String> sendLog(
                @Query("keyCommand") String zipCode,
                @Body HashMap<String, String> logMap);
    }
}
