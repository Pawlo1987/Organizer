package com.example.user.organizer.activity;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.user.organizer.DBUtilities;
import com.example.user.organizer.R;
import com.example.user.organizer.ShowAuthUserEventsRecyclerAdapter;

//-------Активность для вывода(просмотра) событий авторизированого пользователя--------------

public class ShowAuthUserEventsActivity extends AppCompatActivity {

    RecyclerView rvMainShAuUsEvAc;
    ShowAuthUserEventsRecyclerAdapter showAuthUserEventsRecyclerAdapter;  // адаптер для отображения recyclerView
    DBUtilities dbUtilities;

    // поля для доступа к записям БД
    Cursor eventsCursor;                // прочитанные данные

    //колличество events._id в статусе организатора
    int orgStatus;

    TextView tvauthuserevents;
    Context context;
    int idAuthUser = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_show_auth_user_events);

        context = getBaseContext();
        dbUtilities = new DBUtilities(context);
        dbUtilities.open();
        // 1. Данные о пользователе в роле организатора
        // получаем данные из БД в виде курсора (коллекция, возвращенная запросом)
        String query = "SELECT events._id FROM events WHERE events.user_id = \"" + idAuthUser + "\";";
        eventsCursor =  dbUtilities.getDb().rawQuery(query, null);

        //кол-во событий в роли организатора
        orgStatus = eventsCursor.getCount();

        // 2. Объеденный запрос для получения данных об участии пользователя
        // получаем данные из БД в виде курсора (коллекция, возвращенная запросом)
        query = "SELECT cities.name, fields.name, events.date, events.time FROM events " +
                "INNER JOIN fields ON fields._id = events.field_id " +
                "INNER JOIN cities ON cities._id = events.city_id " +
                "WHERE events.user_id = \"" +
                idAuthUser + "\" UNION SELECT cities.name, fields.name, events.date, events.time " +
                "FROM participants " +
                "INNER JOIN events ON events._id = participants.event_id " +
                "INNER JOIN fields ON fields._id = events.field_id " +
                "INNER JOIN cities ON cities._id = events.city_id " +
                "WHERE participants.user_id = \"" + idAuthUser + "\";";

        eventsCursor =  dbUtilities.getDb().rawQuery(query, null);

        // создаем адаптер, передаем в него курсор
//        showAuthUserEventsRecyclerAdapter = new ShowAuthUserEventsRecyclerAdapter(context, eventsCursor);
        // RecycerView для отображения таблицы users БД
        rvMainShAuUsEvAc = (RecyclerView) findViewById(R.id.rvMainShAuUsEvAc);

        rvMainShAuUsEvAc.setAdapter(showAuthUserEventsRecyclerAdapter);
        tvauthuserevents = (TextView) findViewById(R.id.tvauthuserevents);
        tvauthuserevents.setText(String.valueOf(eventsCursor.getCount()));
    }//onCreate

}//ShowAuthUserEventsActivity
