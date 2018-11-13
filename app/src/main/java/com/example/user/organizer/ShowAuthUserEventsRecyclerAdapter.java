package com.example.user.organizer;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

//-------Активность адаптера для вывода(просмотра) событий авторизированого пользователя--------------

public class ShowAuthUserEventsRecyclerAdapter extends RecyclerView.Adapter<ShowAuthUserEventsRecyclerAdapter.ViewHolder> {

    //поля класса AdvertisingAndInformationRecyclerAdapter
    private LayoutInflater inflater;
    private Cursor eventsCursor;
    DBUtilities dbUtilities;

    //конструктор
    ShowAuthUserEventsRecyclerAdapter(Context context, Cursor eventsCursor) {
        this.inflater = LayoutInflater.from(context);
        this.eventsCursor = eventsCursor;
        dbUtilities = new DBUtilities(context);
    } // AdvertisingAndInformationRecyclerAdapter

    //создаем новую разметку(View) путем указания разметки
    @Override
    public ShowAuthUserEventsRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.recycler_adapter_show_auth_user_events, parent, false);
        return new ShowAuthUserEventsRecyclerAdapter.ViewHolder(view);
    }

    //привязываем элементы разметки к переменным объекта(в данном случае к курсору)
    @Override
    public void onBindViewHolder(ShowAuthUserEventsRecyclerAdapter.ViewHolder holder, int position) {
        eventsCursor.moveToPosition(position); // переходим в курсоре на текущую позицию

        // получение данных
        holder.tvCityShAuUsEvReAd.setText(eventsCursor.getString(0)); //город
        holder.tvFieldShAuUsEvReAd.setText(eventsCursor.getString(1));//поле
        holder.tvDateShAuUsEvReAd.setText(eventsCursor.getString(2)); // Дата
        holder.tvTimeShAuUsEvReAd.setText(eventsCursor.getString(3)); //Время
        holder.tvStatusShAuUsEvReAd.setText((eventsCursor.getInt(4) == 0)?"Организатор":"Участник"); // Статус игрока
    } // onBindViewHolder

    //получаем количество элементов объекта(курсора)
    @Override
    public int getItemCount() { return eventsCursor.getCount(); }

    //Создаем класс ViewHolder с помощью которого мы получаем ссылку на каждый элемент
    //отдельного пункта списка
    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvDateShAuUsEvReAd, tvTimeShAuUsEvReAd, tvCityShAuUsEvReAd,
                       tvFieldShAuUsEvReAd, tvStatusShAuUsEvReAd;

        ViewHolder(View view){
            super(view);

            tvDateShAuUsEvReAd = (TextView) view.findViewById(R.id.tvDateShAuUsEvReAd);
            tvTimeShAuUsEvReAd = (TextView) view.findViewById(R.id.tvTimeShAuUsEvReAd);
            tvCityShAuUsEvReAd = (TextView) view.findViewById(R.id.tvCityShAuUsEvReAd);
            tvFieldShAuUsEvReAd = (TextView) view.findViewById(R.id.tvFieldShAuUsEvReAd);
            tvStatusShAuUsEvReAd = (TextView) view.findViewById(R.id.tvStatusShAuUsEvReAd);
        } // ViewHolder
    } // class ViewHolder

}//AdvertisingAndInformationRecyclerAdapter
