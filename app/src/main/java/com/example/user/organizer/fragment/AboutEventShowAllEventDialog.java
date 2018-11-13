package com.example.user.organizer.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;

import com.example.user.organizer.inteface.Datable;

//---------------------- Фрагмент с диалогом подробнее о событии -------------------

public class AboutEventShowAllEventDialog extends DialogFragment {

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

            return builder
                    .setTitle("Подробная информация")
                    .setMessage("Вы уверены?")
//                .setIcon(R.drawable.exlamation)
                    // лямбда-выражение на клик кнопки "Да"
                    .setPositiveButton("OK", null)
                    .setCancelable(false)           // запрет закрытия диалога кнопкой Назад
                    .create();
    }//onCreateDialog
}//AboutEventShowAllEventDialog
