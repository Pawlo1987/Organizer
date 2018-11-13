package com.example.user.organizer.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

import com.example.user.organizer.DBUtilities;
import com.example.user.organizer.inteface.CustomInterface;

//---------------------- Фрагмент с диалогом покинуть событие -------------------
public class LeaveEventDialog extends DialogFragment {
    DBUtilities dbUtilities;
    Context context;
    String user_id;

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
        this.context = context;
        dbUtilities = new DBUtilities(context);
    }//onAttachToContext

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

