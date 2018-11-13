package com.example.user.organizer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.example.user.organizer.fragment.SelectLogoDialog;
import com.example.user.organizer.inteface.SelectLogoInterface;

//---------------Активность для выбора логотипа--------------------------
public class SelectLogoActivity extends AppCompatActivity implements SelectLogoInterface {
    ActionBar actionBar;                //стрелка НАЗАД
    DBUtilities dbUtilities;
    Context context;

    RecyclerView rvUserSeLoAc;      //RecyclerView для пользователей
    String idAuthUser;                 //авторизированный пользователь

//    private static final int REQUEST_POS = 1;

    // адаптер для отображения recyclerView
    SelectLogoRecyclerAdapter selectLogoRecyclerAdapter;

    //параметр для вызова диалога "selectLogo"
    final String ID_SELECT_LOGO_DIALOG = "callDialogSelectLogo";

    SelectLogoDialog selectLogoDialog =
            new SelectLogoDialog(); // диалог подтверждения выбора логотипа

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_logo);

        //добавляем actionBar (стрелка сверху слева)
        actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        context = getBaseContext();
        dbUtilities = new DBUtilities(context);

        // прочитать данные, переданные из активности (из точки вызова)
        idAuthUser = getIntent().getStringExtra("idAuthUser");

        // создаем адаптер, передаем в него курсор
        selectLogoRecyclerAdapter
                = new SelectLogoRecyclerAdapter(this, context, idAuthUser);

        // RecycerView для отображения таблицы users БД
        rvUserSeLoAc = (RecyclerView) findViewById(R.id.rvUserSeLoAc);

        rvUserSeLoAc.setAdapter(selectLogoRecyclerAdapter);

    }//onCreate

    //обработчик actionBar (стрелка сверху слева)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
//                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }//switch
    }//onOptionsItemSelected

//    // точка выхода из DialogFragment при положительных
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == Activity.RESULT_OK) {
//            switch (requestCode) {
//                case REQUEST_POS:
//                    finish();
//                    break;
//            }//switch
//        }//if
//    }//onActivityResult

    @Override
    public void callDialogSelectLogo(Context context, String logo, String userId) {
        Bundle args = new Bundle();    // объект для передачи параметров в диалог
        args.putString("logo", logo);
        args.putString("user_id", idAuthUser);
        selectLogoDialog.setArguments(args);

        // Точка вызова отображение диалогового окна
        selectLogoDialog.show( getSupportFragmentManager(), ID_SELECT_LOGO_DIALOG);
    }//callDialogSelectLogo

}//SelectLogoActivity
