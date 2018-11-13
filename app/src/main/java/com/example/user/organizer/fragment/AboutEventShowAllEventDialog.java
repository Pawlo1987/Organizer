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

//---------------------- Фрагмент с диалогом подробнее о событии -------------------
public class AboutEventShowAllEventDialog extends DialogFragment {
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
                    .setTitle("Подробная информация")
                    .setView(view)
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
}//AboutEventShowAllEventDialog
