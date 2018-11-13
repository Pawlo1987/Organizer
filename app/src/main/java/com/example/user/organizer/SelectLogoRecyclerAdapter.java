package com.example.user.organizer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

//-------Адаптера для вывода(просмотра) при выборе логотипа--------------
public class SelectLogoRecyclerAdapter extends
        RecyclerView.Adapter<SelectLogoRecyclerAdapter.ViewHolder>{

    //поля класса ShowAllEventsRecyclerAdapter
    private LayoutInflater inflater;
    DBUtilities dbUtilities;
    Context context;
    String idAuthUser;         //Авторизированный пользователь

    //конструктор
    public SelectLogoRecyclerAdapter( Context context, String idAuthUser) {
        this.inflater = LayoutInflater.from(context);
        //получение интерфеса из класса Фрагмента
        //для обработки нажатия элементов RecyclerAdapter
        this.context = context;
        this.idAuthUser = idAuthUser;
        dbUtilities = new DBUtilities(context);
    } // SelectLogoRecyclerAdapter

    //создаем новую разметку(View) путем указания разметки
    @Override
    public SelectLogoRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.recycler_adapter_select_logo, parent, false);
        return new SelectLogoRecyclerAdapter.ViewHolder(view);
    }//onCreateViewHolder

    //привязываем элементы разметки к переменным объекта(в данном случае к курсору)
    @Override
    public void onBindViewHolder(SelectLogoRecyclerAdapter.ViewHolder holder, int position) {

    } // onBindViewHolder

    //получаем количество элементов объекта(курсора)
    @Override
    public int getItemCount() { return 0; }

    //Создаем класс ViewHolder с помощью которого мы получаем ссылку на каждый элемент
    //отдельного пункта списка и подключаем слушателя события нажатия меню
    public class ViewHolder extends RecyclerView.ViewHolder{

        ViewHolder(View view) {
            super(view);

        }
    } // class ViewHolder



}//SelectLogoRecyclerAdapter
