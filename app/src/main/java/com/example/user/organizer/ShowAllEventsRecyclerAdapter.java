package com.example.user.organizer;

import android.content.Context;
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
import com.example.user.organizer.fragment.TakePartShowAllEventDialog;


//-------Активность адаптера для вывода(просмотра) всех событий--------------
public class ShowAllEventsRecyclerAdapter extends
        RecyclerView.Adapter<ShowAllEventsRecyclerAdapter.ViewHolder>{

    //поля класса ShowAllEventsRecyclerAdapter
    private LayoutInflater inflater;
    private Cursor eventsCursor;
    DBUtilities dbUtilities;
    Context context;
    int idAuthUser;         //Авторизированный пользователь

    AboutEventShowAllEventDialog aboutEventShowAllEventDialog =
            new AboutEventShowAllEventDialog(); // диалог подтверждения выхода из приложения

    TakePartShowAllEventDialog takePartShowAllEventDialog =
            new TakePartShowAllEventDialog(); // диалог подтверждения выхода из приложения

    final String ID_ABOUT_DIALOG = "aboutEventShowAllEventDialog";  //параметр для вызова диалога "about"
    final String ID_TAKEPART_DIALOG = "takePartShowAllEventDialog";  //параметр для вызова диалога "takePart"

    //конструктор
    public ShowAllEventsRecyclerAdapter(Context context, Cursor eventsCursor, int idAuthUser) {
        this.inflater = LayoutInflater.from(context);
        this.eventsCursor = eventsCursor;
        this.context = context;
        this.idAuthUser = idAuthUser;
        dbUtilities = new DBUtilities(context);
        dbUtilities.open();
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
        holder.idEvent = eventsCursor.getInt(4);                    //idСобытия
        holder.idOrganizer = eventsCursor.getInt(5);                //idОрганизатора
    } // onBindViewHolder

    //получаем количество элементов объекта(курсора)
    @Override
    public int getItemCount() { return eventsCursor.getCount(); }

    //Создаем класс ViewHolder с помощью которого мы получаем ссылку на каждый элемент
    //отдельного пункта списка и подключаем слушателя события нажатия меню
    public class ViewHolder extends RecyclerView.ViewHolder
            implements PopupMenu.OnMenuItemClickListener{
        final TextView tvDateShAlEvReAd, tvTimeShAlEvReAd, tvCityShAlEvReAd, tvFieldShAlEvReAd;
        public int idEvent;
        public int idOrganizer;

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
            Cursor cursor;
            String message;     //строка которую передаем в диалог в поле сообщения
            // Запрос на поиск данного пользователя в данном собитии
            String query = "SELECT events._id FROM events WHERE events._id = "
                            + idEvent +
                            " AND events.user_id = "
                            + idAuthUser +
                            " UNION SELECT participants._id FROM participants " +
                            "WHERE participants.event_id = "
                            + idEvent +
                            " AND participants.user_id = "
                            + idAuthUser +
                            ";";
            cursor = dbUtilities.getDb().rawQuery(query, null);
            cursor.moveToPosition(0); // переходим в курсоре в нулевую позицию
            // если курсор пустой значит по запросу не найдено участие авторизированного
            // пользователя в данном событии (userTakeInPart = false),
            // иначе принимает участие (userTakeInPart = true)
            boolean userTakeInPart = (cursor.getCount() == 0 ? false : true);
            if(userTakeInPart){
                //если данный пользователь уже является участником события
                message = "Вы уже участвуете в данном событии";
            }else{
                message = fullInfoAboutEvent();
            }//if-else

            Bundle args = new Bundle();    // объект для передачи параметров в диалог
            args.putString("message", message);
            args.putBoolean("userTakeInPart", userTakeInPart);
            args.putInt("event_id", idEvent);
            args.putInt("user_id", idAuthUser);
            takePartShowAllEventDialog.setArguments(args);

            // отображение диалогового окна
            takePartShowAllEventDialog.show(((AppCompatActivity)context).
                    getSupportFragmentManager(), ID_TAKEPART_DIALOG);
        }//takePart

        //Вызов диалога для подробной информации о событии
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

        //строка с полной информацией о событии
        private String fullInfoAboutEvent() {
            Cursor cursor;
            // Запрос на полную информацию о собитии
            String query = "SELECT cities.name, fields.name, date, time, duration, price, events.phone FROM events " +
                    "INNER JOIN cities ON cities._id = events.city_id \n" +
                    "INNER JOIN fields ON fields._id = events.field_id \n" +
                    "WHERE events._id = " + idEvent + ";";
            cursor = dbUtilities.getDb().rawQuery(query, null);
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

}//ShowAllEventsRecyclerAdapter
