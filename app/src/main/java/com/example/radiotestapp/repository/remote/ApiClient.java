package com.example.radiotestapp.repository.remote;

import com.example.radiotestapp.model.Event;
import com.example.radiotestapp.model.Log;
import com.example.radiotestapp.repository.Callback;
import com.example.radiotestapp.utils.Logger;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class ApiClient implements IApiClient {

    private final String BASE_URL = "https://172.27.160.83";

    private OkHttpClient okHttpClient = new OkHttpClient()
            .newBuilder()
            .addInterceptor(provideLoggingInterceptor())
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();

    private HttpLoggingInterceptor provideLoggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        return httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL + "/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build();

    RadioTestOnlineClient client = retrofit.create(RadioTestOnlineClient.class);

    @Override
    public void sendLog(Log log, Callback<String> callback) {
        Call<String> call = client.sendLog(log.getLogId());
        call.enqueue(new retrofit2.Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    if (response.body() !=null){
                        Logger.d("ResponseServer not null" + response.body());
                    }else {
                        Logger.d("ResponseServer is null" + response.body());
                    }
                }else {
                    Logger.d("ResponseServer erroe" + response.code());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Logger.d("ResponseServer Failure" + t.getMessage());
            }
        });
    }

    @Override
    public void sendEvent(Event event, Callback<String> callback) {

    }

    private interface RadioTestOnlineClient {

        @GET("save/Batken")
        Call<String> sendLog(String logId);
    }
}
