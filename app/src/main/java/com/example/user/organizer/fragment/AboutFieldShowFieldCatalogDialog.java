package com.example.user.organizer.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
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

import com.example.user.organizer.R;

//---------------------- Фрагмент с диалогом подробнее о поле -------------------
public class AboutFieldShowFieldCatalogDialog extends DialogFragment {
    Spannable text;
    String message;
    @NonNull // создание диалога
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // получить ссылку на активность, вызвавшую диалог
        FragmentActivity current = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(current);

        // Создаем LayoutInflater
        LayoutInflater inflater = current.getLayoutInflater();
        // И с его помощью создаем наше view, которое потом и передадим в метод setView()
        View view = inflater.inflate(R.layout.dialog_info, null);
        TextView tvMessage = (TextView) view.findViewById(R.id.tvMessage);

        // прочитать данные, переданные из активности (из точки вызова)
        message = getArguments().getString("message");
        //Добавляем цвет тексту
        text = new SpannableString(message);

        formatPartOfTheText("Поле находится в городе", 23);
        formatPartOfTheText("Называется", 10);
        formatPartOfTheText("Номер телефона", 14);
        formatPartOfTheText("Освещение", 9);
        formatPartOfTheText("Покрытие", 8);
        formatPartOfTheText("Душ", 3);
        formatPartOfTheText("Крыша", 5);
        formatPartOfTheText("Геопозиция долгота", 18);
        formatPartOfTheText("Геопозиция широта", 18);
        formatPartOfTheText("Адрес", 6);

        tvMessage.setText(text);
        if(tvMessage.getText() != null) {
            Linkify.addLinks(tvMessage, Linkify.PHONE_NUMBERS);
        }

            return builder
                    .setView(view)
                    .setTitle("Подробная информация о футбольном поле")
                    .setIcon(R.drawable.icon_information)
                    // лямбда-выражение на клик кнопки "Да"
                    .setPositiveButton("OK", null)
                    .setCancelable(false)           // запрет закрытия диалога кнопкой Назад
                    .create();
    }//onCreateDialog

    //процедура форматирования части текста
    private void formatPartOfTheText(String s, int i) {
        int iStart = message.indexOf(s);
        int iEnd = iStart + i;
        text.setSpan(new StyleSpan(Typeface.ITALIC), iStart, iEnd,  Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setSpan(new ForegroundColorSpan(Color.BLUE), iStart, iEnd,  Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setSpan(new UnderlineSpan(), iStart, iEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }//formatPartOfTheText
}//AboutFieldShowFieldCatalogDialog
