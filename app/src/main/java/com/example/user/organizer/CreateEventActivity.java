package com.example.user.organizer;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//--Активность для создания нового события(переход на активность создание нового поля --------------
//-- или нового города или активность для добавления нового участника в событие)-------

public class CreateEventActivity extends AppCompatActivity {

    DBUtilities dbUtilities;
    Cursor creEvCursor;                // курсор для чтения данных из БД
    Context context;

    List<String> spListField;            // Данные для спинера выбора поля
    List<String> spListDuration;         // Данные для спинера выбора длительности события
    List<String> spListCity;             // Данные для спинера выбора города

    EditText etPriceCrEv;                //Общая стоимость тренеровки
    EditText evPhoneCrEv;                //номер организатора
    EditText evPasswordCrEv;             //пароль для приватной тренировки

    Spinner spFieldCrEv;                     //объект спинер выбора поля
    Spinner spDurationCrEv;                  //объект спинер выбора длительности события
    Spinner spCityCrEv;                      //объект спинер выбора города

    private ArrayAdapter<String> spAdapterCity;  //Адаптер для спинера
    private ArrayAdapter<String> spAdapterField;  //Адаптер для спинера
    private ArrayAdapter<String> spAdapterDuration;  //Адаптер для спинера

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        //привязка ресурсов к объектам
        etPriceCrEv = (EditText) findViewById(R.id.etPriceCrEv);
        evPhoneCrEv = (EditText) findViewById(R.id.evPhoneCrEv);
        evPasswordCrEv = (EditText) findViewById(R.id.evPasswordCrEv);
        spCityCrEv = (Spinner) findViewById(R.id.spCityCrEv);
        spFieldCrEv = (Spinner) findViewById(R.id.spFieldCrEv);
        spDurationCrEv = (Spinner) findViewById(R.id.spDurationCrEv);

        //инициализация коллекции для спинера
        spListField = new ArrayList<>();
        spListDuration = new ArrayList<>();
        spListCity = new ArrayList<>();

        context = getBaseContext();
        dbUtilities = new DBUtilities(context);
        dbUtilities.open();

        //запрос для получения курсор с данными
        String query = "SELECT name FROM city;";

        //заполнить spListCity данные для отображения в Spinner
        spListCity = dbUtilities.fillList(query);

        //создание адаптера для спинера
        spAdapterCity = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                spListCity
        );

        // назначение адапетра для списка
        spAdapterCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spCityCrEv.setAdapter(spAdapterCity);

        //запрос для получения курсор с данными
        query = "SELECT field.name FROM field WHERE city = " +
                spListCity.indexOf(spCityCrEv.getSelectedItem()) + 1 +";";

        //заполнить spListField данные для отображения в Spinner
        spListField = dbUtilities.fillList(query);

        //создание адаптера для спинера
        spAdapterField = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                spListField
        );

        // назначение адапетра для списка
        spAdapterField.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spFieldCrEv.setAdapter(spAdapterField);

        //заполнить spListField данные для отображения в Spinner
        spListDuration = Arrays.asList(new String[]{ "30", "45", "60", "90", "120", "150", "180"});

        //создание адаптера для спинера
        spAdapterDuration = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                spListDuration
        );

        // назначение адапетра для списка
        spAdapterDuration.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spDurationCrEv.setAdapter(spAdapterDuration);
    }//onCreate

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnNewCityCrEv:            //кнопка создать новое поле
                createNewCity();
                break;
            case R.id.btnNewFieldCrEv:            //кнопка создать новое поле
                createNewField();
                break;

            case R.id.btnDateCrEv:            //кнопка дата

                break;
            case R.id.btnStartTimeCrEv:            //кнопка время начала
                break;
            case R.id.btnAddUser:            //кнопка добавить участника
                break;
            case R.id.btnConfirmCrEv:               //кнопка создать
                createEvent();
                break;
        }//switch
    }//onClick

    //обрабботка нажатия клавиши создания нового поля
    private void createNewField() {
        Intent intent = new Intent(this, CreateFieldActivity.class);
        startActivity(intent);
    }//createNewField

    //обрабботка нажатия клавиши создания нового города
    private void createNewCity() {
        Intent intent = new Intent(this, CreateCityActivity.class);
        startActivity(intent);
    }//createNewCity

    //Создать новое событие
    private void createEvent() {

    }//createEvent
}//CreateEventActivity
