package com.example.user.organizer;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

//---------------Активность для создания нового поля---------------------

public class CreateFieldActivity extends AppCompatActivity implements View.OnFocusChangeListener {

    // коды для идентификации активностей при получении результата
    public final int REQ_SELECT_LOCATION = 1002;

    //Параметр -- "ИМЯ КЛЮЧА"
    public static final String PAR_LAT = "latitude";
    public static final String PAR_LONG = "longitude";

    DBUtilities dbUtilities;
    Context context;
    List<String> spListCity;            // Данные для спинера выбора города
    List<String> spListBoolean;         // Данные для спинера выбора освещение
    List<String> spListCoating;         // Данные для спинера выбора покрытие

    String idAuthUser;      // id авторизированого пользователя
    ActionBar actionBar;                //стрелка НАЗАД

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
    Switch swLocationCrFi;
    TextInputLayout etPhoneCrFiLayout;

    private ArrayAdapter<String> spAdapterCity;    //Адаптер для спинера выбор города
    private ArrayAdapter<String> spAdapterBoolean; //Адаптер для спинера выбор ДУШ, ОСВЕЩЕНИЕ, КРЫША
    private ArrayAdapter<String> spAdapterCoating; //Адаптер для спинера выбор покрытия

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_field);
        context = getBaseContext();
        dbUtilities = new DBUtilities(context);

        //добавляем actionBar (стрелка сверху слева)
        actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        idAuthUser = getIntent().getStringExtra("idAuthUser");

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
        swLocationCrFi = (Switch) findViewById(R.id.swLocationCrFi);
        etPhoneCrFiLayout = (TextInputLayout) findViewById(R.id.etPhoneCrFiLayout);

        //применяем регулярное выражения для правельности ввода номера телефона
        dbUtilities.inputFilterForPhoneNumber(etPhoneCrFi);

        etPhoneCrFi.setOnFocusChangeListener(this);

        //setEnabled - параметр для редактирования поля EditText
        etGeolatCrFi.setEnabled(false);
        etGeolongCrFi.setEnabled(false);

        //инициализация коллекции для спинера
        spListCity = new ArrayList<>();
        spListBoolean = new ArrayList<>();
        spListCoating = new ArrayList<>();

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

        //вешаем слушателя на изменение клавишы switch
        //(клавиша вкл./выкл. возможности редактирования полей с координатами геопозиции)
        if (swLocationCrFi != null) {
            swLocationCrFi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        //setEnabled - параметр для редактирования поля EditText
                        etGeolatCrFi.setEnabled(true);
                        etGeolongCrFi.setEnabled(true);
                    }else{
                        etGeolatCrFi.setEnabled(false);
                        etGeolongCrFi.setEnabled(false);
                    }//if-else
                }//onCheckedChanged
            });//setOnCheckedChangeListener
        }//if
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
            etGeolatCrFi.setText(String.valueOf(data.getDoubleExtra(PAR_LAT,0.)));
            etGeolongCrFi.setText(String.valueOf(data.getDoubleExtra(PAR_LONG,0.)));
        }
    }//onActivityResult

    //обработчик нажатия клавишы Создать запись пользователя
    public void createNewField() {
        if (etNameCrFi.getText().toString().equals("")
                || etPhoneCrFi.getText().toString().equals("")
                || etGeolongCrFi.getText().toString().equals("")
                || etGeolatCrFi.getText().toString().equals("")
                || etAddressCrFi.getText().toString().equals("")) {
            Toast.makeText(this, "Есть пустые поля!", Toast.LENGTH_SHORT).show();
        } else {
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
            String user_id = idAuthUser;

            //добваить данные через объект ContentValues(cv), в таблицу "field"
            dbUtilities.insertIntoFields(city_id, name, phone, light_status, coating_id,
                    shower_status, roof_status, geo_long, geo_lat, address, user_id);
            finish();
        }//if-else
    }//createNewField

    //вернутся в активность авторизации
    private void turnBack() {
        Intent intent = new Intent(this, AuthorizationActivity.class);
        startActivity(intent);
    }//turnBack

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnNewCityCrFi:           //выполнить операцию
                createNewCity();
                break;
            case R.id.btnCreateCrFi:            //выполнить операцию
                createNewField();
                break;
            case R.id.btnSelectGeoposCrFi:      //выполнить операцию
                geoPositionOnMap();
                break;
            case R.id.btnBackCrFi:              //отменить операцию
                turnBack();
                break;
        }//switch
    }//onClick

    private void geoPositionOnMap() {
        Intent intent = new Intent(this, FieldMapsActivity.class);
        intent.putExtra("mapStatus", 1);
        startActivityForResult(intent, REQ_SELECT_LOCATION);
    }//geoPositionOnMap

    //обработка нажатия клавиши создания нового города
    private void createNewCity() {
        Intent intent = new Intent(this, CreateCityActivity.class);
        startActivity(intent);
    }//createNewCity

    //проверка ввода номера телефона
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v != etPhoneCrFi && etPhoneCrFi.getText().toString().isEmpty()) {
            etPhoneCrFiLayout.setErrorEnabled(true);
            etPhoneCrFiLayout.setError(getResources().getString(R.string.error_enter_phone));
        } else {
            etPhoneCrFiLayout.setErrorEnabled(false);
        }
    }//onFocusChange
}//CreateFieldActivity
