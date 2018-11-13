package com.example.user.organizer;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class EditFieldActivity extends AppCompatActivity implements View.OnFocusChangeListener{
    // коды для идентификации активностей при получении результата
    public final int REQ_SELECT_LOCATION = 1002;

    //Параметр -- "ИМЯ КЛЮЧА"
    public static final String PAR_LAT = "latitude";
    public static final String PAR_LONG = "longitude";

    DBUtilities dbUtilities;
    Context context;
    List<String> spListCity;            // Данные для спинера выбора города
    List<String> spListCoating;         // Данные для спинера выбора покрытие

    String idAuthUser;      // id авторизированого пользователя
    String field_id;        // id редактируемого поля
    ActionBar actionBar;                //стрелка НАЗАД

    EditText etNameCrFi;
    EditText etGeolatCrFi;
    EditText etGeolongCrFi;

    EditText etAddressCrFi;
    EditText etPhoneCrFi;

    Spinner spCityCrFi;
    CheckBox cbLightCrFi;
    Spinner spCoatingCrFi;
    CheckBox cbShowerCrFi;
    CheckBox cbRoofCrFi;
    Switch swLocationCrFi;
    TextInputLayout etPhoneCrFiLayout;

    private ArrayAdapter<String> spAdapterCity;    //Адаптер для спинера выбор города
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

        field_id = getIntent().getStringExtra("field_id");
        idAuthUser = getIntent().getStringExtra("idAuthUser");

        List<Field> fieldDef = new ArrayList<>();
        fieldDef = dbUtilities.getListField(field_id,"");

        //привязка ресурсов к объектам
        etNameCrFi = (EditText) findViewById(R.id.etNameCrFi);
        etGeolatCrFi = (EditText) findViewById(R.id.etGeolatCrFi);
        etGeolongCrFi = (EditText) findViewById(R.id.etGeolongCrFi);
        etAddressCrFi = (EditText) findViewById(R.id.etAddressCrFi);
        etPhoneCrFi = (EditText) findViewById(R.id.etPhoneCrFi);
        spCityCrFi = (Spinner) findViewById(R.id.spCityCrFi);
        cbLightCrFi = (CheckBox) findViewById(R.id.cbLightCrFi);
        spCoatingCrFi = (Spinner) findViewById(R.id.spCoatingCrFi);
        cbShowerCrFi = (CheckBox) findViewById(R.id.cbShowerCrFi);
        cbRoofCrFi = (CheckBox) findViewById(R.id.cbRoofCrFi);
        swLocationCrFi = (Switch) findViewById(R.id.swLocationCrFi);
        etPhoneCrFiLayout = (TextInputLayout) findViewById(R.id.etPhoneCrFiLayout);

        //применяем регулярное выражения для правельности ввода номера телефона
        dbUtilities.inputFilterForPhoneNumber(etPhoneCrFi);
        //для появления и исчезновения первой скобки при наборе телефоного номера
        //при наведениии фокуса на поле ввода номера
        etPhoneCrFi.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus&&(etPhoneCrFi.length()==0||etPhoneCrFi.length()==1))etPhoneCrFi.setText("");
                if(hasFocus&&etPhoneCrFi.length()==0)etPhoneCrFi.setText("(");
            }
        });

        //подготовка данных для редактирования
        etNameCrFi.setText(fieldDef.get(0).name);
        etGeolatCrFi.setText(fieldDef.get(0).geo_lat);
        etGeolongCrFi.setText(fieldDef.get(0).geo_long);
        etAddressCrFi.setText(fieldDef.get(0).address);
        etPhoneCrFi.setText(fieldDef.get(0).phone);

        //setEnabled - параметр для редактирования поля EditText
        etGeolatCrFi.setEnabled(false);
        etGeolongCrFi.setEnabled(false);

        //инициализация коллекции для спинера
        spListCity = new ArrayList<>();
        spListCoating = new ArrayList<>();

        //заполнить spListCity данные для отображения в Spinner
        spListCity = dbUtilities.getStrListTableFromDB("cities", "name");

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
                || etPhoneCrFi.length()<14
                || etGeolongCrFi.getText().toString().equals("")
                || etGeolatCrFi.getText().toString().equals("")
                || etAddressCrFi.getText().toString().equals("")) {
            Toast.makeText(this, "Ошибка или пустые поля!", Toast.LENGTH_SHORT).show();
        } else {
            String city_id = String.valueOf(spListCity.indexOf(spCityCrFi.getSelectedItem()) + 1);
            String name = etNameCrFi.getText().toString();
            String phone = etPhoneCrFi.getText().toString();
            String light_status = (cbLightCrFi.isChecked()) ? "1" : "0";
            String coating_id = String.valueOf(spListCoating.indexOf(spCoatingCrFi.getSelectedItem()) + 1);
            String shower_status = (cbShowerCrFi.isChecked()) ? "1" : "0";
            String roof_status = (cbRoofCrFi.isChecked()) ? "1" : "0";
            String geo_long = etGeolongCrFi.getText().toString();
            String geo_lat = etGeolatCrFi.getText().toString();
            String address = etAddressCrFi.getText().toString();
            String user_id = idAuthUser;

            //добваить данные через объект ContentValues(cv), в таблицу "fields"
            dbUtilities.updateFieldsTable(field_id, city_id, name, phone, light_status, coating_id,
                    shower_status, roof_status, geo_long, geo_lat, address, user_id);

            finish();
        }
    }//createNewField

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCreateCrFi:            //выполнить операцию
                createNewField();
                break;
            case R.id.btnSelectGeoposCrFi:      //выполнить операцию
                geoPositionOnMap();
                break;
        }//switch
    }//onClick

    private void geoPositionOnMap() {
        Intent intent = new Intent(this, FieldMapsActivity.class);
        intent.putExtra("mapStatus", 1);
        startActivityForResult(intent, REQ_SELECT_LOCATION);
    }//geoPositionOnMap

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
}//EditFieldActivity
