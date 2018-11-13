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
import java.util.Calendar;
import java.util.List;

//--Активность для редактирования события(переход на активность создание нового поля --------------
//-- или нового города или активность для добавления нового участника в событие)-------
public class EditEventActivity extends AppCompatActivity {

    // коды для идентификации активностей при получении результата
    public final int REQ_ADD_USER = 1001;

    //Параметр -- "ИМЯ КЛЮЧА"
    public static final String PAR_USERS = "select";

    DBUtilities dbUtilities;
    Cursor edEvCursor;                  // курсор для чтения данных из БД
    Context context;

    String authorizLogin = "user1";      // Логин авторизированого пользователя

    boolean flChangeLoginUserList = false;      //  Поменялся список учасников

    ListView lvListOfParticipantsEdEv;   // ListView для выбранных выбранных участников
    List<String> spListField;            // Данные для спинера выбора поля
    List<Integer> spListDuration;        // Данные для спинера выбора длительности события
    List<String> spListCity;             // Данные для спинера выбора города

    List<String> loginUserList;          //коллекция login-ов с выбранными игроками

    EditText etPriceEdEv;                //Общая стоимость тренеровки
    EditText evPhoneEdEv;                //телефон организатора
    EditText evPasswordEdEv;             //пароль для приватной тренировки
    TextView tvDateEdEv;                 //Строка для отображения даты
    TextView tvStartTimeEdEv;            //Строка для отображения время

    Spinner spFieldEdEv;                 //объект спинер выбора поля
    Spinner spDurationEdEv;              //объект спинер выбора длительности события
    Spinner spCityEdEv;                  //объект спинер выбора города

    Calendar calendar = Calendar.getInstance();      // объект для работы с датой и временем

    Intent intent;                          // для получения результатов из активности
    int event_id;
    String query;                           //переменная для запроса
    String eventDate;                       // назначения дата события
    String eventStartTime;                  // Время начала события
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);
        //привязка ресурсов к объектам
        tvDateEdEv = (TextView) findViewById(R.id.tvDateEdEv);
        tvStartTimeEdEv = (TextView) findViewById(R.id.tvStartTimeEdEv);
        etPriceEdEv = (EditText) findViewById(R.id.etPriceEdEv);
        evPhoneEdEv = (EditText) findViewById(R.id.evPhoneEdEv);
        evPasswordEdEv = (EditText) findViewById(R.id.evPasswordEdEv);
        spCityEdEv = (Spinner) findViewById(R.id.spCityEdEv);
        spFieldEdEv = (Spinner) findViewById(R.id.spFieldEdEv);
        spDurationEdEv = (Spinner) findViewById(R.id.spDurationEdEv);
        lvListOfParticipantsEdEv = (ListView) findViewById(R.id.lvListOfParticipantsEdEv);

        //подготовка данных для редактирования
        intent = getIntent();
        event_id = intent.getIntExtra("_id", 0);
        spDurationEdEv.setSelection(intent.getIntExtra("duration", 0));
        etPriceEdEv.setText(String.valueOf(intent.getIntExtra("price", 0)));
        evPasswordEdEv.setText(intent.getStringExtra("password"));
        evPhoneEdEv.setText(intent.getStringExtra("phone"));

        //инициализация коллекции для спинера
        spListField = new ArrayList<>();
        spListDuration = new ArrayList<>();
        spListCity = new ArrayList<>();

        loginUserList = new ArrayList<>();

        setInitialTime();               //начальная установка время
        setInitialDate();               //начальная установка даты
        context = getBaseContext();
        dbUtilities = new DBUtilities(context);
//        dbUtilities.open();

        //запрос для получения курсор с данными
        query = "SELECT name FROM cities;";

        //заполнить spListCity данные для отображения в Spinner
//        spListCity = dbUtilities.fillListStr(query);

        spCityEdEv.setAdapter(buildSpinnerStr(spListCity));
        spCityEdEv.setSelection(spListCity.indexOf(intent.getStringExtra("city_name")));

        //заполнить spListField данные для отображения в Spinner
        int[] a = {30, 45, 60, 90, 120, 150, 180};
        for (int i : a) {
            spListDuration.add(i);
        }//foreach

        spDurationEdEv.setAdapter(buildSpinnerInt(spListDuration));
        spDurationEdEv.setSelection(spListDuration.indexOf(intent.getIntExtra("duration", 0)));

        //заполняем список учасников
        query = "SELECT users.name FROM participants " +
                "INNER JOIN users ON users._id = participants.user_id " +
                "WHERE participants.event_id = " + event_id + ";";
