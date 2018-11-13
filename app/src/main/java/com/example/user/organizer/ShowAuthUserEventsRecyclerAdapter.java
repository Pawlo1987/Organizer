package com.example.user.organizer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.user.organizer.fragment.AboutEventShowAllEventDialog;
import com.example.user.organizer.fragment.DeleteEventDialog;
import com.example.user.organizer.fragment.LeaveEventDialog;

import java.util.ArrayList;
import java.util.List;

//-------Активность адаптера для вывода(просмотра) событий авторизированого пользователя--------------
public class ShowAuthUserEventsRecyclerAdapter extends RecyclerView.Adapter<ShowAuthUserEventsRecyclerAdapter.ViewHolder> {

    //поля класса advertisingAndInformationRecyclerAdapter
    private LayoutInflater inflater;
    Event event = new Event();
    List<Event> eventsList = new ArrayList<>(); //коллекция событий
    Context context;
    String queryAdapt;
    DBUtilities dbUtilities;
    String idAuthUser;         //Авторизированный пользователь

    String eventId;
    String eventCityName;
    String eventFieldName;
    String eventDate;
    String eventTime;
    String eventDuration;
    String eventPrice;
    String eventPassword;
    String eventPhone;
    String eventUserId;

    //параметр для вызова диалога "about"
    final String ID_ABOUT_DIALOG = "aboutEventShowAllEventDialog";
    //параметр для вызова диалога "leave"
    final String ID_LEAVE_DIALOG = "leaveEventShowAuthUserEventDialog";
    //параметр для вызова диалога "delete"
    final String ID_DELETE_DIALOG = "deleteEventShowAuthUserEventDialog";

    AboutEventShowAllEventDialog aboutEventShowAllEventDialog =
            new AboutEventShowAllEventDialog(); // диалог подтверждения выхода из приложения

    LeaveEventDialog leaveEventDialog =
            new LeaveEventDialog(); // диалог подтверждения покинуть событие

    DeleteEventDialog deleteEventDialog =
            new DeleteEventDialog(); // диалог подтверждения удалить событие

    //конструктор
    public ShowAuthUserEventsRecyclerAdapter(Context context, String idAuthUser) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.idAuthUser = idAuthUser;
        dbUtilities = new DBUtilities(context);

