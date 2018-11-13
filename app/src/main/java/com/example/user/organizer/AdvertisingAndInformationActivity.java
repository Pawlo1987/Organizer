package com.example.user.organizer;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AdvertisingAndInformationActivity extends AppCompatActivity {

    // коды для идентификации активностей при получении результата
    public final int REQ_CREATE_NOTE = 1001;

    //Параметр -- "ИМЯ КЛЮЧА"
    public static final String PAR_HEAD = "head";
    public static final String PAR_NOTE = "note";
    public static final String PAR_CITY = "city";
    public static final String PAR_DATE = "date";

    RecyclerView rvNoteLineAdAnInAc;

    // адаптер для отображения recyclerView
    AdvertisingAndInformationRecyclerAdapter AdvertisingAndInformationRecyclerAdapter;
    DBUtilities dbUtilities;
    Spinner spCityAdAnInAc;
    int spPos;                      //позиция спинера

    // поля для доступа к записям БД
    Cursor infoCursor;                // прочитанные данные
    List<String> spListCity;             // Данные для спинера выбора города

    TextView tvAdAnInAc;
    Context context;
    int idAuthUser = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertising_and_information);

        //привязка ресурсов к объектам
        spCityAdAnInAc = (Spinner) findViewById(R.id.spCityAdAnInAc);

        //инициализация коллекции для спинера
        spListCity = new ArrayList<>();

        context = getBaseContext();
        dbUtilities = new DBUtilities(context);
        dbUtilities.open();

        //запрос для получения курсор с данными
        String query = "SELECT name FROM cities;";

        //заполнить spListCity данные для отображения в Spinner
        spListCity = dbUtilities.fillList(query);
        spListCity.add("ВСЕ ГОРОДА");

        spCityAdAnInAc.setAdapter(buildSpinner(spListCity));
        spCityAdAnInAc.setSelection(spListCity.size()-1);

        //Слушатель для позиции спинера и фильтрации RecyclerView по изменению позиции
        spCityAdAnInAc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String query;
                spPos = position;
                //если выбран элемент "ВСЕ ГОРОДА"
                if (spCityAdAnInAc.getItemAtPosition(position).equals("ВСЕ ГОРОДА")){
                    // получаем данные из БД в виде курсора (коллекция, возвращенная запросом)
                    query = "SELECT head, date, cities.name FROM infonotes " +
                            "INNER JOIN cities ON cities._id = infonotes.city;";
                }else {
                    // получаем данные из БД в виде курсора (коллекция, возвращенная запросом)
                    query = "SELECT head, date, cities.name FROM infonotes " +
                            "INNER JOIN cities ON cities._id = infonotes.city WHERE cities.name = \"" +
                            spCityAdAnInAc.getItemAtPosition(position) + "\";";
                }//if-else

                buildUserRecyclerView(query);     //Строим RecyclerView
            }//onItemSelected

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }//onNothingSelected
        });

        // получаем данные из БД в виде курсора (коллекция, возвращенная запросом)
        query = "SELECT head, date, cities.name FROM infonotes " +
                       "INNER JOIN cities ON cities._id = infonotes.city;";

        buildUserRecyclerView(query);     //Строим RecyclerView

        tvAdAnInAc = (TextView) findViewById(R.id.tvAdAnInAc);
        tvAdAnInAc.setText(String.valueOf(infoCursor.getCount()));
    }//onCreate

    //-----------------------Метод для приема результатов из активностей----------------------------
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "Ошибка ввода!!!", Toast.LENGTH_LONG).show();
        }//RESULT_CANCELED

        // обработка результатов по активностям
        if (resultCode == RESULT_OK) {
            // читаем из объекта data полученные данные и выводим в поле результата

            String head = data.getStringExtra(PAR_HEAD);
            String note = data.getStringExtra(PAR_NOTE);
            String date = data.getStringExtra(PAR_DATE);
            int city = data.getIntExtra(PAR_CITY, 0);

            writeDataToBD(head, note, date, city);
        }//RESULT_OK

        //обновим список после обновления
        // получаем данные из БД в виде курсора (коллекция, возвращенная запросом)
        String query = "SELECT head, date, cities.name FROM infonotes " +
                "INNER JOIN cities ON cities._id = infonotes.city;";

        buildUserRecyclerView(query);     //Строим RecyclerView
    }//onActivityResult

    private void writeDataToBD(String head, String note, String date, int city) {
        ContentValues cv = new ContentValues();
        cv.put("head", head);
        cv.put("note", note);
        cv.put("date", date);
        cv.put("city", city);

        //добваить данные через объект ContentValues(cv), в таблицу "user"
        dbUtilities.insertInto(cv, "infonotes");
    }//writeDataToBD


    //строим Spinner
    private ArrayAdapter buildSpinner(List<String> list) {

        ArrayAdapter<String> spinnerAdapter;

        //создание адаптера для спинера
        spinnerAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                list
        );

        // назначение адапетра для списка
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        return spinnerAdapter;
    }//buildCitySpinner

    //Строим RecyclerView
    private void buildUserRecyclerView(String query) {
        infoCursor =  dbUtilities.getDb().rawQuery(query, null);

        // создаем адаптер, передаем в него курсор
        AdvertisingAndInformationRecyclerAdapter
                = new AdvertisingAndInformationRecyclerAdapter(context, infoCursor);
        // RecycerView для отображения таблицы users БД
        rvNoteLineAdAnInAc = (RecyclerView) findViewById(R.id.rvNoteLineAdAnInAc);

        rvNoteLineAdAnInAc.setAdapter(AdvertisingAndInformationRecyclerAdapter);

    }//buildUserRecyclerView

    //Обработка нажатия клавишы "Создать новость"
    public void onClick(View view) {
        Intent intent = new Intent(this, CreateNewsNoteActivity.class);
        startActivityForResult(intent, REQ_CREATE_NOTE);
    }//onClick
}
