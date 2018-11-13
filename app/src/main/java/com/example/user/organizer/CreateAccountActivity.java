package com.example.user.organizer;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
    List<String> spArrayCity;            // Данные для спинера

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

        spArrayCity = new ArrayList<>();

        context = getBaseContext();
        dbUtilities = new DBUtilities(context);
        dbUtilities.open();

        // получаем данные из БД в виде курсора (название существующих городов)
        String query = "SELECT name FROM city;";
        creAccCursor =  dbUtilities.getDb().rawQuery(query, null);

        //заполнить spArrayCity данные отображаемые Spinner
        fillList();

        //создание адаптера для спинера
        spAdapterCity = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                spArrayCity
        );

        // назначение адапетра для списка
        spAdapterCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spDefCityCrAcAc.setAdapter(spAdapterCity);
    }//onCreate

    //заполнить ListCity данные отображаемые Spinner
    private void fillList() {
        int n = creAccCursor.getCount();        //количество данных в курсоре
       // spArrayCity = new String[n];

        for (int i = 0; i < n; i++) {
            creAccCursor.moveToPosition(i); // переходим в курсоре на текущую позицию
            spArrayCity.add(creAccCursor.getString(0));
        }//for
    }//fillList

    //обработчик неажатия клавишы Создать запись пользователя
    public void createNewAccount() {

        ContentValues cv = new ContentValues();
        cv.put("login", etLoginCrAcAc.getText().toString());
        cv.put("password", etPasswordCrAcAc.getText().toString());
        cv.put("name", etNameCrAcAc.getText().toString());
        cv.put("phone_number", etPhoneCrAcAc.getText().toString());
        cv.put("def_city", spArrayCity.indexOf(spDefCityCrAcAc.getSelectedItem()) + 1);
        cv.put("email", etEmailCrAcAc.getText().toString());
        dbUtilities.addNewUser(cv);

        String query = "SELECT * FROM user;";
        creAccCursor =  dbUtilities.getDb().rawQuery(query, null);
        Log.d("myLog", String.format("%d", creAccCursor.getCount()));

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
