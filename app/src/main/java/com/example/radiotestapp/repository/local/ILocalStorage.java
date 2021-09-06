package com.example.radiotestapp.repository.local;

import com.example.radiotestapp.model.SettingsParameter;

public interface ILocalStorage {
    SettingsParameter getSettingsParameter(String name);
    Long saveSettingsParameter(SettingsParameter settingsParameter);
}
