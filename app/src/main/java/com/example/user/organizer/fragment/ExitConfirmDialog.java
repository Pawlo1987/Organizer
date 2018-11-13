package com.example.user.organizer.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.example.user.organizer.inteface.Datable;

//---------------------- Фрагмент с диалогом подтверждения выхода из приложения-------------------
public class ExitConfirmDialog extends DialogFragment {

    boolean flag;
    Datable datable;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        datable = (Datable) context;
    } // onAttach

    @NonNull // создание диалога
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // получить ссылку на активность, вызвавшую диалог
        FragmentActivity current = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(current);

        // прочитать данные, переданные из активности (из точки вызова)
        flag = getArguments().getBoolean("flag", false);

        if (flag)
            return builder
                    .setTitle("Выход")
                    .setMessage("Вы уверены?")
//                .setIcon(R.drawable.exlamation)
                    // лямбда-выражение на клик кнопки "Да"
                    .setPositiveButton("Да", (dialog, whichButton) -> current.finish())
                    .setNegativeButton("Нет", null) // не назначаем слушателя кликов по кнопке "Нет"
                    .setCancelable(false)           // запрет закрытия диалога кнопкой Назад
                    .create();
        else
            return builder
                    .setTitle("Выход")
                    .setMessage("Вы уверены?")
//                .setIcon(R.drawable.exlamation)
                    // лямбда-выражение на клик кнопки "Да"
                    .setPositiveButton("Да", (dialog, whichButton) -> datable.closeApp())
                    .setNegativeButton("Нет", null) // не назначаем слушателя кликов по кнопке "Нет"
                    .setCancelable(false)           // запрет закрытия диалога кнопкой Назад
                    .create();
    }//onCreateDialog
}//class ExitConfirmDialog
