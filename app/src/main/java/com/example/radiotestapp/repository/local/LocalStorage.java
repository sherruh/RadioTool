package com.example.radiotestapp.repository.local;

import com.example.radiotestapp.repository.local.db.Dao;

public class LocalStorage implements ILocalStorage {

    private Dao dao;

    public LocalStorage(Dao dao){
        this.dao = dao;
    }
}
