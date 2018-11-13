package com.example.user.organizer.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;

import com.example.user.organizer.AuthorizationActivity;
import com.example.user.organizer.DBUtilities;
import com.example.user.organizer.NavigationDrawerLogInActivity;

import java.util.List;

//---------------------- Фрагмент с диалогом удаления учетной записи пользователя-------------------
public class DeleteProfileDialog extends DialogFragment {
    DBUtilities dbUtilities;

    List<String> idEventsList;

    // Метод onAttach() вызывается в начале жизненного цикла фрагмента, и именно здесь
    // мы можем получить контекст фрагмента, в качестве которого выступает класс MainActivity.
    //onAttach(Context) не вызовется до API 23 версии вместо этого будет вызван onAttach(Activity),
    //коий устарел с 23 API
    //Так что вызовем onAttachToContext
    //https://ru.stackoverflow.com/questions/507008/%D0%9D%D0%B5-%D1%80%D0%B0%D0%B1%D0%BE%D1%82%D0%B0%D0%B5%D1%82-onattach
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
        String userId = getArguments().getString("user_id");

        String message = "УДАЛИТЬ ВАШ ПРОФИЛЬ?";

        return builder
                .setTitle("Подтверждение изменения данных")
                .setMessage(message)
//                .setIcon(R.drawable.exlamation)
                // лямбда-выражение на клик кнопки "Да"
//                    .setPositiveButton("Подтверждаю",
//                            (dialog, whichButton) -> dbUtilities.updateOneColumnTable(userId, column, logo))
                .setPositiveButton("Подтверждаю", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteProfile(userId);
                    }
                })
                .setNegativeButton("Не подтверждаю", null) // не назначаем слушателя кликов по кнопке "Нет"
                .setCancelable(false)           // запрет закрытия диалога кнопкой Назад
                .create();
    }//onCreateDialog

    // процедура удаления профиля
    private void deleteProfile(String userId) {
        //заполняем списоки id-ов таблицы "events"
        // где авторизированый пользователь является организатором
        idEventsList = dbUtilities.getListValuesByValueAndHisColumn(
                "events", "user_id",
                userId, "id"
        );

        //удаляем все записи в таблице "participants"
        //где принемает участие авторизированый пользователь
        deleteFromParticipantsByUser(userId);

        //удаляем все записи в таблице "notifications"
        // где авторизированый пользователь фигурирует
        deleteFromNotifications(userId);

        //удаляем все записи в таблице "events"
        // где авторизированый пользователь является организатором
        deleteFromEvents(userId);

        //переприсвоим владельцев поля
        assignFieldMaster(userId);

        //Удалить запись из таблицы "users"
        dbUtilities.deleteRowById("users",userId);

        //Вернемся в основную активность для обновления данных
        Intent intent = new Intent(getContext(), AuthorizationActivity.class);
        startActivity(intent);
    }//updateData

    // процедура переприсвоения хозяина поля "fields"
    private void assignFieldMaster(String userId) {
        //заполняем списоки id-ов таблицы "fields"
        // где авторизированый пользователь учавствует
        List<String> idFieldsList = dbUtilities.getListValuesByValueAndHisColumn(
                "fields", "user_id",
                userId, "id"
        );

        //Занесем данные в БД
        for (String idNext : idFieldsList) {
            dbUtilities.updateOneColumnTable(idNext, "user_id", "1", "fields");
        }
    }//assignFieldMaster

    // процедура удаления записей из таблицы "notifications"
    private void deleteFromNotifications(String userId) {
        //заполняем списоки id-ов таблицы "notifications"
        // где авторизированый пользователь учавствует
        List<String> idNotificationList = dbUtilities.getListValuesByValueAndHisColumn(
                "notifications", "user_id",
                userId, "id"
        );

        //заполняем списоки id-ов таблицы "notifications"
        // где авторизированый пользователь является организатором
        for (String idNext : idEventsList) {
            idNotificationList.addAll(
                    dbUtilities.getListValuesByValueAndHisColumn(
                            "notifications", "event_id",
                            idNext, "id"
                    )
            );
        }//foreach

        //удаляем все записи в таблице "events"
        // где принемает участие авторизированый пользователь
        for (String idNext : idNotificationList) {
            //удалил собития
            dbUtilities.deleteRowById("notifications",idNext);
        }//foreach

    }//deleteFromNotifications

    // процедура удаления записей из таблицы "events"
    private void deleteFromEvents(String userId) {
        //удаляем все записи в таблице "participants"
        //по списку idEventsList
        for (String idNext : idEventsList) {
            deleteFromParticipantsByEvent(idNext);
        }//foreach

        //удаляем все записи в таблице "events"
        // где принемает участие авторизированый пользователь
        for (String idNext : idEventsList) {
            //удалил собития
            dbUtilities.deleteRowById("events",idNext);
        }//foreach

    }//deleteFromEvents

    // процедура удаления записей из таблицы "participants" по записи события
    private void deleteFromParticipantsByEvent(String eventId) {
        //заполняем списоки id-ов таблицы "participants"
        // по задонному событию
        List<String> idParticipantsList = dbUtilities.getListValuesByValueAndHisColumn(
                "participants", "event_id",
                eventId, "id"
        );

        // процедура удаления записей из таблицы "participants"
        // по списку idParticipantsList
        for (String idNext : idParticipantsList) {
            dbUtilities.deleteRowById("participants",idNext);
        }//foreach
    }//deleteFromParticipants

    // процедура удаления записей из таблицы "participants" по записи пользователя
    private void deleteFromParticipantsByUser(String userId) {
        //заполняем списоки id-ов таблицы "participants"
        // где принемает участие авторизированый пользователь
        List<String> idParticipantsList = dbUtilities.getListValuesByValueAndHisColumn(
                "participants", "user_id",
                userId, "id"
        );

        // процедура удаления записей из таблицы "participants"
        // по списку idParticipantsList
        for (String idNext : idParticipantsList) {
            dbUtilities.deleteRowById("participants",idNext);
        }//foreach
    }//deleteFromParticipants
}//callDialogDeleteProfile
