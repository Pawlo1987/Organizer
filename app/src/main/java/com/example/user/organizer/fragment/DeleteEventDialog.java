package com.example.user.organizer.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.user.organizer.DBUtilities;
import com.example.user.organizer.Event;
import com.example.user.organizer.R;

import java.util.ArrayList;
import java.util.List;

//---------------------- Фрагмент с диалогом удалить событие -------------------
public class DeleteEventDialog extends DialogFragment {
    DBUtilities dbUtilities;
    Spannable text;
    String message;
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
        Activity current = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(current);

        // прочитать данные, переданные из активности (из точки вызова)
        String event_id = getArguments().getString("event_id");
        String user_id = getArguments().getString("user_id");

        // Создаем LayoutInflater
        LayoutInflater inflater = current.getLayoutInflater();
        // И с его помощью создаем наше view, которое потом и передадим в метод setView()
        View view = inflater.inflate(R.layout.dialog_info, null);
        TextView tvMessage = (TextView) view.findViewById(R.id.tvMessage);

        // прочитать данные, переданные из активности (из точки вызова)
        message = getArguments().getString("message");
        //Добавляем цвет тексту
        text = new SpannableString(message);

        formatPartOfTheText("Событие в городе", 16);
        formatPartOfTheText("На поле", 7);
        formatPartOfTheText("Назначено на", 12);
        formatPartOfTheText("Состоится в", 11);
        formatPartOfTheText("Продолжительность", 16);
        formatPartOfTheText("Стоимость", 9);
        formatPartOfTheText("Телефон", 7);


        tvMessage.setText(text);
        if(tvMessage.getText() != null) {
            Linkify.addLinks(tvMessage, Linkify.PHONE_NUMBERS);
        }

        return builder
                .setTitle("Вы подтверждаете удаление события?")
                .setView(view)
                .setIcon(R.drawable.icon_question)
                // лямбда-выражение на клик кнопки "Да"
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteEvent(event_id, user_id);
                    }
                })
                .setNegativeButton("Нет", null) // не назначаем слушателя кликов по кнопке "Нет"
                .setCancelable(false)           // запрет закрытия диалога кнопкой Назад
                .create();
    }//onCreateDialog

    //подтверждение удаления
    private void deleteEvent(String event_id, String user_id){
        //заполняем список учасников(id)
        List<String> idParticipantsList = dbUtilities.getListValuesByValueAndHisColumn(
                "participants", "event_id",
                event_id, "id"
        );

        List<Event> listEvent = new ArrayList<>();
        //удаляем старый список учасников из таблицы participants
        for (String idParticipants : idParticipantsList) {
            //получаем данные для уведомления
            //коллекция списка событий
            listEvent = dbUtilities.getListEvents(event_id, "", user_id);

            //создаем новые уведомления для участников
            //от организатора события об удалении события
            dbUtilities.insertIntoNotifications(event_id,
                 dbUtilities.searchValueInColumn("participants", "id",
                        "user_id", idParticipants),    //увеомление для организатора
                 dbUtilities.getIdByValue("cities","name",listEvent.get(0).getCityName()),
                 dbUtilities.getIdByValue("fields","name",listEvent.get(0).getFieldName()),
                 listEvent.get(0).getEventTime(), listEvent.get(0).getEventData(), "4", " "
            );

            //удаляем записи участников из таблицы участников
            dbUtilities.deleteRowById("participants",idParticipants);
        }//foreach

        //удаляем записи уведомлений об организации даного события если ктото не получил
        dbUtilities.deleteRowByTwoValueAndTheyColumnName("notifications",
                "event_id",event_id,"message_id","1");

        //удалить собитие из базы
        dbUtilities.deleteRowById("events", event_id);

        Intent intent = new Intent();
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
    }//deleteEvent

    //процедура форматирования части текста
    private void formatPartOfTheText(String s, int i) {
        int iStart = message.indexOf(s);
        int iEnd = iStart + i;
        text.setSpan(new StyleSpan(Typeface.ITALIC), iStart, iEnd,  Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setSpan(new ForegroundColorSpan(Color.BLUE), iStart, iEnd,  Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setSpan(new UnderlineSpan(), iStart, iEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }//formatPartOfTheText

}//DeleteEventDialog
