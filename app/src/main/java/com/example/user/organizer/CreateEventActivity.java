package com.example.user.organizer;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static com.example.user.organizer.R.id.etPasswordCrAcAc;

//--Активность для создания нового события(переход на активность создание нового поля --------------
//-- или нового города или активность для добавления нового участника в событие)-------

public class CreateEventActivity extends AppCompatActivity {

    // коды для идентификации активностей при получении результата
    public final int REQ_ADD_USER = 1001;

    //Параметр -- "ИМЯ КЛЮЧА"
    public static final String PAR_USERS = "select";

    DBUtilities dbUtilities;
    Cursor creEvCursor;                  // курсор для чтения данных из БД
    Context context;

    String authorizLogin = "user1";                // Логин авторизированого пользователя

    ListView lvListOfParticipantsCrEv;   // ListView для выбранных выбранных участников
    List<String> spListField;            // Данные для спинера выбора поля
    List<String> spListDuration;         // Данные для спинера выбора длительности события
    List<String> spListCity;             // Данные для спинера выбора города

    List<String> loginUserList;             //коллекция login-ов с выбранными игроками

    EditText etPriceCrEv;                //Общая стоимость тренеровки
    EditText evPhoneCrEv;                //телефон организатора
    EditText evPasswordCrEv;             //пароль для приватной тренировки
    TextView tvDateCrEv;                 //Строка для отображения даты
    TextView tvStartTimeCrEv;            //Строка для отображения время

    Spinner spFieldCrEv;                     //объект спинер выбора поля
    Spinner spDurationCrEv;                  //объект спинер выбора длительности события
    Spinner spCityCrEv;                      //объект спинер выбора города

    Calendar calendar = Calendar.getInstance();      // объект для работы с датой и временем

    String query;                           //переменная для запроса
    String eventDate;                       // назначения дата события
    String eventStartTime;                  // Время начала события
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
        lvListOfParticipantsCrEv = (ListView) findViewById(R.id.lvListOfParticipantsCrEv);

        //инициализация коллекции для спинера
        spListField = new ArrayList<>();
        spListDuration = new ArrayList<>();
        spListCity = new ArrayList<>();

        loginUserList = new ArrayList<>();

        setInitialTime();               //начальная установка время
        setInitialDate();               //начальная установка даты
        context = getBaseContext();
        dbUtilities = new DBUtilities(context);
        dbUtilities.open();

        //запрос для получения курсор с данными
        query = "SELECT name FROM cities;";

        //заполнить spListCity данные для отображения в Spinner
        spListCity = dbUtilities.fillList(query);

        spCityCrEv.setAdapter(buildSpinner(spListCity));

        //заполнить spListField данные для отображения в Spinner
        spListDuration = Arrays.asList(new String[]{ "30", "45", "60", "90", "120", "150", "180"});

        spDurationCrEv.setAdapter(buildSpinner(spListDuration));

        spCityCrEv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                //запрос для получения курсор с данными
                query = "SELECT fields.name FROM fields INNER JOIN cities ON cities._id = fields.city" +
                        " WHERE cities.name = \"" + spCityCrEv.getItemAtPosition(position) +"\";";

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
        //формирование даты
        eventDate = DateUtils.formatDateTime(
                this,
                calendar.getTimeInMillis(),  // текущее время в миллисекундах
                // выводим это время в привычном представлении - дата и время
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR);

        //установка текста в TextView
        tvDateCrEv.setText("Дата " + eventDate);
    } // setInitialDateTime

    // установка начальных времени
    private void setInitialTime() {
        //формирование время
        eventStartTime = DateUtils.formatDateTime(
                this,
                calendar.getTimeInMillis(),  // текущее время в миллисекундах
                // выводим это время в привычном представлении - дата и время
                DateUtils.FORMAT_SHOW_TIME);

        //установка текста в TextView
        tvStartTimeCrEv.setText("Время " + eventStartTime);
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

    //-----------------------Метод для приема результатов из активностей----------------------------
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "Ошибка ввода!!!", Toast.LENGTH_LONG).show();
        }

        // обработка результатов по активностям
        if (resultCode == RESULT_OK) {
            // читаем из объекта data полученные данные и выводим в поле результата
            //создаем новые данные о самолете из полученных данных
            loginUserList.clear();
            loginUserList.addAll(data.getParcelableArrayListExtra(PAR_USERS));
            fillLV();   //Заполнение и вывод в ListView
        }

        Log.d("MyLog", loginUserList.toString());
    }//onActivityResult

    //заполнение ListView
    private void fillLV() {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, loginUserList);

        // присваиваем адаптер списку
        lvListOfParticipantsCrEv.setAdapter(adapter);
    }//fillLV

    //обработка нажатия клавиши добавить участника
    private void addUser() {
        Intent intent = new Intent(this, SelectParticipantsActivity.class);
        startActivityForResult(intent, REQ_ADD_USER);

    }//addUser

    //обработка нажатия клавиши создания нового поля
    private void createNewField() {
        Intent intent = new Intent(this, CreateFieldActivity.class);
        startActivity(intent);
    }//createNewField

    //обработка нажатия клавиши создания нового города
    private void createNewCity() {

    }//createNewCity

    //Создать новое событие
    private void createEvent() {

        //делаем новую запись в таблицу с событиями
        ContentValues cv = new ContentValues();
        cv.put("city", dbUtilities.findIdbySPObject(
                spCityCrEv.getSelectedItem().toString(),    //Объект спинера(название города)
                "cities",                                   //название таблицы
                "name")                                     //название столбца
        );
        cv.put("field", dbUtilities.findIdbySPObject(
                spFieldCrEv.getSelectedItem().toString(),   //Объект спинера(название поля)
                "fields",                                   //название таблицы
                "name")                                     //название столбца
        );
        cv.put("date", eventDate);
        cv.put("starttime", eventStartTime);
        cv.put("duration", spDurationCrEv.getSelectedItem().toString());
        cv.put("price", etPriceCrEv.getText().toString());
        cv.put("password", evPasswordCrEv.getText().toString());
        cv.put("phone", evPhoneCrEv.getText().toString());

        //добваить данные через объект ContentValues(cv), в таблицу "event"
        dbUtilities.insertInto(cv, "events");

        //делаем новую запись в таблицу с участниками
        //первая запись организатора
        cv = new ContentValues();
        cv.put("eventnumber", dbUtilities.tableSize("events"));
        cv.put("user", authorizLogin);
        cv.put("status", 0);

        //добваить данные через объект ContentValues(cv), в таблицу "participants"
        dbUtilities.insertInto(cv, "participants");

        for (String loginUser : loginUserList) {

            cv = new ContentValues();
            cv.put("eventnumber", dbUtilities.tableSize("events"));
            cv.put("user", dbUtilities.findIdbySPObject(
                    loginUser,
                    "users",
                    "login")
            );
            cv.put("status", 1);

            //добваить данные через объект ContentValues(cv), в таблицу "participants"
            dbUtilities.insertInto(cv, "participants");

        }
        //переходин в актиность LoginPartActivity
        Intent intent = new Intent(this, LoginPartActivity.class);
        startActivity(intent);
    }//createEvent
}//CreateEventActivity
