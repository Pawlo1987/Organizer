package com.example.user.organizer.fragment;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.organizer.DBUtilities;
import com.example.user.organizer.R;

public class AboutUsFragment extends Fragment {
    DBUtilities dbUtilities;
    Context context;
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
        this.context = context;
        dbUtilities = new DBUtilities(context);

    }//onAttachToContext

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View result = inflater.inflate(R.layout.fragment_about_us,  container, false);

        TextView tvInfoAbUsFr = result.findViewById(R.id.tvInfoAbUsFr);
        // прочитать данные, переданные из активности (из точки вызова)
        message = String.format("Приложение разработанно студентом Компьютерной Академии \"ШАГ\" \n\n" +
                "Сайт академии  -  https://itstep.dn.ua \n\n" +
                "Связь с администратором: dolliner.pavel@gmail.com");
        //Добавляем цвет тексту
        text = new SpannableString(message);

        formatPartOfTheText("Приложение разработанно", 23);
        formatPartOfTheText("Сайт академии", 13);
        formatPartOfTheText("Связь с администратором", 23);
        tvInfoAbUsFr.setText(text);

        if(tvInfoAbUsFr.getText() != null) {
            Linkify.addLinks(tvInfoAbUsFr, Linkify.ALL);
        }
        return result;
    } // onCreateView

    //процедура форматирования части текста
    private void formatPartOfTheText(String s, int i) {
        int iStart = message.indexOf(s);
        int iEnd = iStart + i;
        text.setSpan(new StyleSpan(Typeface.ITALIC), iStart, iEnd,  Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setSpan(new ForegroundColorSpan(Color.BLUE), iStart, iEnd,  Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setSpan(new UnderlineSpan(), iStart, iEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }//formatPartOfTheText

}
