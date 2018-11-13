package com.example.user.organizer;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//---------Активность для создания записи нового пользователя ---------------------------

public class CreateAccountActivity extends AppCompatActivity {

    DBUtilities dbUtilities;
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

        //обращаемся к базе для получения списка имен городов
        try(BackgroundWorker bg = new BackgroundWorker()){
            bg.execute("getAllCities");

            String resultdb = bg.get();
            Log.d("FOOTBALL", resultdb);
            JSONObject jResult = new JSONObject(resultdb);

            if(jResult.getString("error").toString().equals("")){
                JSONObject jCities = jResult.getJSONObject("cities");
                Iterator<String> i = jCities.keys();

                while(i.hasNext()){
                    spListCity.add(jCities.getString(i.next()));
                }

                Log.d("FOOTBALL", spListCity.toString());
            }else{
                Toast.makeText(context, "ERROR", Toast.LENGTH_LONG).show();
            }

        }catch(Exception e){
            e.printStackTrace();
        }//try-catch

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

    //обработчик нажатия клавишы Создать запись пользователя
    public void createNewAccount() {
        String login = etLoginCrAcAc.getText().toString();
        String password = etPasswordCrAcAc.getText().toString();
        String name = etNameCrAcAc.getText().toString();
        String phone = etPhoneCrAcAc.getText().toString();
        String city_id = String.valueOf(spListCity.indexOf(spDefCityCrAcAc.getSelectedItem()) + 1);
        String email = etEmailCrAcAc.getText().toString();

        //обращяемся к базе данных на сервер для создания новой записи в таблицу user
        dbUtilities.insertIntoUsers( login, password, name, phone, city_id, email);

    }//CreateNewAccount

    //вернутся в активность авторизации
    private void turnBack() {
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
