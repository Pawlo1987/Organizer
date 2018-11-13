package com.example.user.organizer;

import android.content.Context;
import android.database.Cursor;
import android.preference.EditTextPreference;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SelectParticipantsActivity extends AppCompatActivity {

    String query;   //запрос
    String filter = "";  //фильтрующее слово для бинарного поиска
    RecyclerView rvUserSePaAc;
    int spPos;      //позиция спинера

    EditText edBinarySePaAc; //Строка для бинарного поиска

    // адаптер для отображения recyclerView
    SelectParticipantsRecyclerAdapter SelectParticipantsRecyclerAdapter;
    DBUtilities dbUtilities;

    // поля для доступа к записям БД
    Cursor selectUserCursor;                // прочитанные данные
    Context context;

    List<String> spListCity;            // Данные для спинера выбора города
    int spListCitySize;                 //разме листа (количество городов + 1(ВСЕ ГОРОДА))
    Spinner spCitySePaAc;                     //объект спинер
    private ArrayAdapter<String> spAdapterCity;  //Адаптер для спинера

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_participants);

        context = getBaseContext();
        dbUtilities = new DBUtilities(context);
        dbUtilities.open();

        edBinarySePaAc = (EditText) findViewById(R.id.edBinarySePaAc);
        spCitySePaAc = (Spinner) findViewById(R.id.spCitySePaAc);

        //инициализация коллекции для спинера
        spListCity = new ArrayList<>();

        //запрос для получения курсор с данными
        query = "SELECT name FROM city;";

        buildCitySpinner(query);     //строим Spinner City

        //размер коллецкии спинера
        spListCitySize = spListCity.size();

        //получаем позицию спинера все города
        spPos = spListCitySize-1;

        //Установка позиции спинера поумолчанию "ВСЕ ГОРОДА"
        spCitySePaAc.setSelection(spPos);

        //Слушатель для позиции спинера и фильтрации RecyclerView по изменению позиции
        spCitySePaAc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String query;
                spPos = position;
                //если выбран элемент "ВСЕ ГОРОДА"
                if (spCitySePaAc.getItemAtPosition(position).equals("ВСЕ ГОРОДА")){
                    // получаем данные из БД в виде курсора (коллекция, возвращенная запросом)
                    query = "SELECT user.name, user.login, city.name FROM user INNER JOIN " +
                            "city ON city._id = user.def_city;";
                }else {
                    // получаем данные из БД в виде курсора (коллекция, возвращенная запросом)
                    query = "SELECT user.name, user.login, city.name FROM user INNER JOIN " +
                            "city ON city._id = user.def_city WHERE city.name = \"" +
                            spCitySePaAc.getItemAtPosition(position) + "\";";
                }//if-else

                buildUserRecyclerView(query, filter);     //Строим RecyclerView
            }//onItemSelected

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }//onNothingSelected
        });

        // установка слушателя изменения текста в EditText для бинарного поиска
        // и фильтрации RecyclerView по изменению текста в EditText
        edBinarySePaAc.addTextChangedListener(new TextWatcher() {
            // при изменении текста выполняем фильтрацию
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // получаем данные из БД в виде курсора (коллекция, возвращенная запросом)
                //если выбран элемент "ВСЕ ГОРОДА"
                if (spCitySePaAc.getItemAtPosition(spPos).equals("ВСЕ ГОРОДА")){
                    // получаем данные из БД в виде курсора (коллекция, возвращенная запросом)
                    query = "SELECT user.name, user.login, city.name FROM user INNER JOIN " +
                            "city ON city._id = user.def_city;";
                }else {
                    // получаем данные из БД в виде курсора (коллекция, возвращенная запросом)
                    query = "SELECT user.name, user.login, city.name FROM user INNER JOIN " +
                            "city ON city._id = user.def_city WHERE city.name = \"" +
                            spCitySePaAc.getItemAtPosition(spPos) + "\";";
                }//if-else
                filter = edBinarySePaAc.getText().toString();
                buildUserRecyclerView(query, filter);     //Строим RecyclerView
            }//onTextChanged

            public void afterTextChanged(Editable s) { }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        });
    }//onCreate

    //Строим RecyclerView
    private void buildUserRecyclerView(String query, String filter) {
        selectUserCursor =  dbUtilities.getDb().rawQuery(query, null);

        // создаем адаптер, передаем в него курсор
        SelectParticipantsRecyclerAdapter = new SelectParticipantsRecyclerAdapter(context, selectUserCursor, filter);
        // RecycerView для отображения таблицы users БД
        rvUserSePaAc = (RecyclerView) findViewById(R.id.rvUserSePaAc);

        rvUserSePaAc.setAdapter(SelectParticipantsRecyclerAdapter);
    }//buildUserRecyclerView

    //строим Spinner City
    private void buildCitySpinner(String query) {
        //заполнить spListCity данные для отображения в Spinner
        spListCity = dbUtilities.fillList(query);
        spListCity.add("ВСЕ ГОРОДА");

        //создание адаптера для спинера
        spAdapterCity = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                spListCity
        );

        // назначение адапетра для списка
        spAdapterCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spCitySePaAc.setAdapter(spAdapterCity);
    }//buildCitySpinner

}//SelectParticipantsActivity
