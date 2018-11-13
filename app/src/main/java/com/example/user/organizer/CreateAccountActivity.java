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

import java.util.ArrayList;
import java.util.List;

//---------Активность для создания записи нового пользователя ---------------------------

public class CreateAccountActivity extends AppCompatActivity {

    DBUtilities dbUtilities;
    Cursor creAccCursor;                // курсор для чтения данных из БД
    Context context;
    List<String> spListCity;            // Данные для спинера выбора города

    EditText etNameCrAcAc;
    EditText etLoginCrAcAc;
    EditText etPasswordCrAcAc;
    EditText etConfirmPasswordCrAcAc;
    EditText etEmailCrAcAc;
    EditText etPhoneCrAcAc;
    Spinner spDefCityCrAcAc;                     //объект спинер
    private ArrayAdapter<String> spAdapterCity;  //Адаптер для спинера

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        //привязка ресурсов к объектам
        etNameCrAcAc = (EditText) findViewById(R.id.etNameCrAcAc);
        etLoginCrAcAc = (EditText) findViewById(R.id.etLoginCrAcAc);
        etPasswordCrAcAc = (EditText) findViewById(R.id.etPasswordCrAcAc);
        etConfirmPasswordCrAcAc = (EditText) findViewById(R.id.etConfirmPasswordCrAcAc);
        etEmailCrAcAc = (EditText) findViewById(R.id.etEmailCrAcAc);
        etPhoneCrAcAc = (EditText) findViewById(R.id.etPhoneCrAcAc);
        spDefCityCrAcAc = (Spinner) findViewById(R.id.spDefCityCrAcAc);

        //инициализация коллекции для спинера
        spListCity = new ArrayList<>();

        context = getBaseContext();
        dbUtilities = new DBUtilities(context);
        dbUtilities.open();

        //запрос для получения курсор с данными
        String query = "SELECT name FROM city;";

        //заполнить spListCity данные для отображения в Spinner
        spListCity = dbUtilities.fillList(query);

        //создание адаптера для спинера
        spAdapterCity = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                spListCity
        );

        // назначение адапетра для списка
        spAdapterCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spDefCityCrAcAc.setAdapter(spAdapterCity);
    }//onCreate

    //обработчик неажатия клавишы Создать запись пользователя
    public void createNewAccount() {

        ContentValues cv = new ContentValues();
        cv.put("login", etLoginCrAcAc.getText().toString());
        cv.put("password", etPasswordCrAcAc.getText().toString());
        cv.put("name", etNameCrAcAc.getText().toString());
        cv.put("phone_number", etPhoneCrAcAc.getText().toString());
        cv.put("def_city", spListCity.indexOf(spDefCityCrAcAc.getSelectedItem()) + 1);
        cv.put("email", etEmailCrAcAc.getText().toString());

        //добваить данные через объект ContentValues(cv), в таблицу "user"
        dbUtilities.insertInto(cv, "user");

        //переходин в актиность LoginPartActivity
        Intent intent = new Intent(this, LoginPartActivity.class);
        startActivity(intent);
    }//CreateNewAccount

    //вернутся в активность авторизации
    private void turnBack() {
        Intent intent = new Intent(this, LogoutPartActivity.class);
        startActivity(intent);
    }//turnBack

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCreateCrAcAc:            //выполнить операцию
                createNewAccount();
                break;
            case R.id.btnBackCrAcAc:               //отменить операцию
                turnBack();
                break;
        }//switch
        finish();
    }//onClick

}//CreateAccountActivity
