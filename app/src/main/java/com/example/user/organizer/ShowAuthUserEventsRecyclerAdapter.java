package com.example.user.organizer;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.organizer.inteface.CallDialogsAuthUserEvents;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//-------Адаптера для вывода(просмотра) событий авторизированого пользователя--------------
public class ShowAuthUserEventsRecyclerAdapter extends RecyclerView.Adapter<ShowAuthUserEventsRecyclerAdapter.ViewHolder> {

    //поля класса advertisingAndInformationRecyclerAdapter
    private LayoutInflater inflater;
    Event event = new Event();
    List<Event> eventsList = new ArrayList<>(); //коллекция событий
    Context context;
    DBUtilities dbUtilities;
    CallDialogsAuthUserEvents callDialogsAuthUserEvents;
    String idAuthUser;         //Авторизированный пользователь

    String eventId;
    String eventCityName;
    String eventFieldName;
    String showEventDate;
    String eventDate;
    String eventTime;
    String eventDuration;
    String eventPrice;
    String eventPassword;
    String eventPhone;
    String eventUserId;

    //конструктор
    public ShowAuthUserEventsRecyclerAdapter(CallDialogsAuthUserEvents callDialogsAuthUserEvents,
                                             Context context, String idAuthUser) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.idAuthUser = idAuthUser;
        dbUtilities = new DBUtilities(context);
        //получение интерфеса из класса Фрагмента
        //для обработки нажатия элементов RecyclerAdapter
        this.callDialogsAuthUserEvents = callDialogsAuthUserEvents;

