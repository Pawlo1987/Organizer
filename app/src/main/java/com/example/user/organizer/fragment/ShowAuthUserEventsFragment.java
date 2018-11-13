package com.example.user.organizer.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.organizer.DBUtilities;
import com.example.user.organizer.R;
import com.example.user.organizer.ShowAuthUserEventsRecyclerAdapter;

public class ShowAuthUserEventsFragment extends Fragment {
    RecyclerView rvMainShAuUsEvAc;

    // адаптер для отображения recyclerView
    ShowAuthUserEventsRecyclerAdapter showAuthUserEventsRecyclerAdapter;
    DBUtilities dbUtilities;

    Context context;
    String idAuthUser = "7";

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

        // создаем адаптер, передаем в него курсор
        showAuthUserEventsRecyclerAdapter
                = new ShowAuthUserEventsRecyclerAdapter(context, idAuthUser);

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

        return result;
    } // onCreateView

}
