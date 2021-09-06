package com.example.radiotestapp.repository;

public interface Callback<T> {
    void onSuccess(T t);
    void onFailure(String s);
}
