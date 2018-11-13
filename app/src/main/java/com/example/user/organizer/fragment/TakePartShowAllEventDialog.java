package com.example.user.organizer.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;

import com.example.user.organizer.DBUtilities;
import com.example.user.organizer.inteface.CustomInterface;

//---------------------- Фрагмент с диалогом принять участие -------------------
public class TakePartShowAllEventDialog extends DialogFragment {
    DBUtilities dbUtilities;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        dbUtilities = new DBUtilities(context);
    } // onAttach

    @NonNull // создание диалога
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // получить ссылку на активность, вызвавшую диалог
        FragmentActivity current = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(current);

        // прочитать данные, переданные из активности (из точки вызова)
        String message = getArguments().getString("message");
        Boolean userTakeInPart = getArguments().getBoolean("userTakeInPart", false);
        String event_id = getArguments().getString("event_id");
        String user_id = getArguments().getString("user_id");

        if (userTakeInPart)
            return builder
                    .setTitle("Подтверждение не требуется")
                    .setMessage(message)
//                .setIcon(R.drawable.exlamation)
                    // лямбда-выражение на клик кнопки "Да"
                    .setPositiveButton("Ок", null)
//                    .setNegativeButton("Нет", null) // не назначаем слушателя кликов по кнопке "Нет"
                    .setCancelable(false)           // запрет закрытия диалога кнопкой Назад
                    .create();
        else
            return builder
                    .setTitle("Подтверждение на участие в событии")
                    .setMessage(message)
//                .setIcon(R.drawable.exlamation)
                    // лямбда-выражение на клик кнопки "Да"
                    .setPositiveButton("Подтверждаю",
                            (dialog, whichButton) -> dbUtilities.insertIntoParticipants(event_id,user_id))
                    .setNegativeButton("Не подтверждаю", null) // не назначаем слушателя кликов по кнопке "Нет"
                    .setCancelable(false)           // запрет закрытия диалога кнопкой Назад
                    .create();
    }//onCreateDialog
}//TakePartShowAllEventDialog
