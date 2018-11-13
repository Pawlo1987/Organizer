package com.example.user.organizer.activity;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.user.organizer.DBUtilities;
import com.example.user.organizer.R;
import com.example.user.organizer.ShowAllEventsRecyclerAdapter;

//-------Активность для вывода(просмотра) всех событий--------------

public class ShowAllEventActivity extends AppCompatActivity {
    RecyclerView rvMainShAlEvAc;
    ShowAllEventsRecyclerAdapter showAllEventsRecyclerAdapter;  // адаптер для отображения recyclerView
    DBUtilities dbUtilities;

    // поля для доступа к записям БД
    Cursor eventsCursor;                // прочитанные данные

    TextView tvallevents;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_show_all_event);

        context = getBaseContext();
        dbUtilities = new DBUtilities(context);
        dbUtilities.open();
        // получаем данные из БД в виде курсора (коллекция, возвращенная запросом)
        String query = "SELECT cities.name as cities, fields.name as fields, events.date as date, " +
                "events.starttime as time FROM events INNER JOIN cities ON cities._id = events.city " +
                "INNER JOIN fields ON fields._id = events.field;";
        eventsCursor =  dbUtilities.getDb().rawQuery(query, null);

        // создаем адаптер, передаем в него курсор
        showAllEventsRecyclerAdapter = new ShowAllEventsRecyclerAdapter(context, eventsCursor);
        // RecycerView для отображения таблицы users БД
        rvMainShAlEvAc = (RecyclerView) findViewById(R.id.rvMainShAlEvAc);

        rvMainShAlEvAc.setAdapter(showAllEventsRecyclerAdapter);
        tvallevents = (TextView) findViewById(R.id.tvallevents);
        tvallevents.setText(String.valueOf(eventsCursor.getCount()));
    }//onCreate

}//ShowAllEventActivity
