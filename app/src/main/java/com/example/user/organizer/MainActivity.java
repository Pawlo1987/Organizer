package com.example.user.organizer;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    ShowAllEventsFragment fragm;
//    DBUtilities dbUtilities;
//
//    // поля для доступа к записям БД
//    Cursor eventsCursor;                // прочитанные данные
//    ShowAllEventsRecyclerAdapter showAllEventsRecyclerAdapter;  // адаптер для отображения таблицы
//
//    RecyclerView rvMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        dbUtilities = new DBUtilities(getApplicationContext());
//
//        dbUtilities.open();
//        // получаем данные из БД в виде курсора (коллекция, возвращенная запросом)
//        String query = "SELECT city.name as city, field.name as field, event.date as date, " +
//                "event.starttime as time FROM event INNER JOIN city ON city._id = event.city " +
//                "INNER JOIN field ON field._id = event.field;";
//        eventsCursor =  dbUtilities.getDb().rawQuery(query, null);
//
//        // RecycerView для отображения таблицы users БД
//        rvMain = (RecyclerView) findViewById(R.id.rvMain);
//
//        // создаем адаптер, передаем в него курсор
//        showAllEventsRecyclerAdapter = new ShowAllEventsRecyclerAdapter(this, eventsCursor);
//        rvMain.setAdapter(showAllEventsRecyclerAdapter);

//        вызвать фрагмент
        fragm = new ShowAllEventsFragment().newInstance();
    }//onCreate


}//MainActivity
