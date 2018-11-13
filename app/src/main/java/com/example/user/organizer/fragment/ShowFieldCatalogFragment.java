package com.example.user.organizer.fragment;

//-----------Фрагмент выводит каталог полей--------------------------

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.user.organizer.DBUtilities;
import com.example.user.organizer.Field;
import com.example.user.organizer.FieldMapsActivity;
import com.example.user.organizer.R;
import com.example.user.organizer.ShowFieldCatalogRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ShowFieldCatalogFragment extends Fragment {

    RecyclerView rvMainShFiCaFr;
    // адаптер для отображения recyclerView
    ShowFieldCatalogRecyclerAdapter showFieldCatalogRecyclerAdapter;
    Button btnMapShFiCaFr;
    DBUtilities dbUtilities;

    List<Field> fieldList = new ArrayList<>(); //коллекция полей
    Context context;

    String idAuthUser;                 //авторизированный пользователь

    // Метод onAttach() вызывается в начале жизненного цикла фрагмента, и именно здесь
    // мы можем получить контекст фрагмента, в качестве которого выступает класс MainActivity.
    //onAttach(Context) не вызовется до API 23 версии вместо этого будет вызван onAttach(Activity),
    //коий устарел с 23 API
    //Так что вызовем onAttachToContext
    //https://ru.stackoverflow.com/questions/507008/%D0%9D%D0%B5-%D1%80%D0%B0%D0%B1%D0%BE%D1%82%D0%B0%D0%B5%D1%82-onattach
    @TargetApi(23)
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onAttachToContext(context);
    }//onAttach

    //устарел с 23 API
    //Так что вызовем onAttachToContext
    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onAttachToContext(activity);
        }//if
    }//onAttach

    //Вызовется в момент присоединения фрагмента к активити
    protected void onAttachToContext(Context context) {
        //здесь всегда есть контекст и метод всегда вызовется.
        //тут можно кастовать контест к активити.
        //но лучше к реализуемому ею интерфейсу
        //чтоб не проверять из какого пакета активити в каждом из случаев
        this.context = context;
        dbUtilities = new DBUtilities(context);

        // прочитать данные, переданные из активности (из точки вызова)
        idAuthUser = getArguments().getString("idAuthUser");

        //получаем коллекцию полей
        fieldList = dbUtilities.getListField("");

        // создаем адаптер, передаем в него курсор
        showFieldCatalogRecyclerAdapter = new ShowFieldCatalogRecyclerAdapter(context, fieldList, idAuthUser);

    }//onAttachToContext

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View result = inflater.inflate(R.layout.fragment_show_field_catalog, container, false);

        btnMapShFiCaFr = (Button)result.findViewById(R.id.btnMapShFiCaFr);
        btnMapShFiCaFr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FieldMapsActivity.class);
                startActivity(intent);
            }
        });
        // RecyclerView для отображения таблицы users БД
        rvMainShFiCaFr = result.findViewById(R.id.rvMainShFiCaFr);
        //привязываем адаптер к recycler объекту
        rvMainShFiCaFr.setAdapter(showFieldCatalogRecyclerAdapter);

        return result;
    } // onCreateView
}
