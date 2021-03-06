package com.example.user.organizer;

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
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//---------Активность для создания записи нового пользователя ---------------------------
public class CreateAccountActivity extends AppCompatActivity{

    DBUtilities dbUtilities;
    Context context;
    List<String> spListCity;            // Данные для спинера выбора города
    ActionBar actionBar;                //стрелка НАЗАД

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

        context = getBaseContext();
        dbUtilities = new DBUtilities(context);

        //добавляем actionBar (стрелка сверху слева)
        actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        //привязка ресурсов к объектам
        etNameCrAcAc = (EditText) findViewById(R.id.etNameCrAcAc);
        etLoginCrAcAc = (EditText) findViewById(R.id.etLoginCrAcAc);
        etPasswordCrAcAc = (EditText) findViewById(R.id.etPasswordCrAcAc);
        etConfirmPasswordCrAcAc = (EditText) findViewById(R.id.etConfirmPasswordCrAcAc);
        etEmailCrAcAc = (EditText) findViewById(R.id.etEmailCrAcAc);
        etPhoneCrAcAc = (EditText) findViewById(R.id.etPhoneCrAcAc);
        spDefCityCrAcAc = (Spinner) findViewById(R.id.spDefCityCrAcAc);

        //фильтр для пароля
        etPasswordCrAcAc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                dbUtilities.inputFilterForListener(etPasswordCrAcAc,
                        10,
                        MainActivity.FILTER_STR
                );
            }//afterTextChanged
        });

        //фильтр для пароля
        etConfirmPasswordCrAcAc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                dbUtilities.inputFilterForListener(etConfirmPasswordCrAcAc,
                        10,
                        MainActivity.FILTER_STR
                );
            }//afterTextChanged
        });

        //применяем регулярное выражения для правельности e-mail
        dbUtilities.inputFilterForEmail(etEmailCrAcAc);

        //применяем регулярное выражения для правельности ввода номера телефона
        dbUtilities.inputFilterForPhoneNumber(etPhoneCrAcAc);
        //для появления и исчезновения первой скобки при наборе телефоного номера
        //при наведениии фокуса на поле ввода номера
        etPhoneCrAcAc.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus&&(etPhoneCrAcAc.length()==0||etPhoneCrAcAc.length()==1))etPhoneCrAcAc.setText("");
                if(hasFocus&&etPhoneCrAcAc.length()==0)etPhoneCrAcAc.setText("(");
            }
        });

        etPasswordCrAcAc.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(etPasswordCrAcAc.getText().toString().equals(
                        etConfirmPasswordCrAcAc.getText().toString())){
                    etPasswordCrAcAc.setTextColor(Color.BLACK);
                    etConfirmPasswordCrAcAc.setTextColor(Color.BLACK);
                }else{
                    etPasswordCrAcAc.setTextColor(Color.RED);
                    etConfirmPasswordCrAcAc.setTextColor(Color.RED);
                }
            }
        });
        etConfirmPasswordCrAcAc.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(etPasswordCrAcAc.getText().toString().equals(
                        etConfirmPasswordCrAcAc.getText().toString())){
                    etPasswordCrAcAc.setTextColor(Color.BLACK);
                    etConfirmPasswordCrAcAc.setTextColor(Color.BLACK);
                }else{
                    etPasswordCrAcAc.setTextColor(Color.RED);
                    etConfirmPasswordCrAcAc.setTextColor(Color.RED);
                }
            }
        });

        //инициализация коллекции для спинера
        spListCity = new ArrayList<>();

        //получаем список городов для спинера
        spListCity = dbUtilities.getStrListTableFromDB("cities","name");

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

    //обработчик нажатия клавишы Создать запись пользователя
    public void createNewAccount() {
        //проверка корректности введенного email
        boolean testEmailCorrect = dbUtilities.inputCorrectEmail(etEmailCrAcAc);
        //проверка пустых полей
        if (etNameCrAcAc.getText().toString().equals("")
                || !testEmailCorrect
                || etLoginCrAcAc.getText().toString().equals("")
                || etPasswordCrAcAc.getText().toString().equals("")
                || etConfirmPasswordCrAcAc.getText().toString().equals("")
                || etEmailCrAcAc.getText().toString().equals("")
                || etPhoneCrAcAc.length()<14) {
            Toast.makeText(this, "Ошибка или пустые поля!", Toast.LENGTH_SHORT).show();
        }else if (!etPasswordCrAcAc.getText().toString().equals(etConfirmPasswordCrAcAc.getText().toString())) {
            Toast.makeText(this, "Проверьте поля с паролями!", Toast.LENGTH_SHORT).show();
        } else if(!dbUtilities.getIdByValue("users", "login",
                etLoginCrAcAc.getText().toString()).equals("")){
            Toast.makeText(this, "Такой логин существует!", Toast.LENGTH_SHORT).show();
        }else if(!dbUtilities.getIdByValue("users", "email",
                etEmailCrAcAc.getText().toString()).equals("")){
            Toast.makeText(this, "Такой e-mail существует!", Toast.LENGTH_SHORT).show();
        }else{
            String login = etLoginCrAcAc.getText().toString();
            String password = etPasswordCrAcAc.getText().toString();
            String name = etNameCrAcAc.getText().toString();
            String phone = etPhoneCrAcAc.getText().toString();
            String city_id = String.valueOf(spListCity.indexOf(spDefCityCrAcAc.getSelectedItem()) + 1);
            String email = etEmailCrAcAc.getText().toString();
            String logo = "22";

            //обращяемся к базе данных на сервер для создания новой записи в таблицу user
            dbUtilities.insertIntoUsers(login, password, name, phone, city_id, email, logo);
            finish();
        }//if-if-else
    }//CreateNewAccount


    public void onClick(View view) {
                createNewAccount();
    }//onClick

}//CreateAccountActivity
