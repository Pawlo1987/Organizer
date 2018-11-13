package com.example.user.organizer;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.user.organizer.activity.AdvertisingAndInformationActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

//-------Активность для создания новой записи в новостной и рекламной ленте-----------------

public class CreateNewsNoteActivity extends AppCompatActivity {

    DBUtilities dbUtilities;
    Spinner spCityCrNeNoAc;

    // поля для доступа к записям БД
    Cursor noteCursor;                   // прочитанные данные

    Calendar calendar = Calendar.getInstance();      // объект для работы с датой и временем
    String noteDate;                                 // назначения дата записи

    TextView tvDateCrNeNoAc;            // TextView для вывода даты
    EditText etHeadCrNeNoAc;            // EditText для заголовока
    EditText etNoteCrNeNoAc;            // EditText для новой записи

    Context context;
    int idAuthUser = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_news_note);

        context = getBaseContext();
        dbUtilities = new DBUtilities(context);
        dbUtilities.open();

        tvDateCrNeNoAc = (TextView) findViewById(R.id.tvDateCrNeNoAc);
        etHeadCrNeNoAc = (EditText) findViewById(R.id.etHeadCrNeNoAc);
        etNoteCrNeNoAc = (EditText) findViewById(R.id.etNoteCrNeNoAc);
        spCityCrNeNoAc = (Spinner) findViewById(R.id.spCityCrNeNoAc);

        setInitialDate();           //формирование даты для записи

        //запрос для получения курсор с данными
        String query = "SELECT name FROM cities;";

        buildCitySpinner(query);     //строим Spinner City

    }//onCreate

    //строим Spinner City
    private void buildCitySpinner(String query) {
        List<String> spListCity = new ArrayList<>();    // Данные для спинера выбора города
        //заполнить spListCity данные для отображения в Spinner
        spListCity = dbUtilities.fillListStr(query);

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
        intent.putExtra(AdvertisingAndInformationActivity.PAR_HEAD, etHeadCrNeNoAc.getText().toString());
        intent.putExtra(AdvertisingAndInformationActivity.PAR_NOTE, etNoteCrNeNoAc.getText().toString());
        intent.putExtra(AdvertisingAndInformationActivity.PAR_DATE, noteDate);
        intent.putExtra(AdvertisingAndInformationActivity.PAR_CITY,
                        dbUtilities.findIdbySPObject(
                                spCityCrNeNoAc.getSelectedItem().toString(),    //объект спинера
                                "cities",                              //название таблицы
                                "name"                                //название столбца
                                )
        );

        // собственно передача параметров
        setResult(RESULT_OK, intent);
        finish();
    }//confirmSelect

    // установка даты записи
    private void setInitialDate() {
        //формирование даты
        noteDate = DateUtils.formatDateTime(
                this,
                calendar.getTimeInMillis(),  // текущее время в миллисекундах
                // выводим это время в привычном представлении - дата и время
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR);

        //установка текста в TextView
        tvDateCrNeNoAc.setText(noteDate);
    } // setInitialDateTim

}//CreateNewsNoteActivity
