package com.example.user.organizer.fragment;

import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.organizer.DBUtilities;
import com.example.user.organizer.R;
import com.example.user.organizer.ShowAuthUserEventsRecyclerAdapter;

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
        dbUtilities.open();

        // получаем данные из БД в виде курсора (коллекция, возвращенная запросом)
        String query = "SELECT cities.name as city, fields.name as field, events.date as date, " +
                "events.starttime as starttime, participants.status as status, " +
                "events._id as event_id FROM participants INNER JOIN events " +
                "ON events._id = participants.eventnumber INNER JOIN fields " +
                "ON fields._id = events.field INNER JOIN cities " +
                "ON cities._id = events.city WHERE participants.user = \"" + idAuthUser + "\";";

        eventsCursor =  dbUtilities.getDb().rawQuery(query, null);

        // создаем адаптер, передаем в него курсор
        showAuthUserEventsRecyclerAdapter = new ShowAuthUserEventsRecyclerAdapter(context, eventsCursor);

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
