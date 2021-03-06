package com.example.user.organizer.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.user.organizer.AdvertisingAndInformationRecyclerAdapter;
import com.example.user.organizer.CreateNewsNoteActivity;
import com.example.user.organizer.DBUtilities;
import com.example.user.organizer.Note;
import com.example.user.organizer.R;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

//---------------------- Фрагмент для вывода рекламной ленты -------------------

public class AdvertisingAndInformationFragment extends Fragment {

    // коды для идентификации активностей при получении результата
    public final int REQ_CREATE_NOTE = 1001;

    //Параметр -- "ИМЯ КЛЮЧА"
    public static final String PAR_LOGO = "logo";
    public static final String PAR_HEAD = "head";
    public static final String PAR_CITY = "city";
    public static final String PAR_DATE = "date";
    public static final String PAR_MESSAGE = "message";
    public static final String PAR_TSIZE_MESSAGE = "tsizemessage";
    public static final String PAR_TSTYLE_MESSAGE = "tstylemessage";

    RecyclerView rvNoteLineAdAnInAc;

    // адаптер для отображения recyclerView
    AdvertisingAndInformationRecyclerAdapter advertisingAndInformationRecyclerAdapter;
    DBUtilities dbUtilities;
    Spinner spCityAdAnInAc;         //основной спинер выбора города
    int spPos;                      //позиция спинера

    Button btnNewNoteAdAnInAc;      // кнопка создания новости

    // поля для доступа к записям БД
    List<String> spListCity;             // Данные для спинера выбора города

    Context context;
    String idAuthUser;

    public AdvertisingAndInformationFragment newInstance() {
        AdvertisingAndInformationFragment fragment = new AdvertisingAndInformationFragment();
        return fragment;
    } // FirstPageFragment

    // Метод onAttach() вызывается в начале жизненного цикла фрагмента, и именно здесь
    // мы можем получить контекст фрагмента, в качестве которого выступает класс MainActivity.
    //onAttach(Context) не вызовется до API 23 версии вместо этого будет вызван onAttach(Activity),
    //коий устарел с 23 API
    //Так что вызовем onAttachToContext
    @TargetApi(23)
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onAttachToContext(context);
    }//onAttach

    //устарел с 23 API
    //Так что вызовем onAttachToContext
    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onAttachToContext(activity);
        }//if
    }//onAttach

    //Вызовется в момент присоединения фрагмента к активити
    protected void onAttachToContext(Context context) {
        //здесь всегда есть контекст и метод всегда вызовется.
        //тут можно кастовать контест к активити.
        //но лучше к реализуемому ею интерфейсу
        //чтоб не проверять из какого пакета активити в каждом из случаев
        this.context = context;
        dbUtilities = new DBUtilities(context);
        // прочитать данные, переданные из активности (из точки вызова)
        idAuthUser = getArguments().getString("idAuthUser");
    }//onAttachToContext

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View result = inflater.inflate(R.layout.fragment_advertising_and_information,  container, false);
        FloatingActionButton fabMain = getActivity().findViewById(R.id.fabMain);
        fabMain.setVisibility(View.VISIBLE);

        btnNewNoteAdAnInAc = result.findViewById(R.id.btnNewNoteAdAnInAc);
        // RecycerView для отображения таблицы users БД
        rvNoteLineAdAnInAc = result.findViewById(R.id.rvNoteLineAdAnInAc);
        //Обработка нажатия клавишы "Создать новость"
        btnNewNoteAdAnInAc.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(context, CreateNewsNoteActivity.class);
                startActivityForResult(intent, REQ_CREATE_NOTE);
            }
        });

        //привязка ресурсов к объектам(основной спиннер из активности NavigationDrawerLogInActivity)
        spCityAdAnInAc = getActivity().findViewById(R.id.spCityMain);

        //инициализация коллекции для спинера (коллекция городов)
        spListCity = new ArrayList<>();

        //обращаемся к базе для получения списка имен городов
        spListCity = dbUtilities.getStrListTableFromDB("cities", "name");

        //Слушатель для позиции спинера и фильтрации RecyclerView по изменению позиции
        spCityAdAnInAc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spPos = position;
                //строим новый адаптер RecyclerView
                buildUserRecyclerView(
                        spCityAdAnInAc.getItemAtPosition(spPos).toString()
                );
            }//onItemSelected
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}//onNothingSelected
        });

        //строим новый адаптер RecyclerView
        buildUserRecyclerView(
                spCityAdAnInAc.getItemAtPosition(spCityAdAnInAc.getSelectedItemPosition()).toString()
        );

        //слушатель клавиши обновления списка Recyclerview
        fabMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshList();
            }
        });

        return result;
    } // onCreateView

    //обновляем recyclerview
    private void refreshList() {
        buildUserRecyclerView(spCityAdAnInAc.getSelectedItem().toString());
        rvNoteLineAdAnInAc.setAdapter(advertisingAndInformationRecyclerAdapter);
    }//refreshList

    //-----------------------Метод для приема результатов из активностей----------------------------
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_CANCELED) {
//            Toast.makeText(this, "Ошибка ввода!!!", Toast.LENGTH_LONG).show();
        }//RESULT_CANCELED

        // обработка результатов по активностям
        if (resultCode == RESULT_OK) {
            //возврат из активности создать новое событие
            // читаем из объекта data полученные данные и выводим в поле результата
            String logo = data.getStringExtra(PAR_LOGO);
            String head = data.getStringExtra(PAR_HEAD);
            String date = data.getStringExtra(PAR_DATE);
            String city_id = data.getStringExtra(PAR_CITY);
            String message = data.getStringExtra(PAR_MESSAGE);
            String tsizemessage = data.getStringExtra(PAR_TSIZE_MESSAGE);
            String tstylemessage = data.getStringExtra(PAR_TSTYLE_MESSAGE);
            //обращяемся к БД на сервер для создания новой записи в таблицу notes
            dbUtilities.insertIntoNotes(logo, head, idAuthUser, date, city_id, message, tsizemessage, tstylemessage);
            refreshList();
        }//RESULT_OK

        //строим новый адаптер RecyclerView
        buildUserRecyclerView(
                spCityAdAnInAc.getItemAtPosition(spPos).toString()
        );
    }//onActivityResult

    //Строим RecyclerView
    private void buildUserRecyclerView(String cityName) {
        List<Note> notes = null;    //список новостей

        //получение записией новостей из БД
        if(cityName.equals("ВСЕ ГОРОДА")) {
            notes = dbUtilities.getAllNotes();
        }else{
            //получаем записи новостей из БД по определенному городу
            notes = dbUtilities.getSomeNotesFromDB(cityName);
        }//if-else

        // создаем адаптер, передаем в него курсор
        advertisingAndInformationRecyclerAdapter
                = new AdvertisingAndInformationRecyclerAdapter(context, notes, idAuthUser);

        // RecycerView для отображения таблицы users БД
        rvNoteLineAdAnInAc.setAdapter(advertisingAndInformationRecyclerAdapter);
    }//buildUserRecyclerView
}//AdvertisingAndInformationFragment
