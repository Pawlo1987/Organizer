package com.example.user.organizer;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import static android.view.View.GONE;

//--------Адаптер для вывода и выбора учасников события
public class SelectParticipantsRecyclerAdapter extends RecyclerView.Adapter<SelectParticipantsRecyclerAdapter.ViewHolder> {

    //поля класса SelectParticipantsRecyclerAdapter
    private LayoutInflater inflater;
    private List<Participant> ShowUserList;     //коллекция учасников выбранными игроками
    private String filter;
    DBUtilities dbUtilities;
    private Participant participant = new Participant();
    List<String> loginUserList;     //коллекция логинов участников для добавления в событие

    //конструктор
    SelectParticipantsRecyclerAdapter(Context context, String filter, List<Participant> ShowUserList, List<String> loginUserList) {
        this.inflater = LayoutInflater.from(context);
        this.filter = filter;
        this.ShowUserList = ShowUserList;
        dbUtilities = new DBUtilities(context);
        this.loginUserList = loginUserList;
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
        participant = ShowUserList.get(position);
        String name = participant.getName();
        String login = participant.getLogin();
        String city_id = participant.getCity_id();

        // получение данных
        //фильтрация элементов для бинарного поиска
        if((filter == "")||(name.contains(filter)||login.contains(filter))){
            holder.cbSePaReAd.setChecked(false);        // CheckBox выбор
            holder.tvNameSePaReAd.setText(name);        // имя user
            holder.tvLoginSePaReAd.setText(login);      // логин user
            holder.tvCitySePaReAd.setText(city_id);  // город user
        }else {
            //setVisibility(GONE) отключаем ненужные элементы для просмотра
            holder.cbSePaReAd.setVisibility(GONE);
            holder.tvNameSePaReAd.setVisibility(GONE);
            holder.tvLoginSePaReAd.setVisibility(GONE);
            holder.tvCitySePaReAd.setVisibility(GONE);
        }//if-else

        //слушатель событий при нажатии на CheckBox
        holder.cbSePaReAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.cbSePaReAd.isChecked()){
                    loginUserList.add(login);
                }else{
                    loginUserList.remove(login);
                }//if-else

            }//onClick
        });//setOnClickListener

    } // onBindViewHolder

    //получаем количество элементов объекта(курсора)
    @Override
    public int getItemCount() { return ShowUserList.size(); }

    //Создаем класс ViewHolder с помощью которого мы получаем ссылку на каждый элемент
    //отдельного пункта списка
    public class ViewHolder extends RecyclerView.ViewHolder {
        final CheckBox cbSePaReAd;
        final TextView tvNameSePaReAd, tvLoginSePaReAd, tvCitySePaReAd;

        ViewHolder(View view){
            super(view);

            cbSePaReAd = (CheckBox) view.findViewById(R.id.cbSePaReAd);
            tvNameSePaReAd = (TextView) view.findViewById(R.id.tvNameSePaReAd);
            tvLoginSePaReAd = (TextView) view.findViewById(R.id.tvLoginSePaReAd);
            tvCitySePaReAd = (TextView) view.findViewById(R.id.tvCitySePaReAd);
        } // ViewHolder
    } // class ViewHolder
}//SelectParticipantsRecyclerAdapter
