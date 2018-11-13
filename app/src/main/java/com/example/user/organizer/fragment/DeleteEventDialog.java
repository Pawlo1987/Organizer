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

//---------------------- Фрагмент с диалогом удалить событие -------------------
public class DeleteEventDialog extends DialogFragment {
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
        String event_id = getArguments().getString("event_id");

        return builder
                .setTitle("Подтверждение удаления события")
                .setMessage(message)
//                .setIcon(R.drawable.exlamation)
                // лямбда-выражение на клик кнопки "Да"
                .setPositiveButton("Подтверждаю",
                        (dialog, whichButton) -> dbUtilities.deleteRowById("events", event_id))
                .setNegativeButton("Не подтверждаю", null) // не назначаем слушателя кликов по кнопке "Нет"
                .setCancelable(false)           // запрет закрытия диалога кнопкой Назад
                .create();
    }//onCreateDialog
}//DeleteEventDialog
