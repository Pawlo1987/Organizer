package com.example.user.organizer.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;

import com.example.user.organizer.inteface.CustomInterface;

//---------------------- Фрагмент с диалогом покинуть событие -------------------
public class LeaveEventDialog extends DialogFragment {
    //интерфейс для Принятия участия
    CustomInterface leave;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        leave = (CustomInterface) context;
    } // onAttach

    @NonNull // создание диалога
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // получить ссылку на активность, вызвавшую диалог
        FragmentActivity current = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(current);

        // прочитать данные, переданные из активности (из точки вызова)
        String message = getArguments().getString("message");
        int event_id = getArguments().getInt("event_id", 0);
        int user_id = getArguments().getInt("user_id", 0);

            return builder
                    .setTitle("Подтверждение для покидания события")
                    .setMessage(message)
//                .setIcon(R.drawable.exlamation)
                    // лямбда-выражение на клик кнопки "Да"
                    .setPositiveButton("Подтверждаю",
                            (dialog, whichButton) -> leave.leaveFromEvent(event_id, user_id))
                    .setNegativeButton("Не подтверждаю", null) // не назначаем слушателя кликов по кнопке "Нет"
                    .setCancelable(false)           // запрет закрытия диалога кнопкой Назад
                    .create();
    }//onCreateDialog
}//LeaveEventDialog

