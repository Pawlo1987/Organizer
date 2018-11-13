package com.example.user.organizer.activity;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.organizer.DBUtilities;
import com.example.user.organizer.R;

//-------Активность адаптера для вывода(просмотра) новости и реклама --------------

public class AdvertisingAndInformationRecyclerAdapter extends RecyclerView.Adapter<AdvertisingAndInformationRecyclerAdapter.ViewHolder> {

    //поля класса advertisingAndInformationRecyclerAdapter
    private LayoutInflater inflater;
    private Cursor infoCursor;
    DBUtilities dbUtilities;


    //конструктор
    public AdvertisingAndInformationRecyclerAdapter(Context context, Cursor infoCursor) {
        this.inflater = LayoutInflater.from(context);
        this.infoCursor = infoCursor;
        dbUtilities = new DBUtilities(context);
    } // advertisingAndInformationRecyclerAdapter

    //создаем новую разметку(View) путем указания разметки
    @Override
    public AdvertisingAndInformationRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.recycler_adapter_advertising_and_informationa, parent, false);
        return new AdvertisingAndInformationRecyclerAdapter.ViewHolder(view);
    }

    //привязываем элементы разметки к переменным объекта(в данном случае к курсору)
    @Override
    public void onBindViewHolder(AdvertisingAndInformationRecyclerAdapter.ViewHolder holder, int position) {
        infoCursor.moveToPosition(position); // переходим в курсоре на текущую позицию

        holder.tvHeadAdAvInReAd.setText(infoCursor.getString(0));     //заголовок
        holder.tvDateAdAvInReAd.setText(infoCursor.getString(1));     //дата
        holder.tvCityAdAvInReAd.setText(infoCursor.getString(2));     //город

    } // onBindViewHolder

    //получаем количество элементов объекта(курсора)
    @Override
    public int getItemCount() { return infoCursor.getCount(); }

    //Создаем класс ViewHolder с помощью которого мы получаем ссылку на каждый элемент
    //отдельного пункта списка
    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvHeadAdAvInReAd, tvDateAdAvInReAd, tvCityAdAvInReAd;

        ViewHolder(View view){
            super(view);

            tvHeadAdAvInReAd = (TextView) view.findViewById(R.id.tvHeadAdAvInReAd);
            tvDateAdAvInReAd = (TextView) view.findViewById(R.id.tvDateAdAvInReAd);
            tvCityAdAvInReAd = (TextView) view.findViewById(R.id.tvCityAdAvInReAd);
        } // ViewHolder
    } // class ViewHolder
}//advertisingAndInformationRecyclerAdapter
