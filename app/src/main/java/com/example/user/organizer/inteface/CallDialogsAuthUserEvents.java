package com.example.user.organizer.inteface;

import android.content.Context;

//-------------------Интерфейс для вызова DialogFragment из ShowAuthUserEventsFragment-------------
public interface CallDialogsAuthUserEvents {
    void leaveDialog(Context context, String message, String eventId);
    void deleteDialog(Context context, String message, String eventId);
    void aboutDialog(Context context, String message);
}//CallDialogsAuthUserEvents
