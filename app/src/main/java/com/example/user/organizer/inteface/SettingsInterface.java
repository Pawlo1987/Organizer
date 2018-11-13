package com.example.user.organizer.inteface;

import android.content.Context;

//-----интерфейс для настроек данных пользователя
public interface SettingsInterface {
    void callDialogChangeColumn(Context context, String column, String value,String dataMessage, String eventId);
    void callDialogDeleteProfile(Context context, String eventId);
}//SettingsInterface
