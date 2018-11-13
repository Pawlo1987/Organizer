package com.example.user.organizer.inteface;

import android.content.Context;

//-------------------Интерфейс для вызова DialogFragment из ShowAllEventsFragment-------------
public interface CallDialogsAllEvents {
    void aboutDialog(Context context, String message);
    void takePart(Context context, String eventId, boolean userTakeInPart, String message);
}//CallDialogsAllEvents
