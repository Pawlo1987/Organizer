package com.example.user.organizer;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

//-------Активность для вывода(просмотра) событий авторизированого пользователя--------------

public class ShowAuthUserEventsActivity extends AppCompatActivity {

    RecyclerView rvMainShAuUsEvAc;
    ShowAuthUserEventsRecyclerAdapter ShowAuthUserEventsRecyclerAdapter;  // адаптер для отображения recyclerView
    DBUtilities dbUtilities;

    // поля для доступа к записям БД
    Cursor eventsCursor;                // прочитанные данные

    TextView tvauthuserevents;
    Context context;
    int idAuthUser = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_auth_user_events);

        context = getBaseContext();
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
        ShowAuthUserEventsRecyclerAdapter = new ShowAuthUserEventsRecyclerAdapter(context, eventsCursor);
        // RecycerView для отображения таблицы users БД
        rvMainShAuUsEvAc = (RecyclerView) findViewById(R.id.rvMainShAuUsEvAc);

        rvMainShAuUsEvAc.setAdapter(ShowAuthUserEventsRecyclerAdapter);
        tvauthuserevents = (TextView) findViewById(R.id.tvauthuserevents);
        tvauthuserevents.setText(String.valueOf(eventsCursor.getCount()));
    }//onCreate

}//ShowAuthUserEventsActivity
