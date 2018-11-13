package com.example.user.organizer.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.user.organizer.DBUtilities;
import com.example.user.organizer.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShowListParticipantsDialog extends DialogFragment {
    DBUtilities dbUtilities;
    ListView lvShowListParticipants;
    List<String> lvList;

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


        lvList = new ArrayList<>();
        // прочитать данные, переданные из активности (из точки вызова)
        List<String> loginUserList;          //коллекция login-ов с выбранными игроками
        loginUserList = getArguments().getStringArrayList("participantsLoginList");
        int i = 1;
        for (String s : loginUserList) {
            lvList.add(String.format("%s - (Login): %10s",
                    String.valueOf(i), s));
            i++;
        }
        // Создаем LayoutInflater
        LayoutInflater inflater = current.getLayoutInflater();
        // И с его помощью создаем наше view, которое потом и передадим в метод setView()
        View view = inflater.inflate(R.layout.dialog_show_list_participants, null);
        lvShowListParticipants = (ListView)view.findViewById(R.id.lvShowListParticipants);
        fillLV();
        return builder
                .setTitle("Список участников("+String.valueOf(loginUserList.size())+"):")
                .setView(view)
                .setIcon(R.drawable.icon_information)
//              лямбда-выражение на клик кнопки "Да"
                .setPositiveButton("OK", null)
                .setCancelable(false)           // запрет закрытия диалога кнопкой Назад
                .create();
    }//onCreateDialog

    //заполнение ListView
    private void fillLV() {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, lvList);

        // присваиваем адаптер списку
        lvShowListParticipants.setAdapter(adapter);
    }//fillLV
}//aboutUserInfoDialog
