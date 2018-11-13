package com.example.user.organizer;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.user.organizer.fragment.AdvertisingAndInformationFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

//-------Активность для создания новой записи в новостной и рекламной ленте-----------------

public class CreateNewsNoteActivity extends AppCompatActivity {

    DBUtilities dbUtilities;
    Spinner spCityCrNeNoAc;

    Calendar calendar = Calendar.getInstance();      // объект для работы с датой и временем
    String date;                                 // назначения дата записи
    ActionBar actionBar;                //стрелка НАЗАД

    TextView tvDateCrNeNoAc;            // TextView для вывода даты
    EditText etHeadCrNeNoAc;            // EditText для заголовока
    EditText etMessCrNeNoAc;            // EditText для новой записи

    Context context;
    String idAuthUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_news_note);

        //добавляем actionBar (стрелка сверху слева)
        actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        idAuthUser = getIntent().getStringExtra("idAuthUser");

        context = getBaseContext();
        dbUtilities = new DBUtilities(context);

        tvDateCrNeNoAc = (TextView) findViewById(R.id.tvDateCrNeNoAc);
        etHeadCrNeNoAc = (EditText) findViewById(R.id.etHeadCrNeNoAc);
        etMessCrNeNoAc = (EditText) findViewById(R.id.etMessCrNeNoAc);
        spCityCrNeNoAc = (Spinner) findViewById(R.id.spCityCrNeNoAc);

        setInitialDate();           //формирование даты для записи

        buildCitySpinner();     //строим Spinner City

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

    //строим Spinner City
    private void buildCitySpinner() {
        List<String> spListCity = new ArrayList<>();    // Данные для спинера выбора города

        //заполнить spListCity данные для отображения в Spinner
        //обращаемся к базе для получения списка имен городов
        spListCity = dbUtilities.getStrListTableFromDB("cities", "name");

        ArrayAdapter<String> spinnerAdapter;
        //создание адаптера для спинера
        spinnerAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                spListCity
        );

        // назначение адапетра для списка
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spCityCrNeNoAc.setAdapter(spinnerAdapter);
    }//buildCitySpinner

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnConfirmCrNeNoAc:            //кнопка подтвердить выбор
                confirmSelect();
                break;
            case R.id.btnCancelCrNeNoAc:             //кнопка отменить выбор
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
        //пакуем при помощи Extra - объект
        intent.putExtra(AdvertisingAndInformationFragment.PAR_HEAD, etHeadCrNeNoAc.getText().toString());
        intent.putExtra(AdvertisingAndInformationFragment.PAR_MESSAGE, etMessCrNeNoAc.getText().toString());
        intent.putExtra(AdvertisingAndInformationFragment.PAR_DATE, date);
        intent.putExtra(AdvertisingAndInformationFragment.PAR_CITY,
                        dbUtilities.getIdByValue(
                                "cities",              //название таблицы
                                "name",               //название столбца
                                spCityCrNeNoAc.getSelectedItem().toString() //значение для поиска
                                )
        );

        // собственно передача параметров
        setResult(RESULT_OK, intent);
        finish();
    }//confirmSelect

    // установка даты записи
    private void setInitialDate() {
        //задаем дату в нужном формате для БД
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        date = simpleDateFormat.format(calendar.getTimeInMillis());

        //установка текста в TextView
        tvDateCrNeNoAc.setText(date);
    } // setInitialDateTim

}//CreateNewsNoteActivity
