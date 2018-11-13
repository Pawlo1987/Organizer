package com.example.user.organizer;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

//-------Адаптер для вывода(просмотра) новости и реклама --------------
public class AdvertisingAndInformationRecyclerAdapter extends RecyclerView.Adapter<AdvertisingAndInformationRecyclerAdapter.ViewHolder> {

    //поля класса advertisingAndInformationRecyclerAdapter
    private LayoutInflater inflater;
    DBUtilities dbUtilities;
    Note note;
    List<Note> notes;

    //конструктор
    public AdvertisingAndInformationRecyclerAdapter(Context context, List<Note> notes) {
        this.inflater = LayoutInflater.from(context);
        this.notes = notes;
        dbUtilities = new DBUtilities(context);
    } // advertisingAndInformationRecyclerAdapter

    //создаем новую разметку(View) путем указания разметки
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.recycler_adapter_advertising_and_informationa, parent, false);
        return new ViewHolder(view);
    }

    //привязываем элементы разметки к переменным объекта(в данном случае к курсору)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        note = notes.get(position); // переходим в курсоре на текущую позицию

        holder.tvHeadAdAvInReAd.setText(note.getNoteHead());     //заголовок
        holder.tvDateAdAvInReAd.setText(note.getNoteDate());     //дата
        holder.tvCityAdAvInReAd.setText(note.getNoteCityName()); //город


    } // onBindViewHolder

    //получаем количество элементов объекта(курсора)
    @Override
    public int getItemCount() { return notes.size(); }

    //Создаем класс ViewHolder с помощью которого мы получаем ссылку на каждый элемент
    //отдельного пункта списка
    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvHeadAdAvInReAd, tvDateAdAvInReAd, tvCityAdAvInReAd;

        ViewHolder(View view){
            super(view);

            tvHeadAdAvInReAd = view.findViewById(R.id.tvHeadAdAvInReAd);
            tvDateAdAvInReAd = view.findViewById(R.id.tvDateAdAvInReAd);
            tvCityAdAvInReAd = view.findViewById(R.id.tvCityAdAvInReAd);
        } // ViewHolder
    } // class ViewHolder
}//advertisingAndInformationRecyclerAdapter
