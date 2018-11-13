package com.example.user.organizer.inteface;

import android.content.Context;

//-------------------Интерфейс для вызова DialogFragment из SettingsFragment-------------
public interface SelectLogoInterface {
    void callDialogSelectLogo(Context context, String logo, String eventId);
}//SelectLogoInterface
