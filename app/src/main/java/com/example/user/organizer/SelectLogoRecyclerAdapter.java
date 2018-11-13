package com.example.user.organizer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.user.organizer.inteface.SelectLogoInterface;

import java.util.ArrayList;
import java.util.List;

//-------Адаптера для вывода(просмотра) при выборе логотипа--------------
public class SelectLogoRecyclerAdapter extends
        RecyclerView.Adapter<SelectLogoRecyclerAdapter.ViewHolder>{

    //поля класса ShowAllEventsRecyclerAdapter
    private LayoutInflater inflater;
    DBUtilities dbUtilities;
    Context context;
    String table;           //параметр указывающий в какой таблице менять логотип
    String idAuthUser;         //Авторизированный пользователь
    List<Integer> listLogo = new ArrayList<>();
    SelectLogoInterface selectLogoInterface;
    int nextLogo;       //позиция выбранная в адаптере

    //конструктор
    public SelectLogoRecyclerAdapter(SelectLogoInterface selectLogoInterface,
                                      Context context, String idAuthUser, String table) {
        this.inflater = LayoutInflater.from(context);
        //получение интерфеса из класса Фрагмента
        //для обработки нажатия элементов RecyclerAdapter
        this.context = context;
        this.idAuthUser = idAuthUser;
        this.table = table;

        dbUtilities = new DBUtilities(context);
        this.selectLogoInterface = selectLogoInterface;

        //колличество логотипов в базе
        int n = 0;

        if(table.equals("users")) {
            n = Integer.parseInt(dbUtilities.getLogoCount());
        }else{
            n = Integer.parseInt(dbUtilities.getLogoCount());
        }
        int i = 1;

        //заполняем коллекцию логотипов для адаптера
        while(i<=n){
            listLogo.add(i);
            i=i+2;
        }//while

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
        nextLogo = listLogo.get(position);

        if(table.equals("users")) {
            // Показать картинку
            new DownloadImageTask(holder.ivLeftSeLoReAd)
                    .execute("http://strahovanie.dn.ua/football_db/logo/logo_" + String.valueOf(nextLogo) + ".png");
            new DownloadImageTask(holder.ivRightSeLoReAd)
                    .execute("http://strahovanie.dn.ua/football_db/logo/logo_" + String.valueOf(nextLogo + 1) + ".png");
        }else {
            // Показать картинку
            new DownloadImageTask(holder.ivLeftSeLoReAd)
                    .execute("http://strahovanie.dn.ua/football_db/logo/logo_" + String.valueOf(nextLogo) + ".png");
            new DownloadImageTask(holder.ivRightSeLoReAd)
                    .execute("http://strahovanie.dn.ua/football_db/logo/logo_" + String.valueOf(nextLogo + 1) + ".png");
        }

    } // onBindViewHolder

    //получаем количество элементов объекта
    @Override
    public int getItemCount() { return listLogo.size(); }

    //Создаем класс ViewHolder с помощью которого мы получаем ссылку на каждый элемент
    //отдельного пункта списка и подключаем слушателя события нажатия меню
    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ivLeftSeLoReAd, ivRightSeLoReAd;

        ViewHolder(View view) {
            super(view);
            ivLeftSeLoReAd = view.findViewById(R.id.ivLeftSeLoReAd);
            ivRightSeLoReAd = view.findViewById(R.id.ivRightSeLoReAd);

            ivLeftSeLoReAd.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //вызов диалога по выбору логотипа
                    selectLogoInterface.callDialogSelectLogo(
                            context, String.valueOf(listLogo.get(getAdapterPosition())), idAuthUser, table);
                    return false;
                }
            });

            ivRightSeLoReAd.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //вызов диалога по выбору логотипа
                    selectLogoInterface.callDialogSelectLogo(
                            context, String.valueOf(listLogo.get(getAdapterPosition()) + 1), idAuthUser, table);
                    return false;
                }
            });

        }//ViewHolder

    } // class ViewHolder

}//SelectLogoRecyclerAdapter
