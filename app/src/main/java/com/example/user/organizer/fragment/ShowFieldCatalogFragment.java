package com.example.user.organizer.fragment;

//-----------Фрагмент выводит каталог полей--------------------------




import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.organizer.DBUtilities;
import com.example.user.organizer.Field;
import com.example.user.organizer.R;
import com.example.user.organizer.ShowFieldCatalogRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ShowFieldCatalogFragment extends Fragment {

    RecyclerView rvMainShFiCaFr;
    // адаптер для отображения recyclerView
    ShowFieldCatalogRecyclerAdapter showFieldCatalogRecyclerAdapter;
    DBUtilities dbUtilities;

    List<Field> fieldList = new ArrayList<>(); //коллекция полей
    Context context;

    String idAuthUser;                 //авторизированный пользователь

    // Метод onAttach() вызывается в начале жизненного цикла фрагмента, и именно здесь
    // мы можем получить контекст фрагмента, в качестве которого выступает класс MainActivity.
    @Override
    public void onAttach(Context context) {
        this.context = context;
        dbUtilities = new DBUtilities(context);

        // прочитать данные, переданные из активности (из точки вызова)
        idAuthUser = getArguments().getString("idAuthUser");

        //получаем коллекцию полей
        fieldList = dbUtilities.getListField("");

        // создаем адаптер, передаем в него курсор
        showFieldCatalogRecyclerAdapter = new ShowFieldCatalogRecyclerAdapter(context, fieldList, idAuthUser);
        super.onAttach(context);
    } // onAttach

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View result = inflater.inflate(R.layout.fragment_show_field_catalog, container, false);
        // RecyclerView для отображения таблицы users БД
        rvMainShFiCaFr = result.findViewById(R.id.rvMainShFiCaFr);
        //привязываем адаптер к recycler объекту
        rvMainShFiCaFr.setAdapter(showFieldCatalogRecyclerAdapter);

        return result;
    } // onCreateView
}
