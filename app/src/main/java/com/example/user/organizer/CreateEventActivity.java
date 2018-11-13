package com.example.user.organizer;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.MenuItem;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

//-- Активность для создания нового события(переход на активность создание нового поля --------------
//-- или нового города или активность для добавления нового участника в событие)-------

public class CreateEventActivity extends AppCompatActivity {

    // коды для идентификации активностей при получении результата
    public final int REQ_ADD_USER = 1001;

    //Параметр -- "ИМЯ КЛЮЧА"
    public static final String PAR_USERS = "select";

    DBUtilities dbUtilities;
    Context context;

    ActionBar actionBar;                //стрелка НАЗАД

    String idAuthUser;                  //id Авторизированого пользователя

    ListView lvListOfParticipantsCrEv;   // ListView для выбранных выбранных участников
    List<String> spListField;            // Данные для спинера выбора поля
    List<Integer> spListDuration;        // Данные для спинера выбора длительности события
    List<String> spListCity;             // Данные для спинера выбора города

    List<String> loginUserList;          //коллекция login-ов с выбранными игроками

    EditText etPriceCrEv;                //Общая стоимость тренеровки
    EditText evPhoneCrEv;                //телефон организатора
    EditText evPasswordCrEv;             //пароль для приватной тренировки
    TextView tvDateCrEv;                 //Строка для отображения даты
    TextView tvStartTimeCrEv;            //Строка для отображения время

    Spinner spFieldCrEv;                 //объект спинер выбора поля
    Spinner spDurationCrEv;              //объект спинер выбора длительности события
    Spinner spCityCrEv;                  //объект спинер выбора города

    Calendar calendar = Calendar.getInstance();      // объект для работы с датой и временем

    String showEventDate;                   // дата события для показа
    String eventDateForDB;                  // дата события для БД
    String eventStartTime;                  // Время начала события
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        idAuthUser = getIntent().getStringExtra("idAuthUser");

        //добавляем actionBar (стрелка сверху слева)
        actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

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

        //заполнить spListCity данные для отображения в Spinner
        spListCity = dbUtilities.getStrListTableFromDB("cities", "name");

        spCityCrEv.setAdapter(buildSpinnerStr(spListCity));

        //заполнить spListField данные для отображения в Spinner
        int[] a = {30, 45, 60, 90, 120, 150, 180};
        for (int i : a) {
            spListDuration.add(i);
        }//foreach

        spDurationCrEv.setAdapter(buildSpinnerInt(spListDuration));

        spCityCrEv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){

                //заполнить spListField данные для отображения в Spinner
                spListField = dbUtilities.getSomeFieldsFromDB(spCityCrEv.getItemAtPosition(position).toString());

                spFieldCrEv.setAdapter(buildSpinnerStr(spListField));
            }//onItemSelected

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }//onNothingSelected
        });//setOnItemSelectedListener
    }//onCreate

    //обработчик actionBar (стрелка сверху слева)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }//switch
    }//onOptionsItemSelected

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
        showEventDate = DateUtils.formatDateTime(
                this,
                calendar.getTimeInMillis(),  // текущее время в миллисекундах
                // выводим это время в привычном представлении - дата и время
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR);

        //установка текста в TextView
        //задаем дату в нужном формате для БД
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        eventDateForDB = simpleDateFormat.format(calendar.getTimeInMillis());

        //установка текста в TextView
        tvDateCrEv.setText(showEventDate);
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
        tvStartTimeCrEv.setText(eventStartTime);
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
            case R.id.btnAddUserCrEv:                //кнопка добавить участника
                addUser();
                break;
            case R.id.btnConfirmCrEv:            //кнопка создать
                createEvent();
                Intent intent = new Intent(this, NavigationDrawerLogInActivity.class);
                intent.putExtra("idAuthUser", idAuthUser);
                startActivity(intent);
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
        intent.putExtra("idAuthUser", idAuthUser);
        startActivityForResult(intent, REQ_ADD_USER);

    }//addUser

    //обработка нажатия клавиши создания нового поля
    private void createNewField() {
        Intent intent = new Intent(this, CreateFieldActivity.class);
        intent.putExtra("idAuthUser", idAuthUser);
        startActivity(intent);
    }//createNewField

    //обработка нажатия клавиши создания нового города
    private void createNewCity() {
        Intent intent = new Intent(this, CreateCityActivity.class);
        startActivity(intent);
    }//createNewCity

    //Создать новое событие
    private void createEvent() {

        //делаем новую запись в таблицу с событиями
        String city_id = dbUtilities.getIdByValue("cities", "name",
                spCityCrEv.getSelectedItem().toString()    //Объект спинера(название города)
        );
        String field_id = dbUtilities.getIdByValue("fields", "name",
                spFieldCrEv.getSelectedItem().toString()   //Объект спинера(название поля)
        );
        String date = eventDateForDB;
        String time = eventStartTime;
        String duration = spDurationCrEv.getSelectedItem().toString();
        String price = etPriceCrEv.getText().toString();
        String password = evPasswordCrEv.getText().toString();
        String phone = evPhoneCrEv.getText().toString();
        String user_id = idAuthUser;

        //добваить данные через объект ContentValues(cv), в таблицу "event"
        dbUtilities.insertIntoEvents(field_id, city_id, date, time,
                duration, price, password, phone, user_id);

        //добавляем участников в таблицу participants
        for (String loginUser : loginUserList) {

            //id нового события будет равен максимальному значению id + 1
            String event_id = dbUtilities.getMaxValueInHisColumn("events", "id");

            user_id = dbUtilities.getIdByValue("users",
                    "login", loginUser);

            //добавление новой записи в таблицу participants
            dbUtilities.insertIntoParticipants(event_id, user_id);

            //добавление новой записи в таблицу notifications
            dbUtilities.insertIntoNotifications(event_id, user_id, city_id, field_id, time, date, "1");
        }//foreach

    }//createEvent
}//CreateEventActivity
