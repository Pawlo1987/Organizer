package com.example.user.organizer.inteface;

import android.content.Context;

//-------------------Интерфейс для вызова DialogFragment из ShowAllEventsFragment-------------
public interface AllEventsInterface {
    void callDialogAboutDialog(Context context, String message);
    void callDialogTakePart(Context context, String eventId, boolean userTakeInPart, String message);
}//AllEventsInterface
