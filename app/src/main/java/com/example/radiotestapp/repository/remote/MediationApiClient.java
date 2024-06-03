package com.example.radiotestapp.repository.remote;


import com.example.radiotestapp.model.LogResult;
import com.example.radiotestapp.utils.Logger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
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

public class MediationApiClient implements IMediationApiClient {

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
    public void sendLogResult(LogResult logResult, ApiCallback callback) {
        HashMap<String,String> testResultDataMap = new HashMap<>();

        testResultDataMap.put("altitude", "true");
        testResultDataMap.put("ber", "");
        testResultDataMap.put("bsic", "");
        testResultDataMap.put("cellId", "");
        testResultDataMap.put("channel", "");
        testResultDataMap.put("cqi", "");
        testResultDataMap.put("date", "");
        testResultDataMap.put("dlThrput", logResult.getDownThrput());
        testResultDataMap.put("eNodeB", "");
        testResultDataMap.put("ecNO", "");
        testResultDataMap.put("id", "");
        testResultDataMap.put("isUploaded", "");
        testResultDataMap.put("latitude", "");
        testResultDataMap.put("logId", logResult.getId());
        testResultDataMap.put("logState", "");
        testResultDataMap.put("longitude", "");
        testResultDataMap.put("mcc", "");
        testResultDataMap.put("mnc", "");
        testResultDataMap.put("pci", "");
        testResultDataMap.put("ping", "");
        testResultDataMap.put("psc", "");
        testResultDataMap.put("ps", "");
        testResultDataMap.put("rscp", "");
        testResultDataMap.put("rsrp", "");
        testResultDataMap.put("rsrq", "");
        testResultDataMap.put("rxLevel", "");
        testResultDataMap.put("snr", "");
        testResultDataMap.put("tacLac", "");
        testResultDataMap.put("technology", "SPEEDTEST");
        testResultDataMap.put("ulThrput", logResult.getUploadThrput());
        testResultDataMap.put("youtubeState", "");
        testResultDataMap.put("youtubeQuality", "");

        ArrayList<HashMap<String, String>> maps = new ArrayList<>();
        maps.add(testResultDataMap);

        for (String key : testResultDataMap.keySet()){
            Logger.d("TestResuldDataStart " + key + " " + testResultDataMap.get(key));
        }

        Call<String> call = client.sendTestResultData("Sherruh",testResultDataMap);
        call.enqueue(new retrofit2.Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    if (response.body() !=null){
                        callback.onSuccess("ResponseServer123 Successfully send to server!");
                    }else {
                        Logger.d("ResponseServer123 is null" + response.message());
                        callback.onFailure("ResponseServer123 Response from server is null");
                    }
                }else {
                    try {
                        Logger.d("ResponseServer123 error" + response.raw().message() + " " + response.code()  + response.errorBody().string()
                        );
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    callback.onFailure("ResponseServer123 error" + response.raw().message());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Logger.d("ResponseServer Event Failure" + t.getMessage());
                callback.onFailure("ResponseServer123 sending to the server is failed - " + t.getMessage());
            }
        });
    }

    private interface RadioTestOnlineClient {

        @POST("save")
        @Headers({ "Content-Type: application/json;charset=UTF-8"})
        Call<String> sendTestResultData(
                @Query("keyCommand") String zipCode,
                @Body HashMap<String, String> logMap);
    }
}
