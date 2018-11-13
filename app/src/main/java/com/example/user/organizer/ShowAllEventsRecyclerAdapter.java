package com.example.user.organizer;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.organizer.fragment.SelectDurationDialog;
import com.example.user.organizer.fragment.ShowListParticipantsDialog;
import com.example.user.organizer.inteface.AllEventsInterface;

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
    AllEventsInterface allEventsInterface;
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
    String cityId;
    final String[] value = new String[1];   //строка изъятая из alertDialog при проверке пароля

    //параметр для вызова диалога "showListParticipantsDialog"
    final String ID_SHOW_LIST_PARTICIPANTS_DIALOG = "showListParticipantsDialog";

    ShowListParticipantsDialog showListParticipantsDialog =
            new ShowListParticipantsDialog(); // диалог просмотра списка участиков

    //конструктор
    public ShowAllEventsRecyclerAdapter(AllEventsInterface allEventsInterface,
                                        Context context, String idAuthUser, String cityId) {
        this.inflater = LayoutInflater.from(context);
        //получение интерфеса из класса Фрагмента
        //для обработки нажатия элементов RecyclerAdapter
        this.allEventsInterface = allEventsInterface;
        this.cityId = cityId;
        this.context = context;
        this.idAuthUser = idAuthUser;
        dbUtilities = new DBUtilities(context);

        this.eventsList = dbUtilities.getListEvents("", cityId, idAuthUser);
    } // ShowAllEventsRecyclerAdapter

    // метод для обновления адаптера
    // после внесения изменения в базу данных
    public void updateEventList(){
        eventsList.clear();

        //получаем коллекцию событий
        eventsList = dbUtilities.getListEvents("", cityId, idAuthUser);
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
        holder.tvDateShAlEvReAd.setText(dbUtilities.dateShowFormat(eventShow.eventData)); // Дата
        holder.tvTimeShAlEvReAd.setText(eventShow.eventTime); //Время

        //опредиляемся с цветом CardView взависимости от роли пользователя в собитии
        int color;
        if(eventShow.eventUserStatus.equals("0"))
            color = context.getResources().getColor(R.color.colorMyColorGreen);
        else if(eventShow.eventUserStatus.equals("1"))
            color = context.getResources().getColor(R.color.colorMyColorGold);
        else color = context.getResources().getColor(R.color.colorMyColorGrey);

        //проверка завершилось ли событие
        boolean status = dbUtilities.getEventExecutionStatus(eventShow.eventData, eventShow.eventTime);
        if(status) holder.cvMainShAlEvAc.setCardBackgroundColor( color );
        else holder.cvMainShAlEvAc.setVisibility(View.GONE);

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
        ImageView ivArrowShAlEvReAd, ivParticipantsShAlEvReAd;
        LinearLayout llMainShAlEvReAd;

        ViewHolder(View view){
            super(view);

            llMainShAlEvReAd = view.findViewById(R.id.llMainShAlEvReAd);
            tvDateShAlEvReAd = view.findViewById(R.id.tvDateShAlEvReAd);
            tvTimeShAlEvReAd = view.findViewById(R.id.tvTimeShAlEvReAd);
            tvCityShAlEvReAd = view.findViewById(R.id.tvCityShAlEvReAd);
            tvFieldShAlEvReAd = view.findViewById(R.id.tvFieldShAlEvReAd);
            cvMainShAlEvAc = view.findViewById(R.id.cvMainShAlEvAc);
            ivArrowShAlEvReAd = view.findViewById(R.id.ivArrowShAlEvReAd);
            ivParticipantsShAlEvReAd = view.findViewById(R.id.ivParticipantsShAlEvReAd);

            cvMainShAlEvAc.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    //опредиляемся с цветом CardView взависимости от роли пользователя в собитии
                    int color;
                    if(eventsList.get(getAdapterPosition()).eventUserStatus.equals("0"))
                            color = context.getResources().getColor(R.color.colorMyColorGreen);
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
                    showEventDate = dbUtilities.dateShowFormat(eventDate);
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

            //слушатель события нажатия человечка
            ivParticipantsShAlEvReAd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<String> loginUserList = new ArrayList<>();
                    //получаем колекцию id
                    List<String> idUserList = dbUtilities.getListValuesByValueAndHisColumn(
                            "participants","event_id",
                            eventsList.get(getAdapterPosition()).eventId,"user_id");
                    //получаем колекцию логинов
                    for (String userId : idUserList) {
                        loginUserList.add(dbUtilities.searchValueInColumn(
                                "users","id","login",userId));
                    }//for

                    Bundle args = new Bundle();    // объект для передачи параметров в диалог
                    args.putStringArrayList("participantsLoginList", (ArrayList<String>) loginUserList);

                    showListParticipantsDialog.setArguments(args);
                    // Точка вызова отображение диалогового окна
                    showListParticipantsDialog.show( ((AppCompatActivity)context).
                            getSupportFragmentManager(), ID_SHOW_LIST_PARTICIPANTS_DIALOG);
                }
            });// ivParticipants.setOnClickListener

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
                    showEventDate = dbUtilities.dateShowFormat(eventDate);
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
            if(!userTakeInPart) {
                boolean requestIsReady = false;  //переменная есть ли такой запрос
                //проверка есть ли непрочитаный запрос чтобы не дублирывать его
                requestIsReady = checkIsRequestIsReady(eventId);
                if(requestIsReady) {
                    alertDialogOneButton("Вы уже подавали запрос!", "Организатор еще не получил ваш запрос.");
                }else {
                    //берем пароль из БД
                    String password = dbUtilities.searchValueInColumn(
                            "events", "id", "password", eventId);

                    //если пароль не пустой запрос пароля через alertDialog
                    if (!password.equals(""))
                        alertDialogEditText(context, password, userTakeInPart, eventId, message);
                    else {
                        //через интерфейс allEventsInterface
                        allEventsInterface.callDialogTakePart(context, eventId, userTakeInPart, message);
                    }
                }//if(requestIsReady) {
            }else {
                //через интерфейс allEventsInterface
                allEventsInterface.callDialogTakePart(context, eventId, userTakeInPart, message);
            }//if(!userTakeInPart) {
        }//callDialogTakePart

        //Вызов диалога для подробной информации о событии
        private void aboutEvent() {
            String message = fullInfoAboutEvent();

            //через интерфейс allEventsInterface
            allEventsInterface.callDialogAboutDialog(context, message);
        }//aboutEvent

        //строка с полной информацией о событии
        private String fullInfoAboutEvent() {
            return String.format("Событие в городе %s\n" +
                            "На поле %s \n" +
                            "Назначено на %s\n" +
                            "Состоится в %s\n" +
                            "Продолжительность %s мин\n" +
                            "Стоимость %s руб\n" +
                            "Телефон %s",
                    eventCityName,
                    eventFieldName,
                    dbUtilities.dateShowFormat(eventDate),
                    eventTime,
                    eventDuration,
                    eventPrice,
                    eventPhone);
        }//fullInfoAboutEvent
    } // class ViewHolder

    //AlertDialog Для потверждения пароля если событие запароленно
    private void alertDialogEditText(Context context, String password, boolean userTakeInPart, String eventId, String message) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("Событие запаролено!");
        alert.setMessage("Введите пожалуйста пароль!");
        alert.setIcon(R.drawable.icon_information);
        final EditText input = new EditText(context);
        alert.setView(input);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                value[0] = String.valueOf(input.getText());
                checkPassword(context, password, value[0], userTakeInPart, eventId, message);
                // Do something with value!
            }
        });
        alert.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });
        alert.show();
    }//alertDialogEditText

    //проверка пароля если событие запароленно
    private void checkPassword(Context context, String password, String value, boolean userTakeInPart, String eventId, String message) {
        //проверка совпадения пароля
        if (password.equals(value)) {
            //через интерфейс allEventsInterface
            allEventsInterface.callDialogTakePart(context, eventId, userTakeInPart, message);
        } else {
            alertDialogOneButton("Ошибка!", "Неверный пароль!");
        }//if(password.equals(value))
    }//checkPassword

    //проверка есть ли непрочитаный запрос чтобы не дублирывать его
    private boolean checkIsRequestIsReady(String eventId) {
        List<Notification> someNotification = dbUtilities.getSomeNotifications(eventUserId);
        for (Notification notification : someNotification) {
            //поиск совподающих сообщений
            if(notification.event_id.equals(eventId)
                    && notification.notice.equals(idAuthUser)
                    && notification.message.equals("подал запрос на участие в событии"))
                return true;
        }
        return false;
    }//checkIsRequestIsReady

    //если пароль неверный сообщение оповещающие об этом
    private void alertDialogOneButton(String title, String message) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(title)
                    .setMessage(message)
                    .setIcon(R.drawable.icon_information)
                    .setCancelable(false)
                    .setNegativeButton("ОК",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
    }//alertDialogOneButton
}//ShowAllEventsRecyclerAdapter