        //получаем коллекцию событий
        eventsList = dbUtilities.getListEventsForAuthUser("", idAuthUser);
    } // advertisingAndInformationRecyclerAdapter

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
        holder.tvDateShAuUsEvReAd.setText(eventShow.eventData); // Дата
        holder.tvTimeShAuUsEvReAd.setText(eventShow.eventTime); //Время
        holder.tvStatusShAuUsEvReAd.setText(
                (eventShow.eventUserStatus.equals("0"))?"Участник":"Организатор"
        ); // Статус игрока

    } // onBindViewHolder

    //получаем количество элементов объекта(курсора)
    @Override
    public int getItemCount() { return eventsList.size(); }

    //Создаем класс ViewHolder с помощью которого мы получаем ссылку на каждый элемент
    //отдельного пункта списка
    public class ViewHolder extends RecyclerView.ViewHolder
            implements PopupMenu.OnMenuItemClickListener{
        final TextView tvDateShAuUsEvReAd, tvTimeShAuUsEvReAd, tvCityShAuUsEvReAd,
                       tvFieldShAuUsEvReAd, tvStatusShAuUsEvReAd;

        ViewHolder(View view){
            super(view);

            tvDateShAuUsEvReAd = view.findViewById(R.id.tvDateShAuUsEvReAd);
            tvTimeShAuUsEvReAd = view.findViewById(R.id.tvTimeShAuUsEvReAd);
            tvCityShAuUsEvReAd = view.findViewById(R.id.tvCityShAuUsEvReAd);
            tvFieldShAuUsEvReAd = view.findViewById(R.id.tvFieldShAuUsEvReAd);
            tvStatusShAuUsEvReAd = view.findViewById(R.id.tvStatusShAuUsEvReAd);

            //слушатель события нажатого меню
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popup = new PopupMenu(view.getContext(), view);
                    popup.inflate(
                            (tvStatusShAuUsEvReAd.getText().toString().equals("Организатор"))
                                    ?(R.menu.rva_show_auth_user_event_organize_popup_menu)
                                    :(R.menu.rva_show_auth_user_event_participant_popup_menu)
                    );
                    popup.setOnMenuItemClickListener(ShowAuthUserEventsRecyclerAdapter.ViewHolder.this);
                    popup.show();

                    //получаем данные о нажатом событии
                    event = eventsList.get(getAdapterPosition());
                    eventId = event.eventId;
                    eventCityName = event.cityName;
                    eventFieldName = event.fieldName;
                    eventDate = event.eventData;
                    eventTime = event.eventTime;
                    eventDuration = event.eventDuration;
                    eventPrice = event.eventPrice;
                    eventPassword = event.eventPassword;
                    eventPhone = event.eventPhone;
                    eventUserId = event.eventUserId;
                    //TODO -- надо доделать обновление адаптера
                    notifyDataSetChanged();
                }
            });
        } // ViewHolder

        //обработчик выбраного пункта меню
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.item_leave_event_show_auth_user_rva:      //выбран пункт покинуть событие
                    leaveEvent();
                    break;
                case R.id.item_delete_event_show_auth_user_rva:    //выбран пункт удалить событие
                    deleteEvent();
                    break;
                case R.id.item_edit_event_show_auth_user_rva:    //выбран пункт редактировать событие
                    editEvent();
                    break;
                case R.id.item_about_event_show_auth_user_rva:    //выбран пункт подробная информация
                    aboutEvent();
                    break;
            }//switch

            return false;
        }//onMenuItemClick

        //подробная информация
        private void aboutEvent() {
            String message = fullInfoAboutEvent();
            Log.d("MyLog", message);
            Bundle args = new Bundle();    // объект для передачи параметров в диалог
            args.putString("message", message);
            aboutEventShowAllEventDialog.setArguments(args);

            // отображение диалогового окна
            aboutEventShowAllEventDialog.show(((AppCompatActivity)context).
                    getSupportFragmentManager(), ID_ABOUT_DIALOG);
        }//aboutEvent

        //редактировать событие
        private void editEvent() {
//            // Запрос на полную информацию о собитии
//            String query = "SELECT fields.name, cities.name, date, time, duration, price, password, events.phone " +
//                    "FROM events " +
//                    "INNER JOIN fields ON fields._id = events.field_id " +
//                    "INNER JOIN cities ON cities._id = events.city_id " +
//                    "WHERE events._id = " + idEvent + ";";
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
            Log.d("MyLog", message);
            Bundle args = new Bundle();    // объект для передачи параметров в диалог
            args.putString("message", message);
            args.putString("event_id", eventId);
            deleteEventDialog.setArguments(args);

            // отображение диалогового окна
            deleteEventDialog.show(((AppCompatActivity)context).
                    getSupportFragmentManager(), ID_DELETE_DIALOG);
        }//deleteEvent

        //покинуть событие
        private void leaveEvent() {
            String message = fullInfoAboutEvent();
            Log.d("MyLog", message);
            Bundle args = new Bundle();    // объект для передачи параметров в диалог
            args.putString("message", message);
            args.putString("event_id", eventId);
            args.putString("user_id", idAuthUser);
            leaveEventDialog.setArguments(args);

            // отображение диалогового окна
            leaveEventDialog.show(((AppCompatActivity)context).
                    getSupportFragmentManager(), ID_LEAVE_DIALOG);

            //получаем коллекцию событий
            eventsList = dbUtilities.getListEventsForAuthUser("", idAuthUser);
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
                    eventDate,
                    eventTime,
                    eventDuration,
                    eventPrice,
                    eventPhone);
        }//fullInfoAboutEvent

    } // class ViewHolder

}//ShowAuthUserEventsRecyclerAdapter
