package com.example.user.organizer;


import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

import com.example.user.organizer.fragment.AdvertisingAndInformationFragment;

//-------Главная Активность----------------
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, AuthorizationActivity.class);
        startActivity(intent);

    }//onCreate

    //Завершить приложение
    public void finishApp(){
        finish();
    }

}//MainActivity
