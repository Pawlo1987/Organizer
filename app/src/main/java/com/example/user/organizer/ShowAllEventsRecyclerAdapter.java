package com.example.user.organizer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.user.organizer.fragment.AboutEventShowAllEventDialog;
import com.example.user.organizer.fragment.TakePartShowAllEventDialog;

import java.util.ArrayList;
import java.util.List;


//-------Адаптера для вывода(просмотра) всех событий--------------
public class ShowAllEventsRecyclerAdapter extends
        RecyclerView.Adapter<ShowAllEventsRecyclerAdapter.ViewHolder>{

    //поля класса ShowAllEventsRecyclerAdapter
    private LayoutInflater inflater;
    Event event = new Event();
    List<Event> eventsList = new ArrayList<>(); //коллекция событий
    DBUtilities dbUtilities;
    Context context;
    String idAuthUser;         //Авторизированный пользователь

    String eventId;
    String eventCityName;
    String eventFieldName;
    String eventData;
    String eventTime;
    String eventDuration;
    String eventPrice;
    String eventPhone;
    String eventUserId;

    AboutEventShowAllEventDialog aboutEventShowAllEventDialog =
            new AboutEventShowAllEventDialog(); // диалог подтверждения выхода из приложения

    TakePartShowAllEventDialog takePartShowAllEventDialog =
            new TakePartShowAllEventDialog(); // диалог подтверждения выхода из приложения

    final String ID_ABOUT_DIALOG = "aboutEventShowAllEventDialog";  //параметр для вызова диалога "about"
    final String ID_TAKEPART_DIALOG = "takePartShowAllEventDialog";  //параметр для вызова диалога "takePart"

    //конструктор
    public ShowAllEventsRecyclerAdapter(Context context, List<Event> eventsList, String idAuthUser) {
        this.inflater = LayoutInflater.from(context);
        this.eventsList = eventsList;
        this.context = context;
        this.idAuthUser = idAuthUser;
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
        Event eventShow = eventsList.get(position);

        // получение данных
        holder.tvCityShAlEvReAd.setText(eventShow.cityName); //город
        holder.tvFieldShAlEvReAd.setText(eventShow.fieldName);//поле
        holder.tvDateShAlEvReAd.setText(eventShow.eventData); // Дата
        holder.tvTimeShAlEvReAd.setText(eventShow.eventTime); //Время
    } // onBindViewHolder

    //получаем количество элементов объекта(курсора)
    @Override
    public int getItemCount() { return eventsList.size(); }

    //Создаем класс ViewHolder с помощью которого мы получаем ссылку на каждый элемент
    //отдельного пункта списка и подключаем слушателя события нажатия меню
    public class ViewHolder extends RecyclerView.ViewHolder
            implements PopupMenu.OnMenuItemClickListener{
        final TextView tvDateShAlEvReAd, tvTimeShAlEvReAd,
                tvCityShAlEvReAd, tvFieldShAlEvReAd;
        LinearLayout llMainShAlEvReAd;
        ImageView ivImageShAlEvReAd;

        ViewHolder(View view){
            super(view);

            tvDateShAlEvReAd = view.findViewById(R.id.tvDateShAlEvReAd);
            tvTimeShAlEvReAd = view.findViewById(R.id.tvTimeShAlEvReAd);
            tvCityShAlEvReAd = view.findViewById(R.id.tvCityShAlEvReAd);
            tvFieldShAlEvReAd = view.findViewById(R.id.tvFieldShAlEvReAd);
            llMainShAlEvReAd = view.findViewById(R.id.llMainShAlEvReAd);
            ivImageShAlEvReAd = view.findViewById(R.id.ivImageShAlEvReAd);

            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            view.setBackgroundColor(Color.argb(255,135,135,135));
                            break;
                        case MotionEvent.ACTION_UP:
                            view.setBackgroundColor(Color.argb(255,170,170,170));
                            break;
                        default:
                            view.setBackgroundColor(Color.argb(255,170,170,170));
                            break;

                    }//switch
                    return false;
                }//onTouch
            });//setOnTouchListener

            ivImageShAlEvReAd.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            ivImageShAlEvReAd.setImageResource(R.drawable.football_ball11);
                            break;
                        case MotionEvent.ACTION_UP:
                            ivImageShAlEvReAd.setImageResource(R.drawable.football_ball1);
                            break;
                        default:
                            ivImageShAlEvReAd.setImageResource(R.drawable.football_ball1);
                            break;

                    }//switch
                    return false;
                }//onTouch
            });//setOnTouchListener

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //получаем данные о нажатом событии
                    event = eventsList.get(getAdapterPosition());
                    eventId = event.eventId;
                    eventCityName = event.cityName;
                    eventFieldName = event.fieldName;
                    eventData = event.eventData;
                    eventTime = event.eventTime;
                    eventDuration = event.eventDuration;
                    eventPrice = event.eventPrice;
                    eventPhone = event.eventPhone;
                    eventUserId = event.eventUserId;

                    //выбран пункт подробная информация
                    aboutEvent();
                    return true;
                }//onLongClick
            });//setOnLongClickListener

            //слушатель события нажатого меню
            ivImageShAlEvReAd.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
