package com.example.user.organizer;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;


//-------Активность адаптера для вывода(просмотра) всех событий--------------

public class ShowAllEventsRecyclerAdapter extends
        RecyclerView.Adapter<ShowAllEventsRecyclerAdapter.ViewHolder>{

    //поля класса ShowAllEventsRecyclerAdapter
    int idEvent;
    private LayoutInflater inflater;
    private Cursor eventsCursor;
    DBUtilities dbUtilities;

    //конструктор
    public ShowAllEventsRecyclerAdapter(Context context, Cursor eventsCursor) {
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
        holder.tvCityShAlEvReAd.setText(eventsCursor.getString(0)); //город
        holder.tvFieldShAlEvReAd.setText(eventsCursor.getString(1));//поле
        holder.tvDateShAlEvReAd.setText(eventsCursor.getString(2)); // Дата
        holder.tvTimeShAlEvReAd.setText(eventsCursor.getString(3)); //Время
        holder.idEvent = eventsCursor.getInt(4);

    } // onBindViewHolder

    //получаем количество элементов объекта(курсора)
    @Override
    public int getItemCount() { return eventsCursor.getCount(); }

    //Создаем класс ViewHolder с помощью которого мы получаем ссылку на каждый элемент
    //отдельного пункта списка и подключаем слушателя события нажатия меню
    public class ViewHolder extends RecyclerView.ViewHolder implements PopupMenu.OnMenuItemClickListener{
        final TextView tvDateShAlEvReAd, tvTimeShAlEvReAd, tvCityShAlEvReAd, tvFieldShAlEvReAd;
        public int idEvent;

        ViewHolder(View view){
            super(view);

            tvDateShAlEvReAd = view.findViewById(R.id.tvDateShAlEvReAd);
            tvTimeShAlEvReAd = view.findViewById(R.id.tvTimeShAlEvReAd);
            tvCityShAlEvReAd = view.findViewById(R.id.tvCityShAlEvReAd);
            tvFieldShAlEvReAd = view.findViewById(R.id.tvFieldShAlEvReAd);

            //слушатель события нажатого меню
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popup = new PopupMenu(view.getContext(), view);
                    popup.inflate(R.menu.rva_show_all_event_popup_menu);
                    popup.setOnMenuItemClickListener(ViewHolder.this);
                    popup.show();
                }
            });
        } // ViewHolder

        //обработчик выбраного пункта меню
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.item_take_part_show_all_rva:      //выбран пункт принять участие
                    Log.d("MyLog","takePart = " + idEvent);
                    break;
                case R.id.item_about_event_show_all_rva:    //выбран пункт подробная информация
                    aboutEvent(idEvent);
                    break;
            }//switch
            return false;
        }//onMenuItemClick

        private void aboutEvent(int idEvent) {
            // получаем данные из БД в виде курсора (коллекция, возвращенная запросом)
            String query = "SELECT city_id, field_id, date, time, duration, price, phone FROM events;";
            eventsCursor = dbUtilities.getDb().rawQuery(query, null);
            eventsCursor.moveToPosition(0); // переходим в курсоре на текущую позицию
            eventsCursor.getString(0);
            String massege = "Матч в городе " + eventsCursor.getString(0);


        }//aboutEvent
    } // class ViewHolder
}//ShowAllEventsRecyclerAdapter
