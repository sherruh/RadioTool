package com.example.radiotestapp.repository.local;

import com.example.radiotestapp.model.SettingsParameter;
import com.example.radiotestapp.repository.local.db.Dao;

public class LocalStorage implements ILocalStorage {

    private Dao dao;

    public LocalStorage(Dao dao){
        this.dao = dao;
    }

    @Override
    public SettingsParameter getSettingsParameter(String name) {
         return dao.getSettingParameter(name);
    }

    @Override
    public Long saveSettingsParameter(SettingsParameter settingsParameter) {
        return dao.saveSettingsParameter(settingsParameter);
    }
}