//                    PopupMenu popup = new PopupMenu(view.getContext(), view);
//                    popup.inflate(R.menu.rva_show_all_event_popup_menu);
//                    popup.setOnMenuItemClickListener(ViewHolder.this);
//                    popup.show();


                    //получаем данные о нажатом событии
                    event = eventsList.get(getAdapterPosition());
                    eventId = event.eventId;
                    eventCityName = event.cityName;
                    eventFieldName = event.fieldName;
                    eventData = event.eventData;
                    eventTime = event.eventTime;
                    eventDuration = event.eventDuration;
                    eventPrice = event.eventPrice;
                    eventPhone = event.eventPhone;
                    eventUserId = event.eventUserId;

                    //выбран пункт принять участие
                    takePart();
                }//onClick
            });
        } // ViewHolder

        //обработчик выбраного пункта меню
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.item_take_part_show_all_rva:      //выбран пункт принять участие
                    takePart();
                    break;
                case R.id.item_about_event_show_all_rva:    //выбран пункт подробная информация
                    aboutEvent();
                    break;
            }//switch
            return false;
        }//onMenuItemClick

        //Вызов диалога для принятия участия в событии
        private void takePart() {

            String message;     //строка которую передаем в диалог в поле сообщения

            //принимает ли авторизированный пользователь участие в данном событии
            boolean userTakeInPart = dbUtilities.isTakingPart(eventId, idAuthUser);

            if(userTakeInPart){
                //если данный пользователь уже является участником события
                message = "Вы уже участвуете в данном событии!";
            }else{
                message = fullInfoAboutEvent();
            }//if-else

            Bundle args = new Bundle();    // объект для передачи параметров в диалог
            args.putString("message", message);
            args.putBoolean("userTakeInPart", userTakeInPart);
            args.putString("event_id", eventId);
            args.putString("user_id", idAuthUser);
            takePartShowAllEventDialog.setArguments(args);

            // отображение диалогового окна
            takePartShowAllEventDialog.show(((AppCompatActivity)context).
                    getSupportFragmentManager(), ID_TAKEPART_DIALOG);
        }//takePart

        //Вызов диалога для подробной информации о событии
        private void aboutEvent() {
            String message = fullInfoAboutEvent();
            Bundle args = new Bundle();    // объект для передачи параметров в диалог
            args.putString("message", message);
            aboutEventShowAllEventDialog.setArguments(args);

            // отображение диалогового окна
            aboutEventShowAllEventDialog.show(((AppCompatActivity)context).
                    getSupportFragmentManager(), ID_ABOUT_DIALOG);
        }//aboutEvent

        //строка с полной информацией о событии
        private String fullInfoAboutEvent() {
            return String.format("Собитие в городе %s\n" +
                            "На поле %s \n" +
                            "Назначено на %s\n" +
                            "Cостоится в %s\n" +
                            "Продолжитльность %s мин\n" +
                            "Стоимость %s руб\n" +
                            "Телефон %s",
                    eventCityName,
                    eventFieldName,
                    eventData,
                    eventTime,
                    eventDuration,
                    eventPrice,
                    eventPhone);
        }//fullInfoAboutEvent
    } // class ViewHolder

}//ShowAllEventsRecyclerAdapter
