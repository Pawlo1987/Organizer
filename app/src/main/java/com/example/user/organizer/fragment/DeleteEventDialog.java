package com.example.user.organizer.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

import com.example.user.organizer.DBUtilities;

import java.util.List;

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
        Activity current = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(current);

        // прочитать данные, переданные из активности (из точки вызова)
        String message = getArguments().getString("message");
        String event_id = getArguments().getString("event_id");

        return builder
                .setTitle("Подтверждение удаления события")
                .setMessage(message)
//                .setIcon(R.drawable.exlamation)
                // лямбда-выражение на клик кнопки "Да"
                .setPositiveButton("Подтверждаю", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteEvent(event_id);
                    }
                })
                .setNegativeButton("Не подтверждаю", null) // не назначаем слушателя кликов по кнопке "Нет"
                .setCancelable(false)           // запрет закрытия диалога кнопкой Назад
                .create();
    }//onCreateDialog

    //подтверждение удаления
    private void deleteEvent(String event_id){
        //заполняем список учасников(id)
        List<String> idParticipantsList = dbUtilities.getListValuesByValueAndHisColumn(
                "participants", "event_id",
                event_id, "id"
        );

        //удаляем старый список учасников из таблицы participants
        for (String idParticipants : idParticipantsList) {
            dbUtilities.deleteRowById("participants",idParticipants);
        }//foreach

        //удалил собития
        dbUtilities.deleteRowById("events", event_id);

        Intent intent = new Intent();
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
    }//deleteEvent

}//DeleteEventDialog
