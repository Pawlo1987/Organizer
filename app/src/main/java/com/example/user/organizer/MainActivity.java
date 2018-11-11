package com.example.user.organizer;


import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;


public class MainActivity extends AppCompatActivity {

    final String ID_EXIT_DIALOG = "dialogExitConfirm";
    private ExitConfirmDialog exitDialog; // диалог подтверждения выхода из приложения

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Intent intent = new Intent(this, LoginPartActivity.class);
        startActivity(intent);

    }//onCreate

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);

    } // onCreateOptionsMenu

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case R.id.menuAuthorization:
//                fTrans.add(R.id.fragmentMA, fragCrEv);
                break;


            case R.id.menuFinish:
                exitDialog.show(getSupportFragmentManager(), ID_EXIT_DIALOG);
                return true;

        } // switch
        return false;
    } // onOptionsItemSelected

    // Перехватчик нажатия клавиши Назад для текущей активности - вызвать диалог выхода
    @Override public void onBackPressed() {
        // отображение диалогового окна
        exitDialog.show(getSupportFragmentManager(), ID_EXIT_DIALOG);
    } // onBackPressed


}//MainActivity
