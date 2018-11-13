package com.example.user.organizer;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

//Адаптер GridView для показа всех событий
public class ShowAllEventGridAdapter extends BaseAdapter {

    Event event = new Event();
    List<Event> eventsList = new ArrayList<>(); //коллекция событий
    DBUtilities dbUtilities;
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

    public ShowAllEventGridAdapter(Context context, List<Event> eventsList, String idAuthUser) {
        this.eventsList = eventsList;
        this.context = context;
        this.idAuthUser = idAuthUser;
        dbUtilities = new DBUtilities(context);
    }//ShowAllEventGridAdapter


    //размер коллекции
    @Override
    public int getCount() {
        return eventsList.size();
    }//getCount

    @Override
    public Object getItem(int position) {
        return event = eventsList.get(position);
    }//getItem

    @Override
    public long getItemId(int position) {
        return 0;
    }//getItemId

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return null;
    }//getView
}//ShowAllEventGridAdapter
