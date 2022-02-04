package com.example.radiotestapp.enums;

public enum EEvents {
    YSI,//Youtube init start
    YFI,//Youtube init finish
    YEI,//Youtube init error
    YSB,//Youtube buffering start
    YFB,//Youtube buffering finished. Triggers when video begin to play
    YFL,//Youtube loading finished
    YEB,//Youtube buffering error
    YSP,//Youtube start playing
    US,//Uploading Started
    UE,//Upload error
    UF,//Upload finished
    DS,//Download Started
    DE,//Download Error
    DF//Download Finished
}
