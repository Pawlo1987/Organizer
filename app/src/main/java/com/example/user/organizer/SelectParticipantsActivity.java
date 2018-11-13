package com.example.user.organizer;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

//-------Активность для выбора участников для события-----------------

public class SelectParticipantsActivity extends AppCompatActivity {

    String query;                   //запрос
    String filter = "";             //фильтрующее слово для бинарного поиска
    RecyclerView rvUserSePaAc;      //RecyclerView для учасников
    int spPos;                      //позиция спинера
    List<String> loginUserList;     //коллекция логинов с выбранными игроками

    EditText edBinarySePaAc;        //Строка для бинарного поиска

    // адаптер для отображения recyclerView
    com.example.user.organizer.SelectParticipantsRecyclerAdapter SelectParticipantsRecyclerAdapter;
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

        //инициализация коллекции для выбора участников
        loginUserList = new ArrayList<>();

        edBinarySePaAc = (EditText) findViewById(R.id.edBinarySePaAc);
        spCitySePaAc = (Spinner) findViewById(R.id.spCitySePaAc);

        //инициализация коллекции для спинера
        spListCity = new ArrayList<>();

        //запрос для получения курсор с данными
        query = "SELECT name FROM cities;";

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
                    query = "SELECT users.name, users.login, cities.name FROM users INNER JOIN " +
                            "cities ON cities._id = users.def_city;";
                }else {
                    // получаем данные из БД в виде курсора (коллекция, возвращенная запросом)
                    query = "SELECT users.name, users.login, cities.name FROM users INNER JOIN " +
                            "cities ON cities._id = users.def_city WHERE cities.name = \"" +
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
                    query = "SELECT users.name, users.login, cities.name FROM users INNER JOIN " +
                            "cities ON cities._id = users.def_city;";
                }else {
                    // получаем данные из БД в виде курсора (коллекция, возвращенная запросом)
                    query = "SELECT users.name, users.login, cities.name FROM users INNER JOIN " +
                            "cities ON cities._id = users.def_city WHERE cities.name = \"" +
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
        SelectParticipantsRecyclerAdapter
                = new SelectParticipantsRecyclerAdapter(context, selectUserCursor, filter, loginUserList);
        // RecycerView для отображения таблицы users БД
        rvUserSePaAc = (RecyclerView) findViewById(R.id.rvUserSePaAc);

        rvUserSePaAc.setAdapter(SelectParticipantsRecyclerAdapter);

        // для сохранения отмеченных checkbox при скролинге пропишем доп. функции для recyclerView
        rvUserSePaAc.setDrawingCacheEnabled(true);
        rvUserSePaAc.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        rvUserSePaAc.setItemViewCacheSize(dbUtilities.tableSize("users"));

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

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnConfirmSePaAc:            //кнопка подтвердить выбор
                confirmSelect();
                break;
            case R.id.btnCancelSePaAc:             //кнопка отменить выбор
                cancelSelect();
                break;
        }//switch
    }//onClick

    //возврат к предедыщему меню
    private void cancelSelect() {
        setResult(RESULT_CANCELED);
        finish();
    }//cancelSelect

    //сохранить и передать выборанных игроков
    private void confirmSelect() {

        // создать для передачи результатов
        Intent intent = new Intent();
        //пакуем при помощи Extra Parcelable - объект
        intent.putStringArrayListExtra(CreateEventActivity.PAR_USERS, (ArrayList<String>) loginUserList);

        // собственно передача параметров
        setResult(RESULT_OK, intent);
        finish();
    }//confirmSelect

//    //-----------------сохрание данных при смене ориентации устройства----------------------------
//    // сохранение состояния при повороте устройства
//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        // необходимо сохранить поле tvShow
//        outState.putBinder("rv",rvUserSePaAc);
//        // необходимо сохранить коллекцию
//        outState.putParcelableArrayList(KEY_LIST, (ArrayList<? extends Parcelable>) tempAirplanes);
//        super.onSaveInstanceState(outState);
//    }//onSaveInstanceState
//
//    // Восстановление значений, сохраненных при повороте устройства
//    @Override
//    public void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//
//        // получение ранее сохраненных параметров
//        tvMainAc.setText(savedInstanceState.getString("tv"));
//        tempAirplanes.clear();
//        tempAirplanes.addAll(savedInstanceState.<Airplane>getParcelableArrayList(KEY_LIST));
//
//        airplanesAdapter.notifyDataSetChanged(); //обновить, перерисовать адаптер
//        lvAirplanes.setAdapter(airplanesAdapter);//показать listview
//    }//onRestoreInstanceState

}//SelectParticipantsActivity
