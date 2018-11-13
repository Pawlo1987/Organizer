package com.example.user.organizer;

import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.user.organizer.fragment.AdvertisingAndInformationFragment;
import com.example.user.organizer.fragment.ExitConfirmDialog;
import com.example.user.organizer.fragment.ShowAllEventsFragment;
import com.example.user.organizer.fragment.ShowAuthUserEventsFragment;
import com.example.user.organizer.inteface.CustomInterface;

public class NavigationDrawerLogInActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, CustomInterface{

    DBUtilities dbUtilities;

    final String ID_EXIT_DIALOG = "dialogExitConfirm";          //пааметр для вызова диалога "выход"
    private ExitConfirmDialog exitConfirmDialog; // диалог подтверждения выхода из приложения

    ShowAllEventsFragment showAllEventsFragment;
    ShowAuthUserEventsFragment showAuthUserEventsFragment;
    AdvertisingAndInformationFragment advertisingAndInformationFragment;
    FragmentTransaction fTrans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer_log_in);

        dbUtilities = new DBUtilities(getBaseContext());
        dbUtilities.open();

        exitConfirmDialog = new ExitConfirmDialog();
        showAllEventsFragment = new ShowAllEventsFragment();
        showAuthUserEventsFragment = new ShowAuthUserEventsFragment();
        advertisingAndInformationFragment = new AdvertisingAndInformationFragment();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        fTrans = getFragmentManager().beginTransaction();
        if (id == R.id.create_event) {
            Intent intent= new Intent(this, CreateEventActivity.class);
            startActivity(intent);
        } else if (id == R.id.show_user_events) {
            fTrans.replace(R.id.container, showAuthUserEventsFragment);
        } else if (id == R.id.show_all_events) {
            fTrans.replace(R.id.container, showAllEventsFragment);
        } else if (id == R.id.news_line) {
            fTrans.replace(R.id.container, advertisingAndInformationFragment);
        } else if (id == R.id.log_out) {
            Bundle args = new Bundle();    // объект для передачи параметров в диалог
            args.putBoolean("flag", true);
            exitConfirmDialog.setArguments(args);
            // отображение диалогового окна
            exitConfirmDialog.show(getSupportFragmentManager(), ID_EXIT_DIALOG);
        } else if (id == R.id.exit_app) {
            Bundle args = new Bundle();    // объект для передачи параметров в диалог
            args.putBoolean("flag", false);
            exitConfirmDialog.setArguments(args);
            // отображение диалогового окна
            exitConfirmDialog.show(getSupportFragmentManager(), ID_EXIT_DIALOG);
        }
        fTrans.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Завершить работу приложение
    public void closeApp() {
        moveTaskToBack(true);
        finish();
        System.runFinalizersOnExit(true);
        System.exit(0);
    }//closeApp

    //Добавление записи в таб.Participants для участия в событии
    @Override
    public void insertIntoParticipants(int event_id, int user_id) {
        ContentValues cv = new ContentValues();
        cv.put("event_id", event_id);
        cv.put("user_id", user_id);
        //добваить данные через объект ContentValues(cv), в таблицу "participants"
        dbUtilities.insertInto(cv, "participants");
    }

    //Удаление записи из таб.participants для того чтоб не участвовать в событии
    @Override
    public void leaveFromEvent(int event_id, int user_id) {
        Cursor cursor;
        String query = "SELECT participants._id FROM participants " +
                "WHERE participants.event_id = "
                + event_id + " AND participants.user_id = "
                + user_id + ";";
        cursor = dbUtilities.getDb().rawQuery(query, null);
        cursor.moveToPosition(0); // переходим в курсоре в нулевую позицию
        //добваить данные через объект ContentValues(cv), в таблицу "participants"
        dbUtilities.deleteRowById("participants", cursor.getInt(0));
    }//leaveFromEvent

    //Удаление записи из таб.events для удаления событии
    @Override
    public void deleteEvent(int event_id) {
        dbUtilities.deleteRowById("events", event_id);
    }//deleteEvent
}
