package com.example.user.organizer.fragment;

//-----------Фрагмент выводит каталог полей--------------------------

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import com.example.user.organizer.DBLocalUtilities;
import com.example.user.organizer.DBUtilities;
import com.example.user.organizer.Field;
import com.example.user.organizer.FieldMapsActivity;
import com.example.user.organizer.R;
import com.example.user.organizer.ShowAllEventsRecyclerAdapter;
import com.example.user.organizer.ShowFieldCatalogRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ShowFieldCatalogFragment extends Fragment {

    RecyclerView rvMainShFiCaFr;
    // адаптер для отображения recyclerView
    ShowFieldCatalogRecyclerAdapter showFieldCatalogRecyclerAdapter;
    Button btnMapShFiCaFr;
    DBUtilities dbUtilities;
    DBLocalUtilities dbLocalUtilities;

    int spPos;                      //позиция спинера
    List<String> spListCity;             // Данные для спинера выбора города
    Spinner spCityShFiCaFr;
    List<Field> fieldList = new ArrayList<>(); //коллекция полей
    Context context;

    boolean connection;                // статус подключения к сети
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
        dbLocalUtilities = new DBLocalUtilities(context);
        dbLocalUtilities.open();

        // прочитать данные, переданные из активности (из точки вызова)
        idAuthUser = getArguments().getString("idAuthUser");
        connection = getArguments().getBoolean("connection");
    }//onAttachToContext

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View result = inflater.inflate(R.layout.fragment_show_field_catalog, container, false);
        if (connection) {
            btnMapShFiCaFr = (Button) result.findViewById(R.id.btnMapShFiCaFr);
            btnMapShFiCaFr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), FieldMapsActivity.class);
                    intent.putExtra("mapStatus", 0);
                    intent.putExtra("connection", connection);
                    startActivity(intent);
                }
            });
            // RecyclerView для отображения таблицы users БД
            rvMainShFiCaFr = result.findViewById(R.id.rvMainShFiCaFr);
            //получаем коллекцию полей
            fieldList = dbUtilities.getListField("", "");
            // создаем адаптер, передаем в него курсор
            showFieldCatalogRecyclerAdapter = new ShowFieldCatalogRecyclerAdapter(context, fieldList, "0");
            //привязываем адаптер к recycler объекту
            rvMainShFiCaFr.setAdapter(showFieldCatalogRecyclerAdapter);

            if (!idAuthUser.equals("")) {
                //привязка ресурсов к объектам
                spCityShFiCaFr = getActivity().findViewById(R.id.spCityMain);

                //инициализация коллекции для спинера
                spListCity = new ArrayList<>();
                //обращаемся к базе для получения списка имен городов
                spListCity = dbUtilities.getStrListTableFromDB("cities", "name");

                //Слушатель для позиции спинера и фильтрации RecyclerView по изменению позиции
                spCityShFiCaFr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        spPos = position;
                        //проверка если выбран пункт "Все города"
                        if (spPos == (spListCity.size())) buildUserRecyclerView("");
                        else {
                            String cityId = dbUtilities.getIdByValue("cities", "name",
                                    spCityShFiCaFr.getItemAtPosition(spPos).toString());
                            //строим новый адаптер RecyclerView
                            buildUserRecyclerView(cityId);
                        }//if
                    }//onItemSelected

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }//onNothingSelected
                });

                //проверка если выбран пункт "Все города"
                if (spCityShFiCaFr.getSelectedItemPosition() == (spListCity.size()))
                    buildUserRecyclerView("");
                else {
                    String cityId = dbUtilities.getIdByValue("cities", "name",
                            spCityShFiCaFr.getItemAtPosition(spCityShFiCaFr.getSelectedItemPosition()).toString());
                    //строим новый адаптер RecyclerView
                    buildUserRecyclerView(cityId);
                }

                FloatingActionButton fabMain = getActivity().findViewById(R.id.fabMain);
                fabMain.setVisibility(View.VISIBLE);
                fabMain.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (spCityShFiCaFr.getSelectedItemPosition() == (spListCity.size()))
                            refreshList("");
                        else {
                            String cityId = dbUtilities.getIdByValue("cities", "name",
                                    spCityShFiCaFr.getItemAtPosition(spCityShFiCaFr.getSelectedItemPosition()).toString());
                            //строим новый адаптер RecyclerView
                            refreshList(cityId);
                        }
                    }
                });
            }
        } else {
            btnMapShFiCaFr = (Button) result.findViewById(R.id.btnMapShFiCaFr);
            btnMapShFiCaFr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), FieldMapsActivity.class);
                    intent.putExtra("mapStatus", 0);
                    intent.putExtra("connection", connection);
                    startActivity(intent);
                }
            });
            // RecyclerView для отображения таблицы users БД
            rvMainShFiCaFr = result.findViewById(R.id.rvMainShFiCaFr);
            fieldList = dbLocalUtilities.getFieldList("");
            // создаем адаптер, передаем в него курсор
            showFieldCatalogRecyclerAdapter = new ShowFieldCatalogRecyclerAdapter(context, fieldList, "0");
            //привязываем адаптер к recycler объекту
            rvMainShFiCaFr.setAdapter(showFieldCatalogRecyclerAdapter);
        }

        return result;
    } // onCreateView

    //Строим RecyclerView
    private void buildUserRecyclerView(String cityId) {
        //получаем коллекцию полей
        if (connection) fieldList = dbUtilities.getListField("", cityId);
        else fieldList = dbLocalUtilities.getFieldList(cityId);
        // создаем адаптер, передаем в него курсор
        showFieldCatalogRecyclerAdapter = new ShowFieldCatalogRecyclerAdapter(context, fieldList, idAuthUser);
        //привязываем адаптер к recycler объекту
        rvMainShFiCaFr.setAdapter(showFieldCatalogRecyclerAdapter);
    }//buildUserRecyclerView


    //обновляем recyclerview
    private void refreshList(String cityId) {
        //получаем коллекцию полей
        if (connection) fieldList = dbUtilities.getListField("", cityId);
        else fieldList = dbLocalUtilities.getFieldList(cityId);
        showFieldCatalogRecyclerAdapter.notifyDataSetChanged();
        //привязываем адаптер к recycler объекту
        rvMainShFiCaFr.setAdapter(showFieldCatalogRecyclerAdapter);
    }//refreshList
}
