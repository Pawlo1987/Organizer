package com.example.user.organizer.fragment;

//-----------Фрагмент выводит все активные события--------------------------

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.example.user.organizer.AdvertisingAndInformationRecyclerAdapter;
import com.example.user.organizer.DBUtilities;
import com.example.user.organizer.Note;
import com.example.user.organizer.R;
import com.example.user.organizer.ShowAllEventsRecyclerAdapter;
import com.example.user.organizer.inteface.AllEventsInterface;

import java.util.ArrayList;
import java.util.List;

public class ShowAllEventsFragment extends Fragment
        implements AllEventsInterface {

    RecyclerView rvMainShAlEvAc;
    private static final int REQUEST_POS = 1;

    int spPos;                      //позиция спинера
    List<String> spListCity;             // Данные для спинера выбора города
    Spinner spCityShAlEvAc;
    // адаптер для отображения recyclerView
    ShowAllEventsRecyclerAdapter showAllEventsRecyclerAdapter;
    DBUtilities dbUtilities;
    Context context;

    String idAuthUser;                 //авторизированный пользователь

    AboutEventShowAllEventDialog aboutEventShowAllEventDialog =
            new AboutEventShowAllEventDialog(); // диалог подтверждения выхода из приложения

    TakePartShowAllEventDialog takePartShowAllEventDialog =
            new TakePartShowAllEventDialog(); // диалог подтверждения выхода из приложения

    final String ID_ABOUT_DIALOG = "aboutEventShowAllEventDialog";  //параметр для вызова диалога "about"
    final String ID_TAKE_PART_DIALOG = "takePartShowAllEventDialog";  //параметр для вызова диалога "callDialogTakePart"

    // Метод onAttach() вызывается в начале жизненного цикла фрагмента, и именно здесь
    // мы можем получить контекст фрагмента, в качестве которого выступает класс MainActivity.
    //onAttach(Context) не вызовется до API 23 версии вместо этого будет вызван onAttach(Activity),
    //коий устарел с 23 API
    //Так что вызовем onAttachToContext
    //https://ru.stackoverflow.com/questions/507008/%D0%9D%D0%B5-%D1%80%D0%B0%D0%B1%D0%BE%D1%82%D0%B0%D0%B5%D1%82-onattach
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

        View result = inflater.inflate(R.layout.fragment_show_all_event, container, false);

        FloatingActionButton fabMain = getActivity().findViewById(R.id.fabMain);
        fabMain.setVisibility(View.VISIBLE);

        // RecyclerView для отображения таблицы users БД
        rvMainShAlEvAc = result.findViewById(R.id.rvMainShAlEvAc);
        //привязка ресурсов к объектам
        spCityShAlEvAc = getActivity().findViewById(R.id.spCityMain);
        //инициализация коллекции для спинера
        spListCity = new ArrayList<>();
        //обращаемся к базе для получения списка имен городов
        spListCity = dbUtilities.getStrListTableFromDB("cities", "name");

        //Слушатель для позиции спинера и фильтрации RecyclerView по изменению позиции
        spCityShAlEvAc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spPos = position;
                //проверка если выбран пункт "Все города"
                if(spPos == (spListCity.size()))buildUserRecyclerView("");
                else {
                    String cityId = dbUtilities.getIdByValue("cities", "name",
                            spCityShAlEvAc.getItemAtPosition(spPos).toString());
                    //строим новый адаптер RecyclerView
                    buildUserRecyclerView(cityId);
                }//if
            }//onItemSelected

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }//onNothingSelected
        });

        //проверка если выбран пункт "Все города"
        if(spCityShAlEvAc.getSelectedItemPosition() == (spListCity.size()))buildUserRecyclerView("");
        else {
            String cityId = dbUtilities.getIdByValue("cities", "name",
                    spCityShAlEvAc.getItemAtPosition(spCityShAlEvAc.getSelectedItemPosition()).toString());
            //строим новый адаптер RecyclerView
            buildUserRecyclerView(cityId);
        }

        fabMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshList();
            }
        });

        return result;
    } // onCreateView

    //Строим RecyclerView
    private void buildUserRecyclerView(String cityId) {
        // создаем адаптер, передаем в него курсор
        showAllEventsRecyclerAdapter = new ShowAllEventsRecyclerAdapter(
                this, context, idAuthUser, cityId);
        //привязываем адаптер к recycler объекту
        rvMainShAlEvAc.setAdapter(showAllEventsRecyclerAdapter);
    }//buildUserRecyclerView

    //обновляем recyclerview
    private void refreshList() {
        showAllEventsRecyclerAdapter.updateEventList();
        //привязываем адаптер к recycler объекту
        rvMainShAlEvAc.setAdapter(showAllEventsRecyclerAdapter);
    }//refreshList

    // точка выхода из DialogFragment при положительных
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_POS:
                    showAllEventsRecyclerAdapter.updateEventList();
                    //привязываем адаптер к recycler объекту
                    rvMainShAlEvAc.setAdapter(showAllEventsRecyclerAdapter);
                    break;
            }//switch
        }//if
    }//onActivityResult

    @Override
    public void callDialogAboutDialog(Context context, String message) {
        Bundle args = new Bundle();    // объект для передачи параметров в диалог
        args.putString("message", message);
        aboutEventShowAllEventDialog.setArguments(args);

        // отображение диалогового окна
        aboutEventShowAllEventDialog.show(((AppCompatActivity)context).
                getSupportFragmentManager(), ID_ABOUT_DIALOG);
    }//callDialogAboutDialog

    @Override
    public void callDialogTakePart(Context context, String eventId, boolean userTakeInPart, String message) {
        Bundle args = new Bundle();    // объект для передачи параметров в диалог
        args.putString("message", message);
        args.putBoolean("userTakeInPart", userTakeInPart);
        args.putString("event_id", eventId);
        args.putString("user_id", idAuthUser);
        takePartShowAllEventDialog.setArguments(args);

        // Возврат результата выполнения из DialogFragment во Fragment минуя Activity
        // ссылка (https://habrahabr.ru/post/259805/)
        takePartShowAllEventDialog.setTargetFragment(this, REQUEST_POS);

        // Точка вызова отображение диалогового окна
        takePartShowAllEventDialog.show( getFragmentManager(), ID_TAKE_PART_DIALOG);
    }//callDialogTakePart
}
