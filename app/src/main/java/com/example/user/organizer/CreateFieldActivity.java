package com.example.user.organizer;

import android.content.ContentValues;
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

//---------------Активность для создания нового поля---------------------

public class CreateFieldActivity extends AppCompatActivity {

    DBUtilities dbUtilities;
    Context context;
    List<String> spListCity;            // Данные для спинера выбора города
    List<String> spListBoolean;         // Данные для спинера выбора освещение
    List<String> spListCoating;         // Данные для спинера выбора покрытие

    EditText etNameCrFi;
    EditText etGeolatCrFi;
    EditText etGeolongCrFi;

    EditText etAddressCrFi;
    EditText etPhoneCrFi;

    Spinner spCityCrFi;
    Spinner spLightCrFi;
    Spinner spCoatingCrFi;
    Spinner spShowerCrFi;
    Spinner spRoofCrFi;

    private ArrayAdapter<String> spAdapterCity;    //Адаптер для спинера выбор города
    private ArrayAdapter<String> spAdapterBoolean; //Адаптер для спинера выбор ДУШ, ОСВЕЩЕНИЕ, КРЫША
    private ArrayAdapter<String> spAdapterCoating; //Адаптер для спинера выбор покрытия

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_field);

        //привязка ресурсов к объектам
        etNameCrFi = (EditText) findViewById(R.id.etNameCrFi);
        etGeolatCrFi = (EditText) findViewById(R.id.etGeolatCrFi);
        etGeolongCrFi = (EditText) findViewById(R.id.etGeolongCrFi);
        etAddressCrFi = (EditText) findViewById(R.id.etAddressCrFi);
        etPhoneCrFi = (EditText) findViewById(R.id.etPhoneCrFi);
        spCityCrFi = (Spinner) findViewById(R.id.spCityCrFi);
        spLightCrFi = (Spinner) findViewById(R.id.spLightCrFi);
        spCoatingCrFi = (Spinner) findViewById(R.id.spCoatingCrFi);
        spShowerCrFi = (Spinner) findViewById(R.id.spShowerCrFi);
        spRoofCrFi = (Spinner) findViewById(R.id.spRoofCrFi);

        //инициализация коллекции для спинера
        spListCity = new ArrayList<>();
        spListBoolean = new ArrayList<>();
        spListCoating = new ArrayList<>();

        context = getBaseContext();
        dbUtilities = new DBUtilities(context);

        //заполнить spListCity данные для отображения в Spinner
        spListCity = dbUtilities.getStrListTableFromDB("cities", "name");

        //заполнить spListBooleanInt данные для отображения в Spinner
        spListBoolean.add("Нет");
        spListBoolean.add("Есть");

        //заполнить spListCoating данные для отображения в Spinner
        spListCoating = dbUtilities.getStrListTableFromDB("coatings", "type");

        //создание адаптера для спинера
        spAdapterCity = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                spListCity
        );

        // назначение адапетра для списка
        spAdapterCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spCityCrFi.setAdapter(spAdapterCity);

        //создание адаптера для спинера
        spAdapterCoating = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                spListCoating
        );

        // назначение адапетра для списка
        spAdapterCoating.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spCoatingCrFi.setAdapter(spAdapterCoating);

        //создание адаптера для спинера
        spAdapterBoolean = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                spListBoolean
        );

        // назначение адапетра для списка
        spAdapterBoolean.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spLightCrFi.setAdapter(spAdapterBoolean);
        spShowerCrFi.setAdapter(spAdapterBoolean);
        spRoofCrFi.setAdapter(spAdapterBoolean);
    }//onCreate

    //обработчик неажатия клавишы Создать запись пользователя
    public void createNewField() {

        ContentValues cv = new ContentValues();
        String city_id = String.valueOf(spListCity.indexOf(spCityCrFi.getSelectedItem()) + 1);
        String name = etNameCrFi.getText().toString();
        String phone = etPhoneCrFi.getText().toString();
        String light_status = (spLightCrFi.getSelectedItem().toString() == "Нет") ? "0" : "1";
        String coating_id = String.valueOf(spListCoating.indexOf(spCoatingCrFi.getSelectedItem()) + 1);
        String shower_status = (spShowerCrFi.getSelectedItem().toString() == "Нет") ? "0" : "1";
        String roof_status = (spRoofCrFi.getSelectedItem().toString() == "Нет") ? "0" : "1";
        String geo_long = etGeolongCrFi.getText().toString();
        String geo_lat = etGeolatCrFi.getText().toString();
        String address = etAddressCrFi.getText().toString();

        //добваить данные через объект ContentValues(cv), в таблицу "field"
        dbUtilities.insertIntoFields( city_id, name, phone, light_status, coating_id,
                shower_status, roof_status, geo_long, geo_lat, address);

        finish();
//        //переходин в актиность CreateEventActivity
//        Intent intent = new Intent(this, CreateEventActivity.class);
//        startActivity(intent);
    }//createNewField

    //вернутся в активность авторизации
    private void turnBack() {
        Intent intent = new Intent(this, AuthorizationActivity.class);
        startActivity(intent);
    }//turnBack

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnNewCityCrFi:            //выполнить операцию
                createNewCity();
                break;
            case R.id.btnCreateCrFi:            //выполнить операцию
                createNewField();
                break;
            case R.id.btnBackCrFi:               //отменить операцию
                turnBack();
                break;
        }//switch
        finish();
    }//onClick

    //обработка нажатия клавиши создания нового города
    private void createNewCity() {
        Intent intent = new Intent(this, CreateCityActivity.class);
        startActivity(intent);
    }//createNewCity

}//CreateFieldActivity
