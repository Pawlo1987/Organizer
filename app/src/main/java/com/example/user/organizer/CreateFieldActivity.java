package com.example.user.organizer;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.user.organizer.activity.LoginActivity;

import java.util.ArrayList;
import java.util.List;

//---------------Активность для создания нового поля---------------------

public class CreateFieldActivity extends AppCompatActivity {

    DBUtilities dbUtilities;
    Cursor creFiCursor;                // курсор для чтения данных из БД
    Context context;
    List<String> spListCity;           // Данные для спинера выбора города
    List<String> spListBoolean;    // Данные для спинера выбора освещение
    List<String> spListCoating;        // Данные для спинера выбора покрытие

    EditText etNameCrFi;
    EditText etGeolocationCrFi;
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
        etGeolocationCrFi = (EditText) findViewById(R.id.etGeolocationCrFi);
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
        dbUtilities.open();

        //запрос для получения курсор с данными
        String query = "SELECT name FROM cities;";

        //заполнить spListCity данные для отображения в Spinner
        spListCity = dbUtilities.fillListStr(query);

        //заполнить spListBooleanInt данные для отображения в Spinner
        spListBoolean.add("Нет");
        spListBoolean.add("Есть");

        //запрос для получения курсор с данными
        query = "SELECT type FROM coatings;";

        //заполнить spListCoating данные для отображения в Spinner
        spListCoating = dbUtilities.fillListStr(query);

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
        cv.put("city_id", spListCity.indexOf(spCityCrFi.getSelectedItem()) + 1);
        cv.put("name", etNameCrFi.getText().toString());
        cv.put("light_id", (spLightCrFi.getSelectedItem().toString() == "Нет") ? 1 : 2);
        cv.put("coating_id", spListCoating.indexOf(spCoatingCrFi.getSelectedItem()) + 1);
        cv.put("shower_id", (spShowerCrFi.getSelectedItem().toString() == "Нет") ? 1 : 2);
        cv.put("roof_id", (spRoofCrFi.getSelectedItem().toString() == "Нет") ? 1 : 2);
        cv.put("geo_long", etGeolocationCrFi.getText().toString());
        cv.put("geo_lat", etGeolocationCrFi.getText().toString());
        cv.put("address", etAddressCrFi.getText().toString());
        cv.put("phone", etPhoneCrFi.getText().toString());
        //добваить данные через объект ContentValues(cv), в таблицу "field"
        dbUtilities.insertInto(cv, "fields");

        //переходин в актиность CreateEventActivity
        Intent intent = new Intent(this, CreateEventActivity.class);
        startActivity(intent);
    }//createNewField

    //вернутся в активность авторизации
    private void turnBack() {
        Intent intent = new Intent(this, LoginActivity.class);
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
