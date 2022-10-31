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
            .baseUrl(BASE_URL /*+ "/"*/)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build();

    RadioTestOnlineClient client = retrofit.create(RadioTestOnlineClient.class);

    @Override
    public void sendLog(Log log, Callback<String> callback) {
        HashMap<String,String> logMap = new HashMap<>();
        logMap.put("altitude", String.valueOf(log.getAltitude()));
        logMap.put("ber", String.valueOf(log.getBer()));
        logMap.put("bsic", String.valueOf(log.getBsic()));
        logMap.put("cellId", String.valueOf(log.getCellId()));
        logMap.put("channel", String.valueOf(log.getChannel()));
        logMap.put("cqi", String.valueOf(log.getCqi()));
        logMap.put("date", String.valueOf(log.getDate()));
        logMap.put("dlThrput", String.valueOf(log.getDlThrput()));
        logMap.put("eNodeB", String.valueOf(log.getENodeB()));
        logMap.put("ecNO", String.valueOf(log.getEcNO()));
        logMap.put("id", String.valueOf(log.getId()));
        logMap.put("isUploaded", String.valueOf(log.isUploaded()));
        logMap.put("latitude", String.valueOf(log.getLatitude()));
        logMap.put("logId", String.valueOf(log.getLogId()));
        logMap.put("logState", String.valueOf(log.getLogState()));
        logMap.put("longitude", String.valueOf(log.getLongitude()));
        logMap.put("mcc", String.valueOf(log.getMcc()));
        logMap.put("mnc", String.valueOf(log.getMnc()));
        logMap.put("pci", String.valueOf(log.getPci()));
        logMap.put("ping", String.valueOf(log.getPing()));
        logMap.put("psc", String.valueOf(log.getPsc()));
        logMap.put("rscp", String.valueOf(log.getRscp()));
        logMap.put("rsrp", String.valueOf(log.getRsrp()));
        logMap.put("rsrq", String.valueOf(log.getRsrq()));
        logMap.put("rxLevel", String.valueOf(log.getRxLevel()));
        logMap.put("snr", String.valueOf(log.getSnr()));
        logMap.put("tacLac", String.valueOf(log.getTacLac()));
        logMap.put("technology", String.valueOf(log.getTechnology()));
        logMap.put("ulThrput", String.valueOf(log.getUlThrput()));
        logMap.put("youtubeState", String.valueOf(log.getYoutubeState()));
        logMap.put("youtubeQuality", String.valueOf(log.getYoutubeResolution()));

        Logger.d("ResponseServer " + logMap.toString());

        Call<String> call = client.sendLog("UE_LOG_POST_CD",logMap);
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

        Call<String> call = client.sendLog("UE_LOG_POST_CD",eventMap);
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

    }

    private interface RadioTestOnlineClient {

        @POST("save")
        @Headers({ "Content-Type: application/json;charset=UTF-8"})
        Call<String> sendLog(
                @Query("keyCommand") String zipCode,
                @Body HashMap<String, String> logMap);
    }
}
