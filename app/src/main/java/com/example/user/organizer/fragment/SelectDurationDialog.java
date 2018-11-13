package com.example.user.organizer.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.user.organizer.DBUtilities;
import com.example.user.organizer.NavigationDrawerLogInActivity;
import com.example.user.organizer.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//---------------------- Фрагмент с диалогом выбрать длительности игры-------------------
public class SelectDurationDialog extends DialogFragment {
    DBUtilities dbUtilities;
    RadioButton[] radioButtonsMas;
    TextView tvForResultCrEv;

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
        tvForResultCrEv = current.findViewById(R.id.tvForResultCrEv);

        // Создаем LayoutInflater
        LayoutInflater inflater = current.getLayoutInflater();
        // И с его помощью создаем наше view, которое потом и передадим в метод setView()
        View view = inflater.inflate(R.layout.dialog_select_duration, null);
        List<String> durationList = new ArrayList<>();
        durationList.addAll(Arrays.asList(
                new String[]{"30 минут","45 минут","60 минут","90 минут", "120 минут", "150 минут", "180 минут"}));
        radioButtonsMas = new RadioButton[durationList.size()];

        int i = 0;
        Resources res = getResources();
        for (String s : durationList) {
            int idRes = res.getIdentifier("rbSD"+String.valueOf(i), "id", getContext().getPackageName());
            radioButtonsMas[i] = view.findViewById(idRes);
            radioButtonsMas[i].setVisibility(View.VISIBLE);
            radioButtonsMas[i].setText(s);
            i++;
        }//foreach

            return builder
                    .setTitle("Выберите длительность игры")
                    .setView(view)
//                .setIcon(R.drawable.exlamation)
                    // лямбда-выражение на клик кнопки "Да"
//                    .setPositiveButton("Подтверждаю",
//                            (dialog, whichButton) -> dbUtilities.updateOneColumnTable(userId, "logo", logo))
                    .setPositiveButton("Подтверждаю", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            selectDuration();
                        }
                    })
                    .setNegativeButton("Не подтверждаю", null) // не назначаем слушателя кликов по кнопке "Нет"
                    .setCancelable(false)           // запрет закрытия диалога кнопкой Назад
                    .create();
    }//onCreateDialog

    private void selectDuration() {
        int i = 0;
        for (RadioButton radioButtonsMa : radioButtonsMas) {
            if(radioButtonsMa.isChecked()) break;
            else i++;
            tvForResultCrEv.setText(String.valueOf(i));
        }//foreach
    }//selectDuration
}//callDialogSelectLogo
