package com.example.user.organizer.fragment;

import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.organizer.DBUtilities;
import com.example.user.organizer.R;
import com.example.user.organizer.ShowAuthUserEventsRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ShowAuthUserEventsFragment extends Fragment {
    RecyclerView rvMainShAuUsEvAc;

    // адаптер для отображения recyclerView
    ShowAuthUserEventsRecyclerAdapter showAuthUserEventsRecyclerAdapter;
    DBUtilities dbUtilities;

    // поля для доступа к записям БД
    Cursor eventsCursor;                // прочитанные данные

    TextView tvauthuserevents;
    Context context;
    int idAuthUser = 6;

    public ShowAuthUserEventsFragment newInstance() {

        ShowAuthUserEventsFragment fragment = new ShowAuthUserEventsFragment();

        return fragment;
    } // FirstPageFragment

    // Метод onAttach() вызывается в начале жизненного цикла фрагмента, и именно здесь
    // мы можем получить контекст фрагмента, в качестве которого выступает класс MainActivity.
    @Override
    public void onAttach(Context context) {

        this.context = context;
        dbUtilities = new DBUtilities(context);
//        dbUtilities.open();

        // Объеденный запрос для получения данных об участии пользователя
        // получаем данные из БД в виде курсора (коллекция, возвращенная запросом)
        // В запрос последние полученое значение это статус пользователя (1-организатор, 0-учасник)
        String queryAdapt = "SELECT cities.name, fields.name, eventsList.date, eventsList.time, " +
                "eventsList._id, eventsList.user_id, 1 FROM eventsList " +
                "INNER JOIN fields ON fields._id = eventsList.field_id " +
                "INNER JOIN cities ON cities._id = eventsList.city_id " +
                "WHERE eventsList.user_id = \"" +
                 idAuthUser + "\" UNION SELECT cities.name, fields.name, eventsList.date, eventsList.time, " +
                "eventsList._id, eventsList.user_id, 0 FROM participants " +
                "INNER JOIN eventsList ON eventsList._id = participants.event_id " +
                "INNER JOIN fields ON fields._id = eventsList.field_id " +
                "INNER JOIN cities ON cities._id = eventsList.city_id " +
                "WHERE participants.user_id = \"" + idAuthUser + "\";";

        eventsCursor = null;// dbUtilities.getDb().rawQuery(queryAdapt, null);

        // создаем адаптер, передаем в него курсор
        showAuthUserEventsRecyclerAdapter
                = new ShowAuthUserEventsRecyclerAdapter(context, queryAdapt, idAuthUser);

        super.onAttach(context);
    } // onAttach

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View result = inflater.inflate(R.layout.fragment_show_auth_user_events,  container, false);

        // RecyclerView для отображения таблицы users БД
        rvMainShAuUsEvAc = result.findViewById(R.id.rvMainShAuUsEvAc);
        //привязываем адаптер к recycler объекту
        rvMainShAuUsEvAc.setAdapter(showAuthUserEventsRecyclerAdapter);

        //количество созданных событий пользователя
        tvauthuserevents = result.findViewById(R.id.tvauthuserevents);
        tvauthuserevents.setText(String.valueOf(eventsCursor.getCount()));
        return result;
    } // onCreateView
}
