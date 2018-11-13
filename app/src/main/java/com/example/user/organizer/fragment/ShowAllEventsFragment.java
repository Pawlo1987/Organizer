package com.example.user.organizer.fragment;

//-----------Фрагмент выводит все активные события--------------------------

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
import com.example.user.organizer.ShowAllEventsRecyclerAdapter;

public class ShowAllEventsFragment extends Fragment {

    RecyclerView rvMainShAlEvAc;
    ShowAllEventsRecyclerAdapter showAllEventsRecyclerAdapter;  // адаптер для отображения recyclerView
    DBUtilities dbUtilities;

    // поля для доступа к записям БД
    Cursor eventsCursor;                // прочитанные данные

    TextView tvallevents;
    Context context;

    public ShowAllEventsFragment newInstance() {

        ShowAllEventsFragment fragment = new ShowAllEventsFragment();

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
        String query = "SELECT cities.name, fields.name, events.date, " +
                "events.time, events._id FROM events " +
                "INNER JOIN cities ON cities._id = events.city_id " +
                "INNER JOIN fields ON fields._id = events.field_id;";
        eventsCursor =  dbUtilities.getDb().rawQuery(query, null);

        // создаем адаптер, передаем в него курсор
        showAllEventsRecyclerAdapter = new ShowAllEventsRecyclerAdapter(context, eventsCursor);
        super.onAttach(context);
    } // onAttach

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View result = inflater.inflate(R.layout.fragment_show_all_event, container, false);
        // RecyclerView для отображения таблицы users БД
        rvMainShAlEvAc = result.findViewById(R.id.rvMainShAlEvAc);
        //привязываем адаптер к recycler объекту
        rvMainShAlEvAc.setAdapter(showAllEventsRecyclerAdapter);

        //количество созданных событий
        tvallevents = result.findViewById(R.id.tvallevents);
        tvallevents.setText(String.valueOf(eventsCursor.getCount()));
        return result;
    } // onCreateView
}
