package com.example.user.organizer;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

//----------------Активность для создания нового города-------------------

public class CreateCityActivity extends AppCompatActivity {

    DBUtilities dbUtilities;
    Context context;
    List<String> spListRegion;            // Данные для спинера выбора региона
    ActionBar actionBar;                //стрелка НАЗАД

    EditText etCityNameCrCi;
    Spinner spRegionCrCi;                     //объект спинер
    private ArrayAdapter<String> spAdapterRegion;  //Адаптер для спинера

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_city);

        //добавляем actionBar (стрелка сверху слева)
        actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

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

    //обработчик неажатия клавишы Создать запись пользователя
    public void createNewCity() {
        if (etCityNameCrCi.getText().toString().equals("")) {
            Toast.makeText(this, "Есть пустые поля!", Toast.LENGTH_SHORT).show();
        } else {
            String name = etCityNameCrCi.getText().toString();
            String region_id = String.valueOf(spListRegion.indexOf(spRegionCrCi.getSelectedItem()) + 1);

            //метод для создания новой записи в определоной таблице БД
            dbUtilities.insertIntoCities(name, region_id);

            finish();
        }//if (etCityNameCrCi.getText().toString().equals(""))
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
    }//onClick
}//CreateCityActivity
