package com.example.user.organizer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.user.organizer.fragment.AdvertisingAndInformationFragment;
import com.example.user.organizer.fragment.ChangeColumnDialog;
import com.example.user.organizer.fragment.DeleteNoteNewsDialog;
import com.example.user.organizer.fragment.DeleteProfileDialog;
import com.example.user.organizer.inteface.AdvertisingAndInformInterface;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class EditNewsNoteActivity extends AppCompatActivity implements AdvertisingAndInformInterface {

    // коды для идентификации активностей при получении результата
    public final int REQ_SELECT_LOGO = 1001;

    //Параметр -- "ИМЯ КЛЮЧА"
    public static final String PAR_LOGO = "logo";

    //параметр для вызова диалога "deleteNoteNews"
    final String ID_DELETE_NOTE_NEWS = "deleteNoteNews";

    DeleteNoteNewsDialog deleteNoteNewsDialog =
            new DeleteNoteNewsDialog(); // диалог подтверждения изменения данных

    DBUtilities dbUtilities;
    Spinner spCityEdNeNoAc;
    Spinner spSize;
    Spinner spStyle;

    String logo = "1";
    Calendar calendar = Calendar.getInstance();      // объект для работы с датой и временем
    String date;                                 // назначения дата записи
    ActionBar actionBar;                //стрелка НАЗАД

    TextView tvDateEdNeNoAc;            // TextView для вывода даты
    EditText etHeadEdNeNoAc;            // EditText для заголовока
    EditText etMessEdNeNoAc;            // EditText для новой записи

    Context context;
    String idAuthUser;
    Note noteOld;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_news_note);

        idAuthUser = getIntent().getStringExtra("idAuthUser");
        noteOld = getIntent().getParcelableExtra("note");
        logo = noteOld.getNoteLogo();

        new DownloadImageTask( findViewById(R.id.ivLogoEdNeNoAc))
                .execute("http://strahovanie.dn.ua/football_db/logo/logo_" + logo + ".png");

        //добавляем actionBar (стрелка сверху слева)
        actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        context = getBaseContext();
        dbUtilities = new DBUtilities(context);

        tvDateEdNeNoAc = (TextView) findViewById(R.id.tvDateEdNeNoAc);
        tvDateEdNeNoAc.setText(noteOld.getNoteDate());
        etHeadEdNeNoAc = (EditText) findViewById(R.id.etHeadEdNeNoAc);
        etHeadEdNeNoAc.setText(noteOld.getNoteHead());
        etMessEdNeNoAc = (EditText) findViewById(R.id.etMessEdNeNoAc);
        etMessEdNeNoAc.setText(noteOld.getNoteMessage());
        spCityEdNeNoAc = (Spinner) findViewById(R.id.spCityEdNeNoAc);
        spSize = (Spinner) findViewById(R.id.spSize);
        spStyle = (Spinner) findViewById(R.id.spStyle);

        setInitialDate();           //формирование даты для записи

        buildCitySpinner();     //строим Spinner City

        //слушатель для спинера выбора размера шрифта
        spSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: etMessEdNeNoAc.setTextSize(14); break;
                    case 1: etMessEdNeNoAc.setTextSize(16); break;
                    case 2: etMessEdNeNoAc.setTextSize(18); break;
                    case 3: etMessEdNeNoAc.setTextSize(20); break;
                }//switch
            }//onItemSelected
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }//onNothingSelected
        });

        //слушатель для спинера выбора типа шрифта
        spStyle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: etMessEdNeNoAc.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL)); break;
                    case 1: etMessEdNeNoAc.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD)); break;
                    case 2: etMessEdNeNoAc.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC)); break;
                    case 3: etMessEdNeNoAc.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC)); break;
                }//switch
            }//onItemSelected
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }//onNothingSelected
        });

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

    //строим Spinner City
    private void buildCitySpinner() {
        //строим спинер для городов
        List<String> spListCity = new ArrayList<>();    // Данные для спинера выбора города
        //заполнить spListCity данные для отображения в Spinner
        //обращаемся к базе для получения списка имен городов
        spListCity = dbUtilities.getStrListTableFromDB("cities", "name");
        ArrayAdapter<String> spinnerAdapter;
        //создание адаптера для спинера
        spinnerAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                spListCity
        );
        // назначение адапетра для списка
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCityEdNeNoAc.setAdapter(spinnerAdapter);
        spCityEdNeNoAc.setSelection(Integer.parseInt(dbUtilities.getIdByValue("cities",
                "name", noteOld.getNoteCityName()))-1);

        //строим спинер для размера шрифта
        spinnerAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                new ArrayList<>( Arrays.asList(new String[]{"14", "16", "18", "20"}))
        );
        // назначение адапетра для списка
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSize.setAdapter(spinnerAdapter);
        spSize.setSelection(Integer.parseInt(noteOld.getNoteTextSizeMessage()));

        //строим спинер для типа шрифта
        spinnerAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                new ArrayList<>( Arrays.asList(new String[]{"Н", "Ж", "К", "ЖК"}))
        );
        // назначение адапетра для списка
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spStyle.setAdapter(spinnerAdapter);
        spStyle.setSelection(Integer.parseInt(noteOld.getNoteTextStyleMessage()));
    }//buildCitySpinner

    //-----------------------Метод для приема результатов из активностей----------------------------
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_CANCELED) {
        }//RESULT_CANCELED

        // обработка результатов по активностям
        if (resultCode == RESULT_OK) {
            logo = data.getStringExtra(PAR_LOGO);
            // Показать картинку
            new DownloadImageTask( findViewById(R.id.ivLogoEdNeNoAc))
                    .execute("http://strahovanie.dn.ua/football_db/logo/logo_" + logo + ".png");
        }//RESULT_OK
    }//onActivityResult

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnDeleteEdNeNoAc:            //кнопка удаления поста
                callDialogDeleteNoteNews(context);
                break;
            case R.id.btnSelectLogoEdNeNoAc:         //кнопка выбора логотипа
                Intent intent = new Intent(context, SelectLogoActivity.class);
                intent.putExtra("idAuthUser", idAuthUser);
                intent.putExtra("table", "notes");    //для указания в какой таблице менять логотип
                startActivityForResult(intent, REQ_SELECT_LOGO);
                break;
            case R.id.btnConfirmEdNeNoAc:            //кнопка подтвердить выбор
                confirmSelect();
                break;
            case R.id.btnCancelEdNeNoAc:             //кнопка отменить выбор
                cancelSelect();
                break;
        }//switch
    }//onClick

    //возврат к предедыщему меню
    private void cancelSelect() {
        setResult(RESULT_CANCELED);
        finish();
    }//cancelSelect

    //сохранить и передать выборанных игроков
    private void confirmSelect() {
        //обращяемся к БД на сервер для создания новой записи в таблицу notes
        dbUtilities.updateNotesTable(noteOld.getNoteId(), logo,
                etHeadEdNeNoAc.getText().toString(), idAuthUser, date,
                dbUtilities.getIdByValue(
                        "cities",              //название таблицы
                        "name",               //название столбца
                        spCityEdNeNoAc.getSelectedItem().toString() //значение для поиска
                ),
                etMessEdNeNoAc.getText().toString(),
                String.valueOf(spSize.getSelectedItemPosition()),
                String.valueOf(spStyle.getSelectedItemPosition())
        );

        finish();
    }//confirmSelect

    // установка даты записи
    private void setInitialDate() {
        //задаем дату в нужном формате для БД
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        date = simpleDateFormat.format(calendar.getTimeInMillis());

        //установка текста в TextView
        tvDateEdNeNoAc.setText(date);
    } // setInitialDateTim

    @Override
    public void callDialogDeleteNoteNews(Context context) {
        Bundle args = new Bundle();    // объект для передачи параметров в диалог
        args.putString("noteId", noteOld.getNoteId());
        deleteNoteNewsDialog.setArguments(args);

        // Точка вызова отображение диалогового окна
        deleteNoteNewsDialog.show( getFragmentManager(), ID_DELETE_NOTE_NEWS);

    }//callDialogDeleteProfile
}//EditNewsNoteActivity