//        loginUserList = dbUtilities.fillListStr(query);
        fillLV();

        spCityEdEv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                //запрос для получения курсор с данными
                query = "SELECT fields.name FROM fields INNER JOIN cities ON cities._id = fields.city_id" +
                        " WHERE cities.name = \"" + spCityEdEv.getItemAtPosition(position) +"\";";

                //заполнить spListField данные для отображения в Spinner
//                spListField = dbUtilities.fillListStr(query);

                spFieldEdEv.setAdapter(buildSpinnerStr(spListField));
                spFieldEdEv.setSelection(spListField.indexOf(intent.getStringExtra("field_name")));

            }//onItemSelected

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }//onNothingSelected
        });

        // установка значений даты и время из БД к данному событию
        tvDateEdEv.setText(intent.getStringExtra("date"));
        tvStartTimeEdEv.setText(intent.getStringExtra("time"));
    }//onCreate

    //строим Spinner
    private ArrayAdapter buildSpinnerStr(List<String> list) {

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

    //строим Spinner
    private ArrayAdapter buildSpinnerInt(List<Integer> list) {

        ArrayAdapter<Integer> spinnerAdapter;

        //создание адаптера для спинера
        spinnerAdapter = new ArrayAdapter<Integer>(
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
        tvDateEdEv.setText("Дата " + eventDate);
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
        tvStartTimeEdEv.setText("Время " + eventStartTime);
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
            case R.id.btnNewCityEdEv:            //кнопка создать новое поле
                createNewCity();
                break;
            case R.id.btnNewFieldEdEv:           //кнопка создать новое поле
                createNewField();
                break;
            case R.id.btnDateEdEv:               //кнопка дата
                setDate();
                break;
            case R.id.btnStartTimeEdEv:          //кнопка время начала
                setTime24();
                break;
            case R.id.btnAddUserEdEv:                //кнопка добавить участника
                addUser();
                break;
            case R.id.btnConfirmEdEv:            //кнопка обновить
                updateEvent();
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
            flChangeLoginUserList = true;
            fillLV();   //Заполнение и вывод в ListView
        }

        Log.d("MyLog", loginUserList.toString());
    }//onActivityResult

    //заполнение ListView
    private void fillLV() {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, loginUserList);

        // присваиваем адаптер списку
        lvListOfParticipantsEdEv.setAdapter(adapter);
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
        Intent intent = new Intent(this, CreateCityActivity.class);
        startActivity(intent);
    }//createNewCity

    //обновление событие
    private void updateEvent() {
//        //делаем новую запись в таблицу с событиями
//        ContentValues cv = new ContentValues();
//        cv.put("city_id", dbUtilities.findIdbySPObject(
//                spCityEdEv.getSelectedItem().toString(),    //Объект спинера(название города)
//                "cities",                                   //название таблицы
//                "name")                                     //название столбца
//        );
//        cv.put("field_id", dbUtilities.findIdbySPObject(
//                spFieldEdEv.getSelectedItem().toString(),   //Объект спинера(название поля)
//                "fields",                                   //название таблицы
//                "name")                                     //название столбца
//        );
//        cv.put("date", eventDate);
//        cv.put("time", eventStartTime);
//        cv.put("duration", spDurationEdEv.getSelectedItem().toString());
//        cv.put("price", etPriceEdEv.getText().toString());
//        cv.put("password", evPasswordEdEv.getText().toString());
//        cv.put("phone", evPhoneEdEv.getText().toString());
//        cv.put("user_id", intent.getIntExtra("user_id", 0));
//
//        //добваить данные через объект ContentValues(cv), в таблицу "event"
//        dbUtilities.updateTable(cv, "events", String.valueOf(event_id));
//
//        if(flChangeLoginUserList){
//
//            //заполняем список учасников(_id)
//            List<Integer> idUsersList = new ArrayList<>();
//            query = "SELECT participants._id FROM participants " +
//                    "WHERE participants.event_id = " + event_id + ";";
//            idUsersList = dbUtilities.fillListInt(query);
//
//            //удаляем старый список учасников из таблицы participants
//            for (Integer idUser : idUsersList) {
//                dbUtilities.deleteRowById("participants", idUser);
//            }//foreach
//
//            //добавляем участников в таблицу participants
//            for (String loginUser : loginUserList) {
//
//                cv = new ContentValues();
//                cv.put("event_id", event_id);
//                cv.put("user_id", dbUtilities.findIdbySPObject(
//                        loginUser,
//                        "users",
//                        "login")
//                );
//
//                //добваить данные через объект ContentValues(cv), в таблицу "participants"
//                dbUtilities.insertInto(cv, "participants");
//
//            }//for
//        }//if
//        //переходин в актиность LoginActivity
//        Intent intent = new Intent(this, LoginActivity.class);
//        startActivity(intent);
        finish();
    }//updateEvent
}//EditEventActivity
