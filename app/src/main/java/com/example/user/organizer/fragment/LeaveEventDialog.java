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

//---------------------- Фрагмент с диалогом покинуть событие -------------------
public class LeaveEventDialog extends DialogFragment {
    DBUtilities dbUtilities;
    Context context;
    String user_id;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        this.context = context;

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
        user_id = getArguments().getString("user_id");

        //Ищем id по двум значениям в таблице participants
        String id = dbUtilities.getIdByTwoValues("participants", "event_id", event_id,
                "user_id", user_id);
            return builder
                    .setTitle("Подтверждение для покидания события")
                    .setMessage(message)
//                .setIcon(R.drawable.exlamation)
                    .setPositiveButton("Подтверждаю", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            leaveEvent(id);
                        }
                    })
                    .setNegativeButton("Не подтверждаю", null) // не назначаем слушателя кликов по кнопке "Нет"
                    .setCancelable(false)           // запрет закрытия диалога кнопкой Назад
                    .create();

    }//onCreateDialog

    //подтверждение выхода из участия в тренеровки
    private void leaveEvent(String id){
        //удаления записи из БД( удаление записи из таблицы participants по id)
        dbUtilities.deleteRowById("participants", id);

        Intent intent = new Intent();
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
    }//leaveEvent



}//LeaveEventDialog

