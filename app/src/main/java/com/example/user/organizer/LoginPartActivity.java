package com.example.user.organizer;

import android.app.FragmentTransaction;
import android.app.TabActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TabHost;

//--Активность с инициализацией активных клавиш Tab Tools после авторизации-----

public class LoginPartActivity extends TabActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_part);

        // получаем TabHost
        TabHost tabHost = getTabHost();

        // инициализация была выполнена в getTabHost
        // метод setup вызывать не нужно

        TabHost.TabSpec tabSpec;

        tabSpec = tabHost.newTabSpec("tag1");
        tabSpec.setIndicator("Вкладка 1");
        tabSpec.setContent(new Intent(this, CreateEventActivity.class));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag2");
        tabSpec.setIndicator("Вкладка 2");
        tabSpec.setContent(new Intent(this, ShowAllEventActivity.class));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag3");
        tabSpec.setIndicator("Вкладка 3");
        tabSpec.setContent(new Intent(this, ShowAuthUserEventsActivity.class));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag4");
        tabSpec.setIndicator("Вкладка 4");
        tabSpec.setContent(new Intent(this, ShowAllEventActivity.class));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag5");
        tabSpec.setIndicator("Вкладка 5");
        tabSpec.setContent(new Intent(this, ExitActivity.class));
        tabHost.addTab(tabSpec);

    }
}
