package com.example.radiotestapp.utils;

import android.util.Log;

import androidx.annotation.MainThread;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.concurrent.atomic.AtomicBoolean;

public class SingleLiveEvent<T> extends MutableLiveData<T> {

    @MainThread
    public void observe(LifecycleOwner owner, final Observer<? super T> observer) {

        super.observe(owner, t -> {
            observer.onChanged(t);
        });
    }

    @MainThread
    public void call() {
        setValue(null);
    }
}
