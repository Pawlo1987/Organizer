package com.example.user.organizer;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

//-------Активность для вывода(просмотра) всех событий--------------

public class ShowAllEventActivity extends AppCompatActivity {
    RecyclerView rvMainShAlEvAc;
    ShowAllEventsRecyclerAdapter showAllEventsRecyclerAdapter;  // адаптер для отображения recyclerView
    DBUtilities dbUtilities;

    // поля для доступа к записям БД
    Cursor eventsCursor;                // прочитанные данные

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_event);

        context = getBaseContext();
        dbUtilities = new DBUtilities(context);
        dbUtilities.open();
        // получаем данные из БД в виде курсора (коллекция, возвращенная запросом)
        String query = "SELECT city.name as city, field.name as field, event.date as date, " +
                "event.starttime as time FROM event INNER JOIN city ON city._id = event.city " +
                "INNER JOIN field ON field._id = event.field;";
        eventsCursor =  dbUtilities.getDb().rawQuery(query, null);

        // создаем адаптер, передаем в него курсор
        showAllEventsRecyclerAdapter = new ShowAllEventsRecyclerAdapter(context, eventsCursor);
        // RecycerView для отображения таблицы users БД
        rvMainShAlEvAc = (RecyclerView) findViewById(R.id.rvMainShAlEvAc);

        rvMainShAlEvAc.setAdapter(showAllEventsRecyclerAdapter);
    }//onCreate

}//ShowAllEventActivity