        //получаем коллекцию событий
        eventsList = dbUtilities.getListEvents("", idAuthUser);
    } // advertisingAndInformationRecyclerAdapter

    public ShowAuthUserEventsRecyclerAdapter(Context context) {
        this.context = context;
    }//ShowAuthUserEventsRecyclerAdapter

    // метод для обновления адаптера
    // после внесения изменения в базу данных
    public void updateEventList(){
        eventsList.clear();

        //получаем коллекцию событий
        eventsList = dbUtilities.getListEvents("", idAuthUser);
        notifyDataSetChanged();
    }

    //создаем новую разметку(View) путем указания разметки
    @Override
    public ShowAuthUserEventsRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.recycler_adapter_show_auth_user_events, parent, false);
        return new ShowAuthUserEventsRecyclerAdapter.ViewHolder(view);
    }

    //привязываем элементы разметки к переменным объекта(в данном случае к курсору)
    @Override
    public void onBindViewHolder(ShowAuthUserEventsRecyclerAdapter.ViewHolder holder, int position) {
        Event eventShow = eventsList.get(position);

        // получение данных
        holder.tvCityShAuUsEvReAd.setText(eventShow.cityName); //город
        holder.tvFieldShAuUsEvReAd.setText(eventShow.fieldName);//поле
        holder.tvDateShAuUsEvReAd.setText(dateShowFormat(eventShow.eventData)); // Дата
        holder.tvTimeShAuUsEvReAd.setText(eventShow.eventTime); //Время
        holder.tvStatusShAuUsEvReAd.setText(
                (eventShow.eventUserStatus.equals("0"))?"Участник":"Организатор"
        ); // Статус игрока
        //опредиляемся с цветом CardView взависимости от роли пользователя в собитии
        holder.cvMainShAuUsEvReAd.setCardBackgroundColor(
                (eventShow.eventUserStatus.equals("0"))
                    ?context.getResources().getColor(R.color.colorMyColorGreen)
                    :context.getResources().getColor(R.color.colorMyColorGold)
        );
        if(eventShow.eventUserStatus.equals("2"))
            holder.cvMainShAuUsEvReAd.setVisibility(View.GONE);
    } // onBindViewHolder

    //получаем количество элементов объекта(курсора)
    @Override
    public int getItemCount() { return eventsList.size(); }

    //Создаем класс ViewHolder с помощью которого мы получаем ссылку на каждый элемент
    //отдельного пункта списка
    public class ViewHolder extends RecyclerView.ViewHolder{
        final TextView tvDateShAuUsEvReAd, tvTimeShAuUsEvReAd, tvCityShAuUsEvReAd,
                       tvFieldShAuUsEvReAd, tvStatusShAuUsEvReAd;
        ImageView ivArrowShAuUsEvReAd, ivDeleteEventShAuUsEvReAd;
        CardView cvMainShAuUsEvReAd;

        ViewHolder(View view){
            super(view);
            tvDateShAuUsEvReAd = view.findViewById(R.id.tvDateShAuUsEvReAd);
            tvTimeShAuUsEvReAd = view.findViewById(R.id.tvTimeShAuUsEvReAd);
            tvCityShAuUsEvReAd = view.findViewById(R.id.tvCityShAuUsEvReAd);
            tvFieldShAuUsEvReAd = view.findViewById(R.id.tvFieldShAuUsEvReAd);
            tvStatusShAuUsEvReAd = view.findViewById(R.id.tvStatusShAuUsEvReAd);
            ivArrowShAuUsEvReAd = view.findViewById(R.id.ivArrowShAuUsEvReAd);
            ivDeleteEventShAuUsEvReAd = view.findViewById(R.id.ivDeleteEventShAuUsEvReAd);
            cvMainShAuUsEvReAd = view.findViewById(R.id.cvMainShAuUsEvReAd);

            //onTouch
            cvMainShAuUsEvReAd.setOnTouchListener((v, event) -> {
                //опредиляемся с цветом CardView взависимости от роли пользователя в собитии
                int color = eventsList.get(getAdapterPosition()).eventUserStatus.equals("0")
                        ?context.getResources().getColor(R.color.colorMyColorGreen)
                        :context.getResources().getColor(R.color.colorMyColorGold);

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        cvMainShAuUsEvReAd.setCardBackgroundColor(
                                context.getResources().getColor(R.color.colorMyColorWhite)
                        );
                        break;
                    case MotionEvent.ACTION_UP:
                        cvMainShAuUsEvReAd.setCardBackgroundColor(color);
                        break;
                    default:
                        cvMainShAuUsEvReAd.setCardBackgroundColor(color);
                        break;
                }//switch
                return false;
            });//setOnTouchListener

            //onLongClick
            cvMainShAuUsEvReAd.setOnLongClickListener(v -> {
                //получаем данные о нажатом событии
                event = eventsList.get(getAdapterPosition());
                //если в данном событии авторизированный пользователь организатор
                if(event.eventUserStatus.equals("1")) {
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

                    //выбран пункт редактировать события
                    editEvent();
                }else {
                    Toast.makeText(context, "Нет прав для редактирования!", Toast.LENGTH_SHORT).show();
                }//if-else
                return true;
            });//setOnLongClickListener

            //слушатель события нажатия стрелки
            ivArrowShAuUsEvReAd.setOnClickListener(new View.OnClickListener() {
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
            });//setOnClickListener

            //слушатель события нажатия стрелки
            ivDeleteEventShAuUsEvReAd.setOnClickListener(new View.OnClickListener() {
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

                    view.setFocusable(true);
                    if(tvStatusShAuUsEvReAd.getText().toString().equals("Организатор"))
                         deleteEvent();//выбран пункт удалить событие
                    else leaveEvent();
                }//onClick
            });//setOnClickListener
        } // ViewHolder

        //подробная информация
        private void aboutEvent() {
            String message = fullInfoAboutEvent();

            //через интерфейс СallDialogsAuthUserEvents
            callDialogsAuthUserEvents.aboutDialog(context, message);
        }//aboutEvent

        //редактировать событие
        private void editEvent() {
            Intent intent = new Intent(context, EditEventActivity.class);
            intent.putExtra("id", eventId);
            intent.putExtra("field_name", eventFieldName);
            intent.putExtra("city_name", eventCityName);
            intent.putExtra("date", eventDate);
            intent.putExtra("time", eventTime);
            intent.putExtra("duration", eventDuration);
            intent.putExtra("price", eventPrice);
            intent.putExtra("password", eventPassword);
            intent.putExtra("phone", eventPhone);
            intent.putExtra("user_id", eventUserId);
            context.startActivity(intent);
        }//editEvent

        // Удалить событие
        private void deleteEvent() {
            String message = fullInfoAboutEvent();

            //через интерфейс СallDialogsAuthUserEvents
            callDialogsAuthUserEvents.deleteDialog(context, message, eventId);
        }//deleteEvent

        //покинуть событие
        private void leaveEvent() {
            String message = fullInfoAboutEvent();

            //через интерфейс СallDialogsAuthUserEvents
            callDialogsAuthUserEvents.leaveDialog(context, message, eventId);
        }//leaveEvent

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
                    showEventDate,
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

}//ShowAuthUserEventsRecyclerAdapter
