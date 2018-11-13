package com.example.user.organizer;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.organizer.inteface.CallDialogsAllEvents;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//-------Адаптера для вывода(просмотра) всех событий--------------
public class ShowAllEventsRecyclerAdapter extends
        RecyclerView.Adapter<ShowAllEventsRecyclerAdapter.ViewHolder>{

    //поля класса ShowAllEventsRecyclerAdapter
    private LayoutInflater inflater;
    Event event = new Event();
    List<Event> eventsList = new ArrayList<>(); //коллекция событий
    DBUtilities dbUtilities;
    CallDialogsAllEvents callDialogsAllEvents;
    Context context;
    String idAuthUser;         //Авторизированный пользователь

    String eventId;
    String eventCityName;
    String eventFieldName;
    String showEventDate;
    String eventDate;
    String eventTime;
    String eventDuration;
    String eventPrice;
    String eventPhone;
    String eventUserId;

    //конструктор
    public ShowAllEventsRecyclerAdapter(CallDialogsAllEvents callDialogsAllEvents,
                                        Context context, String idAuthUser) {
        this.inflater = LayoutInflater.from(context);
        //получение интерфеса из класса Фрагмента
        //для обработки нажатия элементов RecyclerAdapter
        this.callDialogsAllEvents = callDialogsAllEvents;
        this.context = context;
        this.idAuthUser = idAuthUser;
        dbUtilities = new DBUtilities(context);

        this.eventsList = dbUtilities.getListEventsForAuthUser("", idAuthUser);
    } // ShowAllEventsRecyclerAdapter

    // метод для обновления адаптера
    // после внесения изменения в базу данных
    public void updateEventList(){
        eventsList.clear();

        //получаем коллекцию событий
        eventsList = dbUtilities.getListEventsForAuthUser("", idAuthUser);

        notifyDataSetChanged();
    }//updateEventList

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
        holder.tvDateShAlEvReAd.setText(dateShowFormat(eventShow.eventData)); // Дата
        holder.tvTimeShAlEvReAd.setText(eventShow.eventTime); //Время

        //опредиляемся с цветом CardView взависимости от роли пользователя в собитии
        int color;
        if(eventShow.eventUserStatus.equals("0"))
            color = context.getResources().getColor(R.color.colorMyColorBlue);
        else if(eventShow.eventUserStatus.equals("1"))
            color = context.getResources().getColor(R.color.colorMyColorGold);
        else color = context.getResources().getColor(R.color.colorMyColorGrey);

        holder.cvMainShAlEvAc.setCardBackgroundColor( color );
    } // onBindViewHolder

    //получаем количество элементов объекта(курсора)
    @Override
    public int getItemCount() { return eventsList.size(); }

    //Создаем класс ViewHolder с помощью которого мы получаем ссылку на каждый элемент
    //отдельного пункта списка и подключаем слушателя события нажатия меню
    public class ViewHolder extends RecyclerView.ViewHolder{
        final TextView tvDateShAlEvReAd, tvTimeShAlEvReAd,
                tvCityShAlEvReAd, tvFieldShAlEvReAd;
        CardView cvMainShAlEvAc;
        ImageView ivArrowShAlEvReAd;

        ViewHolder(View view){
            super(view);

            tvDateShAlEvReAd = view.findViewById(R.id.tvDateShAlEvReAd);
            tvTimeShAlEvReAd = view.findViewById(R.id.tvTimeShAlEvReAd);
            tvCityShAlEvReAd = view.findViewById(R.id.tvCityShAlEvReAd);
            tvFieldShAlEvReAd = view.findViewById(R.id.tvFieldShAlEvReAd);
            cvMainShAlEvAc = view.findViewById(R.id.cvMainShAlEvAc);
            ivArrowShAlEvReAd = view.findViewById(R.id.ivArrowShAlEvReAd);

            cvMainShAlEvAc.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    //опредиляемся с цветом CardView взависимости от роли пользователя в собитии
                    int color;
                    if(eventsList.get(getAdapterPosition()).eventUserStatus.equals("0"))
                            color = context.getResources().getColor(R.color.colorMyColorBlue);
                        else if(eventsList.get(getAdapterPosition()).eventUserStatus.equals("1"))
                            color = context.getResources().getColor(R.color.colorMyColorGold);
                        else color = context.getResources().getColor(R.color.colorMyColorGrey);

                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            cvMainShAlEvAc.setCardBackgroundColor(
                                    context.getResources().getColor(R.color.colorMyColorWhite)
                            );
                            break;
                        case MotionEvent.ACTION_UP:
                            cvMainShAlEvAc.setCardBackgroundColor(color);
                            break;
                        default:
                            cvMainShAlEvAc.setCardBackgroundColor(color);
                            break;
                    }//switch
                    return false;
                }//onTouch
            });//setOnTouchListener

            cvMainShAlEvAc.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //получаем данные о нажатом событии
                    event = eventsList.get(getAdapterPosition());
                    eventId = event.eventId;
                    eventCityName = event.cityName;
                    eventFieldName = event.fieldName;
                    eventDate = event.eventData;
                    showEventDate = dateShowFormat(eventDate);
                    eventTime = event.eventTime;
                    eventDuration = event.eventDuration;
                    eventPrice = event.eventPrice;
                    eventPhone = event.eventPhone;
                    eventUserId = event.eventUserId;

                    //выбран пункт принять участие
                    takePart();
                    return true;
                }//onLongClick
            });//setOnLongClickListener

            //слушатель события нажатия стрелки
            ivArrowShAlEvReAd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //получаем данные о нажатом событии
                    event = eventsList.get(getAdapterPosition());
                    eventId = event.eventId;
                    eventCityName = event.cityName;
                    eventFieldName = event.fieldName;
                    eventDate = event.eventData;
                    showEventDate = dateShowFormat(eventDate);
                    eventTime = event.eventTime;
                    eventDuration = event.eventDuration;
                    eventPrice = event.eventPrice;
                    eventPhone = event.eventPhone;
                    eventUserId = event.eventUserId;

                    //выбран пункт подробная информация
                    aboutEvent();
                }//onClick
            });
        } // ViewHolder

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

            //через интерфейс callDialogsAllEvents
            callDialogsAllEvents.takePart(context, eventId, userTakeInPart, message);
        }//takePart

        //Вызов диалога для подробной информации о событии
        private void aboutEvent() {
            String message = fullInfoAboutEvent();

            //через интерфейс callDialogsAllEvents
            callDialogsAllEvents.aboutDialog(context, message);
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
                    eventDate,
                    eventTime,
                    eventDuration,
                    eventPrice,
                    eventPhone);
        }//fullInfoAboutEvent
    } // class ViewHolder

    //приведение даты в формат вывода
    private String dateShowFormat(String eventDate) {

        //преобразуем StringToDate
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try { date = simpleDateFormat.parse(eventDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }//try-catch
        //преобразуем DateToString в читабельный формат
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", myDateFormatSymbols );
        return  dateFormat.format(date);
    } // dateShowFormat

    //вспомагательный объект для формирования даты
    private DateFormatSymbols myDateFormatSymbols = new DateFormatSymbols() {

        @Override
        public String[] getMonths() {
            return new String[]{"января", "февраля", "марта", "апреля", "мая", "июня",
                    "июля", "августа", "сентября", "октября", "ноября", "декабря"};
        }
    };//DateFormatSymbols

}//ShowAllEventsRecyclerAdapter
