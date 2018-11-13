package com.example.user.organizer.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
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

    // Метод onAttach() вызывается в начале жизненного цикла фрагмента, и именно здесь
    // мы можем получить контекст фрагмента, в качестве которого выступает класс MainActivity.
    //onAttach(Context) не вызовется до API 23 версии вместо этого будет вызван onAttach(Activity),
    //коий устарел с 23 API
    //Так что вызовем onAttachToContext
    @TargetApi(23)
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onAttachToContext(context);
    }//onAttach

    //устарел с 23 API
    //Так что вызовем onAttachToContext
    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onAttachToContext(activity);
        }//if
    }//onAttach

    //Вызовется в момент присоединения фрагмента к активити
    protected void onAttachToContext(Context context) {
        //здесь всегда есть контекст и метод всегда вызовется.
        //тут можно кастовать контест к активити.
        //но лучше к реализуемому ею интерфейсу
        //чтоб не проверять из какого пакета активити в каждом из случаев
        dbUtilities = new DBUtilities(context);
    }//onAttachToContext

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
