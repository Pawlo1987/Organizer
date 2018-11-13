package com.example.user.organizer;

import android.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.user.organizer.fragment.AboutUsFragment;
import com.example.user.organizer.fragment.ShowFieldCatalogFragment;

//-----активность для вызова фрагмента без авторизации
public class ShowSomeFragmentBeforeAuthActivity extends AppCompatActivity {
    ShowFieldCatalogFragment showFieldCatalogFragment;
    AboutUsFragment aboutUsFragment;
    FragmentTransaction fTrans;
    ActionBar actionBar;                //стрелка НАЗАД

    DBUtilities dbUtilities;
    DBLocalUtilities dbLocalUtilities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_some_fragment_before_auth);
        showFieldCatalogFragment = new ShowFieldCatalogFragment();
        aboutUsFragment = new AboutUsFragment();

        String fragment = getIntent().getStringExtra("fragment");
        //добавляем actionBar (стрелка сверху слева)
        actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        dbUtilities = new DBUtilities(this);
        fTrans = getFragmentManager().beginTransaction();

        if (fragment.equals("field")) {
            // объект для передачи параметров в showFieldCatalogFragment
            // фрагмент showFieldCatalogFragment считыват данные с сервера
            // или с локальной БД
            Bundle args = new Bundle();
            //передаем данные об  авторизированном пользователе
            args.putString("idAuthUser", "");
            if (!dbUtilities.isConnection()) {
                args.putBoolean("connection", false);
            } else {
                args.putBoolean("connection", true);
            }//if-else

            showFieldCatalogFragment.setArguments(args);
            fTrans.replace(R.id.container_main_activity, showFieldCatalogFragment);
        }else{
            fTrans.replace(R.id.container_main_activity, aboutUsFragment);
        }
        fTrans.commit();
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
}//ShowSomeFragmentBeforeAuthActivity
