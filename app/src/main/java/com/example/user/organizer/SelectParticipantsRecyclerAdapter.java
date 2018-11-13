package com.example.user.organizer;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

public class SelectParticipantsRecyclerAdapter extends RecyclerView.Adapter<SelectParticipantsRecyclerAdapter.ViewHolder> {

    //поля класса SelectParticipantsRecyclerAdapter
    private LayoutInflater inflater;
    private Cursor selectUserCursor;
    DBUtilities dbUtilities;

    //конструктор
    SelectParticipantsRecyclerAdapter(Context context, Cursor selectUserCursor) {
        this.inflater = LayoutInflater.from(context);
        this.selectUserCursor = selectUserCursor;
        dbUtilities = new DBUtilities(context);
    } // SelectParticipantsRecyclerAdapter

    //создаем новую разметку(View) путем указания разметки
    @Override
    public SelectParticipantsRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.recycler_adapter_select_participants, parent, false);
        return new SelectParticipantsRecyclerAdapter.ViewHolder(view);
    }

    //привязываем элементы разметки к переменным объекта(в данном случае к курсору)
    @Override
    public void onBindViewHolder(SelectParticipantsRecyclerAdapter.ViewHolder holder, int position) {
        selectUserCursor.moveToPosition(position); // переходим в курсоре на текущую позицию

        // получение данных
        holder.cbSePaReAd.setChecked(false);                            // CheckBox выбор
        holder.tvNameSePaReAd.setText(selectUserCursor.getString(0));       // имя user
        holder.tvLoginSePaReAd.setText(selectUserCursor.getString(1));      // логин user
        holder.tvDefCitySePaReAd.setText(selectUserCursor.getString(2));    // город user

//        получаем нажатую позицию адаптера
        int posnow = selectUserCursor.getPosition();

    } // onBindViewHolder

    //получаем количество элементов объекта(курсора)
    @Override
    public int getItemCount() { return selectUserCursor.getCount(); }

    //Создаем класс ViewHolder с помощью которого мы получаем ссылку на каждый элемент
    //отдельного пункта списка
    public class ViewHolder extends RecyclerView.ViewHolder {
        final CheckBox cbSePaReAd;
        final TextView tvNameSePaReAd, tvLoginSePaReAd, tvDefCitySePaReAd;

        ViewHolder(View view){
            super(view);

            cbSePaReAd = (CheckBox) view.findViewById(R.id.cbSePaReAd);
            tvNameSePaReAd = (TextView) view.findViewById(R.id.tvNameSePaReAd);
            tvLoginSePaReAd = (TextView) view.findViewById(R.id.tvLoginSePaReAd);
            tvDefCitySePaReAd = (TextView) view.findViewById(R.id.tvDefCitySePaReAd);
        } // ViewHolder
    } // class ViewHolder
}//SelectParticipantsRecyclerAdapter
