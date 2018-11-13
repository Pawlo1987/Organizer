package com.example.user.organizer;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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
    EditText evPhoneCrEv;                //телефон организатора
    EditText evPasswordCrEv;             //пароль для приватной тренировки
    TextView tvDateCrEv;                 //Строка для отображения даты
    TextView tvStartTimeCrEv;            //Строка для отображения время

    Spinner spFieldCrEv;                     //объект спинер выбора поля
    Spinner spDurationCrEv;                  //объект спинер выбора длительности события
    Spinner spCityCrEv;                      //объект спинер выбора города

    Calendar calendar = Calendar.getInstance();      // объект для работы с датой и временем

    String query; //переменная для запроса

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);


        //привязка ресурсов к объектам
        tvDateCrEv = (TextView) findViewById(R.id.tvDateCrEv);
        tvStartTimeCrEv = (TextView) findViewById(R.id.tvStartTimeCrEv);
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

        setInitialTime();               //начальная установка время
        setInitialDate();               //начальная установка даты
        context = getBaseContext();
        dbUtilities = new DBUtilities(context);
        dbUtilities.open();

        //запрос для получения курсор с данными
        query = "SELECT name FROM city;";

        //заполнить spListCity данные для отображения в Spinner
        spListCity = dbUtilities.fillList(query);

        spCityCrEv.setAdapter(buildSpinner(spListCity));


//        //запрос для получения курсор с данными
//        query = "SELECT field.name FROM field WHERE city = " +
//                spListCity.indexOf(spCityCrEv.getSelectedItem()) + 1 +";";
//
//        //заполнить spListField данные для отображения в Spinner
//        spListField = dbUtilities.fillList(query);
//
//        spFieldCrEv.setAdapter(buildSpinner(spListField));

        //заполнить spListField данные для отображения в Spinner
        spListDuration = Arrays.asList(new String[]{ "30", "45", "60", "90", "120", "150", "180"});

        spDurationCrEv.setAdapter(buildSpinner(spListDuration));

        spCityCrEv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                //запрос для получения курсор с данными
                query = "SELECT field.name FROM field INNER JOIN city ON city._id = field.city" +
                        " WHERE city.name = \"" + spCityCrEv.getItemAtPosition(position) +"\";";

                //заполнить spListField данные для отображения в Spinner
                spListField = dbUtilities.fillList(query);

                spFieldCrEv.setAdapter(buildSpinner(spListField));
            }//onItemSelected

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }//onNothingSelected
        });
    }//onCreate

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

    // установка обработчика изменения/выбора времени
    TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // подготовить и вывести новое время в строке отображения
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            setInitialTime();
        }
    };

    // установка обработчика изменеия/выбора даты
    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // подготовить и вывести новую дату в строке отображения
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDate();
        }
    };

    // установка начальных даты
    private void setInitialDate() {
        tvDateCrEv.setText("Дата " + DateUtils.formatDateTime(
                this,
                calendar.getTimeInMillis(),  // текущее время в миллисекундах
                // выводим это время в привычном представлении - дата и время
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));
    } // setInitialDateTime

    // установка начальных времени
    private void setInitialTime() {
        tvStartTimeCrEv.setText("Время " + DateUtils.formatDateTime(
                this,
                calendar.getTimeInMillis(),  // текущее время в миллисекундах
                // выводим это время в привычном представлении - дата и время
                DateUtils.FORMAT_SHOW_TIME));
    } // setInitialDateTime

    // отображаем диалоговое окно для выбора даты - DatePickerDialog
    public void setDate() {
        new DatePickerDialog(
                this,                  // контекст создания окна
                dateSetListener,                    // слушатель события - дата изменена
                calendar.get(Calendar.YEAR),     // задать год, месяц, и день из объекта-календаря
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH))
                .show();  // показать диалог
    } // setDate

    // отображаем диалоговое окно для выбора времени - TimePickerDialog
    // в 24-х часовом формате
    public void setTime24() {
        new TimePickerDialog(
                this,                      // контекст создания диалогового окна
                timeSetListener,                        // слушатель события изменение времени в диалоге
                calendar.get(Calendar.HOUR_OF_DAY),  // час
                calendar.get(Calendar.MINUTE),       // минута
                true)                                   // 24-х часовый формат времени
                .show();
    } // setTime24

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnNewCityCrEv:            //кнопка создать новое поле
                createNewCity();
                break;
            case R.id.btnNewFieldCrEv:           //кнопка создать новое поле
                createNewField();
                break;
            case R.id.btnDateCrEv:               //кнопка дата
                setDate();
                break;
            case R.id.btnStartTimeCrEv:          //кнопка время начала
                setTime24();
                break;
            case R.id.btnAddUser:                //кнопка добавить участника
                addUser();
                break;
            case R.id.btnConfirmCrEv:            //кнопка создать
                createEvent();
                break;
        }//switch
    }//onClick

    //обработка нажатия клавиши добавить участника
    private void addUser() {
        Intent intent = new Intent(this, SelectParticipantsActivity.class);
        startActivity(intent);
    }//addUser

    //обработка нажатия клавиши создания нового поля
    private void createNewField() {
        Intent intent = new Intent(this, CreateFieldActivity.class);
        startActivity(intent);
    }//createNewField

    //обработка нажатия клавиши создания нового города
    private void createNewCity() {
        Intent intent = new Intent(this, CreateCityActivity.class);
        startActivity(intent);
    }//createNewCity

    //Создать новое событие
    private void createEvent() {

    }//createEvent
}//CreateEventActivity
