package com.example.user.organizer;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.user.organizer.fragment.AdvertisingAndInformationFragment;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

//-------Главная Активность----------------
public class MainActivity extends AppCompatActivity {

    DBUtilities dbUtilities;
    //кнопка проверки соединения
    Button btnCheckConMaAc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnCheckConMaAc = (Button) findViewById(R.id.btnCheckConMaAc);
        btnCheckConMaAc.setEnabled(false);
        dbUtilities = new DBUtilities(this);

        if (!dbUtilities.isConnection()) {
            btnCheckConMaAc.setEnabled(true);
            Toast.makeText(this, "Проблема с подключением!", Toast.LENGTH_LONG).show();
        }else{
            Intent intent = new Intent(this, AuthorizationActivity.class);
            startActivity(intent);
        }//if-else

    }//onCreate

    //Завершить приложение
    public void finishApp(){
        finish();
    }

    public void onClick(View view) {
        if (!dbUtilities.isConnection()) {
//            Toast.makeText(this, "Проблема с подключением к Базе Данных!", Toast.LENGTH_LONG).show();
//            Snackbar.make(view, "\t\t\t\t\t\t\t\t\t\tБАЗА ДАННЫХ НЕ ОТВЕЧАЕТ!", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show();
        }else{
            btnCheckConMaAc.setEnabled(false);
            Intent intent = new Intent(this, AuthorizationActivity.class);
            startActivity(intent);
        }//if-else
    }//onClick
}//MainActivity
