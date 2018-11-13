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

import com.example.user.organizer.DBUtilities;
import com.example.user.organizer.NavigationDrawerLogInActivity;

//------- Фрагмент с диалогом перезаписи одного значения таблицы в учетной записи пользователя ---
public class ChangeColumnDialog extends DialogFragment {
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
        String column = getArguments().getString("column");
        String value = getArguments().getString("value");
        String dataMessage = getArguments().getString("dataMessage");

        //формирование строки message для диалога
        String message = "Новые данные: " + dataMessage;

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
                        updateData(userId, column, value, "users");
                    }
                })
                .setNegativeButton("Не подтверждаю", null) // не назначаем слушателя кликов по кнопке "Нет"
                .setCancelable(false)           // запрет закрытия диалога кнопкой Назад
                .create();
    }//onCreateDialog

    //процедура обновленя данных при нажати на клавишу "Подтверждения
    private void updateData(String userId, String column, String value, String table) {
        //Занесем данные в БД
        dbUtilities.updateOneColumnTable(userId, column, value, table);

        //Вернемся в основную активность для обновления данных
        Intent intent = new Intent(getContext(), NavigationDrawerLogInActivity.class);
        intent.putExtra("idAuthUser", userId);
        startActivity(intent);
    }//updateData
}//callDialogChangeColumn
