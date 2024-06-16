package com.example.radiotestapp7.repository;

public interface Callback<T> {
    void onSuccess(T t);
    void onFailure(String s);
}
