package com.example.radiotestapp.upload_test;

import com.example.radiotestapp.App;
import com.example.radiotestapp.core.Constants;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Uploader {


    public void setUploadAgain(boolean uploadAgain) {
        isUploadAgain = uploadAgain;
    }

    private boolean isUploadAgain = true;
    private OkHttpClient client;

    public void uploadFile(String targetUrl, UploadListener uploadListener){
        File folder = new File(App.context.getExternalFilesDir(null), Constants.LOG_FOLDER);
        File file = new File(folder.getPath(),"/1.bin");
        client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();;
        try {

            RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("file", file.getName(),
                            RequestBody.create(MediaType.parse("text/csv"), file))
                    .addFormDataPart("some-field", "some-value")
                    .build();

            Request request = new Request.Builder()
                    .url(targetUrl)
                    .post(requestBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(final Call call, final IOException e) {
                    uploadListener.onFailure(e.getMessage());
                }

                @Override
                public void onResponse(final Call call, final Response response) throws IOException {
                    if (!response.isSuccessful()) {
                        uploadListener.onFailure("Response is Empty");
                    }
                    if (isUploadAgain) uploadFile(targetUrl, uploadListener);
                    // Upload successful
                }

            });

        } catch (Exception ex) {
            uploadListener.onFailure(ex.getMessage());
        }
    }

    public void cancelUpload(){
        if (client != null) client.dispatcher().cancelAll();
    }

    public interface UploadListener{
        void onFailure(String message);
    }
}
