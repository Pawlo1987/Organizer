package com.example.user.organizer;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


//-------Активность адаптера для вывода(просмотра) всех событий--------------

public class ShowAllEventsRecyclerAdapter extends RecyclerView.Adapter<ShowAllEventsRecyclerAdapter.ViewHolder>{

    //поля класса ShowAllEventsRecyclerAdapter
    private LayoutInflater inflater;
    private Cursor eventsCursor;
    DBUtilities dbUtilities;

    //конструктор
    ShowAllEventsRecyclerAdapter(Context context, Cursor eventsCursor) {
        this.inflater = LayoutInflater.from(context);
        this.eventsCursor = eventsCursor;
        dbUtilities = new DBUtilities(context);
    } // ShowAllEventsRecyclerAdapter

    //создаем новую разметку(View) путем указания разметки
    @Override
    public ShowAllEventsRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.recycler_adapter_show_all_events, parent, false);
        return new ViewHolder(view);
    }

    //привязываем элементы разметки к переменным объекта(в данном случае к курсору)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        eventsCursor.moveToPosition(position); // переходим в курсоре на текущую позицию

        // получение данных
        holder.tvCityShAlEvReAd.setText(eventsCursor.getString(0));//String.format("%d", eventsCursor.getInt(1)));//город
        holder.tvFieldShAlEvReAd.setText(eventsCursor.getString(1));//(String.format("%d", eventsCursor.getInt(2)));//поле
        holder.tvDateShAlEvReAd.setText(eventsCursor.getString(2));// Дата
        holder.tvTimeShAlEvReAd.setText(eventsCursor.getString(3));//Время

//        получаем нажатую позицию адаптера
        int posnow = eventsCursor.getPosition();

    } // onBindViewHolder

    //получаем количество элементов объекта(курсора)
    @Override
    public int getItemCount() { return eventsCursor.getCount(); }

    //Создаем класс ViewHolder с помощью которого мы получаем ссылку на каждый элемент
    //отдельного пункта списка
    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvDateShAlEvReAd, tvTimeShAlEvReAd, tvCityShAlEvReAd, tvFieldShAlEvReAd;

        ViewHolder(View view){
            super(view);

            tvDateShAlEvReAd = (TextView) view.findViewById(R.id.tvDateShAlEvReAd);
            tvTimeShAlEvReAd = (TextView) view.findViewById(R.id.tvTimeShAlEvReAd);
            tvCityShAlEvReAd = (TextView) view.findViewById(R.id.tvCityShAlEvReAd);
            tvFieldShAlEvReAd = (TextView) view.findViewById(R.id.tvFieldShAlEvReAd);
        } // ViewHolder
    } // class ViewHolder
}//ShowAllEventsRecyclerAdapter
