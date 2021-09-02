package com.example.radiotestapp.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "settings_parameter")
public class SettingsParameter {
    @PrimaryKey
    @NonNull
    private String name;
    private String value;

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public SettingsParameter(@NonNull String name, String value) {
        this.name = name;
        this.value = value;
    }
}
