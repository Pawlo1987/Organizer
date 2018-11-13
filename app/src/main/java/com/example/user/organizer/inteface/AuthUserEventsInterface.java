package com.example.user.organizer.inteface;

import android.content.Context;

//-------------------Интерфейс для вызова DialogFragment из ShowAuthUserEventsFragment-------------
public interface AuthUserEventsInterface {
    void callDialogLeaveDialog(Context context, String message, String eventId);
    void callDialogDeleteDialog(Context context, String message, String eventId);
    void callDialogAboutDialog(Context context, String message);
}//AuthUserEventsInterface
