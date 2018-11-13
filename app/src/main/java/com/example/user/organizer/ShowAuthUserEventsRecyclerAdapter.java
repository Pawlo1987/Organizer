package com.example.user.organizer;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import com.example.user.organizer.fragment.ShowAuthUserEventsFragment;

//-------Активность адаптера для вывода(просмотра) событий авторизированого пользователя--------------
public class ShowAuthUserEventsRecyclerAdapter extends RecyclerView.Adapter<ShowAuthUserEventsRecyclerAdapter.ViewHolder> {

    //поля класса advertisingAndInformationRecyclerAdapter
    private LayoutInflater inflater;
    private Cursor eventsCursor;
    Context context;
    String queryAdapt;
    DBUtilities dbUtilities;
    int idAuthUser;         //Авторизированный пользователь

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
    public ShowAuthUserEventsRecyclerAdapter(Context context, String queryAdapt, int idAuthUser) {
        this.inflater = LayoutInflater.from(context);
        this.queryAdapt = queryAdapt;
        this.context = context;
        this.idAuthUser = idAuthUser;
        dbUtilities = new DBUtilities(context);
//        dbUtilities.open();
        eventsCursor =  null;//dbUtilities.getDb().rawQuery(queryAdapt, null);
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
        eventsCursor.moveToPosition(position); // переходим в курсоре на текущую позицию

        // получение данных
        holder.tvCityShAuUsEvReAd.setText(eventsCursor.getString(0)); //город
        holder.tvFieldShAuUsEvReAd.setText(eventsCursor.getString(1));//поле
        holder.tvDateShAuUsEvReAd.setText(eventsCursor.getString(2)); // Дата
        holder.tvTimeShAuUsEvReAd.setText(eventsCursor.getString(3)); //Время
        holder.idEvent = eventsCursor.getInt(4);                    //idСобытия
        holder.idOrganizer = eventsCursor.getInt(5);                //idОрганизатора
        holder.tvStatusShAuUsEvReAd.setText(
                (eventsCursor.getInt(6) == 0)?"Участник":"Организатор"
        ); // Статус игрока

    } // onBindViewHolder

    //получаем количество элементов объекта(курсора)
    @Override
    public int getItemCount() { return eventsCursor.getCount(); }

    //Создаем класс ViewHolder с помощью которого мы получаем ссылку на каждый элемент
    //отдельного пункта списка
    public class ViewHolder extends RecyclerView.ViewHolder
            implements PopupMenu.OnMenuItemClickListener{
        final TextView tvDateShAuUsEvReAd, tvTimeShAuUsEvReAd, tvCityShAuUsEvReAd,
                       tvFieldShAuUsEvReAd, tvStatusShAuUsEvReAd;
        public int idEvent;
        public int idOrganizer;

        ViewHolder(View view){
            super(view);

            tvDateShAuUsEvReAd = (TextView) view.findViewById(R.id.tvDateShAuUsEvReAd);
            tvTimeShAuUsEvReAd = (TextView) view.findViewById(R.id.tvTimeShAuUsEvReAd);
            tvCityShAuUsEvReAd = (TextView) view.findViewById(R.id.tvCityShAuUsEvReAd);
            tvFieldShAuUsEvReAd = (TextView) view.findViewById(R.id.tvFieldShAuUsEvReAd);
            tvStatusShAuUsEvReAd = (TextView) view.findViewById(R.id.tvStatusShAuUsEvReAd);

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

                    //TODO -- надо доделать обновление адаптера
                    eventsCursor =  null;//dbUtilities.getDb().rawQuery(queryAdapt, null);
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
            Cursor cursor;
            // Запрос на полную информацию о собитии
            String query = "SELECT fields.name, cities.name, date, time, duration, price, password, events.phone " +
                    "FROM events " +
                    "INNER JOIN fields ON fields._id = events.field_id " +
                    "INNER JOIN cities ON cities._id = events.city_id " +
                    "WHERE events._id = " + idEvent + ";";
            cursor = null;//dbUtilities.getDb().rawQuery(query, null);
            cursor.moveToPosition(0); // переходим в курсоре в нулевую позицию
            Intent intent = new Intent(context, EditEventActivity.class);
            intent.putExtra("_id", idEvent);
            intent.putExtra("field_name", cursor.getString(0));
            intent.putExtra("city_name", cursor.getString(1));
            intent.putExtra("date", cursor.getString(2));
            intent.putExtra("time", cursor.getString(3));
            intent.putExtra("duration", cursor.getInt(4));
            intent.putExtra("price", cursor.getInt(5));
            intent.putExtra("password", cursor.getString(6));
            intent.putExtra("phone", cursor.getString(7));
            intent.putExtra("user_id", idAuthUser);
            context.startActivity(intent);
        }//editEvent

        // Удалить событие
        private void deleteEvent() {
            String message = fullInfoAboutEvent();
            Log.d("MyLog", message);
            Bundle args = new Bundle();    // объект для передачи параметров в диалог
            args.putString("message", message);
            args.putInt("event_id", idEvent);
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
            args.putInt("event_id", idEvent);
            args.putInt("user_id", idAuthUser);
            leaveEventDialog.setArguments(args);

            // отображение диалогового окна
            leaveEventDialog.show(((AppCompatActivity)context).
                    getSupportFragmentManager(), ID_LEAVE_DIALOG);
        }//leaveEvent

        //строка с полной информацией о событии
        private String fullInfoAboutEvent() {
            Cursor cursor;
            // Запрос на полную информацию о собитии
            String query = "SELECT cities.name, fields.name, date, time, duration, price, events.phone FROM events " +
                    "INNER JOIN cities ON cities._id = events.city_id \n" +
                    "INNER JOIN fields ON fields._id = events.field_id \n" +
                    "WHERE events._id = " + idEvent + ";";
            cursor = null;//dbUtilities.getDb().rawQuery(query, null);
            cursor.moveToPosition(0); // переходим в курсоре в нулевую позицию
            return String.format("Собитие в городе %s\n" +
                            "На поле %s \n" +
                            "Назначено на %s\n" +
                            "Cостоится в %s\n" +
                            "Продолжитльность %d мин\n" +
                            "Стоимость %d руб\n" +
                            "Телефон %s",
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getInt(4),
                    cursor.getInt(5),
                    cursor.getString(6));
        }//fullInfoAboutEvent

    } // class ViewHolder

}//ShowAuthUserEventsRecyclerAdapter
