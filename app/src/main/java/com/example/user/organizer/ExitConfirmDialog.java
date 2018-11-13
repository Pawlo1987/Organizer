package com.example.user.organizer;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;

//---------------------- Фрагмент с диалогом подтверждения выхода из приложения-------------------
public class ExitConfirmDialog extends DialogFragment {

    @NonNull // создание диалога
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // получить ссылку на активность, вызвавшую диалог
        FragmentActivity current = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(current);

        return builder
                .setTitle("Выход")
                .setMessage("Вы уверены?")
//                .setIcon(R.drawable.exlamation)
                // лямбда-выражение на клик кнопки "Да"
                .setPositiveButton("Да", (dialog, whichButton) -> current.finish())
                .setNegativeButton("Нет", null) // не назначаем слушателя кликов по кнопке "Нет"
                .setCancelable(false)           // запрет закрытия диалога кнопкой Назад
                .create();
    } // onCreateDialog
}//class ExitConfirmDialog
