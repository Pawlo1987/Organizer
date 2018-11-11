package com.example.user.organizer;

import android.content.Context;
import android.hardware.Sensor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

//-----------Фрагмент выводит все активные события--------------------------

public class ShowAllEventsFragment extends Fragment {

//    RecyclerView rvMainShAlEvFr;
//    ShowAllEventsRecyclerAdapter showAllEventsRecyclerAdapter;  // адаптер для отображения recyclerView
//
    public ShowAllEventsFragment newInstance() {
        ShowAllEventsFragment fragment = new ShowAllEventsFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    } // FirstPageFragment

    public ShowAllEventsFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        // получить параметр для отображения во фрагменте
//        Bundle args = getArguments();

    } // onCreate

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        FragmentActivity current = getActivity();
//
        View result = inflater.inflate(R.layout.fragment_show_all_events, container, false);
//        // RecycerView для отображения
//        rvMainShAlEvFr = (RecyclerView) result.findViewById(R.id.rvMainShAlEvFr);
//
//        // создаем адаптер
//        showAllEventsRecyclerAdapter = new ShowAllEventsRecyclerAdapter(current);
//        rvMainShAlEvFr.setAdapter();
        return result;
    } // onCreateView
}
