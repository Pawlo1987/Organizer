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
import com.example.user.organizer.Event;
import com.example.user.organizer.R;
import com.example.user.organizer.ShowAllEventsRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ShowAllEventsFragment extends Fragment {

    RecyclerView rvMainShAlEvAc;
    // адаптер для отображения recyclerView
    ShowAllEventsRecyclerAdapter showAllEventsRecyclerAdapter;
    DBUtilities dbUtilities;

    List<Event> eventsList = new ArrayList<>(); //коллекция событий
    Context context;

    String idAuthUser = "7";                 //авторизированный пользователь

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

        //получаем коллекцию событий
        eventsList = dbUtilities.getListEvents("");

        // создаем адаптер, передаем в него курсор
        showAllEventsRecyclerAdapter = new ShowAllEventsRecyclerAdapter(context, eventsList, idAuthUser);
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

        return result;
    } // onCreateView
}
