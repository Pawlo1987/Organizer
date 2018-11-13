package com.example.user.organizer;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

//----------------Активность для создания нового города-------------------

public class CreateCityActivity extends AppCompatActivity {

    DBUtilities dbUtilities;
    Context context;
    List<String> spListRegion;            // Данные для спинера выбора региона

    EditText etCityNameCrCi;
    Spinner spRegionCrCi;                     //объект спинер
    private ArrayAdapter<String> spAdapterRegion;  //Адаптер для спинера

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_city);

        //привязка ресурсов к объектам
        etCityNameCrCi = (EditText) findViewById(R.id.etCityNameCrCi);
        spRegionCrCi = (Spinner) findViewById(R.id.spRegionCrCi);

        //инициализация коллекции для спинера
        spListRegion = new ArrayList<>();

        context = getBaseContext();
        dbUtilities = new DBUtilities(context);

        //заполнить spListCity данные для отображения в Spinner
        spListRegion = dbUtilities.getStrListTableFromDB("regions", "name");

        //создание адаптера для спинера
        spAdapterRegion = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                spListRegion
        );

        // назначение адапетра для списка
        spAdapterRegion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spRegionCrCi.setAdapter(spAdapterRegion);
    }//onCreate

    //обработчик неажатия клавишы Создать запись пользователя
    public void createNewCity() {
        String name = etCityNameCrCi.getText().toString();
        String region_id = String.valueOf(spListRegion.indexOf(spRegionCrCi.getSelectedItem()) + 1);

        //метод для создания новой записи в определоной таблице БД
        dbUtilities.insertIntoCities(name, region_id);

        finish();
    }//CreateNewAccount

    //вернутся в активность авторизации
    private void turnBack() {
        Intent intent = new Intent(this, CreateEventActivity.class);
        startActivity(intent);
    }//turnBack

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCreateCrCi:            //выполнить операцию
                createNewCity();
                break;
            case R.id.btnBackCrCi:               //отменить операцию
                turnBack();
                break;
        }//switch
        finish();
    }//onClick
}//CreateCityActivity
