package com.example.user.organizer;


import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

//-------Главная Активность----------------
public class MainActivity extends AppCompatActivity {

    //глобальная переменная для проверки введенных символов в EditText
    final public static String FILTER_STR = "[ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz" +
            "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюя" +
            "123456789_]*";
    DBUtilities dbUtilities;
    DBLocalUtilities dbLocalUtilities;
    //кнопка проверки соединения
    Button btnCheckConMaAc;
    //кнопка авторизации
    Button btnAuthorizationMaAc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //если из активности NavigationDrawerLogInActivity
        //в параметрах было переданно завершение программы
        if (getIntent().getBooleanExtra("finish", false)) finish();
        setContentView(R.layout.activity_main);

        //https://stackoverflow.com/questions/8438943/how-to-find-view-from-string-instead-of-r-id
        //определение ресурса через стринг и привязка к разметке
        Resources res = getResources();
        int idRes = res.getIdentifier("btnAuthorizationMaAc", "id", this.getPackageName());
        btnAuthorizationMaAc = (Button) findViewById(idRes);

        btnCheckConMaAc = (Button) findViewById(R.id.btnCheckConMaAc);
        dbUtilities = new DBUtilities(this);
        dbLocalUtilities = new DBLocalUtilities(this);
        dbLocalUtilities.open();

        if (!dbUtilities.isConnection()) {
            btnCheckConMaAc.setVisibility(View.VISIBLE);
            btnAuthorizationMaAc.setVisibility(View.GONE);
            Toast.makeText(this, "Проблема с подключением!", Toast.LENGTH_LONG).show();
        }else{
            btnCheckConMaAc.setVisibility(View.GONE);
            btnAuthorizationMaAc.setVisibility(View.VISIBLE);
//            reserveFieldData();
        }//if-else
    }//onCreate

    //резервируем данные о футбольных полях
    private void reserveFieldData() {
        //получаем коллекцию пользователей
        List<User> userList = dbUtilities.getListUser("");
        //получаем коллекцию городов
        List<City> cityList = dbUtilities.getListCity("");
        //получаем коллекцию покрытий
        List<Coating> coatingList = dbUtilities.getListCoating("");
        //получаем коллекцию полей
        List<Field> fieldList = dbUtilities.getListField("","");

        for (User user : userList) {
//            dbLocalUtilities.getUser(cv, "users");
            ContentValues cv = new ContentValues();
            cv.put("id",user.getId());
            cv.put("name", user.getName());

            //добваить данные через объект ContentValues(cv), в таблицу "users"
            dbLocalUtilities.insertInto(cv, "users");
        }//for "users"

        for (City city : cityList) {
            ContentValues cv = new ContentValues();
            cv.put("id",city.getId());
            cv.put("name", city.getName());

            //добваить данные через объект ContentValues(cv), в таблицу "cities"
            dbLocalUtilities.insertInto(cv, "cities");
        }//for "cities"

        for (Coating coating : coatingList) {
            ContentValues cv = new ContentValues();
            cv.put("id",coating.getId());
            cv.put("name", coating.getType());

            //добваить данные через объект ContentValues(cv), в таблицу "coatings"
            dbLocalUtilities.insertInto(cv, "coatings");
        }//for "coatings"

        for (Field field : fieldList) {
            ContentValues cv = new ContentValues();
            cv.put("id",field.getId());
            cv.put("city_id", field.getCity_id());
            cv.put("name", field.getName());
            cv.put("phone", field.getPhone());
            cv.put("light_status", field.getLight_status());
            cv.put("coating_id", field.getCoating_id());
            cv.put("shower_status", field.getShower_status());
            cv.put("roof_status", field.getRoof_status());
            cv.put("geo_long", field.getGeo_long());
            cv.put("geo_lat", field.getGeo_lat());
            cv.put("address", field.getAddress());
            cv.put("user_id", field.getUser_id());

            //добваить данные через объект ContentValues(cv), в таблицу "coatings"
            dbLocalUtilities.insertInto(cv, "fields");
        }//for "fields"
    }//reserveFieldData

    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnCheckConMaAc:
                checkConnection(view);
                break;

            case R.id.btnAuthorizationMaAc:
                authorization(view);
                break;

            case R.id.btnLocalDbFieldMaAc:
                showFieldCatalog();
                break;

            case R.id.btnAboutUs:
                aboutUsFragment();
                break;

            case R.id.btnExitMaAc:
                finish();
                break;
        }//switch

    }//onClick

    //показать фрагмент О нас
    private void aboutUsFragment() {
        Intent intent = new Intent(this, ShowSomeFragmentBeforeAuthActivity.class);
        intent.putExtra("fragment","about");
        startActivity(intent);
    }//aboutUsFragment

    //показать каталог полей
    private void showFieldCatalog() {
        Intent intent = new Intent(this, ShowSomeFragmentBeforeAuthActivity.class);
        intent.putExtra("fragment","field");
        startActivity(intent);
    }//showFieldCatalog

    //процедура авторизации
    private void authorization(View view) {
        if (!dbUtilities.isConnection()) {
            btnCheckConMaAc.setVisibility(View.VISIBLE);
            btnAuthorizationMaAc.setVisibility(View.GONE);
            Snackbar.make(view, "\t\t\t\t\t\t\t\t\t\tБАЗА ДАННЫХ НЕ ОТВЕЧАЕТ!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }else{
            btnCheckConMaAc.setVisibility(View.GONE);
            btnAuthorizationMaAc.setVisibility(View.VISIBLE);
            reserveFieldData();
            Intent intent = new Intent(this, AuthorizationActivity.class);
            startActivity(intent);
        }//if-else
    }//authorization

    //процедура проверки соединения с интернетов
    private void checkConnection(View view) {
        if (!dbUtilities.isConnection()) {
            Snackbar.make(view, "\t\t\t\t\t\t\t\t\t\tБАЗА ДАННЫХ НЕ ОТВЕЧАЕТ!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }else{
            btnCheckConMaAc.setVisibility(View.GONE);
            btnAuthorizationMaAc.setVisibility(View.VISIBLE);
            reserveFieldData();
        }//if-else
    }//checkConnection

    //Откючаем кнопку "Назад"(переопределив процедуру вызова)
    @Override
    public void onBackPressed(){}
}//MainActivity
