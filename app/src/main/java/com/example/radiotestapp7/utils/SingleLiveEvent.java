package com.example.radiotestapp7.utils;

import androidx.annotation.MainThread;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

public class SingleLiveEvent<T> extends MutableLiveData<T> {

  @MainThread
  public void observe(LifecycleOwner owner, final Observer<? super T> observer) {

    super.observe(owner, t -> {
      observer.onChanged(t);
    });
  }

  @MainThread
  public void call() {
    postValue(null);
  }
}
