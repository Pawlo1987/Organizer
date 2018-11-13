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
import android.widget.TextView;

import com.example.user.organizer.DBUtilities;
import com.example.user.organizer.NavigationDrawerLogInActivity;
import com.example.user.organizer.R;

//---------------------- Фрагмент с диалогом выбрать логотип-------------------
public class SelectLogoDialog extends DialogFragment {
    DBUtilities dbUtilities;

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
        String logo = getArguments().getString("logo");
        String table = getArguments().getString("table");

            return builder
                    .setTitle("Подтверждение выбраного логотипа")
//                .setIcon(R.drawable.exlamation)
                    // лямбда-выражение на клик кнопки "Да"
//                    .setPositiveButton("Подтверждаю",
//                            (dialog, whichButton) -> dbUtilities.updateOneColumnTable(userId, "logo", logo))
                    .setPositiveButton("Подтверждаю", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            selectLogo(userId, logo, table);
                        }
                    })
                    .setNegativeButton("Не подтверждаю", null) // не назначаем слушателя кликов по кнопке "Нет"
                    .setCancelable(false)           // запрет закрытия диалога кнопкой Назад
                    .create();
    }//onCreateDialog

    private void selectLogo(String userId, String value, String table) {
        if(table.equals("users")) {
            //Занесем данные в БД
            dbUtilities.updateOneColumnTable(userId, "logo", value, table);

            //Вернемся в основную активность для обновления данных
            Intent intent = new Intent(getContext(), NavigationDrawerLogInActivity.class);
            intent.putExtra("idAuthUser", userId);
            startActivity(intent);
        }else {
            //если работа с диалогом идет из активности создания Новостного поста(CreateNoteNews)
            //то так мы передаем данные о выбранном логотипе
            //через скрытый элемент TextView в Активности (CreateNoteNews)
            ((TextView)getActivity().findViewById(R.id.tvBufferSeLoAc)).setText(value);
        }
    }//selectLogo
}//callDialogSelectLogo
